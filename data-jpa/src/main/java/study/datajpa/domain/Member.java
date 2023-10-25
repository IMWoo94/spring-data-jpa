package study.datajpa.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age", "address"})
// @SQLDelete(sql = "update member set active = false where member_id = ? ")
// @Where(clause = "active = true")
@NamedQuery(
	name = "Member.findByUsername",
	query = "select m from Member m where m.username = :username"
)
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	private String username;

	private int age;

	@Embedded
	private Address address;

	// @Column(columnDefinition = "boolean default true")
	// private boolean active;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	public Member(String username) {
		this.username = username;
		// this.active = true;
	}

	public Member(String username, int age) {
		this.username = username;
		this.age = age;
	}

	public Member(String username, int age, Address address) {
		this.username = username;
		this.age = age;
		this.address = address;
	}

	public Member(String username, int age, Team team) {
		this.username = username;
		this.age = age;
		if (team != null) {
			changeTeam(team);
		}
	}

	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
}
