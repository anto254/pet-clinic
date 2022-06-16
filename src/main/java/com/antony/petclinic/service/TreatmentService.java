package com.antony.petclinic.service;

import com.antony.petclinic.models.Treatment;
import com.antony.petclinic.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentService {
    private final TreatmentRepository treatmentRepository;

    @Autowired
    public TreatmentService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    public Treatment addTreatment(Treatment treatment){
        return treatmentRepository.save(treatment);
    }
}
