package com.mongobooster.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mongobooster.util.Null;

/**
 * Annotates a field.
 * 
 * @author Dieter De Hen
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Field {

    Class<?> type() default Null.class;

    boolean indexed() default false;
}
