package org.ba.kclk.domain;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "client_role")
public class ClientRole {
    @Id
    @GeneratedValue
    UUID id;

    String name;

    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client;
}
