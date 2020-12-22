package com.tpbatch.config;

import com.tpbatch.batch.BatchLauncher;
import com.tpbatch.entities.Transaction;
import com.tpbatch.entities.TransactionContainer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class AppConfig {
    @Autowired public StepBuilderFactory stepBuilderFactory;
    @Autowired public JobBuilderFactory jobBuilderFactory;
    @Autowired public ItemProcessor<TransactionContainer,Transaction> transactionProcces;
    @Autowired public ItemWriter<Transaction> transactionWriter;
    @Autowired private ItemReader<TransactionContainer> transactionReader;
    @Autowired public BatchLauncher batchLauncher;

/*    @Value("/input/Transactions.csv")
    private UrlResource inputResource;*/

    @Bean
    public LineMapper<TransactionContainer> lineMapper() {
        DefaultLineMapper<TransactionContainer> lineMapper = new DefaultLineMapper();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[] { "idTransaction","idCompte", "montant", "dateTransactionStr"});
        lineTokenizer.setDelimiter(",");
        BeanWrapperFieldSetMapper<TransactionContainer> fieldSetMapper = new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(TransactionContainer.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public FlatFileItemReader<TransactionContainer> reader(@Value("${inputFile}") Resource inputFile) {
        FlatFileItemReader<TransactionContainer> itemReader = new FlatFileItemReader();
        itemReader.setLineMapper(lineMapper());
        itemReader.setResource(inputFile);
        return itemReader;
    }


    @Bean(name="importTransactions")
    public Job importTransactions(JobBuilderFactory jobs) {
        return jobs.get("importTransactions")
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<TransactionContainer, Transaction>chunk(2)
                .reader(transactionReader)
                .processor(transactionProcces)
                .writer(transactionWriter)
                .build();
    }

    @Scheduled(cron = "0,10,20,30 * * * * *")
    public void scheduleFixedDelayTask() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        batchLauncher.run();
    }

}
