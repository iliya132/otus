package ru.otus.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Table(name = "address")
public class Address implements Cloneable {
    @Id
    @Column("id")
    private Long id;

    @Column("street")
    private String street;

    @Transient
    private Client client;

    @PersistenceCreator
    public Address(Long id, String street) {
        this(id, street, null);
    }

    @Override
    public Address clone() {
        return new Address(this.id, this.street, this.client);
    }

}
