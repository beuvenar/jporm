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
package com.jporm.commons.core.inject;

import com.jporm.cache.CacheManager;
import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.find.cache.CacheStrategy;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.types.TypeConverterFactory;
import com.jporm.validator.ValidatorService;

/**
 *
 * @author Francesco Cina
 *
 *         22/mag/2011
 *
 */
public interface ServiceCatalog {

    AsyncTaskExecutor getAsyncTaskExecutor();

    CacheManager getCacheManager();

    CacheStrategy getCacheStrategy();

    ClassToolMap getClassToolMap();

    ConfigService getConfigService();

    PropertiesFactory getPropertiesFactory();

    SqlCache getSqlCache();

    TypeConverterFactory getTypeFactory();

    ValidatorService getValidatorService();

}
