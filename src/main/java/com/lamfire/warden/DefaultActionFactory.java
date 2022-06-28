package com.lamfire.warden;

import com.lamfire.utils.ObjectFactory;

/**
 * User: lamfire
 * Date: 14-5-9
 * Time: 上午10:08
 */
public class DefaultActionFactory implements ActionFactory {
    private Class<?> actionClass;
    private ObjectFactory<Action> factory;

    public DefaultActionFactory(Class<?> actionClass){
        this.actionClass = actionClass;
        this.factory = new ObjectFactory<Action>((Class<Action>)actionClass);
    }

    @Override
    public Action make() {
        try{
            return  factory.newInstance();
        }catch (Exception e){
              throw new RuntimeException(e);
        }
    }

    protected ObjectFactory<Action> getObjectFactory(){
       return this.factory;
    }
}
