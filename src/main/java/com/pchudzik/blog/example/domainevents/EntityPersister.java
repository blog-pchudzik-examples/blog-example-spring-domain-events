package com.pchudzik.blog.example.domainevents;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class EntityPersister {
	private final EntityRepository entityRepository;

	@Transactional
	public AnyEntity save(AnyEntity entity) {
		return entityRepository.save(entity);
	}

	public AnyEntity load(Long entityId) {
		return Optional
				.ofNullable(entityRepository.findOne(entityId))
				.orElseThrow(() -> new IllegalArgumentException("No entity with id " + entityId));
	}
}