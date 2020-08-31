package org.squashtest.hibernatekata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.ManagedType;
import java.util.Set;

@SpringBootApplication
@EntityScan({"org.squashtest.hibernatekata"})
public class HibernateKataApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(HibernateKataApplication.class, args);
		EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
		Set<ManagedType<?>> managedTypes = emf.getMetamodel().getManagedTypes();
	}

}
