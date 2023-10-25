package study.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import study.datajpa.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	// @Query 어노테이션은 생략 가능하다.
	// 생략 시 메소드명과 동일한 네임드쿼리가 있는지 확인하고 있으면 쓰고 없으면 그때 메소드 이름을 생성하여 사용된다.
	// @Query(name = "Member.findByUsername")
	List<Member> findByUsername(@Param("username") String username);

	List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

	long countByUsername(String username);

	boolean existsByAge(int age);

	List<Member> findMemberDistinctByUsername(String username);

	List<Member> findFirst3By();

	List<Member> findFirstBy();

	List<Member> findDistinctFirstBy();

	List<Member> findTopBy();

	List<Member> findTop3By();

	List<Member> findDistinctTop3By();
}
