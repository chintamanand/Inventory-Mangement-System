package com.application.controller;

import com.application.config.Constants;
import com.application.config.JwtUtils;
import com.application.dto.JwtResponse;
import com.application.dto.LoginRequest;
import com.application.dto.MessageResponse;
import com.application.dto.SignupRequest;
import com.application.entity.RoleEntity;
import com.application.entity.UserEntity;
import com.application.exception.ServerException;
import com.application.repository.RoleRepository;
import com.application.repository.UserRepository;
import com.application.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
@Log4j2
public class AuthzController {

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
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              HttpServletRequest request) {
        log.info("Entered into signIn Method()");
        log.info("Login Request body -- " + loginRequest.toString());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new ServerException("Invalid UserName and password were passed", Constants.INTERNAL_SERVER,
                    request.getRequestURL().toString(), "authenticateUser()");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwtToken, userDetails.getId(), userDetails.getUsername(),
                userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                          HttpServletRequest request) {
        log.info("Entered RegisterUser() method");
        log.info("SignUpRequest -- " + signUpRequest.toString());
        log.info("Http Servlet Request -- " + request.toString());
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        UserEntity user = new UserEntity(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<RoleEntity> roles = new HashSet<>();

        if (strRoles == null) {
            RoleEntity userRole = roleRepository.findByName(Constants.ERole.ROLE_USER)
                    .orElseThrow(() -> new ServerException(Constants.ROLE_NOT_FOUND, Constants.INTERNAL_SERVER,
                                    request.getRequestURL().toString(), Constants.REGISTER_USER_METHOD));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleEntity adminRole = roleRepository.findByName(Constants.ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new ServerException("Admin " + Constants.ROLE_NOT_FOUND, Constants.INTERNAL_SERVER,
                                        request.getRequestURL().toString(), Constants.REGISTER_USER_METHOD));
                        if (Boolean.TRUE.equals(adminRole.isActive())) {
                            roles.add(adminRole);
                        } else {
                            throw new ServerException("Admin " + Constants.ROLE_NOT_ACTIVE, Constants.INTERNAL_SERVER,
                                    request.getRequestURL().toString(), Constants.REGISTER_USER_METHOD);
                        }
                        break;
                    case "mod":
                        RoleEntity modRole = roleRepository.findByName(Constants.ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new ServerException("Mod " + Constants.ROLE_NOT_FOUND, Constants.INTERNAL_SERVER,
                                        request.getRequestURL().toString(), Constants.REGISTER_USER_METHOD));

                        if (Boolean.TRUE.equals(modRole.isActive())) {
                            roles.add(modRole);
                        } else {
                            throw new ServerException("Mod " + Constants.ROLE_NOT_ACTIVE, Constants.INTERNAL_SERVER,
                                    request.getRequestURL().toString(), Constants.REGISTER_USER_METHOD);
                        }
                        break;
                    default:
                        RoleEntity userRole = roleRepository.findByName(Constants.ERole.ROLE_USER)
                                .orElseThrow(() -> new ServerException(Constants.ROLE_NOT_FOUND, Constants.INTERNAL_SERVER,
                                        request.getRequestURL().toString(), Constants.REGISTER_USER_METHOD));

                        if (Boolean.TRUE.equals(userRole.isActive())) {
                            roles.add(userRole);
                        } else {
                            throw new ServerException(Constants.ROLE_NOT_ACTIVE, Constants.INTERNAL_SERVER,
                                    request.getRequestURL().toString(), Constants.REGISTER_USER_METHOD);
                        }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
