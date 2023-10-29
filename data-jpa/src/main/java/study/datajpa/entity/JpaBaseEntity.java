package study.datajpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Getter;

@MappedSuperclass
@Getter
public class JpaBaseEntity {

	@Column(updatable = false)
	private LocalDateTime createDate;
	private LocalDateTime updateDate;

	@PostLoad
	public void postLoad() {
		System.out.println("JpaBaseEntity.postLoad");
	}

	@PrePersist
	public void prePersist() {
		System.out.println("JpaBaseEntity.prePersist");
		LocalDateTime now = LocalDateTime.now();
		createDate = now;
		updateDate = now;
	}

	@PreUpdate
	public void preUpdate() {
		System.out.println("JpaBaseEntity.preUpdate");
		updateDate = LocalDateTime.now();
	}

}
