package com.learningsecurity.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.learningsecurity.demo.model.User;
import com.learningsecurity.demo.repository.UserRepository;

@Component
public class UserServiceDetailImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
        .orElseThrow(()-> new UsernameNotFoundException(username + "not found"));
        
        return org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .roles(user.getRoles().toArray(new String[0]))
        .build();
    }
    

    
}
