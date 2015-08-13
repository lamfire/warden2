package com.lamfire.warden;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.lamfire.logger.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

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

    private final ActionRegistry registry;

    private HttpRequest request;
    private ByteBuf content;

    public HttpServerInboundHandler(ActionRegistry registry){
        this.registry = registry;
    }

    protected Action getAction(ActionContext context) throws ActionNotFoundException {
        Action action = registry.lookup(context);
        if (action == null) {
            throw new ActionNotFoundException("Not found action for PATH:" + context.getPath());
        }
        return action;
    }

    public void handleRequest(ChannelHandlerContext ctx, HttpRequest request , ByteBuf buf) {
        try {
            HttpRequestParameters params;
            if(HttpMethod.POST.equals(request.getMethod())){
                params = new HttpPostRequestParameters(request,buf);

            }else{
                params = new HttpGetRequestParameters(request);
            }
            ActionContext context = new ActionContext(ctx, request,params);
            Action action = getAction(context);
            action.execute(context);
            HttpResponseUtils.sendHttpResponse(ctx,request,context.getHttpResponse());
        }catch(ActionNotFoundException exception){
            String message = "404 Not Found - " + request.getUri();
            HttpResponseUtils.sendHttpResponse(ctx,request,HttpResponseStatus.NOT_FOUND,"text/plain",message.getBytes());
            LOGGER.error(exception.getMessage(), exception);
        }catch (Throwable t) {
            LOGGER.error(t.getMessage(), t);
        }
    }



    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
        }

        if (msg instanceof HttpContent) {
            HttpContent chunk = (HttpContent) msg;

            //append request data if at readable
            if(chunk.content().readableBytes() > 0 ){//readable
                if(content == null){
                    content = Unpooled.buffer();
                }
                content.writeBytes(chunk.content());
                chunk.release();
            }

            //handle request if at the end
            if (msg instanceof LastHttpContent) {
                handleRequest(ctx,request,content);
                reset();
            }
        }
    }

    private void reset(){
        request = null;
        if(content != null){
            content.release();
            content = null;
        }
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //LOGGER.error(cause.getMessage(),cause);
        ctx.close();
    }
}
