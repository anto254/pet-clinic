package com.antony.petclinic.repository;

import com.antony.petclinic.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
   List<Appointment> findByUserId(Long id);
//
//    @Transactional
//    void deleteByUserId(long userId);
//
//    Appointment delete(Long id);


}
