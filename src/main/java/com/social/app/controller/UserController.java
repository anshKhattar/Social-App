package com.social.app.controller;

import com.social.app.dto.UserUpdateDTO;
import com.social.app.dto.response.MessageResponse;
import com.social.app.dto.response.UserDetailsResponseDTO;
import com.social.app.model.User;
import com.social.app.model.UserDetails;
import com.social.app.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 4800)
@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("api/auth/test/all")
    public MessageResponse allAccess() {
        return new MessageResponse("Server is up.....");
    }

    @PutMapping ("user/updateProfile")
    public UserDetails updateUserProfile(@ModelAttribute UserUpdateDTO userUpdateDTO,
                                         Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return userDetailsService.updateUser(user.getId(),userUpdateDTO);
    }

    @GetMapping("user/{username}")
    public UserDetails getUserDetailsByUserName(@PathVariable String username){
        System.out.println("hi this is hitting");
        return userDetailsService.fetchUserDetailsByUsername(username);

    }

}
