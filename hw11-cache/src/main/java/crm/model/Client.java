package crm.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = {CascadeType.PERSIST},
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    @Column(name = "client_id")
    private List<Phone> phones;

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = setClientIdInPhones(phones);
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name, this.address.clone(), this.phones);
    }

    private List<Phone> setClientIdInPhones(List<Phone> phones) {
        return phones.stream().map(phone -> new Phone(phone.getId(), phone.getNumber(), this)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                '}';
    }
}
