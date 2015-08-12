package com.lamfire.warden;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.HttpHeaders;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActionContext {
    private final DefaultFullHttpResponse response;
	private final HttpRequest request;
	private final ChannelHandlerContext channelHandlerContext;
    private HttpRequestParameters parameters;
    private QueryStringDecoder queryStringDecoder;
	
	ActionContext(ChannelHandlerContext ctx, HttpRequest request,HttpContent content){
		this.request = request;
		this.channelHandlerContext = ctx;
        this.parameters = new HttpRequestParameters(request,content);
        this.response =  new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        this.response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
    }
	
	Channel getChannel(){
		return channelHandlerContext.channel();
	}

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public boolean isKeepAlive(){
        return HttpHeaders.isKeepAlive(request);
    }

    public void writeResponse(String message) {
        if(message == null){
            return ;
        }
        writeResponse(message.getBytes());
    }

    public void writeResponse(String message,Charset charset) {
        if(message == null){
            return ;
        }
        writeResponse(message.getBytes(charset));
    }

    public void writeResponse(String message,String charset) {
        if(message == null){
            return ;
        }
        writeResponse(message.getBytes(Charset.forName(charset)));
    }

    public void writeResponse(byte[] message) {
        if(message == null){
            return ;
        }
        this.response.content().writeBytes(message);
    }

    public void sendRedirect(String redirectUrl){
        Channel channel = getChannel();
        if (!channel.isActive() || !channel.isWritable()) {
            return;
        }

        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(302));
        response.headers().set(HttpHeaders.Names.LOCATION, redirectUrl);
        channel.write(response).addListener(ChannelFutureListener.CLOSE);
    }

    public byte[] getRequestBody(){
        return parameters.getHttpRequestContentAsBytes();
    }

    public String getRequestBodyAsString(){
          return new String(getRequestBody(),getRequestCharset());
    }

	public String getRemoteAddress(){
		InetSocketAddress addr = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
		return addr.getAddress().getHostAddress();
	}
	
	public int getRemotePort(){
		InetSocketAddress addr = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
		return addr.getPort();
	}

    private String findAddress(String forwardFor){
        if(forwardFor == null || forwardFor.length() == 0){
            return null;
        }
        String[] addresses = forwardFor.split(",");

        for(String addr : addresses){
            if(!"unknown".equalsIgnoreCase(addr)){
                return addr;
            }
        }

        return null;
    }

    public String getRealRemoteAddr() {
        String addr;
        String forwardedFor =  request.headers().get("X-Forwarded-For");
        if((addr = findAddress(forwardedFor)) != null){
            return addr.trim();
        }

        forwardedFor = request.headers().get("Proxy-Client-IP");
        if((addr = findAddress(forwardedFor)) != null){
            return addr.trim();
        }

        forwardedFor = request.headers().get("WL-Proxy-Client-IP");
        if((addr = findAddress(forwardedFor)) != null){
            return addr.trim();
        }

        return getRemoteAddress();
    }

	public String getRequestHeader(String key){
		return request.headers().get(key);
	}
	
	public Set<String> getRequestHeaderNames(){
		return request.headers().names();
	}
	
	public String getPath(){
        if(queryStringDecoder == null){
            queryStringDecoder = new QueryStringDecoder(request.getUri());
        }
		return queryStringDecoder.path();
	}

    public Charset getRequestCharset(){
        String charset = this.getRequestHeader("Charset");
        if(charset == null){
            charset = "utf-8";
        }
        return Charset.forName(charset);
    }

    public synchronized Map<String,List<String>> getRequestParameters(){
        return parameters.getHttpRequestParameters();
    }

    public List<String> getRequestParameters(String name){
        return parameters.getHttpRequestParameters(name);
    }

    public String getRequestParameter(String name){
        return parameters.getHttpRequestParameter(name);
    }

    public Set<String> getHttpRequestParameterNames(){
        return parameters.getHttpRequestParameterNames();
    }
	
	public void setResponseHeader(String key,Object value){
		this.response.headers().set(key, value);
	}

	public HttpRequest getHttpRequest() {
		return request;
	}

	public DefaultFullHttpResponse getHttpResponse() {
		return response;
	}
}