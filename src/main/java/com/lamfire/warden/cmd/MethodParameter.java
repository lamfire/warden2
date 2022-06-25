package com.lamfire.warden.cmd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


class MethodParameter {
    private Method method;
    private int index;
    private Class<?> parameterType;
    private String parameterName;
    private Annotation[] parameterAnnotations;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Annotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public void setParameterAnnotations(Annotation[] parameterAnnotations) {
        this.parameterAnnotations = parameterAnnotations;
    }

}
