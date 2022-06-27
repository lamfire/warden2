package com.lamfire.warden.cmd;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
public @interface CODEC {
    String codec();
}