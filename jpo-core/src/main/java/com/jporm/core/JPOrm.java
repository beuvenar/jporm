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
package com.jporm.core;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.inject.ServiceCatalogImpl;
import com.jporm.core.query.strategy.QueryExecutionStrategy;
import com.jporm.core.session.Session;
import com.jporm.core.session.SessionProvider;
import com.jporm.core.session.impl.SessionImpl;
import com.jporm.core.transaction.Transaction;
import com.jporm.core.transaction.TransactionCallback;
import com.jporm.core.transaction.TransactionDefinition;
import com.jporm.core.transaction.TransactionVoid;
import com.jporm.core.transaction.TransactionVoidCallback;
import com.jporm.sql.dialect.DBProfile;

/**
 *
 * @author Francesco Cina'
 *
 * 26/ago/2011
 */
public class JPOrm implements JPO {

	private static Integer JPORM_INSTANCES_COUNT = Integer.valueOf(0);
	private final JPOConfigImpl config = new JPOConfigImpl();
	private final ServiceCatalogImpl serviceCatalog;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Integer instanceCount;

	/**
	 * Create a new instance of JPOrm.
	 *
	 * @param sessionProvider
	 */
	public JPOrm(final SessionProvider sessionProvider) {
		synchronized (JPORM_INSTANCES_COUNT) {
			instanceCount = JPORM_INSTANCES_COUNT++;
		}
		logger.info("Building new instance of JPO (instance [{}])", instanceCount); //$NON-NLS-1$
		serviceCatalog = config.getServiceCatalog();
		serviceCatalog.setSessionProvider(sessionProvider);
		serviceCatalog.setSession(new SessionImpl(serviceCatalog, sessionProvider));
		updateDBProfile(sessionProvider.getDBType().getDBProfile());
	}

	@Override
	public final Session session() {
		return serviceCatalog.getSession();
	}

	@Override
	public synchronized void destory() {
		serviceCatalog.destroy();
	}


	public ServiceCatalog getServiceCatalog() {
		return serviceCatalog;
	}

	private void updateDBProfile(DBProfile dbProfile) {
		serviceCatalog.setDbProfile(dbProfile);
		serviceCatalog.setQueryExecutionStrategy(QueryExecutionStrategy.build(dbProfile.getDbFeatures().isReturnCountsOnBatchUpdate()));
	}

	/**
	 * @return the config
	 */
	@Override
	public JPOConfig config() {
		return config;
	}

	@Override
	public <T> Transaction<T> tx(TransactionCallback<T> transactionCallback) {
		return session().tx(transactionCallback);
	}

	@Override
	public <T> Transaction<T> tx(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return session().tx(transactionDefinition, transactionCallback);
	}

	@Override
	public <T> CompletableFuture<T> txAsync(TransactionCallback<T> transactionCallback) {
		return session().txAsync(transactionCallback);
	}

	@Override
	public <T> CompletableFuture<T> txAsync(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return session().txAsync(transactionDefinition, transactionCallback);
	}

	@Override
	public <T> T txNow(TransactionCallback<T> transactionCallback) {
		return session().txNow(transactionCallback);
	}

	@Override
	public <T> T txNow(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return session().txNow(transactionDefinition, transactionCallback);
	}

	@Override
	public TransactionVoid txVoid(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		return session().txVoid(transactionDefinition, transactionCallback);
	}

	@Override
	public TransactionVoid txVoid(TransactionVoidCallback transactionCallback) {
		return session().txVoid(transactionCallback);
	}

	@Override
	public CompletableFuture<Void> txVoidAsync(TransactionVoidCallback transactionCallback) {
		return session().txVoidAsync(transactionCallback);
	}

	@Override
	public CompletableFuture<Void> txVoidAsync(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		return session().txVoidAsync(transactionDefinition, transactionCallback);
	}

	@Override
	public void txVoidNow(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		session().txVoidNow(transactionDefinition, transactionCallback);

	}

	@Override
	public void txVoidNow(TransactionVoidCallback transactionCallback) {
		session().txVoidNow( transactionCallback);
	}

}