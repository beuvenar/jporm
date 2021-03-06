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
package com.jporm.rm.session.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.inject.ClassToolMap;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.persistor.Persistor;
import com.jporm.rm.query.delete.CustomDeleteQuery;
import com.jporm.rm.query.delete.impl.CustomDeleteQueryImpl;
import com.jporm.rm.query.delete.impl.DeleteQueryImpl;
import com.jporm.rm.query.delete.impl.DeleteQueryListDecorator;
import com.jporm.rm.query.find.CustomFindQuery;
import com.jporm.rm.query.find.CustomFindQueryWhere;
import com.jporm.rm.query.find.CustomResultFindQueryBuilder;
import com.jporm.rm.query.find.FindQuery;
import com.jporm.rm.query.find.impl.CustomFindQueryImpl;
import com.jporm.rm.query.find.impl.CustomResultFindQueryBuilderImpl;
import com.jporm.rm.query.find.impl.FindQueryImpl;
import com.jporm.rm.query.save.CustomSaveQuery;
import com.jporm.rm.query.save.SaveOrUpdateQuery;
import com.jporm.rm.query.save.SaveQuery;
import com.jporm.rm.query.save.impl.CustomSaveQueryImpl;
import com.jporm.rm.query.save.impl.SaveOrUpdateQueryListDecorator;
import com.jporm.rm.query.save.impl.SaveQueryImpl;
import com.jporm.rm.query.update.CustomUpdateQuery;
import com.jporm.rm.query.update.UpdateQuery;
import com.jporm.rm.query.update.impl.CustomUpdateQueryImpl;
import com.jporm.rm.query.update.impl.UpdateQueryImpl;
import com.jporm.rm.session.ScriptExecutor;
import com.jporm.rm.session.Session;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.rm.session.script.ScriptExecutorImpl;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dialect.DBType;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class SessionImpl implements Session {

    private final ServiceCatalog serviceCatalog;
    private final ConnectionProvider sessionProvider;
    private final ClassToolMap classToolMap;
    private final SqlFactory sqlFactory;
    private final DBType dbType;
    private final boolean autoCommit;

    public SessionImpl(final ServiceCatalog serviceCatalog, final ConnectionProvider sessionProvider, final boolean autoCommit) {
        this.serviceCatalog = serviceCatalog;
        this.sessionProvider = sessionProvider;
        this.autoCommit = autoCommit;
        classToolMap = serviceCatalog.getClassToolMap();
        dbType = sessionProvider.getDBType();
        sqlFactory = new SqlFactory(classToolMap, serviceCatalog.getPropertiesFactory());
    }

    @Override
    public <BEAN> int delete(final BEAN bean) throws JpoException {
        return delete(Arrays.asList(bean));
    }

    @Override
    public final <BEAN> CustomDeleteQuery<BEAN> delete(final Class<BEAN> clazz) throws JpoException {
        final CustomDeleteQueryImpl<BEAN> delete = new CustomDeleteQueryImpl<BEAN>(clazz, serviceCatalog, sqlExecutor(), sqlFactory, dbType);
        return delete;
    }

    @Override
    public <BEAN> int delete(final Collection<BEAN> beans) throws JpoException {
        final DeleteQueryListDecorator queryList = new DeleteQueryListDecorator();
        Map<Class<?>, List<BEAN>> beansByClass = beans.stream().collect(Collectors.groupingBy(BEAN::getClass));
        beansByClass.forEach((clazz, classBeans) -> {
            queryList.add(new DeleteQueryImpl<BEAN>(classBeans, (Class<BEAN>) clazz, serviceCatalog, sqlExecutor(), sqlFactory, dbType));
        });
        return queryList.execute();
    }

    @Override
    public final <BEAN> CustomFindQuery<BEAN> find(final Class<BEAN> clazz) throws JpoException {
        return find(clazz, clazz.getSimpleName());
    }

    private final <BEAN> FindQuery<BEAN> find(final Class<BEAN> clazz, final ClassDescriptor<BEAN> descriptor, final String[] pks, final Object[] values)
            throws JpoException {
        FindQueryImpl<BEAN> findQuery = new FindQueryImpl<BEAN>(serviceCatalog, clazz, clazz.getSimpleName(), sqlExecutor(), sqlFactory, dbType);
        CustomFindQueryWhere<BEAN> query = findQuery.where();
        for (int i = 0; i < pks.length; i++) {
            query.eq(pks[i], values[i]);
        }
        query.limit(1);
        return findQuery;
    }

    @Override
    public final <BEAN> CustomFindQuery<BEAN> find(final Class<BEAN> clazz, final String alias) throws JpoException {
        final CustomFindQueryImpl<BEAN> query = new CustomFindQueryImpl<BEAN>(serviceCatalog, clazz, alias, sqlExecutor(), sqlFactory, dbType);
        return query;
    }

    @Override
    public <BEAN> CustomResultFindQueryBuilder find(final String... selectFields) {
        return new CustomResultFindQueryBuilderImpl(selectFields, serviceCatalog, sqlExecutor(), sqlFactory, dbType);
    }

    @Override
    public final <BEAN> FindQuery<BEAN> findById(final Class<BEAN> clazz, final Object value) throws JpoException {
        ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);
        ClassDescriptor<BEAN> descriptor = ormClassTool.getDescriptor();
        String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
        return this.find(clazz, descriptor, pks, new Object[] { value });
    }

    @Override
    public final <BEAN> FindQuery<BEAN> findByModelId(final BEAN model) throws JpoException {
        Class<BEAN> modelClass = (Class<BEAN>) model.getClass();
        ClassTool<BEAN> ormClassTool = classToolMap.get(modelClass);
        ClassDescriptor<BEAN> descriptor = ormClassTool.getDescriptor();
        String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
        Object[] values = ormClassTool.getPersistor().getPropertyValues(pks, model);
        return find(modelClass, descriptor, pks, values);
    }

    /**
     * @return the sessionProvider
     */
    public ConnectionProvider getSessionProvider() {
        return sessionProvider;
    }

    @Override
    public <BEAN> BEAN save(final BEAN bean) {
        return saveQuery(bean).execute().get(0);
    }

    @Override
    public <BEAN> CustomSaveQuery save(final Class<BEAN> clazz, final String... fields) throws JpoException {
        final CustomSaveQuery update = new CustomSaveQueryImpl<BEAN>(clazz, fields, serviceCatalog, sqlExecutor(), sqlFactory, dbType);
        return update;
    }

    @Override
    public <BEAN> List<BEAN> save(final Collection<BEAN> beans) throws JpoException {
        return saveQuery(beans).execute();
    }

    @Override
    public <BEAN> BEAN saveOrUpdate(final BEAN bean) throws JpoException {
        return saveOrUpdateQuery(bean).execute().get(0);
    }

    @Override
    public <BEAN> List<BEAN> saveOrUpdate(final Collection<BEAN> beans) throws JpoException {
        serviceCatalog.getValidatorService().validateThrowException(beans);

        final SaveOrUpdateQueryListDecorator<BEAN> queryList = new SaveOrUpdateQueryListDecorator<BEAN>();
        Map<Class<?>, List<BEAN>> beansByClass = beans.stream().collect(Collectors.groupingBy(BEAN::getClass));
        beansByClass.forEach((clazz, classBeans) -> {
            Class<BEAN> clazzBean = (Class<BEAN>) clazz;
            Persistor<BEAN> persistor = classToolMap.get(clazzBean).getPersistor();
            classBeans.forEach(classBean -> {
                queryList.add(saveOrUpdateQuery(classBean, persistor));
            });
        });
        return queryList.execute();
    }

    @SuppressWarnings("unchecked")
    private <BEAN> SaveOrUpdateQuery<BEAN> saveOrUpdateQuery(final BEAN bean) throws JpoException {
        serviceCatalog.getValidatorService().validateThrowException(bean);
        Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
        final ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);
        return saveOrUpdateQuery(bean, ormClassTool.getPersistor());
    }

    private <BEAN> SaveOrUpdateQuery<BEAN> saveOrUpdateQuery(final BEAN bean, final Persistor<BEAN> persistor) {
        if (toBeSaved(bean, persistor)) {
            return saveQuery(bean);
        }
        return updateQuery(bean);
    }

    @SuppressWarnings("unchecked")
    private <BEAN> SaveQuery<BEAN> saveQuery(final BEAN bean) {
        serviceCatalog.getValidatorService().validateThrowException(bean);
        Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
        return new SaveQueryImpl<BEAN>(Arrays.asList(bean), clazz, serviceCatalog, sqlExecutor(), sqlFactory, dbType);
    }

    private <BEAN> SaveOrUpdateQuery<BEAN> saveQuery(final Collection<BEAN> beans) throws JpoException {
        serviceCatalog.getValidatorService().validateThrowException(beans);
        final SaveOrUpdateQueryListDecorator<BEAN> queryList = new SaveOrUpdateQueryListDecorator<BEAN>();
        Map<Class<?>, List<BEAN>> beansByClass = beans.stream().collect(Collectors.groupingBy(BEAN::getClass));
        beansByClass.forEach((clazz, classBeans) -> {
            queryList.add(new SaveQueryImpl<BEAN>(classBeans, (Class<BEAN>) clazz, serviceCatalog, sqlExecutor(), sqlFactory, dbType));
        });
        return queryList;
    }

    @Override
    public final ScriptExecutor scriptExecutor() throws JpoException {
        return new ScriptExecutorImpl(this);
    }

    @Override
    public SqlExecutor sqlExecutor() throws JpoException {
        return new SqlExecutorImpl(sessionProvider, serviceCatalog.getTypeFactory(), autoCommit);
    }

    /**
     * Returns whether a bean has to be saved. Otherwise it has to be updated
     * because it already exists.
     *
     * @return
     */
    private <BEAN> boolean toBeSaved(final BEAN bean, final Persistor<BEAN> persistor) {
        if (persistor.hasGenerator()) {
            return persistor.useGenerators(bean);
        } else {
            return !findByModelId(bean).exist();
        }
    }

    @Override
    public <BEAN> BEAN update(final BEAN bean) throws JpoException {
        return updateQuery(bean).execute().get(0);
    }

    @Override
    public final <BEAN> CustomUpdateQuery update(final Class<BEAN> clazz) throws JpoException {
        final CustomUpdateQueryImpl update = new CustomUpdateQueryImpl(clazz, serviceCatalog, sqlExecutor(), sqlFactory, dbType);
        return update;
    }

    @Override
    public <BEAN> List<BEAN> update(final Collection<BEAN> beans) throws JpoException {
        serviceCatalog.getValidatorService().validateThrowException(beans);
        final SaveOrUpdateQueryListDecorator<BEAN> queryList = new SaveOrUpdateQueryListDecorator<BEAN>();
        Map<Class<?>, List<BEAN>> beansByClass = beans.stream().collect(Collectors.groupingBy(BEAN::getClass));
        beansByClass.forEach((clazz, classBeans) -> {
            queryList.add(new UpdateQueryImpl<BEAN>(classBeans, (Class<BEAN>) clazz, serviceCatalog, sqlExecutor(), sqlFactory, dbType));
        });
        return queryList.execute();
    }

    private <BEAN> UpdateQuery<BEAN> updateQuery(final BEAN bean) throws JpoException {
        serviceCatalog.getValidatorService().validateThrowException(bean);
        return new UpdateQueryImpl<BEAN>(Arrays.asList(bean), (Class<BEAN>) bean.getClass(), serviceCatalog, sqlExecutor(), sqlFactory, dbType);
    }

}
