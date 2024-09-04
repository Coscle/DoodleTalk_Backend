package com.doodle.member;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.doodle.entity.Member;
import com.doodle.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV1 implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findByEmail(String email) {
        log.info("찾는 멤버의 이메일: {}", email);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일로 회원을 찾을 수 없습니다: " + email));
    }

    @Override
    public Member findByNickname(String nickname) {
        log.info("찾는 멤버의 닉네임: {}", nickname);
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("해당 닉네임으로 회원을 찾을 수 없습니다: " + nickname));
    }
}
