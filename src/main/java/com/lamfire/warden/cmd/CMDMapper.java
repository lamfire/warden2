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
    private DataCodec codec;
    private String cmdKey;
    public void register(Class<?> actionClass)  {
        if(!Action.class.isAssignableFrom(actionClass)){
            return;
        }

        CMD_CONFIG conf = actionClass.getAnnotation(CMD_CONFIG.class);
        if(conf == null){
            LOGGER.debug("["+actionClass.getName() + "] is assignable from CMDAction,but not found 'CMD_CONFIG' annotation.");
            return;
        }

        this.cmdKey = conf.key();
        LOGGER.debug("[CMD_CONFIG] : " + actionClass.getName()+" ,cmdKey="+cmdKey);
        String codecClassName = conf.codec();
        if(StringUtils.isNotBlank(codecClassName)) {
            LOGGER.debug("[CMD_CONFIG] : " + actionClass.getName()+" ,codec="+codecClassName);
            try {
                this.codec = (DataCodec)Class.forName(codecClassName).newInstance();
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

    public String getCmdKey() {
        return cmdKey;
    }

    public void setCmdKey(String cmdKey) {
        this.cmdKey = cmdKey;
    }

    public ActionMethod getActionMethod(String name){
        return this.mappers.get(name);
    }

    public DataCodec getCodec() {
        return codec;
    }
}
