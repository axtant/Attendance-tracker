package com.learningsecurity.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learningsecurity.demo.model.Attendance;



public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
