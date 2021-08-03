package com.lxd.util;

import java.lang.annotation.*;

/**
 * 当前用户
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {

}
