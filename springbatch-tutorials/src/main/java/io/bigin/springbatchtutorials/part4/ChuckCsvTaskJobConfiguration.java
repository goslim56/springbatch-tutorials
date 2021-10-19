
/*
 * Created by Mingi Park on 2021/10/19
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Dev Backend Team <mingi@bigin.io>, 2021/10/19
 */

package io.bigin.springbatchtutorials.part4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;


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
public class ChuckCsvTaskJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  public ChuckCsvTaskJobConfiguration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  @Bean
  public Job csvTaskJob() throws Exception {
    return this.jobBuilderFactory.get("csvTaskJob")
        .incrementer(new RunIdIncrementer())
        .start(this.csvTaskStep())
        .build();
  }

  @Bean
  public Step csvTaskStep() throws Exception {
    return this.stepBuilderFactory.get("csvTaskStep")
        .<Person, Person>chunk(10)
        .reader(csvReader())
        .writer(csvPrinter())
        .build();
  }

  private ItemWriter<? super Person> csvPrinter() throws Exception {
    DelimitedLineAggregator<Person> lineAggregator = new DelimitedLineAggregator<>();
    lineAggregator.setDelimiter(",");

    BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(new String[]{
        "id", "name", "age", "address"
    }); // field names
    lineAggregator.setFieldExtractor(fieldExtractor);

    FlatFileItemWriter<Person> itemWriter = new FlatFileItemWriterBuilder<Person>()
        .name("csvWriter")
        .encoding("UTF-8")
        .resource(new FileSystemResource("output/part4-wirter-user.csv"))
        .lineAggregator(lineAggregator)
        .headerCallback(writer -> writer.write("id,이름,나이,주소"))
        .footerCallback(writer -> writer.write("<END> \n"))
        .append(true)
        .build();

    itemWriter.afterPropertiesSet();

    return itemWriter;
  }

  private FlatFileItemReader<? extends Person> csvReader() throws Exception {
    DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    tokenizer.setNames("id", "name", "age", "address");
    lineMapper.setLineTokenizer(tokenizer);

    lineMapper.setFieldSetMapper(fieldSet -> {
      int id = fieldSet.readInt("id");
      String name = fieldSet.readString("name");
      int age = fieldSet.readInt("age");
      String address = fieldSet.readString("address");

      return new Person(id, name, age, address);
    });

    FlatFileItemReader<Person> itemReader = new FlatFileItemReaderBuilder<Person>()
        .name("csvReader")
        .encoding("UTF-8")
        .resource(new ClassPathResource("part4-user.csv"))
        .linesToSkip(1) //colum
        .lineMapper(lineMapper)
        .build();

    itemReader.afterPropertiesSet();
    return itemReader;
  }
}
