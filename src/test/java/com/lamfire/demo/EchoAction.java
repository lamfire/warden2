package com.lamfire.demo;

import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.anno.ACTION;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.OutputStream;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.EXPIRES;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-8-12
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
@ACTION(path="/echo",singleton = true)
public class EchoAction implements Action {
    @Override
    public void execute(ActionContext context) {
//        System.out.println(context.getHttpRequestHeaderNames());
//        System.out.println(context.getHttpRequestParameterNames());
//        System.out.println(context.getHttpRequestContentAsString());

        byte[] message = context.getRequestBody();
        context.writeResponse(message);

        //FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(message));
        //FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        //response.content().writeBytes(message);
        //response.headers().set(CONTENT_LENGTH,response.content().readableBytes());
        //response.headers().set(EXPIRES, 0);

        //HttpResponseUtils.sendHttpResponse(context.getChannelHandlerContext(),context.getHttpRequest(),response);
    }
}
