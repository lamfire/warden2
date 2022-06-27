package com.lamfire.warden.cmd;

import com.lamfire.json.JSON;
import com.lamfire.warden.ActionContext;

import java.lang.reflect.Method;

import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * Created by lamfire on 16/11/28.
 */
class MethodArgumentResolver {
    private MethodParameter[] methodParameters;

    public MethodArgumentResolver(Method method){
        Parameter[] parameters = method.getParameters();
        this.methodParameters = new MethodParameter[parameters.length];
        for(int i=0;i<parameters.length;i++){
            MethodParameter mp = new MethodParameter();
            mp.setMethod(method);
            mp.setIndex(i);
            mp.setParameterType(parameters[i].getType());
            mp.setParameterAnnotations(parameters[i].getAnnotations());
            this.methodParameters[i] = mp;
        }
    }

    public Object[] resolveArguments(ActionContext context, Object data){
        Object[] args = new Object[methodParameters.length];
        for(int i=0;i<methodParameters.length;i++){
            String parameterName = methodParameters[i].getParameterName();
            Class<?> type = methodParameters[i].getParameterType();
            if(JSON.class == type){
                args[i] = data;
                continue;
            }
            if(ActionContext.class == type){
                args[i] = context;
                continue;
            }
            if(String.class == type){
                args[i] = data.toString();
                continue;
            }
            if(byte[].class == type && data instanceof byte[]){
                args[i] = data;
                continue;
            }
        }
        return args;
    }
}
