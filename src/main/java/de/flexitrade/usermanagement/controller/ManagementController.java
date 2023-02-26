package de.flexitrade.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.flexitrade.common.web.request.RegisterUserRequest;
import de.flexitrade.usermanagement.service.ManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/usermanagement")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ManagementController {
	private final ManagementService registerService;

	@PostMapping(value = "register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequest registerRequest) {
		return registerService.registerUser(registerRequest);
	}
	
	@GetMapping(value = "activate")
	public ResponseEntity<?> activate(@RequestParam String username, @RequestParam String activation) {
		return registerService.activateUser(username, activation);
	}
}