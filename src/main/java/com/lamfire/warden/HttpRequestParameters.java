package com.lamfire.warden;

import com.lamfire.utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-8-12
 * Time: 下午5:02
 * To change this template use File | Settings | File Templates.
 */
class HttpRequestParameters {
    private HttpRequest  request;
    private HttpContent content;
    private Map<String,List<String>> httpRequestParameters;
    private byte[] requestContentBytes;

    public HttpRequestParameters(HttpRequest  request,HttpContent content){
        this.request = request;
        this.content = content;
    }

    public byte[] getHttpRequestContentAsBytes(){
        if(requestContentBytes != null){
            return requestContentBytes;
        }

        if (request.getMethod().equals(HttpMethod.POST)) {
            ByteBuf buf =  content.content();
            int len = buf.readableBytes();
            requestContentBytes = new byte[len];
            buf.readBytes(requestContentBytes);
            buf.release();
        }else{
            String queryString = StringUtils.substringAfter(request.getUri(), "?");
            this.requestContentBytes =  queryString.getBytes();
        }

        return requestContentBytes;
    }

    public String getHttpRequestContentAsString(){
        return new String(getHttpRequestContentAsBytes());
    }

    public Map<String,List<String>> getHttpRequestParameters(){
        if(httpRequestParameters != null){
            return httpRequestParameters;
        }

        if (request.getMethod().equals(HttpMethod.GET)){
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
            this.httpRequestParameters = queryStringDecoder.parameters();
        }
        byte[] data = this.getHttpRequestContentAsBytes();
        if(data != null){
            String queryString = "?" + new String(data);
            QueryStringDecoder decoder = new QueryStringDecoder(queryString);
            this.httpRequestParameters = decoder.parameters();
        }
        return httpRequestParameters;
    }

    public List<String> getHttpRequestParameters(String name){
        Map<String,List<String>> params = getHttpRequestParameters();
        if(params == null){
            return null;
        }
        return  params.get(name);
    }

    public String getHttpRequestParameter(String name){
        Map<String,List<String>> params = getHttpRequestParameters();
        if(params == null){
            return null;
        }
        List<String> list =  params.get(name);
        if(list!=null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    public Set<String> getHttpRequestParameterNames(){
        Map<String,List<String>> params = getHttpRequestParameters();
        if(params == null){
            return null;
        }
        return params.keySet();
    }

}
