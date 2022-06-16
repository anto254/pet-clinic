package com.antony.petclinic.models.exception;

import java.text.MessageFormat;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(final Long id) {
        super(MessageFormat.format("could not find appointment with id: {0}", id));
    }
}
