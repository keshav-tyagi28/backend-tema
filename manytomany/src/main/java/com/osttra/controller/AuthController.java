
package com.osttra.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osttra.helper.JwtUtils;
import com.osttra.repository.UserRepository;
import com.osttra.to.CustomUserDetails;
import com.osttra.to.JWTRequest;
import com.osttra.to.JWTResponse;





@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
	
	@Autowired
	  AuthenticationManager authenticationManager;

	  @Autowired
	  UserRepository userRepository;

	  @Autowired
	  PasswordEncoder encoder;

//	  @Autowired
//	  JWTHelper jwtUtils;
	  
	  @Autowired
	  JwtUtils jwtUtils;

	  
	  @PostMapping("/signin")
	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody JWTRequest loginRequest) {
	      try {
	          Authentication authentication = authenticationManager.authenticate(
	              new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

	          CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

	          SecurityContextHolder.getContext().setAuthentication(authentication);
	          String jwt = jwtUtils.generateToken(userDetails);

	          List<String> roles = userDetails.getAuthorities().stream()
	              .map(item -> item.getAuthority())
	              .collect(Collectors.toList());

	          return ResponseEntity.ok(new JWTResponse(jwt, userDetails.getUsername(), roles));
	      } catch (BadCredentialsException e) {
	          
	          Map<String, String> response = new HashMap<>();
	          response.put("message", "Incorrect username or password.");
	          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	      }
	  }

	  
	  
	  
	  
//	  @PostMapping("/signin")
//	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody JWTRequest loginRequest) {
//
//		 
//	    Authentication authentication = authenticationManager.authenticate(
//	        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//	    
//	    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); 
//
//	    SecurityContextHolder.getContext().setAuthentication(authentication);
////	    String jwt = jwtUtils.generateToken(authentication);
//	    String jwt = jwtUtils.generateToken(userDetails);
//	    
////	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();   
//	   // CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); 
//	    List<String> roles = userDetails.getAuthorities().stream()
//	        .map(item -> item.getAuthority())
//	        .collect(Collectors.toList());
//
//	    return ResponseEntity.ok(new JWTResponse(jwt,userDetails.getUsername(), roles));
//	  
//		  
////		  return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		  }

	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
//	  @PostMapping("/signup")
//	  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//	    System.out.println("hiii "+signUpRequest);
//		  if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//	      return ResponseEntity
//	          .badRequest()
//	          .body(new MessageResponse("Error: Username is already taken!"));
//	    }
//
//	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//	      return ResponseEntity
//	          .badRequest()
//	          .body(new MessageResponse("Error: Email is already in use!"));
//	    }
//
//	    // Create new user's account
//	    User user = new User(signUpRequest.getUsername(), 
//	               signUpRequest.getEmail(),
//	               encoder.encode(signUpRequest.getPassword()));
//
//	    Set<String> strRoles = signUpRequest.getRole();
//	    Set<Role> roles = new HashSet<>();
//
//	    if (strRoles == null) {
//	    	System.out.println("1");
//	      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//	          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//	      roles.add(userRole);
//	    } else {
//	      strRoles.forEach(role -> {
//	        switch (role) {
//	        case "admin":
//	          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//	          roles.add(adminRole);
//
//	          break;
//	        case "mod":
//	          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
//	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//	          roles.add(modRole);
//
//	          break;
//	        default:
//	        	System.out.println("defa");
//	          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//	          roles.add(userRole);
//	        }
//	      });
//	    }
//
//	    user.setRoles(roles);
//	    userRepository.save(user);
//
//	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//	  }

}