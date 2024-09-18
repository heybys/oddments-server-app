package com.heybys.oddments.base.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public @interface UseCase {

    @AliasFor(annotation = Service.class)
    String value() default "";
}
