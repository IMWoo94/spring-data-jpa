package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.domain.Address;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	TeamRepository teamRepository;
	@PersistenceContext
	EntityManager em;

	@Autowired
	MemberQueryRepository memberQueryRepository;

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

	}

	@Test
	void testDtoAndValueAndEmbedded() {
		Member member1 = new Member("AAA", 10, new Address("서울", "123", "경기"));
		Member member2 = new Member("AAA", 20, new Address("서울", "123", "경기"));

		Team teamA = new Team("teamA");
		teamRepository.save(teamA);
		member1.changeTeam(teamA);
		member2.changeTeam(teamA);
		memberRepository.save(member1);
		memberRepository.save(member2);

		List<Address> memberAddress = memberRepository.findMemberAddress("AAA");
		for (Address address : memberAddress) {
			System.out.println("address.toString() = " + address.toString());
		}

		List<String> memberUsername = memberRepository.findMemberUsername("AAA");
		for (String s : memberUsername) {
			System.out.println("s = " + s);
		}

		List<MemberDto> memberDto = memberRepository.findMemberDto(member1.getUsername(), member1.getAge());
		for (MemberDto m : memberDto) {
			System.out.println("m.toString() = " + m.toString());
		}

	}

	@Test
	void paramBindingTest() {
		Member member1 = new Member("AAA", 10, new Address("서울", "123", "경기"));
		Member member2 = new Member("AAA", 20, new Address("서울", "123", "경기"));

		Team teamA = new Team("teamA");
		teamRepository.save(teamA);
		member1.changeTeam(teamA);
		member2.changeTeam(teamA);
		memberRepository.save(member1);
		memberRepository.save(member2);

		memberRepository.findNumberBy(member1.getUsername(), member1.getAge());

		memberRepository.findCollectionBy(Arrays.asList(member1.getUsername(), member2.getUsername()));
	}

	@Test
	void returnTypeTest() {
		Member member1 = new Member("AAA", 10, new Address("서울", "123", "경기"));
		Member member2 = new Member("AAA", 20, new Address("서울", "123", "경기"));

		Team teamA = new Team("teamA");
		teamRepository.save(teamA);
		member1.changeTeam(teamA);
		member2.changeTeam(teamA);
		memberRepository.save(member1);
		memberRepository.save(member2);

		// collection 으로 반환 시 정보 o
		List<Member> collcetionByUsername = memberRepository.findCollcetionByUsername(member1.getUsername());
		System.out.println("collcetionByUsername = " + collcetionByUsername.size());

		// collection 으로 반환 시 정보 x
		List<Member> collcetionByUsername1 = memberRepository.findCollcetionByUsername("none");
		System.out.println("collcetionByUsername1 = " + collcetionByUsername1.size());

		// 단건 조회 시
		// 한건도 없다면 널 리턴 -> 이거는 스프링 데이터 JPA 가 예외를 잡아서 null 로 던저주는 것이다.
		// 원래는 javax.persistence.NoResultException 예외 발생
		Member oneByUsername = memberRepository.findOneByUsername("none");
		System.out.println("oneByUsername = " + oneByUsername);

		// 단건 조회 시 1건 이상인 경우
		// IncorrectResultSizeDataAccessException
		// 실제 예외 -> javax.persistence.NonUniqueResultException
		Assertions.assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
			memberRepository.findOneByUsername(member1.getUsername());
		});

		// 옵셔널 단건 조회 시
		Optional<Member> none = memberRepository.findOptionalByUsername("none");
		System.out.println("none = " + none);

		// 옵셔널 단건 조회 시 결과 2 건 이상인 경우
		Assertions.assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
			memberRepository.findOptionalByUsername(member1.getUsername());
		});
	}

	@Test
	void paging() {

		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member1", 10));

		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));
		int age = 10;

		// when
		Page<Member> members = memberRepository.findByAge(age, pageRequest);
		// Slice<Member> members = memberRepository.findByAge(age, pageRequest);
		// List<Member> members = memberRepository.findByAge(age, pageRequest);

		Page<MemberDto> toMap = members.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

		// then
		List<Member> content = members.getContent();
		for (Member member : members) {
			System.out.println("member.getUsername() = " + member.getUsername());
		}

		assertThat(members.get().count()).isEqualTo(3);
		assertThat(members.getTotalElements()).isEqualTo(6);
		assertThat(members.getSize()).isEqualTo(3);
		assertThat(members.getNumber()).isEqualTo(0);
		assertThat(members.getTotalPages()).isEqualTo(2);
		assertThat(members.isFirst()).isTrue();
		assertThat(members.hasNext()).isTrue();

	}

	@Test
	void bulkUpdate() {

		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 19));
		memberRepository.save(new Member("member3", 20));
		memberRepository.save(new Member("member4", 21));
		memberRepository.save(new Member("member5", 40));

		// when
		int i = memberRepository.bulkAgePlus(20);

		List<Member> byUsername = memberRepository.findByUsername("member5");
		Member member = byUsername.get(0);

		System.out.println("member = " + member);
		// then
		assertThat(member.getAge()).isEqualTo(40);
		assertThat(i).isEqualTo(3);

		// 벌크 연산은 영속성 컨텍스트를 무시하고 DB에 바로 접근한다.
		// 따라서 영속성에 있는 엔티티와 값이 불일치하는 경우가 발생할 수 있느니
		// 벌크 연산은 맨 처음 사용하거나 사용 후 동기화 처리를 하자.

	}

	@Test
	void findMemberLazy() {

		// given
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member1", 10, teamB);
		memberRepository.save(member1);
		memberRepository.save(member2);

		em.flush();
		em.clear();
		// 영속성 컨텍스트 반영 후 클리어 ~ 비어있는상태

		// when
		List<Member> members = memberRepository.findGraphByUsername("member1");
		for (Member member : members) {
			System.out.println("member = " + member);
			System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
			System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
		}

	}

	@Test
	void queryHint() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member1", 10, teamB);
		memberRepository.save(member1);
		memberRepository.save(member2);

		em.flush();
		em.clear();
		// Member findMember = memberRepository.findById(member1.getId()).get();
		// findMember.setUsername("member3");
		System.out.println("==========start==============");

		List<Member> members = memberRepository.findQueryHintByUsername("member1");
		System.out.println("===========findQueryHintByUsername start=============");
		// for (Member member : members) {
		// 	member.setUsername("member2");
		// }

		// System.out.println("========================");
		// List<Member> members2 = memberRepository.findQueryHintByUsername("member1");
		// System.out.println("========================");
		em.find(Member.class, member1.getId());
		System.out.println("========================");
		em.flush();

	}

	@Test
	void LockTest() {

		Member member1 = new Member("member1", 10);
		Member member2 = new Member("member1", 10);
		memberRepository.save(member1);
		memberRepository.save(member2);
		em.flush();
		em.clear();

		List<Member> findMembers = memberRepository.findLockByUsername("member1");

	}

	@Test
	void callCustom() {
		List<Member> memberCustom = memberRepository.findMemberCustom();

		System.out.println("=============");
		memberQueryRepository.findAllMembers();
	}

	@Test
	void specBasic() {
		// given
		Team teamA = new Team("teamA");
		em.persist(teamA);

		Member m1 = new Member("m1", 0, teamA);
		Member m2 = new Member("m2", 0, teamA);

		em.persist(m1);
		em.persist(m2);

		em.flush();
		em.clear();

		// when
		Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
		List<Member> result = memberRepository.findAll(spec);

		assertThat(result.size()).isEqualTo(1);

	}

	@Test
	void queryByExample() {
		// given
		Team teamA = new Team("teamA");
		em.persist(teamA);

		Member m1 = new Member("m1", 0, teamA);
		Member m2 = new Member("m2", 0, teamA);

		em.persist(m1);
		em.persist(m2);

		em.flush();
		em.clear();

		// when
		// Probe
		// 한계 inner 까지는 가능하다.
		// left 조인이 안되며, 제약조건이 단순하게만 사용가능하다.
		Member member = new Member("m1");
		Team team = new Team("teamA");
		member.setTeam(team);

		ExampleMatcher matcher = ExampleMatcher.matching()
			.withIgnorePaths("age");
		Example<Member> example = Example.of(member, matcher);
		List<Member> result = memberRepository.findAll(example);
		assertThat(result.size()).isEqualTo(1);

	}

	@Test
	void projections() {
		// given
		Team teamA = new Team("teamA");
		em.persist(teamA);

		Member m1 = new Member("m1", 0, teamA);
		Member m2 = new Member("m2", 0, teamA);

		em.persist(m1);
		em.persist(m2);

		em.flush();
		em.clear();

		// when
		List<NestedCloseProjections> result = memberRepository.findProjectionsByUsername("m1",
			NestedCloseProjections.class);

		for (NestedCloseProjections nestedCloseProjections : result) {
			System.out.println("nestedCloseProjections.getUsername() = " + nestedCloseProjections.getUsername());
			System.out.println(
				"nestedCloseProjections.getTeam().getName() = " + nestedCloseProjections.getTeam().getName());

		}
	}

	@Test
	void nativeQuery() {
		// given
		Team teamA = new Team("teamA");
		em.persist(teamA);

		Member m1 = new Member("m1", 0, teamA);
		Member m2 = new Member("m2", 0, teamA);

		em.persist(m1);
		em.persist(m2);

		em.flush();
		em.clear();

		Page<MemberProjection> result = memberRepository.findByNativeProjections(PageRequest.of(0, 10));
		List<MemberProjection> content = result.getContent();
		for (MemberProjection memberProjection : content) {
			System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
			System.out.println("memberProjection.getId() = " + memberProjection.getId());
			System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());
		}
	}
}