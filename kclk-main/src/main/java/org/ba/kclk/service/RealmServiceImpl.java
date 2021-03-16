package org.ba.kclk.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.logging.log4j.util.Strings;
import org.ba.kclk.dao.RealmDao;
import org.ba.kclk.model.Realm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RealmServiceImpl implements RealmService {
    @Autowired
    RealmDao realmDao;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public List<Realm> findAll() {
        return realmDao.findAll().stream().map(c -> objectMapper.convertValue(c, Realm.class)).collect(Collectors.toList());
    }

    @Override
    public Realm insert(Realm realm) {
        if (realmDao.findById(realm.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "found existed" + realm.getId());
        }

        var realmToSave = objectMapper.convertValue(realm, org.ba.kclk.domain.Realm.class);

        if (Strings.isEmpty(realmToSave.getSecret())) {
            realmToSave.setSecret(RandomString.make(255));
        }

        var saved = realmDao.save(realmToSave);
        return objectMapper.convertValue(saved, Realm.class);
    }

    @Override
    public Realm findById(String id) {
        var r = realmDao.findById(id);
        return r.map(realm -> objectMapper.convertValue(realm, Realm.class)).orElse(null);
    }
}
