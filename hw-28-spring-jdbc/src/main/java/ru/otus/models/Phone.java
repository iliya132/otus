package ru.otus.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Phone implements Cloneable {
    @Id
    private Long id;
    private String number;
    @Transient
    private Client client;

    @PersistenceCreator
    public Phone(Long id, String number) {
        this(id, number, null);
    }

    @Override
    public Phone clone() {
        return new Phone(id, number, client);
    }
}
