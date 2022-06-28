package com.lamfire.warden;

public class SingletonActionFactory extends DefaultActionFactory {

    private final Action instance ;

    public SingletonActionFactory(Class<?> actionClass) {
        super(actionClass);
        this.instance = super.make();
    }

    @Override
    public Action make() {
        return this.instance;
    }
}
