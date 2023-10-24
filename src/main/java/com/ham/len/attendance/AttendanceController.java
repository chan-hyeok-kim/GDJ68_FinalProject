package com.ham.len.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/attendance/*")
public class AttendanceController {
	@Autowired
	private AttendanceService attendanceService;
	
	@GetMapping("status")
	public String getStatus(@ModelAttribute AttendanceVO humanResourceVO) {
		attendanceService.getStatus();
		
		return "attendance/status";
	}	
}