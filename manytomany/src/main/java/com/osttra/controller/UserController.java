package com.osttra.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.osttra.entity.User;
import com.osttra.entity.UserGroup;
import com.osttra.helper.JWTHelper;
import com.osttra.service.CustomUserDetailsService;
import com.osttra.service.userdetailservice;
import com.osttra.to.JWTRequest;

@RestController
public class UserController {

	
	@Autowired
	userdetailservice userDetailsService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	JWTHelper jwtHelper;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/check")
	public String check()
	{
		return "checking";
	}
	
	@PostMapping("/token")
	public String generateToken(@RequestBody JWTRequest jwtRequest) {
		
		System.out.println("JWT request is "+jwtRequest);
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
		String jwtToken = jwtHelper.generateToken(userDetails);
		System.out.println("JWT Token is "+jwtToken);
		
		return jwtToken;
		
	}

	
	
	  	@PostMapping("/registeruser")
	    public User addUser(@RequestBody User user) {
	  		
			String password = user.getPassword();
			String encodedPassword = this.bCryptPasswordEncoder.encode(password);
			System.out.println(encodedPassword);
			user.setPassword(encodedPassword);
			
	        return userDetailsService.saveUser(user);
	    }
	  
	    
	    @GetMapping(value="/all", produces = "application/json")
	    @ResponseBody
	    public List<User> getAllUser() {

	    	List<User> users = userDetailsService.getAllUser();
	    	
	    	return users;
	    }
	    
	    @GetMapping("/{username}/groups")
	    @ResponseBody
	    public Set<UserGroup> getUserGroups(@PathVariable String username) {
	    	
	        User user = userDetailsService.getUserById(username);

	        if (user == null) {
	            System.out.println("null");
	        }

	        Set<UserGroup> userGroups = user.getUserGroups();
	        
	        return userGroups;
	    }
	    
	    
	    
	    @PutMapping("/update/{username}")
	    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody User updatedUser) {

	    	
	    	try {
	    		User existingUser = userDetailsService.getUserById(username);
	    		User updated = userDetailsService.saveUser(updatedUser);
	    		
	            return ResponseEntity.ok(updated);
	        }
	        
	    	catch (UsernameNotFoundException ex) {
	            
	    		
	    		Map<String, String> response = new HashMap<>();
	            response.put("error", "User not found");
	            response.put("message", "The requested user does not exist.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    		
	    		
	        }

 

	     

	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	}
	

