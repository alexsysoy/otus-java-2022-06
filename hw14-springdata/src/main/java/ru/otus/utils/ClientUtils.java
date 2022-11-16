package ru.otus.utils;

import org.springframework.stereotype.Service;
import ru.otus.domain.Client;
import ru.otus.domain.Phone;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class ClientUtils {
    public void convertStringToPhonesWithoutDTO(Client client) {
        Set<Phone> result = new HashSet<>();
        Set<Phone> phones = client.getPhones();
        if (phones != null && !phones.isEmpty()) {
            Phone phone = phones.stream().findFirst().orElseThrow();
            Arrays.stream(phone.getNumber().split(" ")).forEach(number -> result.add(new Phone(number)));
        }
        client.setPhones(result);
    }
}
