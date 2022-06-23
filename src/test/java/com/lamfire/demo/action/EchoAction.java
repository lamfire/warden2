package com.lamfire.demo.action;

import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.anno.ACTION;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-8-12
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
@ACTION(path="/echo2",singleton = true)
public class EchoAction implements Action {
    @Override
    public void execute(ActionContext context) {
//        System.out.println(context.parameters().asString());
//        System.out.println(context.parameters().getInt("age"));
//        System.out.println(context.parameters().get("name"));
//        System.out.println(context.parameters().getInts("items"));


        byte[] message = context.getRequestBody();
        context.writeResponse(message);
        System.out.println(new String(message));
    }
}
