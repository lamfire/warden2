package com.lamfire.warden;

import com.lamfire.utils.NumberUtils;
import com.lamfire.utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.nio.charset.Charset;
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
public class HttpRequestParameters {
    private HttpRequest  request;
    private HttpContent content;
    private Map<String,List<String>> httpRequestParameters;
    private byte[] requestContentBytes;

    public HttpRequestParameters(HttpRequest  request,HttpContent content){
        this.request = request;
        this.content = content;
    }

    byte[] getHttpRequestContentAsBytes(){
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

    String getHttpRequestContentAsString(){
        return new String(getHttpRequestContentAsBytes());
    }

    Map<String,List<String>> getHttpRequestParameters(){
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

    List<String> getHttpRequestParameters(String name){
        Map<String,List<String>> params = getHttpRequestParameters();
        if(params == null){
            return null;
        }
        return  params.get(name);
    }

    String getHttpRequestParameter(String name){
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

    Set<String> getHttpRequestParameterNames(){
        Map<String,List<String>> params = getHttpRequestParameters();
        if(params == null){
            return null;
        }
        return params.keySet();
    }



    public Set<String> names(){
        return getHttpRequestParameterNames();
    }

    public byte[] asBytes(){
        return getHttpRequestContentAsBytes();
    }

    public String asString(Charset charset){
        return new String(getHttpRequestContentAsBytes(),charset);
    }

    public String asString(String charset){
        return new String(getHttpRequestContentAsBytes(),Charset.forName(charset));
    }

    public String get(String name){
        return getHttpRequestParameter(name);
    }

    public int getInt(String name){
        return NumberUtils.toInt(getHttpRequestParameter(name),0);
    }

    public long getLong(String name){
        return NumberUtils.toLong(getHttpRequestParameter(name),0);
    }

    public float getFloat(String name){
        return NumberUtils.toFloat(getHttpRequestParameter(name),0.0f);
    }

    public double getDouble(String name){
        return NumberUtils.toDouble(getHttpRequestParameter(name),0.0d);
    }

    public String getString(String name){
        return getHttpRequestParameter(name);
    }

}
