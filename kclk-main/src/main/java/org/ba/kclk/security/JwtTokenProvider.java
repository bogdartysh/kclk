package org.ba.kclk.security;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.ba.kclk.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class JwtTokenProvider {
    @Autowired
    TokenService tokenService;

    public Authentication getAuthentication(String token) {
        if (!StringUtils.isEmpty(token)) {
            var userInfo = tokenService.getUserInfo("admin", token);
            return new UsernamePasswordAuthenticationToken("admin", "",
                    userInfo.getRoles().stream().map(s -> (GrantedAuthority) () -> s).collect(Collectors.toList()));
        }

        return new UsernamePasswordAuthenticationToken("any", "", Collections.emptyList());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}