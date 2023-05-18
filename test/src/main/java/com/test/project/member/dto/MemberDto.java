package com.test.project.member.dto;

import com.test.project.member.constant.Role;
import lombok.Data;
@Data
public class MemberDto {
    private Long id;
    private String email;
    private String name;
    private Role role;

}
