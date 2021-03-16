package org.ba.kclk.dao;

import org.ba.kclk.domain.Client;
import org.ba.kclk.domain.ClientRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientRoleDao  extends JpaRepository<ClientRole, UUID> {
    List<ClientRole> findAllByClient(Client client);
}
