package com.heartlink.member.controller;

import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입, 로그인 페이지 이동
    @GetMapping("/sign")
    public String signInUp(Model model) {
        model.addAttribute("memberDto", new MemberDto()); // 빈 객체를 추가하여 폼 바인딩을 준비
        return "member/sign-in-up"; // templates/member/sign-in-up.html을 렌더링
    }

    // 회원가입 요청 처리
    @PostMapping("/register")
    public String registerMember(@Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
        // 유효성 검사 실패 시 회원가입 페이지로 다시 이동
        if (bindingResult.hasErrors()) {
            return "member/sign-in-up";
        }

        // 비밀번호와 확인 비밀번호가 일치하지 않을 경우 처리
        if (!memberDto.isPasswordConfirmed()) {
            model.addAttribute("error", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "member/sign-in-up";
        }

        try {
            memberService.registerMember(memberDto);
            model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
            return "redirect:/member/sign"; // 회원가입 후 로그인 페이지로 리디렉션
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "member/sign-in-up"; // 에러 발생 시 다시 회원가입 페이지로 이동
        }
    }

    // 이메일 중복 체크 요청 처리
    @GetMapping("/check-email")
    @ResponseBody
    public String checkEmailDuplicate(@RequestParam String email) {
        if (memberService.isEmailDuplicate(email)) {
            return "이미 사용 중인 이메일입니다.";
        }
        return "사용 가능한 이메일입니다.";
    }

    // 닉네임 중복 체크 요청 처리
    @GetMapping("/check-nickname")
    @ResponseBody
    public String checkNicknameDuplicate(@RequestParam String nickname) {
        if (memberService.isNicknameDuplicate(nickname)) {
            return "이미 사용 중인 닉네임입니다.";
        }
        return "사용 가능한 닉네임입니다.";
    }
}
