package io.bigin.springbatchtutorials.part7;

import io.bigin.springbatchtutorials.part5.Person;
import io.bigin.springbatchtutorials.util.CustomItemReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JpaJob3Configuration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job JpaToCsvJob() throws Exception {
        return this.jobBuilderFactory.get("JpaToCsvJob")
                .incrementer(new RunIdIncrementer())
                .start(this.JpaReaderStep())
                .build();
    }

    @Bean
    public Step JpaReaderStep() throws Exception {
        return this.stepBuilderFactory.get("JpaToCsvStep")
                .<Person, Person>chunk(5)
                .reader(jpaReaderPerson())
                .writer(jpaWriter())
                .build();
    }

    private ItemWriter<? super Person> jpaWriter() throws Exception {
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
                .resource(new FileSystemResource("output/part7-wirter-user.csv"))
                .lineAggregator(lineAggregator)
                .headerCallback(writer -> writer.write("id,이름,나이,주소"))
                .footerCallback(writer -> writer.write("<END> \n"))
                .append(true)
                .build();

        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    private JpaPagingItemReader<Person> jpaReaderPerson() {
        return new JpaPagingItemReaderBuilder<Person>()
                .queryString("SELECT p FROM person p")
                .pageSize(5)
                .entityManagerFactory(entityManagerFactory)
                .name("personPagingReader")
                .build();
    }

}
