package com.osttra.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.osttra.entity.User;
import com.osttra.entity.UserGroup;
import com.osttra.service.CustomUserGroupDetailsService;
import com.osttra.service.userdetailservice;

@RestController
public class UserGroupController {

	@Autowired
	CustomUserGroupDetailsService customUserGroupDetailsService;
	
//	   @PostMapping("/add")
//	    public UserGroup addUserGroup(@RequestBody UserGroup userGroup) {
//	        return customUserGroupDetailsService.saveUserGroup(userGroup);
//	    }
	    
	    @GetMapping("/allgroups")
	    public List<UserGroup> getAllUserGroups() {
	        return customUserGroupDetailsService.getAllUserGroups();
	    }
	    
	    @GetMapping("/{userGroupId}/users")
	    public Set<User> getUsers(@PathVariable Long userGroupId) {

	        UserGroup userGroup = customUserGroupDetailsService.getUserGroupById(userGroupId);

	        if (userGroup == null) {
	            System.out.println("null");
	        }

	        Set<User> user = userGroup.getUsers();
	        
	        return user;
	    }
}
