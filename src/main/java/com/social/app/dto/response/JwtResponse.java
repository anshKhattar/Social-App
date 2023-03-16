package com.social.app.dto.response;

import com.social.app.enums.RoleTypeEnum;
import com.social.app.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String id;
    private String username;
    private String email;
    private RoleTypeEnum role;




}
