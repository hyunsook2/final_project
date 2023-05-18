package com.test.project.member.service;

import com.test.project.member.entity.User;
import com.test.project.member.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public User saveMember(User user){
        validateDuplicateMember(user);
        return userRepository.save(user);
    }
    private void validateDuplicateMember(User user){
        User findUser = userRepository.findByEmail(user.getEmail());
        if(findUser !=null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user ==null){
            throw new UsernameNotFoundException(email);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword()) // 패스워드도 설정해야 합니다
                .roles(user.getRole().toString())
                .build();
    }
}
