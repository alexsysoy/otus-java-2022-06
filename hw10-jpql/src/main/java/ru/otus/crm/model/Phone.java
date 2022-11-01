package ru.otus.crm.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Phone implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    private Client client;

    public Phone(Long id, String number) {
        this.id = null;
        this.number = number;
    }

    @Override
    public Phone clone() {
        return new Phone(this.id, this.number, this.client.clone());
    }
}
