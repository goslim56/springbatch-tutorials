/*
 * Created by Gyeong-Han Kim on 2021/10/07
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Dev2 Backend Team <goslim@bigin.io>, 2021/10/07
 */
package io.bigin.springbatchtutorials.part2;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * create on 2021/10/07. create by IntelliJ IDEA.
 *
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author Gyeong-Han Kim
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class JdbcCursorJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private static final int chuckSize  = 5;
  private final DataSource dataSource;

  @Bean
  public Job jdbcCursorJob() {
    return jobBuilderFactory.get("jdbcCursorJob")
        .incrementer(new RunIdIncrementer())
        .start(jdbcStep1())
        .build();
  }

  @Bean
  public Step jdbcStep1() {
    return stepBuilderFactory.get("jdbcStep1")
        .<Person,Person>chunk(chuckSize)
        .reader(jdbcReader())
//        .processor(baseProcessor())
        .writer(print())
        .build();
  }

  public ItemWriter<Person> print() {
    return items -> {
      items.forEach(System.out::println);
    };
  }

  public ItemProcessor<Person, Person> baseProcessor() {
    log.info("process is called!!");
    return item ->  {
      return item;
    };
  }

  public    ItemReader<Person> jdbcReader() {
    return new JdbcCursorItemReaderBuilder<Person>()
        .fetchSize(chuckSize)
        .dataSource(dataSource)
        .rowMapper(new BeanPropertyRowMapper<>(Person.class))
        .sql("select name, age, address from person")
        .name("person reader")
        .build();
  }
}