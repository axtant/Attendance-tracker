package com.learningsecurity.demo.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learningsecurity.demo.model.User;
import com.learningsecurity.demo.repository.UserRepository;

@Service
public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        return userRepository.save(user);
    }

    public void deleteUserByUsername(String username) {
        LOGGER.info("Attempting to delete user with username: " + username);
        userRepository.deleteByUsername(username);
        LOGGER.info("User with username " + username + " has been deleted");
    }

    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
