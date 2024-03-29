package com.lamfire.warden.cmd;

import com.lamfire.code.MD5;
import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import com.lamfire.simplecache.Cache;
import com.lamfire.utils.StringUtils;
import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;

public class CMDAction implements Action {
    private static final Logger LOGGER = Logger.getLogger("CMDAction");
    static final String DEFAULT_CMD = "default";
    private final CMDMapper mapper = new CMDMapper();
    private final MethodVisitor methodVisitor = new MethodVisitor();

    public CMDAction(){
        mapper.register(this.getClass());
    }

    private String getCacheKey(final Object data){
        if(data == null){
            return null;
        }
        if(data instanceof byte[]){
            return MD5.hash((byte[])data);
        }
        if(data instanceof JSON){
            return MD5.hash(((JSON)data).toBytes());
        }
        if(data instanceof String){
            return MD5.hash(((String)data).getBytes());
        }
        return String.valueOf(data.hashCode());
    }

    @Override
    public void execute(ActionContext context) {
        CmdCodec codec = mapper.getCodec();
        byte[] data = null;
        try {
            data = codec.decode(context);
        }catch (Throwable t){
            onCodecException(context,codec,t);
            return;
        }

        CmdREQ<Object> req = codec.parseCmd(context,data);
        String cmd = req.getCmd();
        if(StringUtils.isBlank(cmd)){
            cmd = DEFAULT_CMD;
        }

        ActionMethod method = mapper.getActionMethod(cmd);
        if(method == null){
            on404(context,cmd,req.getData());
            return;
        }

        try {
            Object resultObj = null;
            Cache<String,Object> cache = method.getCache();

            if(cache != null){
                // in cache
                String cacheKey = getCacheKey(req.getData());
               if(cacheKey != null){
                   resultObj = cache.get(cacheKey);
                   LOGGER.debug("Find in cache : ["+cacheKey +"] = " + resultObj);
                   if(resultObj == null){
                       //cache expired
                       resultObj = methodVisitor.visit(context, this, method.getActionMethod(), method.resolveMethodArguments(context, req.getData()));
                       cache.set(cacheKey,resultObj);
                   }
               }
            }else {
                resultObj = methodVisitor.visit(context, this, method.getActionMethod(), method.resolveMethodArguments(context, req.getData()));
            }

            if(resultObj == null){
                return;
            }
            byte[] respResult = null;
            if (resultObj instanceof byte[]) {
                respResult = (byte[])resultObj;
            }else if(resultObj instanceof JSON){
                respResult = ((JSON)resultObj).toBytes();
            }else if(resultObj instanceof String){
                respResult = ((String)resultObj).getBytes();
            }
            respResult = codec.encode(context,respResult);
            context.writeResponse(respResult);
            return;
        }catch (Throwable e){
            onThrowable(context,cmd,req.getData(),e);
            return;
        }
    }

    public void on404(ActionContext context,String cmd,Object data){
        LOGGER.error(context.getRealRemoteAddr() +","+context.getPath()+",Not found : CMD="+cmd +",data=" + data.toString());
        context.setResponseStatus(404);
    }

    public void onThrowable(ActionContext context,String cmd,Object data,Throwable throwable){
        LOGGER.error(context.getRealRemoteAddr() +" -> " + this.getClass().getName() +"." + cmd +"("+data.toString()+"),invoke exception : " + throwable.getMessage());
        LOGGER.error(throwable.getMessage(),throwable);
        context.setResponseStatus(500);
        context.writeResponse(throwable.getMessage());
    }

    public void onCodecException(ActionContext context,CmdCodec codec,Throwable throwable){
        if(codec != null){
            LOGGER.error(context.getRealRemoteAddr() +" -> " + codec.getClass().getName() +".invoke exception : " + throwable.getMessage());
        }else{
            LOGGER.error(context.getRealRemoteAddr() +" -> " + this.getClass().getName() +" Not found 'CODEC' annotation.");
        }
        LOGGER.error(throwable.getMessage(),throwable);
        context.setResponseStatus(499);
        context.writeResponse(throwable.getMessage());
    }
}
