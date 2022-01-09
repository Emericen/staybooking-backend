package com.usc.staybooking.service;
import com.usc.staybooking.exception.UserAlreadyExistException;
import com.usc.staybooking.model.Authority;
import com.usc.staybooking.model.User;
import com.usc.staybooking.model.UserRole;
import com.usc.staybooking.repository.AuthorityRepository;
import com.usc.staybooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public RegisterService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(User user, UserRole role) {
        if (userRepository.existsById(user.getUsername())) {
            throw new UserAlreadyExistException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        authorityRepository.save(new Authority(user.getUsername(), role.name()));
    }
}
