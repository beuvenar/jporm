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
package com.jporm.rx.core.session.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.core.BaseTestApi;
import com.jporm.rx.core.connection.Connection;
import com.jporm.rx.core.connection.UpdateResult;
import com.jporm.rx.core.connection.UpdateResultImpl;
import com.jporm.rx.core.session.SqlExecutor;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class SqlExecutorImplTest extends BaseTestApi {

	@Test
	public void connection_should_be_closed_after_query_execution() throws JpoException, InterruptedException, ExecutionException {
		ConnectionTestImpl conn = new ConnectionTestImpl();
		assertFalse(conn.closed);
		SqlExecutor sqlExecutor = new SqlExecutorImpl(new TypeConverterFactory(), () -> {
			return CompletableFuture.<Connection>completedFuture(conn);
		});

		String result = sqlExecutor.query("", rsr -> {
			return "helloWorld";
		}, new ArrayList<Object>()).get();

		assertEquals("helloWorld", result);

		Thread.sleep(50);
		assertTrue(conn.closed);
	}

	@Test
	public void connection_should_be_closed_after_query_exception() throws JpoException, InterruptedException, ExecutionException {
		ConnectionTestImpl conn = new ConnectionTestImpl();
		assertFalse(conn.closed);
		SqlExecutor sqlExecutor = new SqlExecutorImpl(new TypeConverterFactory(), () -> {
			return CompletableFuture.<Connection>completedFuture(conn);
		});

		CompletableFuture<Object> future = sqlExecutor.query("", rsr -> {
			getLogger().info("Throwing exception");
			throw new RuntimeException("exception during query execution");
		}, new ArrayList<Object>());

		try {
			future.get();
			fail("It should throw an exception before");
		} catch(Exception e) {
			//ignore it
		}

		assertTrue( future.isCompletedExceptionally() );

		Thread.sleep(50);
		assertTrue(conn.closed);
	}


	@Test
	public void connection_should_be_closed_after_update_execution() throws JpoException, InterruptedException, ExecutionException {
		ConnectionTestImpl conn = new ConnectionTestImpl();
		assertFalse(conn.closed);
		SqlExecutor sqlExecutor = new SqlExecutorImpl(new TypeConverterFactory(), () -> {
			return CompletableFuture.<Connection>completedFuture(conn);
		});

		int result = sqlExecutor.update("", new ArrayList<Object>()).get().updated();

		assertEquals(0, result);
		Thread.sleep(50);
		assertTrue(conn.closed);
	}


	@Test
	public void connection_should_be_closed_after_u_exception() throws JpoException, InterruptedException, ExecutionException {
		ConnectionTestImpl conn = new ConnectionTestImpl();
		assertFalse(conn.closed);
		SqlExecutor sqlExecutor = new SqlExecutorImpl(new TypeConverterFactory(), () -> {
			return CompletableFuture.<Connection>completedFuture(conn);
		});
		CompletableFuture<UpdateResult> future = sqlExecutor.update("", new GeneratedKeyReader() {
			@Override
			public void read(ResultSet generatedKeyResultSet) {
				throw new RuntimeException("exception during query execution");
			}
			@Override
			public String[] generatedColumnNames() {
				return new String[0];
			}
		},new ArrayList<Object>());

		try {
			future.get();
			fail("It should throw an exception before");
		} catch(Exception e) {
			//ignore it
		}

		assertTrue( future.isCompletedExceptionally() );
		Thread.sleep(50);
		assertTrue(conn.closed);
	}

	class ConnectionTestImpl implements Connection {

		public boolean closed = false;

		@Override
		public <T> CompletableFuture<T> query(String sql, StatementSetter pss, ResultSetReader<T> rse) {
			return CompletableFuture.supplyAsync(() -> rse.read(null), Executors.newFixedThreadPool(1));
		}

		@Override
		public CompletableFuture<UpdateResult> update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter pss) {
			return CompletableFuture.supplyAsync(() -> {
				generatedKeyReader.read(null);
				return new UpdateResultImpl(0);
			}, Executors.newFixedThreadPool(1));
		}

		@Override
		public CompletableFuture<Void> close() {
			closed = true;
			return CompletableFuture.completedFuture(null);
		}

		@Override
		public CompletableFuture<Void> commit() {
			return CompletableFuture.completedFuture(null);
		}

		@Override
		public CompletableFuture<Void> rollback() {
			return CompletableFuture.completedFuture(null);
		}

	}
}