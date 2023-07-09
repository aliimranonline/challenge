package com.mainteny.challenge.jobConfigs;

import com.mainteny.challenge.daos.Employee;
import com.mainteny.challenge.repositories.EmployeeRepo;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired
    private JobBuilderFactory jobFactory;

    @Autowired
    private StepBuilderFactory stepFactory;

    @Autowired
    private EmployeeRepo employeeRepo;

    //Define ItemReader Bean to be used by the Employee Job
    @Bean
    public FlatFileItemReader<Employee> reader(){
        return new FlatFileItemReaderBuilder<Employee>()
                .name("employeeReader")
                .resource(new ClassPathResource("employees.csv"))
                .delimited()
                .names(new String[]{"firstName","lastName"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>(){{setTargetType(Employee.class);}})
                .build();


    }

    //Define ItemProcessor Bean to be used by the Employee Job
    @Bean
    public EmployeeProcessor processor(){
        return new EmployeeProcessor();
    }

    //Define ItemWriter Bean to be used by the Employee Job
    @Bean
    public RepositoryItemWriter<Employee> writer(){
        RepositoryItemWriter<Employee> employeeWriter = new RepositoryItemWriter<>();
        employeeWriter.setRepository(employeeRepo);
        employeeWriter.setMethodName("save");
        return employeeWriter;
    }

    //Define Step to be used by the Employee Job
    @Bean
    public Step step1(){
        return stepFactory.get("employee-extraction").
                <Employee, Employee>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(name ="extractEmployeeDataJob")
    public Job runJob(){
        return jobFactory.get("dump-employee-details")
                .flow(step1()).end().build();
    }
}
