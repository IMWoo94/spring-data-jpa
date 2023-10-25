package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.domain.Member;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	@PersistenceContext
	EntityManager em;

	@Test
	void testMember() {
		System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
		Member member = new Member("memberA");
		Member savedMember = memberRepository.save(member);

		Member findMember = memberRepository.findById(savedMember.getId()).get();

		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

		assertThat(findMember).isEqualTo(member); // JPA 엔티티 동일성 보장
	}

	@Test
	void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");
		memberRepository.save(member1);
		memberRepository.save(member2);

		// 단건 조회 검증
		Member findMember1 = memberRepository.findById(member1.getId()).get();
		Member findMember2 = memberRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1); // JPA 엔티티 동일성 보장
		assertThat(findMember2).isEqualTo(member2); // JPA 엔티티 동일성 보장

		// 리스트 조회 검증
		List<Member> all = memberRepository.findAll();
		assertThat(all.size()).isEqualTo(2);

		// 카운트 검증
		long count1 = memberRepository.count();
		assertThat(count1).isEqualTo(2);

		// 삭제 검증
		memberRepository.delete(member1);
		memberRepository.delete(member2);

		// 카운트 검증
		long count2 = memberRepository.count();
		assertThat(count2).isEqualTo(0);
	}

	@Test
	void queryMethodFnTest() {
		Member member = new Member("query1");
		memberRepository.save(member);

		em.flush();
		em.clear();

		Member referenceById = memberRepository.getReferenceById(member.getId());
		System.out.println("referenceById.getClass() = " + referenceById.getClass());

		em.flush();
		em.clear();

		List<Member> byUsername = memberRepository.findByUsername(member.getUsername());
		for (Member member1 : byUsername) {
			System.out.println("member1.getUsername() = " + member1.getUsername());
		}
	}

	@Test
	public void findByUsernameAndAgeGreaterThan() {
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("AAA", 20);

		memberRepository.save(member1);
		memberRepository.save(member2);

		List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);

	}

	@Test
	void queryMethodUseTest() {

		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("AAA", 20);
		Member member3 = new Member("AAA", 10);
		Member member4 = new Member("BBB", 20);
		Member member5 = new Member("BBB", 10);
		Member member6 = new Member("BBB", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);
		memberRepository.save(member3);
		memberRepository.save(member4);
		memberRepository.save(member5);
		memberRepository.save(member6);

		em.flush();
		em.clear();

		System.out.println("test ==========================");
		// memberRepository.countByUsername("AAA");
		// boolean result = memberRepository.existsByAge(20);
		// System.out.println("result = " + result);
		// memberRepository.findMemberDistinctByUsername("AAA");
		// memberRepository.findDistinctFirstBy();
		// memberRepository.findFirstBy();
		// memberRepository.findFirst3By();
		// memberRepository.findDistinctTop3By();
		// memberRepository.findTopBy();
		// memberRepository.findTop3By();
	}

	@Test
	void testNamedQuery() {
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("AAA", 20);

		memberRepository.save(member1);
		memberRepository.save(member2);

		List<Member> result = memberRepository.findByUsername("AAA");

		List<Member> user = memberRepository.findUser(member1.getUsername(), member1.getAge());
		for (Member member : user) {
			System.out.println("member.toString() = " + member.toString());
		}

		List<Member> member = memberRepository.findMember(member1.getUsername(), member1.getAge());
		for (Member m : member) {
			System.out.println("m.toString() = " + m.toString());
		}

	}

}