package com.test.project.member.dto;

import com.test.project.member.constant.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class JoinDto {


    @NotEmpty(message = "이메일은 필수 입력입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;
    @NotBlank(message = "이름은 필수 입력입니다.")
    private String name;
    @NotEmpty(message = "비밀번호는 필수 입력입니다.")
    @Length(min=8,max=16,message="비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String password;

    @NotEmpty(message = "필수")
    private String nickName;

    @Column(name="age_range")
    @NotEmpty(message = "필수")
    private String ageRange;

    @NotEmpty(message = "필수")
    private String mobile;

    @NotEmpty(message = "필수")
    private String gender;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;


}
