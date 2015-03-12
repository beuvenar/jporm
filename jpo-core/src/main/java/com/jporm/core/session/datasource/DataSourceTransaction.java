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
package com.jporm.core.session.datasource;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoRollbackException;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.commons.core.transaction.TransactionIsolation;

/**
 *
 * @author Francesco Cina
 *
 * 18/giu/2011
 */
public class DataSourceTransaction implements Transaction {

    private final DataSourceConnection conn;
    private final DataSourceTransactionManager transactionManager;
    private boolean isClosed = false;
    private boolean rollbackOnly = false;

    public DataSourceTransaction(final DataSourceSessionProvider dataSourceSessionProvider, final TransactionDefinition transactionDefinition, final DataSourceTransactionManager dataSourceTransactionManager) {
        transactionManager = dataSourceTransactionManager;
        conn = dataSourceSessionProvider.getConnection(transactionDefinition.isReadOnly());
        if (transactionDefinition.getTimeout()>0) {
        	getConnection().setExpireInstant(System.currentTimeMillis() + (transactionDefinition.getTimeout()*1000));
        }
        if (transactionDefinition.getIsolationLevel() != TransactionIsolation.DEFAULT) {
            getConnection().setTransactionIsolation(transactionDefinition.getIsolationLevel().getTransactionIsolation());
        }
    }

    @Override
    public void setRollbackOnly() throws JpoException {
        transactionManager.setRollbackOnly(this);
    }

    @Override
    public void rollback() throws JpoException {
        transactionManager.rollback(this);
    }

    @Override
    public void commit() throws JpoException, JpoRollbackException {
        transactionManager.commit(this);
    }


    /**
     * @return the conn
     */
    public DataSourceConnection getConnection() {
        return conn;
    }

    /**
     * @return the isClosed
     */
    @Override
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * @param isClosed the isClosed to set
     */
    public void setClosed(final boolean isClosed) {
        this.isClosed = isClosed;
    }

    /**
     * @return the rollbackOnly
     */
    public boolean isRollbackOnly() {
        return rollbackOnly;
    }

    /**
     * @param rollbackOnly the rollbackOnly to set
     */
    public void setRollbackOnly(final boolean rollbackOnly) {
        this.rollbackOnly = rollbackOnly;
    }

}
