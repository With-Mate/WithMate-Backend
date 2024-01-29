package com.gdscewha.withmate.domain.member.controller;

import com.gdscewha.withmate.domain.member.MemberCreateForm;
import com.gdscewha.withmate.domain.member.dto.MemberCreateDto;
import com.gdscewha.withmate.domain.member.service.MemberService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    private final MemberService memberService;
    private final ModelMapper modelMapper;

    private static final String REDIRECT_HOME = "redirect:/";
    private static final String SIGNUP_FORM = "signup_form";

    @Autowired
    public SignupController(MemberService memberService, ModelMapper modelMapper) {
        this.memberService = memberService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("MemberCreateForm", new MemberCreateForm());
        return SIGNUP_FORM;
    }

    @PostMapping("/api/signup")
    public String handleSignupForm(@Valid MemberCreateForm memberCreateForm) {
        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            return SIGNUP_FORM;
        }

        MemberCreateDto memberCreateDto = modelMapper.map(memberCreateForm, MemberCreateDto.class);
        memberService.createMember(memberCreateDto);

        return REDIRECT_HOME;
    }
}

