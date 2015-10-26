/*******************************************************************************
 * Copyright 2015 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.commons.core.async.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import com.jporm.commons.core.async.AsyncTimedTaskExecutor;

public class BlockingAsyncTaskExecutor implements AsyncTimedTaskExecutor {

	@Override
	public <T> CompletableFuture<T> execute(Supplier<T> task) {
		CompletableFuture<T> future = new CompletableFuture<T>();
		try {
			future.complete(task.get());
		} catch (RuntimeException e) {
			future.completeExceptionally(e);
		}
		return future;
	}

	@Override
	public <T> CompletableFuture<T> execute(Supplier<T> task, long timeout, TimeUnit timeUnit) {
		return execute(task);
	}

	@Override
	public CompletableFuture<Void> execute(Runnable task) {
		CompletableFuture<Void> future = new CompletableFuture<>();
		try {
			task.run();
			future.complete(null);
		} catch (RuntimeException e) {
			future.completeExceptionally(e);
		}
		return future;
	}

	@Override
	public CompletableFuture<Void> execute(Runnable task, long timeout, TimeUnit timeUnit) {
		return execute(task);
	}

}