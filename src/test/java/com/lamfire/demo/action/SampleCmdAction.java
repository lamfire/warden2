package com.lamfire.demo.action;


import com.lamfire.json.JSON;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.anno.ACTION;
import com.lamfire.warden.cmd.*;

@CODEC(codec = "com.lamfire.demo.utils.JSONCmdCodec")
@ACTION(path="/api",singleton = true)
public class SampleCmdAction extends CMDAction {

    @CMD(name = "default")
    public byte[] execute(ActionContext context,byte[] data){
        //data.put("addr",context.getRemoteAddress());
        return data;
    }

    @CMD(name = "register")
    public JSON register(ActionContext context,JSON data){
        data.put("addr",context.getRemoteAddress());
        return data;
    }

    @CACHED
    @CMD(name = "login")
    public JSON login(JSON data){
        return data;
    }

    @CMD(name = "add")
    public void add(JSON data){
        System.out.println(data);
        //throw new RuntimeException("RuntimeException");
    }

    @CMD(name = "del")
    public byte[] del(String data){
        System.out.println(data);
        return null;
    }

}
