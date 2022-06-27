package com.lamfire.demo.utils;

import com.lamfire.code.Base64;
import com.lamfire.warden.ActionContext;

public class MyDataCodec {

    public byte[] encode(ActionContext context, byte[] data) {
        System.out.println("base64 encode...");
        return Base64.encode(data).getBytes();
    }


    public byte[] decode(ActionContext context, byte[] data) {
        System.out.println("base64 decode...");
        return Base64.decode(new String(data));
    }
}
