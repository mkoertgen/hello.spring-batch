package com.example.demo

import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource

@Configuration // https://www.baeldung.com/spring-boot-spring-batch
class BatchConfiguration {
	@Bean
	fun personReader(entityManagerFactory: EntityManagerFactory) : ItemReader<Person> {
		// TODO: inject query via properties
		return JpaPagingItemReader<Person>().apply {
			setQueryString("SELECT p FROM Person p")
			setEntityManagerFactory(entityManagerFactory)
			setPageSize(100)
		}
	}

	@Bean
	fun personProcessor() : ItemProcessor<Person, Person> {
		// TODO: inject conversion logic via properties
		return ItemProcessor<Person, Person> { it }
	}

	@Bean
	fun personWriter() : ItemWriter<Person> {
		// TODO: inject conversion logic via properties
		return ItemWriter<Person> { items -> items.forEach { println(it) } }
	}

    @Bean
	fun personStep(
        reader: ItemReader<Person>,
        processor: ItemProcessor<Person, Person>,
        writer: ItemWriter<Person>,
        jobs: JobRepository,
        transactions: DataSourceTransactionManager
	) : Step {
		return StepBuilder("personStep", jobs)
			.chunk<Person, Person>(1000, transactions)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build()
	}

	@Bean
	fun personJob(jobs: JobRepository, step: Step): Job {
		// https://docs.spring.io/spring-batch/reference/job/configuring.html
		return JobBuilder("personJob", jobs)
			.preventRestart()
			.start(step)
			//.next(step2)
			.build()
	}

	@Bean
	fun transactionManager(dataSource: DataSource): DataSourceTransactionManager {
		return DataSourceTransactionManager(dataSource)
	}

//	@Bean
//	fun jobLauncher(jobs: JobRepository): JobLauncher {
//		// TODO: use Argo Workflows or k8s job to launch jobs when in K8s
//		return TaskExecutorJobLauncher().apply {
//			setJobRepository(jobs)
//			afterPropertiesSet()
//		}
//	}
}