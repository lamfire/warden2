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
        byte[] data = context.getRequestBody();
        DataCodec codec = mapper.getCodec();
        if(codec != null){
            data = codec.decode(context,data);
        }
        JSON req = JSON.fromBytes(data);
        String key = mapper.getCmdKey();
        String cmd = req.getString(key);
        ActionMethod method = mapper.getActionMethod(cmd);
        JSON resp = null;

        try {
            resp = (JSON) methodVisitor.visit(context, this, method.getActionMethod(), method.resolveMethodArguments(context, req));
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            context.setResponseStatus(500);
            context.writeResponse(e.getMessage());
        }

        if(resp != null) {
            byte[] respResult = resp.toBytes();
            if (codec != null) {
                respResult = codec.encode(context, respResult);
            }
            context.writeResponse(respResult);
        }
    }
}
