package com.lamfire.warden.cmd;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD })
public @interface CACHED {
	int maxElements() default 1000;
    long timeToLiveMillis() default 60000;
}
