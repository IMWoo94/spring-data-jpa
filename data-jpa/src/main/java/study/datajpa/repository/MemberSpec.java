package study.datajpa.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import study.datajpa.domain.Member;
import study.datajpa.domain.Team;

public class MemberSpec {

	public static Specification<Member> teamName(final String teamName) {
		return new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				if (StringUtils.isEmpty(teamName)) {
					return null;
				}

				Join<Member, Team> t = root.join("team", JoinType.INNER);// 회원과 팀 조인
				return criteriaBuilder.equal(t.get("name"), teamName);
			}
		};
	}

	public static Specification<Member> username(final String username) {
		return new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				if (StringUtils.isEmpty(username)) {
					return null;
				}
				return criteriaBuilder.equal(root.get("username"), username);
			}
		};
	}

}
