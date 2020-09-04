package com.openuniversity.springjwt.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openuniversity.springjwt.models.ERole;
import com.openuniversity.springjwt.models.Role;
import com.openuniversity.springjwt.models.User;
import com.openuniversity.springjwt.payload.request.LoginRequest;
import com.openuniversity.springjwt.payload.request.SignupRequest;
import com.openuniversity.springjwt.payload.response.JwtResponse;
import com.openuniversity.springjwt.payload.response.MessageResponse;
import com.openuniversity.springjwt.repository.RoleRepository;
import com.openuniversity.springjwt.repository.UserRepository;
import com.openuniversity.springjwt.security.jwt.JwtUtils;
import com.openuniversity.springjwt.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		if (signUpRequest.getInitialApplicant().getNic().length() == 9){
//                for (int i =0; i<=initialApplicant.getNic().length();i++){
//                    int nicNew = initialApplicant.getNic().charAt(i);
//                    if (i == 7){
//
//                    }
//                }
			String myNic = "19" + signUpRequest.getInitialApplicant().getNic();
			StringBuilder newNic = new StringBuilder(myNic);
			newNic.insert(7, "0");
			System.out.println(newNic);
			signUpRequest.getInitialApplicant().setNicFake(newNic.toString());
			System.out.println(signUpRequest.getInitialApplicant().getNicFake());
		}else if (signUpRequest.getInitialApplicant().getNic().length() == 12){
			signUpRequest.getInitialApplicant().setNicFake(signUpRequest.getInitialApplicant().getNic());
		}

		//Generate random passwords
		int password = new Random().nextInt();
		long passwordUnsigned = Integer.toUnsignedLong(password);
		String newPassword = String.valueOf(passwordUnsigned);
		System.out.println("password***********" + newPassword);
		System.out.println("userName" + signUpRequest.getInitialApplicant().getNicFake());

		// Create new user's account
		User user = new User(signUpRequest.getInitialApplicant().getNicFake(),
							 signUpRequest.getEmail(),
							 encoder.encode(newPassword), signUpRequest.getInitialApplicant());

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role applicantRole = roleRepository.findByName(ERole.ROLE_APPLICANT)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(applicantRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "student":
					Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(studentRole);

					break;
				case "staff":
					Role staffRole = roleRepository.findByName(ERole.ROLE_STAFF)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(staffRole);

					break;

					case "superadmin":
						Role superAdmin = roleRepository.findByName(ERole.ROLE_SUPER_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(superAdmin);

						break;

					case "admin":
						Role admin = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(admin);

						break;

				default:
					Role applicantRole = roleRepository.findByName(ERole.ROLE_APPLICANT )
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(applicantRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
