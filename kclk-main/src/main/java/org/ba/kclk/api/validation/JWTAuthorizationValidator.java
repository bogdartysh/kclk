package org.ba.kclk.api.validation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JWTAuthorizationValidator implements
        ConstraintValidator<JWTAuthorizationConstraint, String> {
    JWT validatingJWTInstance;

    @Override
    public void initialize(JWTAuthorizationConstraint value) {
        validatingJWTInstance = new JWT();
    }

    @Override
    public boolean isValid(String contactField,
                           ConstraintValidatorContext cxt) {
        try {
            var decoded = validatingJWTInstance.decodeJwt(
                    contactField.replace("Bearer ", ""));
            return decoded != null;
        } catch (JWTDecodeException e) {
            return false;
        }
    }
}
