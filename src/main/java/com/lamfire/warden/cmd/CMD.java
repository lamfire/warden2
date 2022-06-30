package com.lamfire.warden.cmd;

import java.lang.annotation.*;

/**
 * Method URL Mapping
 * User: linfan
 * Date: 16-4-18
 * Time: 下午5:07
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD})
public @interface CMD {
    String name() default CMDAction.DEFAULT_CMD;
}

