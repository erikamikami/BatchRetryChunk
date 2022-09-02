package com.example.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class RetryProcessor implements ItemProcessor<String, String>{
	
	@Value("${retry.num}")
	private Integer retryNum;
	
	private int count = 1;
	
	
	@Override
	public String process(String item) throws Exception {
		if(item.equals("World") && count < retryNum) {
			count++;
			throw new Exception("リトライしました");
		}
		
		item = item + "★";
		log.info("Processor:{}", item);
		return item;
	}

}
