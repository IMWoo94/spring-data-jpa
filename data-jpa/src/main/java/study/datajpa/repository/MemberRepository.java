package study.datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import study.datajpa.domain.Address;
import study.datajpa.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	// @Query 어노테이션은 생략 가능하다.
	// 생략 시 메소드명과 동일한 네임드쿼리가 있는지 확인하고 있으면 쓰고 없으면 그때 메소드 이름을 생성하여 사용된다.
	// @Query(name = "Member.findByUsername")
	@Query("select m from Member m where m.username = :username and m.age > 20")
	List<Member> findByUsername(@Param("username") String username);

	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param("username") String username, @Param("age") int age);

	@Query("select new study.datajpa.repository.MemberDto(m.id, m.username, t.name) from Member m join m.team t where m.username = :username and m.age = :age")
	List<MemberDto> findMemberDto(@Param("username") String username, @Param("age") int age);

	@Query("select m.address from Member m where m.username = :username")
	List<Address> findMemberAddress(@Param("username") String username);

	@Query("select m.username from Member m where m.username = :username")
	List<String> findMemberUsername(@Param("username") String username);

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

	@Query("select m from Member m where m.username = ?1 and m.age= ?2")
	List<Member> findNumberBy(String username, int age);

	@Query("select m from Member m where m.username in :names")
	List<Member> findCollectionBy(@Param("names") Collection<String> names);

	// 반환 타입
	// 컬렉션, 단건, 옵셔널
	@Query("select m from Member m where m.username = :username")
	List<Member> findCollcetionByUsername(@Param("username") String username);

	@Query("select m from Member m where m.username = :username")
	Member findOneByUsername(@Param("username") String username);

	@Query("select m from Member m where m.username = :username")
	Optional<Member> findOptionalByUsername(@Param("username") String username);

	// count 쿼리 사용
	@Query(value = "select m from Member m left join m.team t",
		countQuery = "select count(m.username) from Member m")
	Page<Member> findByAge(int age, Pageable pageable);
	// count 쿼리 사용 안함
	// Slice<Member> findByAge(int age, Pageable pageable);
	//	// count 쿼리 사용 안함
	// List<Member> findByAge(int age, Pageable pageable);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);

	@Query("select m from Member m left join fetch m.team")
	List<Member> findMemberFetchJoin();

	@Override
	@EntityGraph(attributePaths = {"team"})
	List<Member> findAll();

	@EntityGraph(attributePaths = {"team"})
	@Query("select m from Member m")
	List<Member> findMemberEntityGraph();

	// @EntityGraph(attributePaths = {"team"})
	@EntityGraph(value = "Member.all")
	List<Member> findGraphByUsername(@Param("username") String username);

	@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
	List<Member> findQueryHintByUsername(@Param("username") String username);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Member> findLockByUsername(String username);
}
