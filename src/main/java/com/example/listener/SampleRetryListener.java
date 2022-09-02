package com.example.listener;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SampleRetryListener implements RetryListener{
	
	// リトライの前に呼ばれる
	@Override
	public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
		return true;
	}

	// リトライの後に呼ばれる
	@Override
	public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
			Throwable throwable) {
		// 何もしない
	}

	// リトライが失敗すると呼ばれる
	@Override
	public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
			Throwable throwable) {
		log.info("リトライに失敗しました：回数={}, message={}", context.getRetryCount(), throwable.getMessage());
	}
	
	

}
