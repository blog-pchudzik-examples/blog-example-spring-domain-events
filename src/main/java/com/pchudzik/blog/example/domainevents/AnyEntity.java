package com.pchudzik.blog.example.domainevents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@ToString
class AnyEntity extends AbstractAggregateRoot implements Serializable {
	@Id
	@GeneratedValue
	@Getter
	private Long id;

	private String description;

	public AnyEntity(String description) {
		this.description = description;
	}

	void update(String newDescription) {
		registerEvent(new DescriptionUpdated(this.id, description, newDescription));
		description = newDescription;
	}
}