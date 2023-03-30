package com.social.app.service;

import com.social.app.dto.UserUpdateDTO;
import com.social.app.dto.response.UserDetailsResponseDTO;
import com.social.app.enums.RoleTypeEnum;
import com.social.app.helpers.CloudinaryService;
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
    @Autowired
    CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User User = userRepository.findByUsername(username)
                                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return User;
    }

    public UserDetails fetchUserDetailsByUsername(String username){
        UserDetails dbUser = userDetailsRepository.findByUsername(username).get();
        System.out.println(dbUser);
        return dbUser;
    }
    public UserDetailsResponseDTO loadUserByUserId(String userId){
        UserDetails userDetails = userDetailsRepository.findByUserId(userId).get();

        return new UserDetailsResponseDTO(
                userDetails.getUserId(),
                userDetails.getName(),
                userDetails.getAge(),
                userDetails.getGender(),
                userDetails.getProfilePic());
    }


    public UserDetails loadUserDetailsByUserId(String userId) {
        return userDetailsRepository.findByUserId(userId).get();
    }

    public User getUserByUserId(String userId){
        User user = userRepository.findById(userId).orElse(null);
        return user;
    }

    public User loadUserByUserEmail(String email){
        User user = userRepository.findByEmail(email);

        return user;
    }

    public UserDetails updateUser(String userId, UserUpdateDTO userUpdateDTO){
        UserDetails dbUserDetails = userDetailsRepository.findByUserId(userId).get();
        if (userUpdateDTO.getAge() != 0){
            dbUserDetails.setAge(userUpdateDTO.getAge());
        }
        if (userUpdateDTO.getName() != null){
            dbUserDetails.setName(userUpdateDTO.getName());
        }
        if (userUpdateDTO.getGender() != null){
            dbUserDetails.setGender(userUpdateDTO.getGender());
        }

        if (userUpdateDTO.getProfilePic() != null){
            String updateProfilePicURL = cloudinaryService.upload(userUpdateDTO.getProfilePic());
            dbUserDetails.setProfilePic(updateProfilePicURL);
        }

        userDetailsRepository.save(dbUserDetails);
        return dbUserDetails;
    }




    public void saveEnableUser(String userId){
        UserDetails userDetails = loadUserDetailsByUserId(userId);
        userDetails.setEnabled(true);
        userDetailsRepository.save(userDetails);
    }
}
