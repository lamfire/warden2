package com.lamfire.warden.cmd;

import com.lamfire.logger.Logger;
import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.Caches;
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
        if(conf != null){
            String codecClassName = conf.codec();
            if (StringUtils.isNotBlank(codecClassName)) {
                LOGGER.debug("[CODEC] : " + actionClass.getName() + " ,codec=" + codecClassName);
                try {
                    this.codec = (CmdCodec) Class.forName(codecClassName).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }else {
            LOGGER.warn("["+actionClass.getName() + "] is assignable from CMDAction,but not found 'CODEC' annotation.");
        }
        Collection<Method> methods = ClassUtils.getAllDeclaredMethodsByAnnotation(actionClass, CMD.class);
        for(Method m : methods) {
            CMD cmd = m.getAnnotation(CMD.class);
            String cmdName = cmd.name();
            ActionMethod method = new ActionMethod(cmdName, actionClass,m);

            //cache
            CACHED cached = m.getAnnotation(CACHED.class);
            if(cached != null){
                Cache<String,Object> cache = Caches.makeLruCache(cached.maxElements(),cached.timeToLiveMillis());
                method.setCache(cache);
            }
            mappers.put(cmdName, method);
            LOGGER.debug("[CMD] : " + cmdName + " -> " + actionClass.getName()+"."+m.getName()+",cached=" + cached);
        }
    }

    public ActionMethod getActionMethod(String name){
        return this.mappers.get(name);
    }

    public CmdCodec getCodec() {
        return codec;
    }
}
