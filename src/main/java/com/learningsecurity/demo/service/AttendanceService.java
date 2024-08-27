package com.learningsecurity.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learningsecurity.demo.model.Attendance;
import com.learningsecurity.demo.model.User;
import com.learningsecurity.demo.repository.AttendanceRepository;
import com.learningsecurity.demo.repository.UserRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    public void markAttendance(String username, double latitude, double longitude) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

    
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Attendance attendance = new Attendance();
            attendance.setUser(user);
            attendance.setLatitude(latitude);
            attendance.setLongitude(longitude);
            attendance.setTimestamp(LocalDateTime.now().toString());

            attendanceRepository.save(attendance);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<Attendance> getAllAttendanceRecords() {
        return attendanceRepository.findAll();
    }
}