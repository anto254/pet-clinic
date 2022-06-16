package com.antony.petclinic.service;

import com.antony.petclinic.models.Appointment;
//import com.antony.petclinic.models.exception.AppointmentNotFoundException;
import com.antony.petclinic.repository.AppointmentRepository;
import com.antony.petclinic.util.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment addAppointment(Appointment appointment){
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> searchAppointment(final List<SearchCriteria> params) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Appointment> query = builder.createQuery(Appointment.class);
        final Root r = query.from(Appointment.class);

        Predicate predicate = builder.conjunction();
        AppointmentSearchQueryCriteriaConsumer searchConsumer = new AppointmentSearchQueryCriteriaConsumer(predicate, builder, r);
        params.stream().forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }

//    public List<Appointment> getAppointments(){
//        return appointmentRepository.findAll();
//    }
//
//    public Appointment getAppointment(Long id){
//        return appointmentRepository.findById(id).orElseThrow(() ->
//                new AppointmentNotFoundException(id));
//    }
//
//    public Appointment deleteAppointment(Long id){
//        Appointment appointment = getAppointment(id);
//        appointmentRepository.delete(id);
//        return appointment;
//    }
//
//    @Transactional
//    public Appointment editAppointment(Long id, Appointment appointment){
//        Appointment appointmentToEdit = getAppointment(id);
//        appointmentToEdit.setAppointmentDate(appointment.getAppointmentDate());
//        appointmentToEdit.setAppointmentType(appointment.getAppointmentType());
//        appointmentToEdit.setDescription(appointment.getDescription());
//        appointmentToEdit.setAppointmentTime(appointment.getAppointmentTime());
//        appointmentToEdit.setPetNo(appointment.getPetNo());
//        appointmentToEdit.setDescription(appointment.getDescription());
//        appointmentToEdit.setSpecies(appointment.getSpecies());
//        return appointmentToEdit;
//    }
}
