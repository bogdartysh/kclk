package org.ba.kclk.dao;

import org.ba.kclk.domain.Realm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealmDao  extends JpaRepository<Realm, String> {
}
