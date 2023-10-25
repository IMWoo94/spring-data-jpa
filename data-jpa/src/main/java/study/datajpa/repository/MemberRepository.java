package study.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import study.datajpa.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	List<Member> findByUsername(String username);

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
