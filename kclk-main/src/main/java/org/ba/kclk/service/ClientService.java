package org.ba.kclk.service;

import org.ba.kclk.model.Client;
import org.ba.kclk.model.ClientSecret;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<Client> findAll(String realmId);

    Client findByRealmIdAndClientId(String realmId, UUID id);

    Client update(String realmId, Client client);

    Client insert(String realmId, Client client);

    ClientSecret getClientSecret(String realmId, UUID id);

    ClientSecret updateClientSecret(String realmId, UUID id, ClientSecret clientSecret);
}
