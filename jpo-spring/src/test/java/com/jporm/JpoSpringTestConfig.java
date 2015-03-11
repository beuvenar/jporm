/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jporm.test.TestConstants;
import com.jporm.transactional.H2TransactionalExecutor;

@Configuration
@EnableTransactionManagement
@PropertySource({TestConstants.CONFIG_FILE})
public class JpoSpringTestConfig {

	@Bean
	public DataSource getH2DataSource(final Environment env) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("H2.jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("H2.jdbc.url"));
		dataSource.setUsername(env.getProperty("H2.jdbc.username"));
		dataSource.setPassword(env.getProperty("H2.jdbc.password"));
		dataSource.setDefaultAutoCommit(false);

		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager getH2DataSourceTransactionManager(final DataSource dataSource) {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource);
		return txManager;
	}

	@Bean
	public H2TransactionalExecutor getH2TransactionalExecutor() {
		return new H2TransactionalExecutor();
	}

	@Bean
	public SpringLiquibase getSpringLiquibase(final DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog(TestConstants.LIQUIBASE_FILE);
		//liquibase.setContexts("development, production");
		return liquibase;
	}
}