package com.pchudzik.blog.example.domainevents;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
class TransactionalService {
	private final EntityRepository entityRepository;

	@Transactional
	public void updateEntity(Long id, String dessc) {
		final AnyEntity entity = entityRepository.findOne(id);
		entity.update(dessc);
	}
}
