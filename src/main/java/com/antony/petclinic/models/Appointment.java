package com.antony.petclinic.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import java.sql.Timestamp;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Appointment {
     @Id
    @GeneratedValue
     private Long id;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    private String appointmentTime;
@JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private String appointmentDate;
    private Long petNo;
    private String species;
    private String appointmentType;
    private String email;
    private String description;
    private AppointmentStatus status = AppointmentStatus.Pending;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Appointment that = (Appointment) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "createdAt = " + createdAt + ", " +
                "appointmentDate = " + appointmentDate + ", " +
                "petNo = " + petNo + ", " +
                "species = " + species + ", " +
                "appointmentType = " + appointmentType + ", " +
                "description = " + description + ", " +
                "email = " + email + ", " +
                "status = " + status + ")";
    }
}
