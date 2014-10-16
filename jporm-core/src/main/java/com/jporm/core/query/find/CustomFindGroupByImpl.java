/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.core.query.find;

import java.math.BigDecimal;
import java.util.List;

import com.jporm.core.query.clause.GroupByImpl;
import com.jporm.exception.OrmException;
import com.jporm.exception.OrmNotUniqueResultException;
import com.jporm.query.LockMode;
import com.jporm.query.clause.WhereExpressionElement;
import com.jporm.query.find.CustomFindGroupBy;
import com.jporm.query.find.CustomFindOrderBy;
import com.jporm.query.find.CustomFindQuery;
import com.jporm.query.find.CustomFindWhere;
import com.jporm.session.ResultSetReader;
import com.jporm.session.ResultSetRowReader;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 23, 2013
 * 
 * @author Francesco Cina'
 * @version $Revision
 */
public class CustomFindGroupByImpl extends GroupByImpl<CustomFindQuery> implements CustomFindGroupBy {

	private final CustomFindQuery customFindQuery;

	public CustomFindGroupByImpl(final CustomFindQuery customFindQuery) {
		this.customFindQuery = customFindQuery;
	}

	@Override
	public String renderSql() {
		return customFindQuery.renderSql();
	}

	@Override
	public void renderSql(final StringBuilder queryBuilder) {
		customFindQuery.renderSql(queryBuilder);
	}

	@Override
	public CustomFindQuery distinct(final boolean distinct) throws OrmException {
		return customFindQuery.distinct(distinct);
	}

	@Override
	public <T> T get(final ResultSetReader<T> rsr) throws OrmException {
		return customFindQuery.get(rsr);
	}

	@Override
	public void appendValues(final List<Object> values) {
		customFindQuery.appendValues(values);
	}

	@Override
	public CustomFindOrderBy orderBy() throws OrmException {
		return customFindQuery.orderBy();
	}

	@Override
	public CustomFindQuery lockMode(final LockMode lockMode) {
		return customFindQuery.lockMode(lockMode);
	}

	@Override
	public CustomFindQuery maxRows(final int maxRows) throws OrmException {
		return customFindQuery.maxRows(maxRows);
	}

	@Override
	public <T> List<T> get(final ResultSetRowReader<T> rsrr) throws OrmException {
		return customFindQuery.get(rsrr);
	}

	@Override
	public CustomFindQuery firstRow(final int firstRow) throws OrmException {
		return customFindQuery.firstRow(firstRow);
	}

	@Override
	public <T> T getUnique(final ResultSetRowReader<T> rsrr) throws OrmException, OrmNotUniqueResultException {
		return customFindQuery.getUnique(rsrr);
	}

	@Override
	public CustomFindQuery queryTimeout(final int queryTimeout) {
		return customFindQuery.queryTimeout(queryTimeout);
	}

	@Override
	public List<Object[]> getList() throws OrmException {
		return customFindQuery.getList();
	}

	@Override
	public Object[] get() {
		return customFindQuery.get();
	}

	@Override
	public Integer getIntUnique() throws OrmException {
		return customFindQuery.getIntUnique();
	}

	@Override
	public Long getLongUnique() throws OrmException {
		return customFindQuery.getLongUnique();
	}

	@Override
	public Double getDoubleUnique() throws OrmException {
		return customFindQuery.getDoubleUnique();
	}

	@Override
	public Float getFloatUnique() throws OrmException {
		return customFindQuery.getFloatUnique();
	}

	@Override
	public String getStringUnique() throws OrmException {
		return customFindQuery.getStringUnique();
	}

	@Override
	public Boolean getBooleanUnique() throws OrmException {
		return customFindQuery.getBooleanUnique();
	}

	@Override
	public BigDecimal getBigDecimalUnique() throws OrmException {
		return customFindQuery.getBigDecimalUnique();
	}

	@Override
	public Integer getInt() {
		return customFindQuery.getInt();
	}

	@Override
	public Long getLong() {
		return customFindQuery.getLong();
	}

	@Override
	public Double getDouble() {
		return customFindQuery.getDouble();
	}

	@Override
	public Float getFloat() {
		return customFindQuery.getFloat();
	}

	@Override
	public String getString() {
		return customFindQuery.getString();
	}

	@Override
	public Boolean getBoolean() {
		return customFindQuery.getBoolean();
	}

	@Override
	public BigDecimal getBigDecimal() {
		return customFindQuery.getBigDecimal();
	}

	@Override
	protected CustomFindQuery sqlQuery() {
		return customFindQuery;
	}

	@Override
	public CustomFindWhere where(final WhereExpressionElement... expressionElements) {
		return customFindQuery.where(expressionElements);
	}

	@Override
	public CustomFindWhere where(final List<WhereExpressionElement> expressionElements) {
		return customFindQuery.where(expressionElements);
	}

	@Override
	public CustomFindWhere where(final String customClause, final Object... args) {
		return customFindQuery.where(customClause, args);
	}

}