package de.flexitrade.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.flexitrade.usermanagement.service.UsermanagementService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/usermanagement")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsermanagementController {
	private final UsermanagementService usermanagementService;
}