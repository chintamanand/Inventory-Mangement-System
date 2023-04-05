package com.application.service;

import com.application.entity.UserEntity;
import com.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //https://www.bezkoder.com/spring-boot-jwt-authentication/
    //https://www.bezkoder.com/angular-spring-boot-jwt-auth/
    //https://www.bezkoder.com/angular-12-jwt-auth/
    //https://www.digitalocean.com/community/tutorials/angular-reactive-forms-introduction

    //https://www.bezkoder.com/spring-boot-jwt-authentication/

    @Autowired
    UserRepository userRepository;

    //From username, get UserDetails to create an Authentication object
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

}
