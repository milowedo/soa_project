package agh.soa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="zones")
@Data
@NoArgsConstructor
public class Zone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idzone")
    private int id;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "zone")
    private List<Street> streets;
}