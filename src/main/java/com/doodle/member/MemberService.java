package com.doodle.member;

import java.util.Optional;

import com.doodle.entity.Member;

public interface MemberService {

	Member findByEmail(String email);
	Member findByNickname(String nickname);
}
