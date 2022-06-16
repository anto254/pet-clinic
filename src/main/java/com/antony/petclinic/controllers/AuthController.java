package com.antony.petclinic.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.antony.petclinic.models.*;
import com.antony.petclinic.models.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.antony.petclinic.payload.request.LoginRequest;
import com.antony.petclinic.payload.request.SignupRequest;
import com.antony.petclinic.payload.response.JwtResponse;
import com.antony.petclinic.payload.response.MessageResponse;
import com.antony.petclinic.repository.RoleRepository;
import com.antony.petclinic.repository.UserRepository;
import com.antony.petclinic.security.jwt.JwtUtils;
import com.antony.petclinic.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {


  
  AuthenticationManager authenticationManager;
  UserRepository userRepository;
  RoleRepository roleRepository;
  PasswordEncoder encoder;
  JwtUtils jwtUtils;

  
  @Autowired
  public AuthController(AuthenticationManager authenticationManager, 
                        UserRepository userRepository, 
                        RoleRepository roleRepository,
                        PasswordEncoder encoder,

                        JwtUtils jwtUtils){
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;

    
  }
  
  
  
  
  
  

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), 
               signUpRequest.getEmail(),
            signUpRequest.getFirstName(),
            signUpRequest.getLastName(),
            signUpRequest.getMobile(),
            signUpRequest.getGender(),
            signUpRequest.getAddress(),
            signUpRequest.getCity(),
               encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "vet":
          Role modRole = roleRepository.findByName(ERole.ROLE_VET)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @GetMapping("/user/allUsers")
  public ResponseEntity<List<User>> getAllUsers(){
    List<User>  user = userRepository.findAll();
    return new ResponseEntity<>(user, HttpStatus.OK);

  }

  @PutMapping("changePass/{id}")
  public ResponseEntity<User> changePassword(@PathVariable("id") long id, @RequestBody User user){
    User _user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));
    _user.setPassword(encoder.encode(user.getPassword()));
    return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);

  }



  @DeleteMapping("/deleteUser/{id}")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id){
    userRepository.deleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/getUserById/{userId}")
  public Optional<User> getUserById(@PathVariable("userId") Long userId){
    if (!userRepository.existsById(userId)){
      throw new ResourceNotFoundException("Not found appointment with id = " + userId);
    }

    return   userRepository.findById(userId);


  }

  @PutMapping("updateUser/{id}")
  public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user){
    User _user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));
    _user.setAddress(user.getAddress());
    _user.setCity(user.getCity());
    _user.setEmail(user.getEmail());
    _user.setFirstName(user.getFirstName());
    _user.setLastName(user.getLastName());
    _user.setMobile(user.getMobile());

    return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);

  }

}
