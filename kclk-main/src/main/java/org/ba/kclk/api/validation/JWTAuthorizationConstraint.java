package org.ba.kclk.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = JWTAuthorizationValidator.class)
@Target( { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface JWTAuthorizationConstraint {
    String message() default "Invalid beartoken auth";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
