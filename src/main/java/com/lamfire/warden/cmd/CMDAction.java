package com.lamfire.warden.cmd;

import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
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
        byte[] data = codec.decode(context);

        CmdREQ<JSON> req = codec.parseCmd(context,data);
        String cmd = req.getCmd();
        ActionMethod method = mapper.getActionMethod(cmd);
        if(method == null){
            LOGGER.error(context.getRealRemoteAddr() +","+context.getPath()+",CMD="+cmd +",Not found.");
            context.setResponseStatus(404);
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
            }
            respResult = codec.encode(context,respResult);
            context.writeResponse(respResult);
            return;
        }catch (Exception e){
            LOGGER.error(this.getClass().getName() +"." + method.getActionMethod().getName() +",invoke exception : " + e.getMessage());
            LOGGER.error(e.getMessage(),e);
            context.setResponseStatus(500);
            context.writeResponse(e.getMessage());
            return;
        }
    }
}
