package com.test.project.member.controller;

import com.test.project.member.dto.JoinDto;
import com.test.project.member.entity.User;
import com.test.project.member.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public String joinForm(Model model, HttpSession session) {
        JoinDto joinDto = (JoinDto) session.getAttribute("joinDto");
        session.removeAttribute("joinDto"); // 세션에서 joinDto 제거

        if (joinDto != null) {
            String mobile = joinDto.getMobile();
            if (mobile != null) {
                mobile = mobile.replace("+82", "0").replaceAll("\\s|-", "");
                joinDto.setMobile(mobile);
            }
            System.out.println(joinDto);
            model.addAttribute("joinDto", joinDto);
            model.addAttribute("readOnly", true);
        } else {
            model.addAttribute("joinDto", new JoinDto());
            model.addAttribute("readOnly", false);
        }

        return "member/join";
    }


    @PostMapping("/join")
    public String joinSubmit(@Valid JoinDto joinDto, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        try {
            User user = User.saveUser(joinDto, passwordEncoder);
            userService.saveMember(user);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }

        // 세션에서 joinDto 제거
        session.removeAttribute("joinDto");

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() { return "member/login";}


    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/login";
    }

}
