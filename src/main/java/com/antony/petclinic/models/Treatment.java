package com.antony.petclinic.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor

public class Treatment {
    @Id
    @GeneratedValue
    private Long id;
    private String disease;
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private String date;
    private String species;
    private String email;
    private String petName;
    private String treatmentDesc;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Treatment treatment = (Treatment) o;
        return id != null && Objects.equals(id, treatment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "disease = " + disease + ", " +
                "date = " + date + ", " +
                "species = " + species + ", " +
                "petName = " + petName + ", " +
                "email = " + email + ", " +
                "treatment = " + treatmentDesc + ")";
    }
}
