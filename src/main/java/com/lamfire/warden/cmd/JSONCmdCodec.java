package com.lamfire.warden.cmd;

import com.lamfire.json.JSON;
import com.lamfire.warden.ActionContext;

public class JSONCmdCodec implements CmdCodec<JSON> {
    public CmdREQ<JSON> parseCmd(ActionContext context, byte[] data){
        JSON json = JSON.fromBytes(data);
        String cmd = json.getString("cmd");
        return new CmdREQ(cmd,json);
    }

    public byte[] decode(ActionContext context){
        return context.getRequestBody();
    }

    public byte[] encode(ActionContext context,byte[] data){
        return data;
    }
}
