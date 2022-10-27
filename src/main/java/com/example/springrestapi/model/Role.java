package com.example.springrestapi.model;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;


@Entity
@Data
@Table(name = "roles")
public class Role extends BaseEntity{


    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

    @Override
    public String toString() {
        return new StringJoiner(", ", Role.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("users=" + users)
                .toString();
    }
}
