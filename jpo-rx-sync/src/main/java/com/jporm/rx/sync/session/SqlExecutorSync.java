/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rx.sync.session;

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.rx.connection.UpdateResult;
import com.jporm.sql.dialect.DBType;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;
import com.jporm.types.io.StatementSetter;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * @author Francesco Cina 02/lug/2011 An executor to perform plain SQL queries
 */
@Suspendable
public interface SqlExecutorSync {

//	/**
//	 * Issue multiple SQL updates on a single JDBC Statement using batching.
//	 *
//	 * @param sql
//	 *           defining a List of SQL statements that will be executed.
//	 * @return an array of the number of rows affected by each statement
//	 */
//	int[] batchUpdate(Stream<String> sqls);
//
//	/**
//	 * Issue multiple SQL updates on a single JDBC Statement using batching. The values on the generated
//	 * PreparedStatement are set using an IPreparedStatementCreator.
//	 *
//	 * @param sql
//	 *           defining a List of SQL statements that will be executed.
//	 * @param psc
//	 *           the creator to bind values on the PreparedStatement
//	 * @return an array of the number of rows affected by each statement
//	 */
//	int[] batchUpdate(String sql, BatchPreparedStatementSetter psc);
//
//	/**
//	 * Issue multiple SQL updates on a single JDBC Statement using batching. The same query is executed for every Object
//	 * array present in the args list which is the list of arguments to bind to the query.
//	 *
//	 * @param sql
//	 *           defining a List of SQL statements that will be executed.
//	 * @param args
//	 *           defining a List of Object arrays to bind to the query.
//	 * @return an array of the number of rows affected by each statement
//	 */
//	int[] batchUpdate(String sql, Stream<Object[]> args);
//
//	/**
//	 * Issue a single SQL execute, typically a DDL statement.
//	 *
//	 * @param sql
//	 *           static SQL to execute
//	 */
//	void execute(String sql);

	/**
	 * Execute a query given static SQL, reading the ResultSet with a IResultSetReader.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param rse
	 *           object that will extract all rows of results
	 * @param args
	 *           arguments to bind to the query
	 * @return an arbitrary result object, as returned by the IResultSetExtractor
	 */
	<T> T query(String sql, ResultSetReader<T> rse, Collection<?> args);

	/**
	 * Execute a query given static SQL, reading the ResultSet with a IResultSetReader.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param rse
	 *           object that will extract all rows of results
	 * @param args
	 *           arguments to bind to the query
	 * @return an arbitrary result object, as returned by the IResultSetExtractor
	 */
	<T> T query(String sql, ResultSetReader<T> rse, Object... args);

	/**
	 * Execute a query given static SQL, reading the ResultSet with a {@link ResultSetRowReader}.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param rsrr
	 *           object that will extract all rows of results
	 * @param args
	 *           arguments to bind to the query
	 * @return an arbitrary result object, as returned by the {@link ResultSetRowReader}
	 */
	<T> List<T> query(String sql, ResultSetRowReader<T> rsrr, Collection<?> args);

	/**
	 * Execute a query given static SQL, reading the ResultSet with a {@link ResultSetRowReader}.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param rsrr
	 *           object that will extract all rows of results
	 * @param args
	 *           arguments to bind to the query
	 * @return an arbitrary result object, as returned by the {@link ResultSetRowReader}
	 */
	<T> List<T> query(String sql, ResultSetRowReader<T> rsrr, Object... args);

