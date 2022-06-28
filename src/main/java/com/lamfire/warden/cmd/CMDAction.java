package com.lamfire.warden.cmd;

import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import com.lamfire.utils.StringUtils;
import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;

public class CMDAction implements Action {
    private static final Logger LOGGER = Logger.getLogger("CMDAction");
    private final CMDMapper mapper = new CMDMapper();
    private final MethodVisitor methodVisitor = new MethodVisitor();

    public CMDAction(){
        mapper.register(this.getClass());
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
            cmd = "index";
        }

        ActionMethod method = mapper.getActionMethod(cmd);
        if(method == null){
            on404(context,cmd,req.getData());
            return;
        }

        try {
            Object resultObj = methodVisitor.visit(context, this, method.getActionMethod(), method.resolveMethodArguments(context, req.getData()));
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
        LOGGER.error(context.getRealRemoteAddr() +" -> " + codec.getClass().getName() +".invoke exception : " + throwable.getMessage());
        LOGGER.error(throwable.getMessage(),throwable);
        context.setResponseStatus(499);
        context.writeResponse(throwable.getMessage());
    }
}
