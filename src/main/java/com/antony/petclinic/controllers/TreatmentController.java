package com.antony.petclinic.controllers;



import com.antony.petclinic.models.Treatment;
import com.antony.petclinic.models.exception.ResourceNotFoundException;
import com.antony.petclinic.repository.TreatmentRepository;
import com.antony.petclinic.repository.UserRepository;
import com.antony.petclinic.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
public class TreatmentController {
    private final UserRepository userRepository;
    private final TreatmentRepository treatmentRepository;
    private final TreatmentService treatmentService;

    @Autowired
    public TreatmentController(UserRepository userRepository, TreatmentRepository treatmentRepository, TreatmentService treatmentService) {
        this.userRepository = userRepository;
        this.treatmentRepository = treatmentRepository;
        this.treatmentService = treatmentService;
    }

    @PostMapping("/user/treatments")
    public ResponseEntity<Treatment> addTreatment(@RequestBody Treatment treatment){
          Treatment  treatment1 = treatmentRepository.save(treatment);

        return new ResponseEntity<>(treatment1, HttpStatus.CREATED);

    }

    @GetMapping("/user/allTreatments")
    public ResponseEntity<List<Treatment>> getAllTreatments(){
       List<Treatment>  treatment = treatmentRepository.findAll();
        return new ResponseEntity<>(treatment, HttpStatus.OK);

    }

    @GetMapping("/user/{userEmail}/treatments")
    public ResponseEntity<List<Treatment>> getTreatmentByUserEmail(@PathVariable(value = "userEmail") String userEmail){
        if(!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("Not found user with email: " + userEmail);
        }
        List<Treatment> treatments = treatmentRepository.findByEmail(userEmail);
        return new ResponseEntity<>(treatments, HttpStatus.OK);
    }



    @DeleteMapping("deleteTreatment/{id}")
    public ResponseEntity<HttpStatus> deleteTreatment(@PathVariable("id") long id){
        treatmentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
