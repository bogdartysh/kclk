package org.ba.kclk.service;

import org.ba.kclk.model.Realm;

import java.util.List;

public interface RealmService {
    List<Realm> findAll();
    Realm insert( Realm realm);
    Realm findById(String id);
}
