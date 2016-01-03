package com.education.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yzzhao on 12/17/15.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface Public {
    boolean requireWeChatCode() default false;
    boolean requireWeChatUser() default false;
    boolean requireAdminPassword() default false;
    boolean requireAdminAccount() default false;
}
