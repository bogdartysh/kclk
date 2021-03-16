package org.ba.kclk.dao;

import org.ba.kclk.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientDao extends JpaRepository<Client, UUID> {

    @Query("SELECT c " +
            "FROM Client c " +
            "WHERE c.id = :id " +
            "AND c.realm.id = :realmId")
    Optional<Client> findByRealmIdAndId(String realmId, UUID id);

    @Query("SELECT c " +
            "FROM Client c " +
            "WHERE c.clientId = :clientId " +
            "AND c.realm.id = :realmId")
    Optional<Client> findByRealmIdAndClientId(String realmId, String clientId);

    @Query("SELECT c " +
            "FROM Client c " +
            "WHERE c.realm.id = :realmId")
    List<Client> findAllByRealm(String realmId);
}
