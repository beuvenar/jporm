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
package com.jporm.core.query.crud.executor;

import com.jporm.core.inject.ServiceCatalog;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 14, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class OrmCRUDQueryExecutorImpl implements OrmCRUDQueryExecutor {

    private final OrmCRUDQueryExecutorSaveOrUpdate save;
    private final OrmCRUDQueryExecutorFind find;
    private final OrmCRUDQueryExecutorDelete delete;

    public OrmCRUDQueryExecutorImpl(final ServiceCatalog serviceCatalog) {
        save = new OrmCRUDQueryExecutorSaveOrUpdateImpl(serviceCatalog);
        find = new OrmCRUDQueryExecutorFindImpl(serviceCatalog);
        delete = new OrmCRUDQueryExecutorDeleteImpl(serviceCatalog);
    }

    @Override
    public OrmCRUDQueryExecutorSaveOrUpdate saveOrUpdate() {
        return save;
    }

    @Override
    public OrmCRUDQueryExecutorFind find() {
        return find;
    }

    @Override
    public OrmCRUDQueryExecutorDelete delete() {
        return delete;
    }

}
