/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.core.query.find.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jporm.annotation.LockMode;
import com.jporm.core.dialect.querytemplate.QueryTemplate;
import com.jporm.core.exception.JpoException;
import com.jporm.core.exception.JpoNotUniqueResultException;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.AQueryRoot;
import com.jporm.core.query.ResultSetReader;
import com.jporm.core.query.ResultSetRowReader;
import com.jporm.core.query.clause.WhereExpressionElement;
import com.jporm.core.query.find.CustomFindQuery;
import com.jporm.core.query.find.CustomFindQueryGroupBy;
import com.jporm.core.query.find.CustomFindQueryOrderBy;
import com.jporm.core.query.find.CustomFindQueryWhere;
import com.jporm.core.query.namesolver.NameSolver;
import com.jporm.core.query.namesolver.impl.NameSolverImpl;
import com.jporm.core.session.Session;
import com.jporm.core.session.SqlExecutor;

/**
 * @author Francesco Cina 20/giu/2011
 */
public class CustomFindQueryImpl extends AQueryRoot implements CustomFindQuery {

	private final CustomFindSelectImpl select;
	private final Session session;
	private int _queryTimeout = 0;
	private int _maxRows = 0;
	private LockMode _lockMode = LockMode.NO_LOCK;
	private final CustomFindQueryWhereImpl where = new CustomFindQueryWhereImpl(this);
	private final CustomFindQueryOrderByImpl orderBy = new CustomFindQueryOrderByImpl(this);
	private final CustomFindQueryGroupByImpl groupBy = new CustomFindQueryGroupByImpl(this);
	private final CustomFindFromImpl from;
	private int versionStatus = 0;
	private final NameSolver nameSolver;
	private int _firstRow = -1;
	private final QueryTemplate queryTemplate;

	public CustomFindQueryImpl(final String[] selectFields, final ServiceCatalog serviceCatalog, final Class<?> clazz,
			final String alias) {
		super(serviceCatalog);
		session = serviceCatalog.getSession();
		queryTemplate = serviceCatalog.getDbProfile().getQueryTemplate();
		nameSolver = new NameSolverImpl(serviceCatalog, false);
		from = new CustomFindFromImpl(this, serviceCatalog, clazz, nameSolver.register(clazz, alias), nameSolver);
		select = new CustomFindSelectImpl(selectFields);
	}

	@Override
	public final void appendValues(final List<Object> values) {
		where.appendElementValues(values);
		groupBy.appendElementValues(values);
	}

	@Override
	public CustomFindQuery distinct(final boolean distinct) {
		select.setDistinct(distinct);
		return this;
	}

	@Override
	public CustomFindQuery firstRow(final int firstRow) throws JpoException {
		_firstRow = firstRow;
		return this;
	}

	@Override
	public CustomFindQuery fullOuterJoin(final Class<?> joinClass) {
		return from.fullOuterJoin(joinClass);
	}

