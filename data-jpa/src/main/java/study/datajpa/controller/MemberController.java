package study.datajpa.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

	@GetMapping("/members")
	public Page<MemberDto> list(@Qualifier("member") Pageable pageable) {
		Page<Member> page = memberRepository.findAll(pageable);
		return page.map(MemberDto::new);
	}

	@GetMapping("/members_page")
	public Page<MemberDto> list_page(
		@PageableDefault(size = 10, sort = "username", direction = Sort.Direction.DESC) @Qualifier("paging") Pageable pageable) {
		Page<Member> page = memberRepository.findAll(pageable);
		return page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
	}

	@PostConstruct
	public void init() {
		for (int i = 0; i < 100; i++) {
			memberRepository.save(new Member("user" + i, i));
		}
	}
}
