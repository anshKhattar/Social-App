package com.social.app.service;

import com.social.app.model.User;
import com.social.app.model.User;
import com.social.app.model.UserDetails;
import com.social.app.repository.UserDetailsRepository;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.social.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Override
    @Transactional
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User User = userRepository.findByUsername(username)
                                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return User;
    }

    public UserDetails loadUserByUserId(String userId){
        UserDetails userDetails = userDetailsRepository.findByUserId(userId);

        return userDetails;
    }


    public User getUserByUserId(String userId){
        User user = userRepository.findById(userId).orElse(null);
        return user;
    }

    public User loadUserByUserEmail(String email){
        User user = userRepository.findByEmail(email);

        return user;
    }



    public void saveEnableUser(String userId){
        UserDetails userDetails = loadUserByUserId(userId);
        userDetails.setEnabled(true);
        userDetailsRepository.save(userDetails);
    }
}
