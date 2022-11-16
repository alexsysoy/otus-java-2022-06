package ru.otus.repostory;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.domain.Client;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Override
    @Query(value = """
            select c.id           as client_id,
                   c.name         as client_name,
                   a.client_id    as address_id,
                   a.street       as client_street,
                   n.id           as nickname_id,
                   n.nick         as client_nike,
                   p.id           as client_phone_id,
                   p.number       as client_phone
            from client c
                     left outer join address a
                                     on c.id = a.client_id
                     left outer join nickname n
                                     on c.id = n.client_id
                     left outer join phone p
                                     on p.client_id = c.id
            order by c.id
                                                          """,
            resultSetExtractorClass = ClientResultSetExtractor.class)
    List<Client> findAll();

    @Query(value = """
            select c.id           as client_id,
                   c.name         as client_name,
                   a.client_id    as address_id,
                   a.street       as client_street,
                   n.id           as nickname_id,
                   n.nick         as client_nike,
                   p.id           as client_phone_id,
                   p.number       as client_phone
            from client c
                     left outer join address a
                                     on c.id = a.client_id
                     left outer join nickname n
                                     on c.id = n.client_id
                     left outer join phone p
                                     on p.client_id = c.id
            where c.name = :name
                                                          """,
            resultSetExtractorClass = ClientResultSetExtractor.class)
    List<Client> findByName(@Param("name") String name);
}
