package study.datajpa.repository;

import java.util.List;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import study.datajpa.domain.Member;

/**
 * 사용자 정의 구현 클래스
 * 규칙 : 리포지토리 인터페이스 이름 + Impl
 * 스프링 데이터 JPA 가 인식해서 스프링 빈으로 등록합니다.
 * 스프링 데이터 2.0 부터는 사용자 정의 인터페이스명 + Impl 방식도 지원합니다.
 * 우선순위는 사용자 정의 인터페이스명 -> 리포지토리 인터페이스명 순 이다.
 */
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

	private final EntityManager em;

	@Override
	public List<Member> findMemberCustom() {
		System.out.println("MemberRepositoryCustomImpl.findMemberCustom2");
		return em.createQuery("select m from Member m", Member.class)
			.getResultList();
	}
}
