package study.datajpa.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.datajpa.domain.Member;
import study.datajpa.repository.MemberDto;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class HelloController {

	private final MemberRepository memberRepository;

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@GetMapping("/hello/jpapaging")
	public ResponseEntity<Page<MemberDto>> memberPaging() {
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));
		memberRepository.save(new Member("member6", 10));

		Page<Member> members = memberRepository.findByAge(10, PageRequest.of(0, 4));
		Page<MemberDto> map = members.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
