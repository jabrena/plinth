package info.jab.ms.gods.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "greek_god")
public class GreekGod extends PanacheEntity {

    @Column(name = "name", nullable = false, unique = true, length = 100)
    public String name;
}
