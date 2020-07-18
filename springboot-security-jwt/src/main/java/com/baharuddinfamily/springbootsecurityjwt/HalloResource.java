package com.baharuddinfamily.springbootsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baharuddinfamily.springbootsecurityjwt.modal.AuthenticationRequest;
import com.baharuddinfamily.springbootsecurityjwt.modal.AuthenticationResponse;
import com.baharuddinfamily.springbootsecurityjwt.services.MyUserDetailsService;
import com.baharuddinfamily.springbootsecurityjwt.util.JwtUtil;

@RestController
public class HalloResource {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService myUserDetailsServices;

	@Autowired
	private JwtUtil jwtUti;

	@GetMapping("/hello")
	public String start() {
		return "Happy Hacking!.";
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthentication(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		final UserDetails userDetails = myUserDetailsServices.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUti.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}
