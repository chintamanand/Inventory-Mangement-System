package com.application.dto;

import lombok.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto implements Serializable {

    private static final long serialVersionUID = -2683865501922998906L;

    private Long id;

    private String userId;

    private String userName;

    private String password;

    private String email;

    private Date createdDate;

    private Date lastModifiedDate;

    private Set<RoleDto> roles;

}
