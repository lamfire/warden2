package com.lamfire.warden.cmd;

import com.lamfire.logger.Logger;
import com.lamfire.utils.ClassUtils;
import com.lamfire.utils.StringUtils;
import com.lamfire.warden.Action;


import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: linfan
 * Date: 16-4-18
 * Time: 下午5:05
 */
class CMDMapper {
    private static final Logger LOGGER = Logger.getLogger(CMDMapper.class);
    private final Map<String, ActionMethod> mappers = new ConcurrentHashMap<>();
    private CmdCodec codec = new DefaultCmdCodec();
    public void register(Class<?> actionClass)  {
        if(!Action.class.isAssignableFrom(actionClass)){
            return;
        }

        CODEC conf = actionClass.getAnnotation(CODEC.class);
        if(conf == null){
            LOGGER.error("["+actionClass.getName() + "] is assignable from CMDAction,but not found 'CODEC' annotation.");
            return;
        }

        String codecClassName = conf.codec();
        if(StringUtils.isNotBlank(codecClassName)) {
            LOGGER.debug("[CODEC] : " + actionClass.getName()+" ,codec="+codecClassName);
            try {
                this.codec = (CmdCodec)Class.forName(codecClassName).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(),e);
            }
        }

        if(!Action.class.isAssignableFrom(actionClass)){
            return;
        }
        Collection<Method> methods = ClassUtils.getAllDeclaredMethodsByAnnotation(actionClass, CMD.class);
        for(Method m : methods) {
            CMD cmd = m.getAnnotation(CMD.class);
            String cmdName = cmd.name();
            ActionMethod method = new ActionMethod(cmdName, actionClass,m);
            mappers.put(cmdName, method);
            LOGGER.debug("[CMD] : " + cmdName + " -> " + actionClass.getName()+"."+m.getName());
        }
    }

    public ActionMethod getActionMethod(String name){
        return this.mappers.get(name);
    }

    public CmdCodec getCodec() {
        return codec;
    }
}
