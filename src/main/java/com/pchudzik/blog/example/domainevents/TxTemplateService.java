package com.pchudzik.blog.example.domainevents;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
class TxTemplateService {
	private final TransactionTemplate txTemplate;
	private final EntityRepository entityRepository;

	public void updateDescription(Long id, String desc) {
		txTemplate.execute(status -> {
			final AnyEntity anyEntity = entityRepository.findOne(id);
			anyEntity.update(desc);
			return null;
		});
	}
}
