package org.ba.kclk.api;

import io.org.ba.kclk.api.AuthApi;
import lombok.extern.slf4j.Slf4j;
import org.ba.kclk.model.*;
import org.ba.kclk.service.ClientService;
import org.ba.kclk.service.RealmService;
import org.ba.kclk.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@Validated
public class AuthRestController implements AuthApi {
    @Autowired
    ClientService clientService;


    @Autowired
    RealmService realmService;

    @Autowired
    TokenService tokenService;


    @Override
    public ResponseEntity<List<Realm>> authAdminRealmsGet() {
        return new ResponseEntity<>(
                realmService.findAll(),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Realm> authAdminRealmsRealmIdGet(String realmId) {
        return new ResponseEntity<>(
                realmService.findById(realmId),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Realm> authAdminRealmsPost(Realm realm) {
        return new ResponseEntity<>(
                realmService.insert(realm),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<Client>> authAdminRealmsRealmIdClientsGet(String realmId) {
        return new ResponseEntity<>(
                clientService.findAll(realmId),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Client> authAdminRealmsRealmIdClientsIdGet(String realmId, UUID id) {
        return new ResponseEntity<>(
                clientService.findByRealmIdAndClientId(realmId, id),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Client> authAdminRealmsRealmIdClientsIdPut(String realmId, UUID clientId, Client client) {
        if (!clientId.equals(client.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "client's id is immutable");
        return new ResponseEntity<>(
                clientService.update(realmId, client),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Client> authAdminRealmsRealmIdClientsPost(String realmId, Client client) {
        return new ResponseEntity<>(
                clientService.insert(realmId, client),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Token> authRealmsRealmIdProtocolOpenidConnectTokenPost(
            String realmId, String grantType, String clientSecret, String clientId) {
        return new ResponseEntity<>(tokenService.generateToken(realmId, clientSecret, clientId), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<UserInfo> authRealmsRealmIdProtocolOpenidConnectUserinfoPost(
            String realmId, String authorization) {
        return new ResponseEntity<>(
                tokenService.getUserInfo(realmId, authorization),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ClientSecret> authAdminRealmsRealmIdClientsIdClientSecretGet(String realmId, UUID id) {
        return new ResponseEntity<>(
                clientService.getClientSecret(realmId, id),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ClientSecret> authAdminRealmsRealmIdClientsIdClientSecretPost(String realmId, UUID id, ClientSecret clientSecret) {
        return new ResponseEntity<>(
                clientService.updateClientSecret(realmId, id, clientSecret),
                HttpStatus.OK);
    }
}
