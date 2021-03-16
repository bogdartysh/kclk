package org.ba.kclk.service;

import org.ba.kclk.api.validation.JWTAuthorizationConstraint;
import org.ba.kclk.model.Token;
import org.ba.kclk.model.UserInfo;
import org.springframework.validation.annotation.Validated;


@Validated
public interface TokenService {
    Token generateToken(String realmId, String clientSecret, String clientId);

    UserInfo getUserInfo(String realmId, @JWTAuthorizationConstraint String token);
}
