package org.ba.kclk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.ba.kclk.dao.ClientDao;
import org.ba.kclk.dao.RealmDao;
import org.ba.kclk.model.Client;
import org.ba.kclk.model.ClientAttributes;
import org.ba.kclk.model.ClientSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ClientDao clientDao;

    @Autowired
    RealmDao realmDao;

    @Override
    public List<Client> findAll(String realmId) {
        return clientDao.findAllByRealm(realmId).stream()
                .map(this::transformToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Client findByRealmIdAndClientId(String realmId, UUID id) {
        var clientDomain = clientDao.findByRealmIdAndId(realmId, id);
        return clientDomain.map(this::transformToDto).orElse(null);
    }

    @Override
    public Client update(String realmId, Client clientDto) {
        var clientDomain = transformToDomain(clientDto, realmId);
        var saved = clientDao.save(clientDomain);
        return transformToDto(saved);
    }

    @Override
    public Client insert(String realmId, Client clientDto) {
        if (clientDao.findByRealmIdAndClientId(realmId, clientDto.getClientId()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "client with such id exists in realm");

        var clientToSave = transformToDomain(clientDto, realmId);

        clientToSave.setId(UUID.randomUUID());
        var saved = clientDao.save(clientToSave);
        return transformToDto(saved);
    }

    @Override
    public ClientSecret getClientSecret(@NotBlank String realmId, @NotNull UUID id) {
        var client = clientDao.findByRealmIdAndId(realmId, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "client not found"));
        return new ClientSecret().value(client.getClientSecret());
    }

    @Override
    public ClientSecret updateClientSecret(String realmId, UUID id, ClientSecret clientSecret) {
        var client = clientDao.findByRealmIdAndId(realmId, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "client not found"));
        client.setClientSecret(clientSecret.getValue());
        var saved = clientDao.save(client);
        return new ClientSecret().value(saved.getClientSecret());
    }

    org.ba.kclk.domain.Client transformToDomain(Client clientDto, String realmId) {
        var clientToSave = objectMapper.convertValue(clientDto, org.ba.kclk.domain.Client.class);
        var realm = realmDao.findById(realmId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Realm not exists"));
        clientToSave.setRealm(realm);
        clientToSave.setAccessTokenLifespan(clientDto.getAttributes().getAccessTokenLifespan());
        clientToSave.setClientSecret(RandomString.make(36));
        return clientToSave;
    }

    Client transformToDto(org.ba.kclk.domain.Client clientDomain) {
        var clientDto = objectMapper.convertValue(clientDomain, Client.class);
        if (clientDto.getAttributes() == null) clientDto.setAttributes(new ClientAttributes());
        clientDto.getAttributes().setAccessTokenLifespan(clientDomain.getAccessTokenLifespan());
        return clientDto;
    }

}
