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
package com.jporm.session;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jporm.exception.OrmException;
import com.jporm.query.delete.DeleteQuery;
import com.jporm.query.delete.CustomDeleteQuery;
import com.jporm.query.find.CustomFindQuery;
import com.jporm.query.find.FindQueryBase;
import com.jporm.query.find.FindQuery;
import com.jporm.query.save.SaveQuery;
import com.jporm.query.save.SaveOrUpdateQuery;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.query.update.UpdateQuery;
import com.jporm.transaction.Transaction;
import com.jporm.transaction.TransactionCallback;
import com.jporm.transaction.TransactionDefinition;
import com.jporm.transaction.TransactionVoid;
import com.jporm.transaction.TransactionVoidCallback;

/**
 *
 * @author Francesco Cina
 *
 * 21/mag/2011
 *
 */
public interface Session {

	/**
	 * Delete one bean from the database
	 *
	 * @param bean
	 * @param cascade
	 * @return
	 */
	<BEAN> int delete(BEAN bean) throws OrmException;

	/**
	 * Delete the beans from the database
	 * @param <BEAN>
	 * @param beans the beans to delete
	 * @throws OrmException
	 * @return
	 */
	<BEAN> int delete(Collection<BEAN> beans) throws OrmException;

	/**
	 * Delete one bean from the database
	 *
	 * @param bean
	 * @param cascade
	 * @return
	 */
	<BEAN> DeleteQuery<BEAN> deleteQuery(BEAN bean) throws OrmException;

	/**
	 * Delete the beans from the database
	 * @param <BEAN>
	 * @param beans the beans to delete
	 * @throws OrmException
	 * @return
	 */
	<BEAN> DeleteQuery<List<BEAN>> deleteQuery(Collection<BEAN> beans) throws OrmException;

	/**
	 * Delete entries from a specific table
	 * @param clazz the TABLE related Class
	 * @throws OrmException
	 */
	<BEAN> CustomDeleteQuery<BEAN> deleteQuery(Class<BEAN> clazz) throws OrmException;

	/**
	 * Find a bean using the bean type and id(s).
	 * @param <BEAN>
	 * @param bean
	 * @return
	 * @throws OrmException
	 */
	<BEAN> FindQueryBase<BEAN> find(BEAN bean) throws OrmException;

	/**
	 * Find a bean using its ID.
	 *
	 * @param <BEAN>
	 * @param clazz The Class of the bean to load
	 * @param idValue the value of the identifying column of the bean
	 * @return
	 */
	<BEAN> FindQueryBase<BEAN> find(Class<BEAN> clazz, Object idValue);

	/**
	 * Find a bean using its IDs.
	 *
	 * @param <BEAN>
	 * @param clazz The Class of the bean to load
	 * @param idValues an ordered array with the values of the identifying columns of the bean
	 * @return
	 * @throws OrmException
	 */
	<BEAN> FindQueryBase<BEAN> find(Class<BEAN> clazz, Object[] idValues) throws OrmException;

	/**
	 * Create a new query to find bean
	 * @param <BEAN>
	 * @param clazz The class of the bean that will be retrieved by the query execution. The simple class name will be used as alias for the class
	 * @return
	 * @throws OrmException
	 */
	<BEAN> FindQuery<BEAN> findQuery(Class<BEAN> clazz) throws OrmException;

	/**
	 * Create a new query to find bean
	 * @param <BEAN>
	 * @param clazz The class of the bean that will be retrieved by the query execution.
	 * @param alias The alias of the class in the query.
	 * @return
	 * @throws OrmException
	 */
	<BEAN> FindQuery<BEAN> findQuery(Class<BEAN> clazz, String alias) throws OrmException;

	/**
	 * Create a new custom query that permits to specify a custom select clause.
	 * @param select the custom select clause
	 * @param clazz The class of the object that will be retrieved by the query execution.
	 * @param alias The alias of the class in the query.
	 * @return
	 * @throws OrmException
	 */
	CustomFindQuery findQuery(String select, Class<?> clazz, String alias ) throws OrmException;

	/**
	 * Create a new custom query that permits to specify which fields have to be loaded.
	 * The 'selectFields' array contains the name of the fields to fetch.
	 * @param selectFields the name of the fields to fetch
	 * @param clazz The class of the object that will be retrieved by the query execution.
	 * @param alias The alias of the class in the query.
	 * @return
	 * @throws OrmException
	 */
	CustomFindQuery findQuery(String[] selectFields, Class<?> clazz, String alias ) throws OrmException;

	/**
	 * Persist the new bean in the database
	 * @param <BEAN>
	 * @param bean
	 * @throws OrmException
	 * @return
	 */
	<BEAN> BEAN save(BEAN bean);

