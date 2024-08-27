package com.learningsecurity.demo.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learningsecurity.demo.model.AttendanceRequest;
import com.learningsecurity.demo.model.User;
import com.learningsecurity.demo.service.AttendanceService;
import com.learningsecurity.demo.service.UserService;
import com.learningsecurity.demo.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService UserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/signup")
    public void signUp(@RequestBody User user) {
        userService.saveUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            UserDetails userDetails = UserDetailsService.loadUserByUsername(user.getUsername());

            // Extract the role from the user details
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER"); // Default role if none found

            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            // Prepare response with JWT and role
            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            response.put("role", role);
            response.put("username", user.getUsername());

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception occurred", e);
            return new ResponseEntity<>(Collections.singletonMap("error", "Incorrect username or password"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(@RequestBody AttendanceRequest request) {
        String username = request.getUsername();
        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();

        try {
            attendanceService.markAttendance(username, latitude, longitude);
            return ResponseEntity.ok("Attendance marked successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
