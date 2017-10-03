package com.pchudzik.blog.example.domainevents;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.util.AnnotationDetectionMethodCallback;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
class DomainEventsHolder {
	private static final boolean UNIQUE_ANNOTATION = true;

	private final ApplicationEventPublisher eventPublisher;
	private final Object entity;

	public void publishAndClearEvents() {
		getEvents().forEach(eventPublisher::publishEvent);
		clearEvents();
	}

	private Collection<Object> getEvents() {
		return domainEventsMethod()
				.map(method -> (Collection<Object>) ReflectionUtils.invokeMethod(method, entity))
				.orElse(emptyList());
	}

	private void clearEvents() {
		final AnnotationDetectionMethodCallback<AfterDomainEventPublication> methodCallback = new AnnotationDetectionMethodCallback<>(
				AfterDomainEventPublication.class,
				UNIQUE_ANNOTATION);
		ReflectionUtils.doWithMethods(entity.getClass(), methodCallback);
		final Method method = methodCallback.getMethod();

		if (method != null) {
			ReflectionUtils.makeAccessible(method);

			ReflectionUtils.invokeMethod(method, entity);
		}
	}

	public boolean hasDomainEvents() {
		return domainEventsMethod().isPresent();
	}

	private Optional<Method> domainEventsMethod() {
		final AnnotationDetectionMethodCallback<DomainEvents> methodCallback = new AnnotationDetectionMethodCallback<>(
				DomainEvents.class,
				UNIQUE_ANNOTATION);
		ReflectionUtils.doWithMethods(entity.getClass(), methodCallback);
		return Optional
				.ofNullable(methodCallback.getMethod())
				.map(method -> {
					ReflectionUtils.makeAccessible(method);
					return method;
				});
	}
}