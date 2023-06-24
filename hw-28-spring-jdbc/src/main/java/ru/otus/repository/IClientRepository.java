package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.models.Client;

@Repository
public interface IClientRepository extends CrudRepository<Client, Long> {
}
