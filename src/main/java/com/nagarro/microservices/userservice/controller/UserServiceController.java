package com.nagarro.microservices.userservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.microservices.userservice.Configuration;



@RestController
@RequestMapping("/api/user")
public class UserServiceController {

	
	@Autowired
	Configuration configuration;
	
	
	
	@GetMapping("/test")
	public String getOrderTest() {
		
		return "POC - Devops - CI CD using Jenkins & OpenShift Phase-I";
	}

	
	
	
	

}

