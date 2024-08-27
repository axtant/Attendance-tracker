package com.learningsecurity.demo.controller;


import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learningsecurity.demo.model.Attendance;
import com.learningsecurity.demo.model.User;
import com.learningsecurity.demo.repository.UserRepository;
import com.learningsecurity.demo.service.AttendanceService;
import com.learningsecurity.demo.service.UserService;



@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/getall")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User userInDb = userService.findByUsername(username);
        if (userInDb == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        userInDb.setUsername(user.getUsername());
        userInDb.setEmail(user.getEmail());
        userInDb.setPassword(user.getPassword());

        userService.saveUser(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    

    @DeleteMapping
    public void deleteUser() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        LOGGER.info("Deleting user with username: " + username);
        userRepository.deleteByUsername(authentication.getName());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Attendance>> getAllAttendanceRecords() {
        List<Attendance> attendanceRecords = attendanceService.getAllAttendanceRecords();
        return ResponseEntity.ok(attendanceRecords);
    }
}
