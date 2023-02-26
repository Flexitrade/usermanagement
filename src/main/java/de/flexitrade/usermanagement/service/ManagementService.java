package de.flexitrade.usermanagement.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import de.flexitrade.common.persistence.entity.User;
import de.flexitrade.common.persistence.repository.UserRepository;
import de.flexitrade.common.web.exception.ApiException;
import de.flexitrade.common.web.request.RegisterUserRequest;
import de.flexitrade.common.web.response.ExceptionResponse;
import de.flexitrade.common.web.response.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ManagementService {
	private final UserRepository userRepository;

	public ResponseEntity<?> registerUser(RegisterUserRequest registerUserRequest) {
		if (userRepository.existsByUsername(registerUserRequest.getUsername())) {
			new ApiException(HttpStatus.BAD_REQUEST, "USERNAME_TAKEN").toResponseEntity();
		}

		if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
			return new ApiException(HttpStatus.BAD_REQUEST, "EMAIL_IN_USE").toResponseEntity();
		}
		// Create new user's account
		try {
			String uniqueId = UUID.randomUUID().toString().replace("-", "");
			User user = new User().toBuilder()
					.username(registerUserRequest.getUsername())
					.email(registerUserRequest.getEmail())
					.password(new BCryptPasswordEncoder().encode(registerUserRequest.getPassword()))
					.uniqueSecret(uniqueId)
					.build();
			userRepository.save(user);
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ExceptionResponse(e.getMessage()));
		}
	}

	public ResponseEntity<?> activateUser(@Valid String username, String secret) {
		final var optUser = userRepository.findByUsername(username);
		if (optUser.isEmpty()) {
			return new ApiException(HttpStatus.BAD_REQUEST, "UNKNOWN_USER").toResponseEntity();
		}
		User user = optUser.get();
		
		if (user.getIsAccountNonLocked() == true || !user.getUniqueSecret().equals(secret)) {
			return new ApiException(HttpStatus.BAD_REQUEST, "KEY_INVALID").toResponseEntity();
		}
		
		user.setIsAccountNonLocked(true);
		user.setUniqueSecret("none");
		userRepository.save(user);
		
		try {
			return ResponseEntity.status(HttpStatus.SEE_OTHER).location(new URI("/success")).build();
		} catch (URISyntaxException e) {
			return new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).toResponseEntity();
		}		
		
	}
}
