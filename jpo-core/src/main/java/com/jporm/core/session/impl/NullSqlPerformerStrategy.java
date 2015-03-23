/*******************************************************************************
 * Copyright 2013 Francesco Cina'
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
package com.jporm.core.session.impl;

import java.util.stream.Stream;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.core.query.ResultSetReader;
import com.jporm.core.session.BatchPreparedStatementSetter;
import com.jporm.core.session.GeneratedKeyReader;
import com.jporm.core.session.PreparedStatementSetter;
import com.jporm.core.session.Session;
import com.jporm.core.session.SqlPerformerStrategy;
import com.jporm.core.transaction.TransactionCallback;

/**
 *
 * @author Francesco Cina'
 *
 * Dec 20, 2011
 */
public class NullSqlPerformerStrategy implements SqlPerformerStrategy {

	@Override
	public void execute(final String sql) throws JpoException {
		// do nothing
	}

	@Override
	public <T> T query(final String sql, final PreparedStatementSetter pss, final ResultSetReader<T> rse) 	throws JpoException {
		return null;
	}

	@Override
	public int update(final String sql, final PreparedStatementSetter psc) throws JpoException {
		return 0;
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final PreparedStatementSetter psc) throws JpoException {
		return 0;
	}

	@Override
	public int[] batchUpdate(final Stream<String> sqls) throws JpoException {
		return new int[0];
	}

	@Override
	public int[] batchUpdate(final String sql, final Stream<Object[]> args) throws JpoException {
		return new int[0];
	}

	@Override
	public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
		return new int[0];
	}

	@Override
	public <T> T doInTransaction(Session session, TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return null;
	}

}
