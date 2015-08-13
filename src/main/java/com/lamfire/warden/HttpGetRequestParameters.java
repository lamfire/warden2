package com.lamfire.warden;

import com.lamfire.utils.Lists;
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
public class HttpGetRequestParameters extends BaseHttpRequestParameters {
    private HttpRequest  request;
    private Map<String,List<String>> httpRequestParameters;
    private byte[] requestContentBytes;

    public HttpGetRequestParameters(HttpRequest request){
        this.request = request;
    }

    byte[] getHttpRequestBodyAsBytes(){
        if(requestContentBytes != null){
            return requestContentBytes;
        }

        String queryString = StringUtils.substringAfter(request.getUri(), "?");
        this.requestContentBytes =  queryString.getBytes();
        return requestContentBytes;
    }

    Map<String,List<String>> getHttpRequestParameters(){
        if(httpRequestParameters != null){
            return httpRequestParameters;
        }
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        this.httpRequestParameters = queryStringDecoder.parameters();
        return httpRequestParameters;
    }
}
