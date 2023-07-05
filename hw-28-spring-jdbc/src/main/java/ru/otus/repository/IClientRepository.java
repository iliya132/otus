package ru.otus.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.models.Client;

@Repository
public interface IClientRepository extends ListCrudRepository<Client, Long> {
}
