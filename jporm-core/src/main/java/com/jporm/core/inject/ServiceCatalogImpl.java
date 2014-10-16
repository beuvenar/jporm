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
package com.jporm.core.inject;

import com.jporm.JPO;
import com.jporm.cache.CacheManager;
import com.jporm.core.cache.SimpleCacheManager;
import com.jporm.core.dialect.DBProfile;
import com.jporm.core.dialect.UnknownDBProfile;
import com.jporm.core.mapper.ClassToolMap;
import com.jporm.core.mapper.ClassToolMapImpl;
import com.jporm.core.persistor.type.TypeFactory;
import com.jporm.core.query.crud.cache.CRUDQueryCache;
import com.jporm.core.query.crud.cache.CRUDQueryCacheImpl;
import com.jporm.core.query.crud.executor.OrmCRUDQueryExecutor;
import com.jporm.core.query.crud.executor.OrmCRUDQueryExecutorImpl;
import com.jporm.core.query.find.cache.CacheStrategy;
import com.jporm.core.query.find.cache.CacheStrategyImpl;
import com.jporm.core.query.namesolver.PropertiesFactory;
import com.jporm.core.session.NullSessionProvider;
import com.jporm.core.session.SessionImpl;
import com.jporm.core.validator.NullValidatorService;
import com.jporm.validator.ValidatorService;


/**
 *
 * @author Francesco Cina
 *
 * 22/mag/2011
 */
public class ServiceCatalogImpl implements ServiceCatalog {

	private final TypeFactory typeFactory = new TypeFactory();
	private final ClassToolMap classToolMap;
	private DBProfile dbProfile = new UnknownDBProfile();
	private ValidatorService validatorService = new NullValidatorService();
	private CacheManager cacheManager = new SimpleCacheManager();
	private final PropertiesFactory propertiesFactory = new PropertiesFactory();
	private final CacheStrategy cacheStrategy = new CacheStrategyImpl(this);
	private SessionImpl session = new SessionImpl(this, new NullSessionProvider());
	private final OrmCRUDQueryExecutor ormQueryExecutor = new OrmCRUDQueryExecutorImpl(this);
	private final CRUDQueryCache crudQueryCache = new CRUDQueryCacheImpl();

	public ServiceCatalogImpl(final JPO jpOrm) {
		classToolMap = new ClassToolMapImpl(jpOrm);
	}

	@Override
	public TypeFactory getTypeFactory() {
		return typeFactory;
	}

	/**
	 * @return the dbProfile
	 */
	@Override
	public DBProfile getDbProfile() {
		return dbProfile;
	}

	/**
	 * @param dbProfile the dbProfile to set
	 */
	public void setDbProfile(final DBProfile dbProfile) {
		this.dbProfile = dbProfile;
	}

	/**
	 * @return the validatorService
	 */
	@Override
	public ValidatorService getValidatorService() {
		return validatorService;
	}

	/**
	 * @param validatorService the validatorService to set
	 */
	public void setValidatorService(final ValidatorService validatorService) {
		this.validatorService = validatorService;
	}

	/**
	 * @return the cacheManager
	 */
	@Override
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public PropertiesFactory getPropertiesFactory() {
		return propertiesFactory;
	}

	/**
	 * @return the cacheStrategy
	 */
	@Override
	public CacheStrategy getCacheStrategy() {
		return cacheStrategy;
	}

	/**
	 * @return the session
	 */
	@Override
	public SessionImpl getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(final SessionImpl session) {
		this.session = session;
	}

	@Override
	public OrmCRUDQueryExecutor getOrmQueryExecutor() {
		return ormQueryExecutor;
	}

	@Override
	public CRUDQueryCache getCrudQueryCache() {
		return crudQueryCache;
	}

	@Override
	public ClassToolMap getClassToolMap() {
		return classToolMap;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
