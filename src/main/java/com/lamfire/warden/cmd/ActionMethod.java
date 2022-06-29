package com.lamfire.warden.cmd;

import com.lamfire.logger.Logger;
import com.lamfire.simplecache.Cache;
import com.lamfire.warden.ActionContext;
import java.lang.reflect.Method;

class ActionMethod {
	private static final Logger LOGGER = Logger.getLogger(ActionMethod.class);

	private String cmd;
	private Class<?> actionClass;
	private Method actionMethod;
	private MethodArgumentResolver argumentResolver;
	private Cache<String,Object> cache;

    public ActionMethod(String cmd, Class<?> actionClass, Method actionMethod) {
        this.cmd = cmd;
        this.actionClass = actionClass;
		this.actionMethod = actionMethod;
		this.argumentResolver = new MethodArgumentResolver(actionMethod);
    }

	public Object[] resolveMethodArguments(ActionContext context, Object data){
		return argumentResolver.resolveArguments(context,data);
	}

	public Method getActionMethod() {
		return actionMethod;
	}

    public Class<?> getActionClass() {
        return actionClass;
    }

	public String getCmd() {
		return cmd;
	}

	public MethodArgumentResolver getArgumentResolver() {
		return argumentResolver;
	}

	public Cache<String, Object> getCache() {
		return cache;
	}

	public void setCache(Cache<String, Object> cache) {
		this.cache = cache;
	}
}
