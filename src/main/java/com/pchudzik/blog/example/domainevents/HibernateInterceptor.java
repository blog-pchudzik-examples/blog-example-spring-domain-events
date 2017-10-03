package com.pchudzik.blog.example.domainevents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Component
class HibernateInterceptor extends EmptyInterceptor {
	private static final boolean NOT_PARALLEL = false;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	public void postFlush(Iterator entities) {
		final Stream<Object> entitiesStream = StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(entities, Spliterator.ORDERED),
				NOT_PARALLEL);

		entitiesStream
				.map(this::createEventHolder)
				.filter(DomainEventsHolder::hasDomainEvents)
				.forEach(DomainEventsHolder::publishAndClearEvents);
	}

	private DomainEventsHolder createEventHolder(Object entity) {
		return new DomainEventsHolder(eventPublisher, entity);
	}
}