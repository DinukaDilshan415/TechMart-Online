package me.dinuka.tmo.util.monitor;

import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Target({TYPE, FIELD, PARAMETER, METHOD})
@Retention(RUNTIME)
public @interface MetricsCollectorQualifier {
}

