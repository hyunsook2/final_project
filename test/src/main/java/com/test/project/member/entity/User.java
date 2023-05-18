package com.test.project.member.entity;


import com.test.project.member.constant.Role;
import com.test.project.member.dto.JoinDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.password.PasswordEncoder;


@Table(name="user")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="password")
    private String password;

    @Column(nullable = false, name="name")
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, name = "nickname")
    private String nickName;

    @Column(nullable = false, name= "ageRange")
    private String ageRange;

    @Column(nullable = false, name = "mobile")
    private String mobile;

    @Column(nullable = false, name = "gender")
    private String gender;

    public User update(String name, String email, Role role, String ageRange, String mobile, String gender) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.nickName = nickName;
        this.ageRange = ageRange;
        this.mobile = mobile;
        this.gender = gender;
        return this;
    }

    public User(String name, String email, String ageRange, String mobile,String gender) {
        this.name = name;
        this.email = email;
        this.ageRange = ageRange;
        this.mobile = mobile;
        this.gender = gender;
    }

    public static User saveUser(JoinDto joinDto, PasswordEncoder passwordEncoder){
        User user =new User();
        user.setEmail(joinDto.getEmail());
        user.setName(joinDto.getName());
        user.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        user.setNickName(joinDto.getNickName());
        user.setAgeRange(joinDto.getAgeRange());
        user.setMobile(joinDto.getMobile());
        user.setGender(joinDto.getGender());
        user.setNickName(joinDto.getNickName());
        user.setRole(Role.USER);
        return user;
    }
}