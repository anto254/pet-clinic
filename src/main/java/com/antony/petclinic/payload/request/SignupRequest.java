package com.antony.petclinic.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import javax.validation.constraints.*;

@Getter
@Setter
public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;
  private String firstName;
  private String lastName;
  private Long mobile;
  private String gender;
  private String address;
  private String city;

  private Set<String> role;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;


}
