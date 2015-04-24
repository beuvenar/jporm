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
package com.jporm.core.session.jdbctemplate;

import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.core.JPO;
import com.jporm.core.JPOBuilder;

/**
 *
 * @author cinafr
 *
 */
public class JPOrmJdbcTemplateBuilder extends JPOBuilder {

	public static JPOrmJdbcTemplateBuilder get() {
		return new JPOrmJdbcTemplateBuilder();
	}

	/**
	 * Create a {@link JPO} instance
	 * @param sessionProvider
	 * @return
	 */
	public JPO build(final DataSource dataSource, final PlatformTransactionManager platformTransactionManager) {
		return build(new JdbcTemplateSessionProvider(dataSource, platformTransactionManager));
	}

}
