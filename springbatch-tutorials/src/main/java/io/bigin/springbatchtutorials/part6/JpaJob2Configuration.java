
/*
 * Created by Mingi Park on 2021/10/19
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Dev Backend Team <mingi@bigin.io>, 2021/10/19
 */

package io.bigin.springbatchtutorials.part6;

import io.bigin.springbatchtutorials.part5.Person;
import io.bigin.springbatchtutorials.util.CustomItemReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * create on 2021/10/19. create by IntelliJ IDEA.
 *
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author Mingi Park
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Configuration
@Slf4j
public class JpaJob2Configuration {

  private JobBuilderFactory jobBuilderFactory;
  private StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;

  public JpaJob2Configuration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory,
      EntityManagerFactory entityManagerFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.entityManagerFactory = entityManagerFactory;
  }

  @Bean
  public Job JpaWriterJob() throws Exception {
    return this.jobBuilderFactory.get("JpaWriterJob")
        .incrementer(new RunIdIncrementer())
        .start(this.JpaWriterStep())
        .build();
  }

  @Bean
  public Step JpaWriterStep() throws Exception {
    return this.stepBuilderFactory.get("JpaWriterStep")
        .<Person, Person>chunk(5)
        .reader(readRandomPerson())
        .writer(jpaWriter())
        .build();
  }

  private ItemWriter<? super Person> jpaWriter() throws Exception {
    JpaItemWriter<Person> itemWriter = new JpaItemWriterBuilder<Person>()
        .entityManagerFactory(entityManagerFactory)
        .build();
    itemWriter.afterPropertiesSet();
    return itemWriter;
  }

  private ItemReader<? extends Person> readRandomPerson() {
    return new CustomItemReader<Person>(getRandomItems());
  }

  private List<Person> getRandomItems() {
    List<Person> items = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      items.add(new Person("test name" + i, new Random().nextInt(70), "seoul"));
    }
    return items;
  }
}
