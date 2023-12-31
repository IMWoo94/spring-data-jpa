package study.datajpa.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.repository.MemberRepository;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

	@PersistenceContext
	EntityManager em;

	@Autowired
	MemberRepository memberRepository;

	@Test
	void testEntity() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		em.persist(teamA);
		em.persist(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		Member member3 = new Member("member3", 30, teamB);
		Member member4 = new Member("member4", 40, teamB);
		Member member5 = new Member("member5", 50, teamB);

		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);
		em.persist(member5);
		em.flush();
		em.clear();

		List<Member> members = em.createQuery("select m from Member m ", Member.class).getResultList();
		for (Member member : members) {
			System.out.println("member.getUsername() = " + member.getUsername());
			System.out.println(" -> member.getTeam().getName() = " + member.getTeam().getName());
		}

		em.flush();
		em.clear();

	}

	@Test
	void JpaEventBaseEntity() throws Exception {
		// given
		Member member = new Member("member1");

		memberRepository.save(member); // @PostLoad ,@PrePersist

		Thread.sleep(100);
		member.setUsername("member2");

		em.flush();
		em.clear();

		// when
		Member findMember = memberRepository.findById(member.getId()).get();

		// then
		System.out.println("findMember.createdDate = " + findMember.getCreateDate());
		System.out.println("findMember.updatedDate = " + findMember.getLastModifiedDate());
		System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
		System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());
	}

}