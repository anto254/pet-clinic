package com.antony.petclinic.controllers;


import com.antony.petclinic.models.Appointment;

import com.antony.petclinic.models.User;
import com.antony.petclinic.models.exception.ResourceNotFoundException;
import com.antony.petclinic.repository.AppointmentRepository;
import com.antony.petclinic.repository.UserRepository;
import com.antony.petclinic.service.AppointmentService;
import com.antony.petclinic.util.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentController(AppointmentService appointmentService,
                                 UserRepository userRepository,
                                 AppointmentRepository appointmentRepository) {
        this.appointmentService =  appointmentService;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;

    }

    @PostMapping("/user/{userId}/appointments")
    public ResponseEntity<Appointment> addAppointment(@PathVariable(value = "userId") Long userId,
                                                      @RequestBody Appointment appointment){
        Appointment appointment1 = userRepository.findById(userId).map(user -> {appointment.setUser(user);
        return appointmentService.addAppointment(appointment);}).orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + userId));

        return new ResponseEntity<>(appointment1, HttpStatus.CREATED);

    }


    @GetMapping("/getAppointmentById/{appointmentId}")
    public Optional<Appointment> getAppointmentById(@PathVariable("appointmentId") Long appointmentId){
        if (!appointmentRepository.existsById(appointmentId)){
            throw new ResourceNotFoundException("Not found appointment with id = " + appointmentId);
        }

       return   appointmentRepository.findById(appointmentId);


    }


    @GetMapping("/user/{userId}/appointments")
    public ResponseEntity<List<Appointment>>getAppointmentsByUserId(@PathVariable(value = "userId") Long userId) {
       if(!userRepository.existsById(userId)){
           throw new ResourceNotFoundException("Not found user with id = " + userId);
       }

       List<Appointment> appointments = appointmentRepository.findByUserId(userId);
       return new ResponseEntity<>(appointments, HttpStatus.OK);

    }

    @GetMapping("/user/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(){
        List<Appointment>  appointments = appointmentRepository.findAll();
        return new ResponseEntity<>(appointments, HttpStatus.OK);

    }

    @GetMapping("/searchAppointment")
    public List<Appointment> search(@RequestParam(value = "search", required = false) String search) {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }
        return appointmentService.searchAppointment(params);
    }


    @DeleteMapping("deleteAppointment/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id){
        appointmentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("editAppointment/{id}")
    public ResponseEntity<Appointment> updateComment(@PathVariable("id") long id, @RequestBody Appointment appointment) {
        Appointment appointmentToEdit = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment " + id + "not found"));

        appointmentToEdit.setAppointmentDate(appointment.getAppointmentDate());
        appointmentToEdit.setAppointmentType(appointment.getAppointmentType());
        appointmentToEdit.setDescription(appointment.getDescription());
        appointmentToEdit.setAppointmentTime(appointment.getAppointmentTime());
        appointmentToEdit.setPetNo(appointment.getPetNo());
        appointmentToEdit.setSpecies(appointment.getSpecies());

        return new ResponseEntity<>(appointmentRepository.save(appointmentToEdit), HttpStatus.OK);
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Appointment> updateStatus(@PathVariable("id") long id, @RequestBody Appointment appointment){
        Appointment appointment1 = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        appointment1.setStatus(appointment.getStatus());

        return  new ResponseEntity<>(appointmentRepository.save(appointment1), HttpStatus.OK);
    }



}
