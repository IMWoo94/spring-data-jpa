package study.datajpa.controller;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.datajpa.domain.Member;
import study.datajpa.repository.MemberDto;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberRepository memberRepository;

	@GetMapping("/members/{id}")
	public String findMember(@PathVariable("id") Long id) {
		Member member = memberRepository.findById(id).get();
		return member.getUsername();
	}

	@GetMapping("/members2/{id}")
	public String findMember(@PathVariable("id") Member member) {
		return member.getUsername();
	}

	@GetMapping("/members3/{id}")
	public String findMember(@PathVariable("id") MemberDto member) {
		return member.getUsername();
	}

	@PostConstruct
	public void init() {
		Member member = new Member("member1");
		memberRepository.save(member);
	}
}