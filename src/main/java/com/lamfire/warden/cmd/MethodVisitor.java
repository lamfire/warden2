package com.lamfire.warden.cmd;

import com.lamfire.logger.Logger;
import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class MethodVisitor {
	public Object visit(ActionContext context,Action action, Method method, Object[] parameters) throws InvocationTargetException, IllegalAccessException {
		return method.invoke(action,parameters);
	}
}
