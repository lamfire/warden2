package com.lamfire.demo.utils;

import com.lamfire.code.Base64;
import com.lamfire.json.JSON;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.cmd.CmdCodec;
import com.lamfire.warden.cmd.CmdREQ;

public class JSONCmdCodec implements CmdCodec<JSON> {
    public CmdREQ<JSON> parseCmd(ActionContext context, byte[] data){
        JSON json = JSON.fromBytes(data);
        String cmd = json.getString("cmd");
        return new CmdREQ(cmd,json);
    }

    public byte[] decode(ActionContext context){
        return Base64.decode(new String(context.getRequestBody()));
    }

    public byte[] encode(ActionContext context,byte[] data){
        return Base64.encode(data).getBytes();
    }
}