	/**
	 * Persist the new beans in the database
	 * @param beans the beans to persist
	 * @param cascade whether to persist the children recursively
	 * @return
	 * @throws OrmException
	 */
	<BEAN> List<BEAN> save(Collection<BEAN> beans) throws OrmException;

	/**
	 * Persist the new bean in the database
	 * @param <BEAN>
	 * @param bean
	 * @throws OrmException
	 * @return
	 */
	<BEAN> SaveQuery<BEAN> saveQuery(BEAN bean);

	/**
	 * Persist the new beans in the database
	 * @param beans the beans to persist
	 * @param cascade whether to persist the children recursively
	 * @return
	 * @throws OrmException
	 */
	<BEAN> SaveQuery<List<BEAN>> saveQuery(Collection<BEAN> beans) throws OrmException;

	/**
	 * For each bean in the list, update the bean if it exists,
	 * otherwise saves it
	 * @param bean the bean to persist
	 * @return
	 * @throws OrmException
	 */
	<BEAN> BEAN saveOrUpdate(BEAN bean) throws OrmException;

	/**
	 * For each bean in the list, update the bean if it exists,
	 * otherwise saves it
	 * @param beans the beans to persist
	 * @param cascade whether to saveOrUpdate the children recursively
	 * @return
	 * @throws OrmException
	 */
	<BEAN> List<BEAN> saveOrUpdate(Collection<BEAN> beans) throws OrmException;

	/**
	 * For each bean in the list, update the bean if it exists,
	 * otherwise saves it
	 * @param bean the bean to persist
	 * @return
	 * @throws OrmException
	 */
	<BEAN> SaveOrUpdateQuery<BEAN> saveOrUpdateQuery(BEAN bean) throws OrmException;

	/**
	 * For each bean in the list, update the bean if it exists,
	 * otherwise saves it
	 * @param beans the beans to persist
	 * @param cascade whether to saveOrUpdate the children recursively
	 * @return
	 * @throws OrmException
	 */
	<BEAN> SaveOrUpdateQuery<List<BEAN>> saveOrUpdateQuery(Collection<BEAN> beans) throws OrmException;

	/**
	 * A script executor useful to execute multiple sql statement from files.
	 * @return
	 * @throws OrmException
	 */
	ScriptExecutor scriptExecutor() throws OrmException;

	/**
	 * An executor to perform any kind of plain SQL statements.
	 * @return
	 */
	SqlExecutor sqlExecutor();

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	<T> Transaction<T> tx(TransactionCallback<T> transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	<T> Transaction<T> tx(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback);

	/**
	 * Execute asynchronously a Transaction
	 * @param transactionCallback
	 * @return
	 */
	<T> CompletableFuture<T> txAsync(TransactionCallback<T> transactionCallback);

	/**
	 * Execute asynchronously a Transaction
	 * @param transactionCallback
	 * @return
	 */
	<T> CompletableFuture<T> txAsync(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	<T> T txNow(TransactionCallback<T> transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	<T> T txNow(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	TransactionVoid txVoid(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	TransactionVoid txVoid(TransactionVoidCallback transactionCallback);

	/**
	 * Execute asynchronously a Transaction
	 * @param transactionCallback
	 * @return
	 */
	CompletableFuture<Void> txVoidAsync(TransactionVoidCallback transactionCallback);

	/**
	 * Execute asynchronously a Transaction
	 * @param transactionCallback
	 * @return
	 */
	CompletableFuture<Void> txVoidAsync(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	void txVoidNow(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	void txVoidNow(TransactionVoidCallback transactionCallback);

	/**
	 * @param aggregatedUser
	 * @return
	 */
	<BEAN> UpdateQuery<BEAN> updateQuery(BEAN bean) throws OrmException;

	/**
	 * @param aggregatedUser
	 * @return
	 */
	<BEAN> BEAN update(BEAN bean) throws OrmException;

	/**
	 * Update the values of the existing beans in the database
	 * @param <BEAN>
	 * @param beans the beans to update
	 * @throws OrmException
	 * @return
	 */
	<BEAN> UpdateQuery<List<BEAN>> updateQuery(Collection<BEAN> beans) throws OrmException;

	/**
	 * Update the values of the existing beans in the database
	 * @param <BEAN>
	 * @param beans the beans to update
	 * @throws OrmException
	 * @return
	 */
	<BEAN> List<BEAN> update(Collection<BEAN> beans) throws OrmException;


	/**
	 * Update the entries of a specific TABLE
	 * @param clazz the TABLE related Class
	 * @throws OrmException
	 */
	<BEAN> CustomUpdateQuery updateQuery(Class<BEAN> clazz) throws OrmException;

}
