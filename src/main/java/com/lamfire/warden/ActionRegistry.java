package com.lamfire.warden;

import com.lamfire.logger.Logger;
import com.lamfire.utils.ClassLoaderUtils;
import com.lamfire.utils.ClassPathScanner;
import com.lamfire.warden.anno.ACTION;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActionRegistry {
	private static final Logger LOGGER = Logger.getLogger(ActionRegistry.class);
	private final Map<String,ActionFactory> mapping = new HashMap<String, ActionFactory>();

    public void mapping(Class<?> actionClass){
        ACTION actionAnno = actionClass.getAnnotation(ACTION.class);
        if(actionAnno == null){
            LOGGER.warn("["+actionClass.getName() + "] is assignable from Action,but not found 'ACTOIN' annotation.");
            return;
        }
        String uri = actionAnno.path();
        if(actionAnno.singleton() ){
            mappingSingletonAction(uri,actionClass);
        } else{
            mappingAction(uri,actionClass);
        }
    }

    public void mapping(Class<?>[] actionClasses){
        for(Class<?> cls : actionClasses){
            mapping(cls);
        }
    }

    public void mapping(Collection<Class<?>> classes){
        for(Class<?> cls : classes){
            mapping(cls);
        }
    }

	public void mappingAction(String uri,Class<?> actionClass){
		mapping.put(uri, new DefaultActionFactory(actionClass));
		LOGGER.info("[Action]:" + uri +" -> " + actionClass.getName());
	}

    public void mappingSingletonAction(String uri,Class<?> actionClass) {
        mapping.put(uri, new SingletonActionFactory(actionClass));
        LOGGER.info("[SingletonAction]:" + uri +" -> " + actionClass.getName());
    }

    public ActionFactory getActionFactory(String uri){
        ActionFactory factory =  mapping.get(uri);
        if(factory != null){
            return factory;
        }
        return mapping.get("*");
    }
	
	public Action lookup(ActionContext context){
        ActionFactory factory = getActionFactory(context.getPath());
        if(factory == null){
            return null;
        }
        return factory.make();
	}
	
	public void mappingPackage(String packageName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Set<Class> set = ClassPathScanner.scan(packageName,true,false,false,null);
        //Set<Class<?>> set = ClassLoaderUtils.getClasses(packageName);
		for(Class<?> clzz : set){
			if(!Action.class.isAssignableFrom(clzz)){
				continue;
			}
            Class<? extends Action> actionClass =  (Class<? extends Action>)clzz;
            mapping(actionClass);
		}
	}

    public boolean isEmpty(){
        return mapping.isEmpty();
    }
}
