package com.lamfire.warden;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.lamfire.logger.Logger;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-8-12
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = Logger.getLogger(HttpServerInboundHandler.class);
    private HttpRequest request;

    protected Action getAction(ActionContext context) throws ActionNotFoundException {
        Action action = ActionRegistry.getInstance().lookup(context);
        if (action == null) {
            throw new ActionNotFoundException("Not found action mapping PATH:" + context.getPath());
        }
        return action;
    }

    public void handleRequest(ChannelHandlerContext ctx, HttpRequest request , HttpContent requestContent) {
        try {
            ActionContext context = new ActionContext(ctx, request,requestContent);
            Action action = getAction(context);
            action.execute(context);
            HttpResponseUtils.sendHttpResponse(ctx,request,context.getHttpResponse());
        }catch(ActionNotFoundException exception){
            HttpResponseUtils.sendHttpResponse(ctx,request,HttpResponseStatus.NOT_FOUND);
            LOGGER.error(exception.getMessage(), exception);
        }catch (Throwable t) {
            LOGGER.error(t.getMessage(), t);
        }
    }



    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            String uri = request.getUri();
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            handleRequest(ctx,request,content);
        }
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error(cause.getMessage());
        ctx.close();
    }

    private void writeHttpResponse(String responseBody, ChannelHandlerContext ctx, HttpResponseStatus status) throws UnsupportedEncodingException {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,status, Unpooled.wrappedBuffer(responseBody .getBytes(Charset.forName("utf-8"))));
        response.headers().set(CONTENT_TYPE, "text/json");
        response.headers().set(CONTENT_LENGTH,response.content().readableBytes());
        response.headers().set(EXPIRES, 0);
        if (HttpHeaders.isKeepAlive(request)) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.write(response);
        ctx.flush();
    }
}
