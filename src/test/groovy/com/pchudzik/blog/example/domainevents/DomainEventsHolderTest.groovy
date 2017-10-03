package com.pchudzik.blog.example.domainevents

import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.AfterDomainEventPublication
import org.springframework.data.domain.DomainEvents
import spock.lang.Specification
import spock.lang.Unroll

class DomainEventsHolderTest extends Specification {
	def eventPublisher = Mock(ApplicationEventPublisher)

	@Unroll
	def "should detect when entity has domain events"() {
		when:
		final eventsHolder = new DomainEventsHolder(
				eventPublisher,
				entity)

		then:
		eventsHolder.hasDomainEvents() == hasDomainEvents

		where:
		entity                          || hasDomainEvents
		new WithDomainEvents(["event"]) || true
		new WithoutDomainEvents()       || false
	}

	def "should publish domain events"() {
		given:
		final events = ["event 1", "event 2"]
		final eventsHolder = new DomainEventsHolder(
				eventPublisher,
				new WithDomainEvents(events))

		when:
		eventsHolder.publishAndClearEvents()

		then:
		1 * eventPublisher.publishEvent(events[0])
		1 * eventPublisher.publishEvent(events[1])
	}

	def "should clear domain events collection after events publish"() {
		given:
		final entity = new DomainEventsAndAfterPublicationAction(["event"])
		final eventsHolder = new DomainEventsHolder(eventPublisher, entity)

		when:
		eventsHolder.publishAndClearEvents()

		then:
		entity.events.isEmpty()
	}

	def "should do nothing when has no clear events method"() {
		given:
		final entity = new WithDomainEvents(["event"])
		final eventsHolder = new DomainEventsHolder(eventPublisher, entity)

		when:
		eventsHolder.publishAndClearEvents();

		then:
		noExceptionThrown()
	}

	private static class WithoutDomainEvents {}

	private static class WithDomainEvents {
		final List<Object> events

		WithDomainEvents(Collection<? extends Object> events) {
			this.events = new ArrayList<>(events)
		}

		@DomainEvents
		List<Object> domainEvents() {
			return events
		}
	}


	private static class DomainEventsAndAfterPublicationAction extends WithDomainEvents {
		private DomainEventsAndAfterPublicationAction(Collection<? extends Object> events) {
			super(events)
		}

		@AfterDomainEventPublication
		void clearDomainEvents() {
			events.clear()
		}
	}
}
