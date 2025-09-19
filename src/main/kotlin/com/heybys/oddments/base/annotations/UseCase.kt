package com.heybys.oddments.base.annotations

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Service
@Validated
annotation class UseCase(@get:AliasFor(annotation = Service::class) val value: String = "")
