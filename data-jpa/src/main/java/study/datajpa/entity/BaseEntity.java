package study.datajpa.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

/**
 * auditing 을 적용하고 싶다면, 꼭 listeners 를 등록해주어야 한다.
 * 전체에 적용하고 싶다면, 별도의 xml 에 등록 하고 전체적으로 등록이 되도록 선언하자.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {

	@CreatedBy
	@Column(updatable = false)
	private String createdBy;
	@LastModifiedBy
	private String lastModifiedBy;

}
