package org.ba.kclk.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.ba.kclk.dao.ClientDao;
import org.ba.kclk.dao.ClientRoleDao;
import org.ba.kclk.dao.RealmDao;
import org.ba.kclk.domain.ClientRole;
import org.ba.kclk.model.Token;
import org.ba.kclk.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    @Autowired
    ClientDao clientDao;

    @Autowired
    ClientRoleDao clientRoleDao;

    @Autowired
    RealmDao realmDao;

    @Autowired
    Clock clock;


    @Override
    public Token generateToken(String realmId, String clientSecret, String clientId) {
        var realm = realmDao.findById(realmId);
        if (realm.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "realm not found");

        var client = clientDao.findByRealmIdAndClientId(realmId, clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "client not found in realm"));
        if (!client.getClientSecret().equals(clientSecret))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid client secret");

        var algorithm = Algorithm.HMAC256(realm.get().getSecret());

        var expirationDate =  Date.from(ZonedDateTime
                .now(clock)
                .plusSeconds(client.getAccessTokenLifespan())
                .toInstant());

        var token = JWT.create()
                .withKeyId(client.getId().toString())
                .withExpiresAt(expirationDate)
                .sign(algorithm);

        return new Token().accessToken(token);
    }

    @Override
    public UserInfo getUserInfo(String realmId, String authorization) {
        var token = authorization.replace("Bearer ", "");
        var realm = realmDao.findById(realmId);
        if (realm.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "realm not found");
        var jwt = JWT.require(Algorithm.HMAC256(realm.get().getSecret())).build();
        var decodedJWT = jwt.verify(token);
        var ui = new UserInfo();
        if (decodedJWT.getExpiresAt().before(Date.from(ZonedDateTime
                .now(clock)
                .toInstant()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token expired");
        }
        var client = clientDao.findById(UUID.fromString(decodedJWT.getKeyId()));
        if (client.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "key id not found");
        }
        ui.setSub(client.get().getId());
        ui.setRoles(clientRoleDao.findAllByClient(client.get())
                .stream().map(ClientRole::getName).collect(Collectors.toList()));
        return ui;
    }
}