	/**
	 * Execute a query given static SQL and read the result as an bigDecimal value. It returns null if no rows are
	 * returned. It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	BigDecimal queryForBigDecimal(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as an BigDecimal value. It returns null if no rows are
	 * returned. It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	BigDecimal queryForBigDecimal(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as a BigDecimal value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	BigDecimal queryForBigDecimalUnique(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as a BigDecimal value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	BigDecimal queryForBigDecimalUnique(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as an Boolean value. It returns null if no rows are returned.
	 * It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	Boolean queryForBoolean(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as an Boolean value. It returns null if no rows are returned.
	 * It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	Boolean queryForBoolean(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as a boolean value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	Boolean queryForBooleanUnique(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as a boolean value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	Boolean queryForBooleanUnique(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as an double value. It returns null if no rows are returned.
	 * It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	Double queryForDouble(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as an Double value. It returns null if no rows are returned.
	 * It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	Double queryForDouble(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as a double value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	Double queryForDoubleUnique(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as a double value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	Double queryForDoubleUnique(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as an Float value. It returns null if no rows are returned.
	 * It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	Float queryForFloat(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as an float value. It returns null if no rows are returned.
	 * It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	Float queryForFloat(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as a float value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	Float queryForFloatUnique(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as a float value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	Float queryForFloatUnique(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as an Integer value. It returns null if no rows are returned.
	 * It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	Integer queryForInt(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as an Integer value. It returns null if no rows are returned.
	 * It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	Integer queryForInt(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as an int value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	Integer queryForIntUnique(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as an int value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	Integer queryForIntUnique(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as an long value. It returns null if no rows are returned. It
	 * returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	Long queryForLong(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as an long value. It returns null if no rows are returned. It
	 * returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	Long queryForLong(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as an long value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	Long queryForLongUnique(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as an long value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	Long queryForLongUnique(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as an String value. It returns null if no rows are returned.
	 * It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	String queryForString(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as an String value. It returns null if no rows are returned.
	 * It returns the first value if more than one row is returned.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 */
	String queryForString(String sql, Object... args);

	/**
	 * Execute a query given static SQL and read the result as a String value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	String queryForStringUnique(String sql, Collection<?> args);

	/**
	 * Execute a query given static SQL and read the result as a String value
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if no results or more than one result is returned by the query
	 */
	String queryForStringUnique(String sql, Object... args);

	/**
	 * Execute a query given static SQL, reading the ResultSet with a {@link ResultSetRowReader}.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param rsrr
	 *           object that will extract th result's row
	 * @param args
	 *           arguments to bind to the query
	 * @return an arbitrary result object, as returned by the {@link ResultSetRowReader}
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if not exactly one row is returned by the query execution
	 */
	<T> T queryForUnique(String sql, ResultSetRowReader<T> rsrr, Collection<?> args);

	/**
	 * Execute a query given static SQL, reading the ResultSet with a {@link ResultSetRowReader}.
	 *
	 * @param sql
	 *           SQL query to execute
	 * @param rsrr
	 *           object that will extract th result's row
	 * @param args
	 *           arguments to bind to the query
	 * @return an arbitrary result object, as returned by the {@link ResultSetRowReader}
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *            if not exactly one row is returned by the query execution
	 */
	<T> T queryForUnique(String sql, ResultSetRowReader<T> rsrr, Object... args);

	/**
	 * Perform a single SQL update operation (such as an insert, update or delete statement).
	 *
	 * @param sql
	 *           static SQL to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return the number of rows affected
	 */
	UpdateResult update(String sql, Collection<?> args);

	/**
	 * Issue an update statement using a PreparedStatementCreator to provide SQL and any required parameters. Generated
	 * keys can be read using the IGeneratedKeyReader.
	 *
	 * @param sql
	 *           static SQL to execute
	 * @param generatedKeyReader
	 *           IGeneratedKeyReader to read the generated key
	 * @return the number of rows affected
	 */
	UpdateResult update(String sql, GeneratedKeyReader generatedKeyReader, Collection<?> args);

	/**
	 * Issue an update statement using a PreparedStatementCreator to provide SQL and any required parameters. Generated
	 * keys can be read using the IGeneratedKeyReader.
	 *
	 * @param sql
	 *           static SQL to execute
	 * @param generatedKeyReader
	 *           IGeneratedKeyReader to read the generated key
	 * @return the number of rows affected
	 */
	UpdateResult update(String sql, GeneratedKeyReader generatedKeyReader, Object... args);

	/**
	 * Issue an update statement using a PreparedStatementCreator to provide SQL and any required parameters. Generated
	 * keys can be read using the GeneratedKeyReader.
	 *
	 * @param sql
	 *           static SQL to execute
	 * @param psc
	 * @return the number of rows affected
	 */
	UpdateResult update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter psc);

	/**
	 * Perform a single SQL update operation (such as an insert, update or delete statement).
	 *
	 * @param sql
	 *           static SQL to execute
	 * @param args
	 *           arguments to bind to the query
	 * @return the number of rows affected
	 */
	UpdateResult update(String sql, Object... args);

	/**
	 * Perform a single SQL update operation (such as an insert, update or delete statement).
	 *
	 * @param sql
	 *           static SQL to execute
	 * @param psc
	 * @return the number of rows affected
	 */
	UpdateResult update(String sql, StatementSetter psc);

	/**
	 * Return the DB type of the underlying database
	 * @return
	 */
	DBType dbType();
}