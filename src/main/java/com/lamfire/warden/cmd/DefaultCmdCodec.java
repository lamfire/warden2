package com.lamfire.warden.cmd;

import com.lamfire.json.JSON;
import com.lamfire.warden.ActionContext;

public class DefaultCmdCodec implements CmdCodec<JSON> {
    public CmdREQ<JSON> parseCmd(ActionContext context, byte[] data){
        return new CmdREQ(CMDAction.DEFAULT_CMD,data);
    }

    public byte[] decode(ActionContext context){
        return context.getRequestBody();
    }

    public byte[] encode(ActionContext context,byte[] data){
        return data;
    }
}
