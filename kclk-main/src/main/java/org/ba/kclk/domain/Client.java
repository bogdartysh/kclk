package org.ba.kclk.domain;

import lombok.*;
import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="client")
public class Client {
    @Id
    @GeneratedValue
    UUID id;
    String clientId;
    Boolean enabled;
    String clientSecret;
    Long accessTokenLifespan;

    @ManyToOne
    @JoinColumn(name = "realm_id")
    Realm realm;
}
