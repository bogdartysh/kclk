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
@Table(name = "realm")
public class Realm {
    @Id
    String id;

    Boolean enabled;

    String secret;
}
