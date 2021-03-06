/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rm;

import javax.sql.DataSource;

import com.jporm.commons.core.builder.AbstractJpoBuilder;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.connection.impl.DataSourceConnectionProvider;
import com.jporm.sql.dialect.DBType;

public class JpoRmBuilder extends AbstractJpoBuilder<JpoRmBuilder> {

    public static JpoRmBuilder get() {
        return new JpoRmBuilder();
    }

    private JpoRmBuilder() {
    }

    /**
     * Create a {@link JpoRm} instance
     * 
     * @param connectionProvider
     * @return
     */
    public JpoRm build(final ConnectionProvider connectionProvider) {
        return new JpoRmImpl(connectionProvider, getServiceCatalog());
    }

    /**
     * Create a {@link JpoRm} instance
     * 
     * @param dataSource
     * @return
     */
    public JpoRm build(final DataSource dataSource) {
        return build(new DataSourceConnectionProvider(dataSource));
    }

    /**
     * Create a {@link JpoRm} instance
     * 
     * @param dataSource
     * @param dbType
     * @return
     */
    public JpoRm build(final DataSource dataSource, final DBType dbType) {
        return build(new DataSourceConnectionProvider(dataSource, dbType));
    }

}
