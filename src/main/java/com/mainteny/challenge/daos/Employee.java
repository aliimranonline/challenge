package com.mainteny.challenge.daos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employee_id")
    private int id;

    @Column(name ="first_name")
    private String firstName;

    @Column(name ="last_name")
    private String lastName;
}