	@Override
	public CustomFindQuery fullOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
		return from.fullOuterJoin(joinClass, joinClassAlias);
	}

	@Override
	public CustomFindQuery fullOuterJoin(final Class<?> joinClass, final String onLeftProperty,
			final String onRigthProperty) {
		return from.fullOuterJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public CustomFindQuery fullOuterJoin(final Class<?> joinClass, final String joinClassAlias,
			final String onLeftProperty, final String onRigthProperty) {
		return from.fullOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
	}

	@Override
	public Object[] get() {
		return getExecutor()
				.queryForArray(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public <T> T get(final ResultSetReader<T> rse) throws JpoException {
		return getExecutor().query(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), rse, getValues());
	}

	@Override
	public <T> List<T> get(final ResultSetRowReader<T> rsrr) throws JpoException {
		return getExecutor().query(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), rsrr, getValues());
	}

	@Override
	public BigDecimal getBigDecimal() throws JpoException {
		return getExecutor().queryForBigDecimal(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public Optional<BigDecimal> getBigDecimalOptional() throws JpoException {
		return Optional.ofNullable(getBigDecimal());
	}

	@Override
	public BigDecimal getBigDecimalUnique() throws JpoException {
		return getExecutor().queryForBigDecimalUnique(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows),
				getValues());
	}

	@Override
	public Boolean getBoolean() throws JpoException {
		return getExecutor().queryForBoolean(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public Optional<Boolean> getBooleanOptional() throws JpoException {
		return Optional.ofNullable(getBoolean());
	}

	@Override
	public Boolean getBooleanUnique() throws JpoException {
		return getExecutor().queryForBooleanUnique(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows),
				getValues());
	}

	@Override
	public Double getDouble() {
		return getExecutor().queryForDouble(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public Optional<Double> getDoubleOptional() {
		return Optional.ofNullable(getDouble());
	}

	@Override
	public Double getDoubleUnique() throws JpoException {
		return getExecutor().queryForDoubleUnique(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows),
				getValues());
	}

	private SqlExecutor getExecutor() {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = session.sqlExecutor();
		sqlExec.setTimeout(getTimeout());
		return sqlExec;
	}

	@Override
	public Float getFloat() {
		return getExecutor().queryForFloat(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public Optional<Float> getFloatOptional() {
		return Optional.ofNullable(getFloat());
	}

	@Override
	public Float getFloatUnique() throws JpoException {
		return getExecutor()
				.queryForFloatUnique(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public Integer getInt() {
		return getExecutor().queryForInt(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public Optional<Integer> getIntOptional() {
		return Optional.ofNullable(getInt());
	}

	@Override
	public Integer getIntUnique() throws JpoException {
		return getExecutor().queryForIntUnique(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public List<Object[]> getList() {
		return getExecutor().queryForList(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	public LockMode getLockMode() {
		return _lockMode;
	}

	@Override
	public Long getLong() {
		return getExecutor().queryForLong(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public Optional<Long> getLongOptional() {
		return Optional.ofNullable(getLong());
	}

	@Override
	public Long getLongUnique() throws JpoException {
		return getExecutor().queryForLongUnique(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public Optional<Object[]> getOptional() {
		return Optional.ofNullable(get());
	}

	@Override
	public final int getStatusVersion() {
		return versionStatus + select.getElementStatusVersion() + from.getElementStatusVersion()
				+ where.getElementStatusVersion() + orderBy.getElementStatusVersion() + groupBy.getElementStatusVersion();
	}

	@Override
	public String getString() {
		return getExecutor().queryForString(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public Optional<String> getStringOptional() {
		return Optional.ofNullable(getString());
	}

	@Override
	public String getStringUnique() throws JpoException {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = session.sqlExecutor();
		sqlExec.setTimeout(getTimeout());
		return sqlExec.queryForStringUnique(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), values);
	}

	@Override
	public int getTimeout() {
		return _queryTimeout;
	}

	@Override
	public Object[] getUnique() {
		return getExecutor()
				.queryForArrayUnique(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), getValues());
	}

	@Override
	public <T> T getUnique(final ResultSetRowReader<T> rsrr) throws JpoException, JpoNotUniqueResultException {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = session.sqlExecutor();
		sqlExec.setTimeout(getTimeout());
		return sqlExec.queryForUnique(queryTemplate.paginateSQL(renderSql(), _firstRow, _maxRows), rsrr, values);
	}

	private List<Object> getValues() {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		return values;
	}

	@Override
	public CustomFindQueryGroupBy groupBy(final String... fields) throws JpoException {
		groupBy.setFields(fields);
		return groupBy;
	}

	@Override
	public CustomFindQuery innerJoin(final Class<?> joinClass) {
		return from.innerJoin(joinClass);
	}

	@Override
	public CustomFindQuery innerJoin(final Class<?> joinClass, final String joinClassAlias) {
		return from.innerJoin(joinClass, joinClassAlias);
	}

	@Override
	public CustomFindQuery innerJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
		return from.innerJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public CustomFindQuery innerJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty,
			final String onRigthProperty) {
		return from.innerJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
	}

	public boolean isDistinct() throws JpoException {
		return select.isDistinct();
	}

	@Override
	public CustomFindQuery join(final Class<?> joinClass) {
		return from.join(joinClass);
	}

	@Override
	public CustomFindQuery join(final Class<?> joinClass, final String joinClassAlias) {
		return from.join(joinClass, joinClassAlias);
	}

	@Override
	public CustomFindQuery leftOuterJoin(final Class<?> joinClass) {
		return from.leftOuterJoin(joinClass);
	}

	@Override
	public CustomFindQuery leftOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
		return from.leftOuterJoin(joinClass, joinClassAlias);
	}

	@Override
	public CustomFindQuery leftOuterJoin(final Class<?> joinClass, final String onLeftProperty,
			final String onRigthProperty) {
		return from.leftOuterJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public CustomFindQuery leftOuterJoin(final Class<?> joinClass, final String joinClassAlias,
			final String onLeftProperty, final String onRigthProperty) {
		return from.leftOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
	}

	@Override
	public CustomFindQuery lockMode(final LockMode lockMode) {
		_lockMode = lockMode;
		versionStatus++;
		return this;
	}

	@Override
	public final CustomFindQuery maxRows(final int maxRows) throws JpoException {
		_maxRows = maxRows;
		return this;
	}

	@Override
	public CustomFindQuery naturalJoin(final Class<?> joinClass) {
		return from.naturalJoin(joinClass);
	}

	@Override
	public CustomFindQuery naturalJoin(final Class<?> joinClass, final String joinClassAlias) {
		return from.naturalJoin(joinClass, joinClassAlias);
	}

	@Override
	public final CustomFindQueryOrderBy orderBy() throws JpoException {
		return orderBy;
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		select.renderSqlElement(queryBuilder, nameSolver);
		from.renderSqlElement(queryBuilder, nameSolver);
		where.renderSqlElement(queryBuilder, nameSolver);
		groupBy.renderSqlElement(queryBuilder, nameSolver);
		orderBy.renderSqlElement(queryBuilder, nameSolver);
		queryBuilder.append(_lockMode.getMode());
	}

	@Override
	public CustomFindQuery rightOuterJoin(final Class<?> joinClass) {
		return from.rightOuterJoin(joinClass);
	}

	@Override
	public CustomFindQuery rightOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
		return from.rightOuterJoin(joinClass, joinClassAlias);
	}

	@Override
	public CustomFindQuery rightOuterJoin(final Class<?> joinClass, final String onLeftProperty,
			final String onRigthProperty) {
		return from.rightOuterJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public CustomFindQuery rightOuterJoin(final Class<?> joinClass, final String joinClassAlias,
			final String onLeftProperty, final String onRigthProperty) {
		return from.rightOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
	}

	@Override
	public final CustomFindQuery timeout(final int queryTimeout) {
		_queryTimeout = queryTimeout;
		return this;
	}

	@Override
	public String toString() {
		return from.toString();
	}

	@Override
	public CustomFindQueryWhere where(final List<WhereExpressionElement> expressionElements) {
		where.and(expressionElements);
		return where;
	}

	@Override
	public CustomFindQueryWhere where(final String customClause, final Object... args) {
		where.and(customClause, args);
		return where;
	}

	@Override
	public CustomFindQueryWhere where(final WhereExpressionElement... expressionElements) {
		if (expressionElements.length > 0) {
			where.and(expressionElements);
		}
		return where;
	}

}