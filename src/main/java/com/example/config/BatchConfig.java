package com.example.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.chunk.RetryProcessor;
import com.example.chunk.RetryReader;
import com.example.chunk.RetryWriter;
import com.example.listener.SampleRetryListener;

@Configuration("BatchConfig")
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private RetryReader retryReader;
	
	@Autowired
	private RetryProcessor retryProcessor;
	
	@Autowired
	private RetryWriter retryWriter;
	
	@Autowired
	private SampleRetryListener sampleRetryListener;
	
	@Value("${retry.num}")
	private Integer retryNum;
	
	/** Stepの生成 **/
	@Bean
	public Step retryChunkStep() {
		return stepBuilderFactory.get("RetryChunkStep")
									.<String, String>chunk(10)
									.reader(retryReader)
									.processor(retryProcessor)
									.writer(retryWriter)
									.faultTolerant() // リトライの設定
									.retryLimit(retryNum) // リトライ回数
									.retry(Exception.class) // リトライ対象の例外クラス
									.listener(sampleRetryListener) // リトライ用リスナー
									.build();
	}
	
	/** Jobの生成 **/
	@Bean
	public Job retryChunkJob() {
		return jobBuilderFactory.get("RetryChunkStep")
									.incrementer(new RunIdIncrementer())
									.start(retryChunkStep())
									.build();
	}

}
