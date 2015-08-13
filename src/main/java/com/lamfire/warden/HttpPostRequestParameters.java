package com.lamfire.warden;

import com.lamfire.utils.Lists;
import com.lamfire.utils.Maps;
import com.lamfire.utils.NumberUtils;
import com.lamfire.utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.*;

import java.io.IOException;
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
public class HttpPostRequestParameters extends BaseHttpRequestParameters{
    private final HttpRequest  request;
    private final ByteBuf content;

    private final Map<String,List<String>> params = Maps.newHashMap();
    private byte[] body;
    private boolean _parsed = false;

    public HttpPostRequestParameters(HttpRequest request, ByteBuf content){
        this.request = request;
        this.content = content;
    }

    byte[] getHttpRequestBodyAsBytes(){
        if(body != null){
            return body;
        }
        if(content == null){
            return null;
        }

        content.readerIndex(0);
        int len = content.readableBytes();
        body = new byte[len];
        content.readBytes(body);

        return body;
    }

    Map<String,List<String>> getHttpRequestParameters(){
        if(_parsed){
            return params;
        }

        byte[] body  =  getHttpRequestBodyAsBytes() ;
        if(body == null){
            _parsed = true;
            return params;
        }

        QueryStringDecoder decoder = new QueryStringDecoder("?"+new String(body)) ;
        params.putAll(decoder.parameters());
        _parsed = true;
        return params;
    }


}
