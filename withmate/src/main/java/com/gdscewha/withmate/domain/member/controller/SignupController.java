package com.gdscewha.withmate.domain.member.controller;

import com.gdscewha.withmate.domain.member.MemberCreateForm;
import com.gdscewha.withmate.domain.member.dto.MemberCreateDto;
import com.gdscewha.withmate.domain.member.dto.MemberProfileDto;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class SignupController {

    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("MemberCreateForm", new MemberCreateForm());
        return "signup_form";
    }
    @GetMapping("/current")
    public ResponseEntity<?> current() {
        Member member = memberService.getCurrentMember();
        return ResponseEntity.ok().body(member);
    }

    @GetMapping("/")
    public ResponseEntity<?> home() {
        MemberProfileDto memberProfileDto = memberService.getMemberProfile(1L);
        return ResponseEntity.ok().body(memberProfileDto);
    }

    @PostMapping("/signup")
    public String handleSignupForm(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            return "signup_form";
        }

        MemberCreateDto memberCreateDto = modelMapper.map(memberCreateForm, MemberCreateDto.class);
        memberService.createMember(memberCreateDto);

        return "redirect:/";
    }
}

