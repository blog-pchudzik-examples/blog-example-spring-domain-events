package com.pchudzik.blog.example.domainevents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DomainEventsApplication {

	public static void main(String[] args) {
		final ConfigurableApplicationContext ctx = SpringApplication.run(DomainEventsApplication.class, args);

		final TransactionalService transactionalService = ctx.getBean(TransactionalService.class);
		final TxTemplateService txTemplateService = ctx.getBean(TxTemplateService.class);
		final EntityPersister entityPersister = ctx.getBean(EntityPersister.class);

		final Long entityId = entityPersister.save(new AnyEntity("initial description")).getId();
		log.info("Entity {}", entityPersister.load(entityId));

		transactionalService.updateEntity(entityId, "transactional description");
		log.info("Entity {}", entityPersister.load(entityId));

		txTemplateService.updateDescription(entityId, "tx template description");
		log.info("Entity {}", entityPersister.load(entityId));

	}
}
