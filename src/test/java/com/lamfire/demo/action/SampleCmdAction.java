package com.lamfire.demo.action;

import com.lamfire.code.Base64;
import com.lamfire.json.JSON;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.anno.ACTION;
import com.lamfire.warden.cmd.CMD;
import com.lamfire.warden.cmd.CMDAction;
import com.lamfire.warden.cmd.CODEC;
import com.lamfire.warden.cmd.CmdREQ;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-8-12
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
@CODEC(codec = "com.lamfire.demo.utils.JSONCmdCodec")
@ACTION(path="/api",singleton = true)
public class SampleCmdAction extends CMDAction {

    @CMD(name = "register")
    public JSON register(ActionContext context,JSON data){
        data.put("addr",context.getRemoteAddress());
        return data;
    }

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
    public byte[] del(JSON data){
        return data.toBytes();
    }

}
