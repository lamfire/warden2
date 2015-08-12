package com.lamfire.warden;

import com.lamfire.logger.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class HttpResponseUtils {
	private static final Logger LOGGER = Logger.getLogger(HttpResponseUtils.class);

    public static void sendHttpResponse(ChannelHandlerContext ctx,HttpRequest request, HttpResponse response){
        if(response instanceof DefaultFullHttpResponse){
            DefaultFullHttpResponse res = (DefaultFullHttpResponse)response;
            res.headers().set(CONTENT_LENGTH,res.content().readableBytes());
        }
        if (HttpHeaders.isKeepAlive(request)) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        response.headers().set(EXPIRES, 0);
        ctx.write(response);
        ctx.flush();
    }

    public static void sendHttpResponse(ChannelHandlerContext ctx,HttpRequest request, HttpResponseStatus status,String contentType,ByteBuf buf){
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,status, buf);
        response.headers().set(CONTENT_TYPE,contentType);
        response.headers().set(CONTENT_LENGTH,response.content().readableBytes());
        response.headers().set(EXPIRES, 0);
        sendHttpResponse(ctx,request,response);
    }

    public static void sendHttpResponse(ChannelHandlerContext ctx,HttpRequest request, HttpResponseStatus status){
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,status);
        response.headers().set(EXPIRES, 0);
        sendHttpResponse(ctx,request,response);
    }

    public static void sendHttpResponse(ChannelHandlerContext ctx,HttpRequest request, HttpResponseStatus status,String contentType,String responseBody,String charset){
        sendHttpResponse(ctx,request,status,contentType,Unpooled.wrappedBuffer(responseBody.getBytes(Charset.forName(charset))));
    }

    public static void sendHttpResponse(ChannelHandlerContext ctx,HttpRequest request, HttpResponseStatus status,String contentType,byte[] bodyBytes){
        sendHttpResponse(ctx,request,status,contentType,Unpooled.wrappedBuffer(bodyBytes));
    }



    public static void sendHttpRedirectResponse(ChannelHandlerContext ctx, String redirectUrl) {
        if (!ctx.channel().isActive() || !ctx.channel().isWritable()) {
            return;
        }
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(302));
        response.headers().set(HttpHeaders.Names.LOCATION, redirectUrl);
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    }
}
