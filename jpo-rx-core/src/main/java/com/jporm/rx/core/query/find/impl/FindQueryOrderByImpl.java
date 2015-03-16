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
package com.jporm.rx.core.query.find.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jporm.annotation.LockMode;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.clause.impl.OrderByImpl;
import com.jporm.rx.core.query.find.FindQuery;
import com.jporm.rx.core.query.find.FindQueryOrderBy;
import com.jporm.rx.core.query.find.FindQueryWhere;
import com.jporm.sql.query.clause.WhereExpressionElement;

/**
 *
 * @author ufo
 *
 * @param <BEAN>
 */
public class FindQueryOrderByImpl<BEAN> extends OrderByImpl<FindQueryOrderBy<BEAN>> implements FindQueryOrderBy<BEAN> {

	private final FindQuery<BEAN> findQuery;

	public FindQueryOrderByImpl(com.jporm.sql.query.clause.OrderBy sqlOrderBy, final FindQuery<BEAN> findQuery) {
		super(sqlOrderBy);
		this.findQuery = findQuery;
	}

	@Override
	public void appendValues(final List<Object> values) {
		this.findQuery.appendValues(values);
	}

	@Override
	public FindQuery<BEAN> distinct(final boolean distinct) throws JpoException {
		return this.findQuery.distinct(distinct);
	}

	@Override
	public FindQuery<BEAN> firstRow(final int firstRow) throws JpoException {
		return this.findQuery.firstRow(firstRow);
	}

	@Override
	public CompletableFuture<BEAN> get() throws JpoException {
		return findQuery.get();
	}

	@Override
	public FindQuery<BEAN> lockMode(final LockMode lockMode) {
		return this.findQuery.lockMode(lockMode);
	}

	@Override
	public FindQuery<BEAN> maxRows(final int maxRows) throws JpoException {
		return this.findQuery.maxRows(maxRows);
	}

	@Override
	protected FindQueryOrderBy<BEAN> orderBy() throws JpoException {
		return this;
	}

	@Override
	public FindQuery<BEAN> query() {
		return this.findQuery;
	}

	@Override
	public String renderRowCountSql() throws JpoException {
		return this.findQuery.renderRowCountSql();
	}

	@Override
	public String renderSql() {
		return this.findQuery.renderSql();
	}

	@Override
	public void renderSql(final StringBuilder stringBuilder) {
		this.findQuery.renderSql(stringBuilder);
	}

	@Override
	public FindQueryWhere<BEAN> where(final List<WhereExpressionElement> expressionElements) {
		return findQuery.where(expressionElements);
	}

	@Override
	public FindQueryWhere<BEAN> where(final String customClause, final Object... args) {
		return findQuery.where(customClause, args);
	}

	@Override
	public FindQueryWhere<BEAN> where(final WhereExpressionElement... expressionElements) {
		return findQuery.where(expressionElements);
	}

	@Override
	public int getVersion() {
		return findQuery.getVersion();
	}

}
