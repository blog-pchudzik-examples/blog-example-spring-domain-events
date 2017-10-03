package com.pchudzik.blog.example.domainevents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class DescriptionChangListener {
	@EventListener
	public void onDescriptionChange(DescriptionUpdated event) {
		log.info("Description of {}, modified from {}, to {}",
				event.getEntityId(),
				event.getOldDescription(),
				event.getNewDescription());
	}
}
