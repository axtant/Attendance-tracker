package com.learningsecurity.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {
    private String username;
    private double latitude;
    private double longitude;
    private String timestamp;
}
