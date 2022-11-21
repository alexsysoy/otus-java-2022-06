package ru.otus.repostory;

import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.domain.Address;
import ru.otus.domain.Client;
import ru.otus.domain.Nickname;
import ru.otus.domain.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientResultSetExtractor implements ResultSetExtractor<List<Client>> {
    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();
        Set<Pair<Long, Phone>> phonePairs = new HashSet<>();
        Long prevClientId = null;
        while (rs.next()) {
            var clientId = Long.valueOf(rs.getLong("client_id"));
            Client client = null;
            if (prevClientId == null || !prevClientId.equals(clientId)) {
                client = new Client(clientId, rs.getString("client_name"), new Address(""), new Nickname(""), new HashSet<Phone>());
                clientList.add(client);
                prevClientId = clientId;
            }

            var phone = rs.getString("client_phone");
            if (phone != null) {
                phonePairs.add(Pair.of(clientId, new Phone(rs.getLong("client_phone_id"), rs.getString("client_phone"))));
            }

            if (client != null) {
                var nickId = Long.valueOf(rs.getLong("nickname_id"));
                client.setNickname(new Nickname(nickId, rs.getString("client_nike")));
                var clientStreet = rs.getString("client_street");
                if (clientStreet != null) {
                    client.setAddress(new Address(clientId, clientStreet));
                }
            }
        }
        addPhonesToClient(clientList, phonePairs);
        return clientList;
    }

    private void addPhonesToClient(ArrayList<Client> clientList, Set<Pair<Long, Phone>> phonePairs) {
        for (Client client : clientList) {
            for (Pair<Long, Phone> phonePair : phonePairs) {
                if (phonePair.getFirst().equals(client.getId())) {
                    client.getPhones().add(phonePair.getSecond());
                }
            }
        }
    }
}