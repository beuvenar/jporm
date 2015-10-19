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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.jporm.annotation.cache.CacheInfo;
import com.jporm.annotation.cascade.CascadeInfo;
import com.jporm.annotation.cascade.CascadeType;
import com.jporm.exception.OrmException;
import com.jporm.mapper.OrmClassTool;
import com.jporm.mapper.ServiceCatalog;
import com.jporm.query.crud.ADelete;
import com.jporm.query.crud.AFind;
import com.jporm.query.crud.ASave;
import com.jporm.query.crud.ASaveOrUpdate;
import com.jporm.query.crud.AUpdate;
import com.jporm.query.crud.Delete;
import com.jporm.query.crud.Find;
import com.jporm.query.crud.Save;
import com.jporm.query.crud.SaveOrUpdate;
import com.jporm.query.crud.Update;
import com.jporm.query.crud.executor.SaveOrUpdateType;
import com.jporm.query.delete.DeleteQuery;
import com.jporm.query.delete.DeleteQueryOrm;
import com.jporm.query.delete.DeleteWhere;
import com.jporm.query.find.CustomFindQuery;
import com.jporm.query.find.CustomFindQueryOrm;
import com.jporm.query.find.FindQuery;
import com.jporm.query.find.FindQueryOrm;
import com.jporm.query.find.FindWhere;
import com.jporm.query.save.SaveQueryOrm;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.query.update.CustomUpdateQueryImpl;
import com.jporm.query.update.UpdateQueryOrm;
import com.jporm.script.ScriptExecutor;
import com.jporm.script.ScriptExecutorImpl;
import com.jporm.transaction.OrmTransactionDefinition;
import com.jporm.transaction.Transaction;
import com.jporm.transaction.TransactionDefinition;

/**
 *
 * @author Francesco Cina
 *
 * 27/giu/2011
 */
public class SessionImpl implements Session {

    private final ServiceCatalog serviceCatalog;
    private final SessionProvider sessionProvider;

    public SessionImpl(final ServiceCatalog serviceCatalog, final SessionProvider sessionProvider) {
        this.serviceCatalog = serviceCatalog;
        this.sessionProvider = sessionProvider;
    }

    @Override
    public <BEAN> Delete<BEAN> delete(final BEAN bean) {
        return new ADelete<BEAN>() {
            @Override
            public int now() {
                Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
                final OrmClassTool<BEAN> ormClassTool = getOrmClassToolMap().getOrmClassTool(clazz);
                DeleteWhere<BEAN> query = deleteQuery(clazz).cascade(getCascade()).where();
                String[] pks = ormClassTool.getClassMap().getPrimaryKeyColumnJavaNames();
                Object[] pkValues = ormClassTool.getOrmPersistor().getPropertyValues(pks, bean);
                for (int i = 0; i < pks.length; i++) {
                    query.eq(pks[i], pkValues[i]);
                }
                return query.now();
            }
        };
    }

    @Override
    public final <BEAN> Delete<List<BEAN>> delete(final List<BEAN> beans) throws OrmException {
        return new ADelete<List<BEAN>>(){
            @Override
            public int now() {
                int result = 0;
                for (final BEAN bean : beans) {
                    result += delete(bean).cascade(getCascade()).now();
                }
                return result;
            }
        };
    }

    @Override
    public final <BEAN> DeleteQuery<BEAN> deleteQuery(final Class<BEAN> clazz) throws OrmException {
        final DeleteQueryOrm<BEAN> delete = new DeleteQueryOrm<BEAN>(clazz, serviceCatalog);
        return delete;
    }

    @Override
    public <T> T doInTransaction(final TransactionCallback<T> transactionCallback)
            throws OrmException {
        return doInTransaction(new OrmTransactionDefinition(), transactionCallback);
    }

    @Override
    public <T> T doInTransaction(final TransactionDefinition transactionDefinition,
            final TransactionCallback<T> transactionCallback) throws OrmException {
        T result;
        Transaction tx = transaction(transactionDefinition);
        try {
            result = transactionCallback.doInTransaction(this);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } catch (Error e) {
            tx.rollback();
            throw e;
        }
        return result;
    }

    @Override
    public final <BEAN> Find<BEAN> find(final BEAN bean) throws OrmException {
        OrmClassTool<BEAN> ormClassTool = (OrmClassTool<BEAN>) getOrmClassToolMap().getOrmClassTool(bean.getClass());
        String[] pks = ormClassTool.getClassMap().getPrimaryKeyColumnJavaNames();
        Object[] values =  ormClassTool.getOrmPersistor().getPropertyValues(pks, bean);
        return find((Class<BEAN>) bean.getClass(), pks, values);
    }

    @Override
    public final <BEAN> Find<BEAN> find(final Class<BEAN> clazz, final Object value) throws OrmException {
        OrmClassTool<BEAN> ormClassTool = getOrmClassToolMap().getOrmClassTool(clazz);
        String[] pks = ormClassTool.getClassMap().getPrimaryKeyColumnJavaNames();
    	if (pks.length > 1) {
    		throw new RuntimeException("Cannot use this find method for class [" + clazz.getName() + "]. Expected maximum one primary key field but found " + pks.length + ": " + Arrays.toString(pks));
    	}
    	return find(clazz, pks, new Object[]{value});
    }

    private final <BEAN> Find<BEAN> find(final Class<BEAN> clazz, final String[] pks, final Object[] values) throws OrmException {
    	final OrmClassTool<BEAN> ormClassTool = getOrmClassToolMap().getOrmClassTool(clazz);

        return new AFind<BEAN>() {
            @Override
            public BEAN get() {
                CacheInfo cacheInfo = ormClassTool.getClassMap().getCacheInfo();
                FindWhere<BEAN> query = findQuery(clazz, clazz.getSimpleName())
                        .lazy(isLazy()).cache(cacheInfo.cacheToUse(getCache())).ignore(getIgnoredFields()).where();
                for (int i = 0; i < pks.length; i++) {
                    query.eq(pks[i], values[i]);
                }
                return query.maxRows(1).get();
            }

            @Override
            public BEAN getUnique() {
                FindWhere<BEAN> query = findQuery(clazz, clazz.getSimpleName())
                        .lazy(isLazy()).cache(getCache()).ignore(getIgnoredFields()).where();
                for (int i = 0; i < pks.length; i++) {
                    query.eq(pks[i], values[i]);
                }
                return query.maxRows(1).getUnique();
            }

            @Override
            public boolean exist() {
                FindWhere<BEAN> query = findQuery(clazz).where();
                for (int i = 0; i < pks.length; i++) {
                    query.eq(pks[i], values[i]);
                }
                return query.maxRows(1).getRowCount()>0;
            }
        };
    }

    @Override
    public final <BEAN> FindQuery<BEAN> findQuery(final Class<BEAN> clazz) throws OrmException {
        return findQuery(clazz, clazz.getSimpleName());
    }

    @Override
    public final <BEAN> FindQuery<BEAN> findQuery(final Class<BEAN> clazz, final String alias) throws OrmException {
        final FindQueryOrm<BEAN> query = new FindQueryOrm<BEAN>(serviceCatalog, clazz, alias);
        return query;
    }

    @Override
    public final CustomFindQuery findQuery(final String selectClause, final Class<?> clazz, final String alias ) throws OrmException {
        final CustomFindQueryOrm query = new CustomFindQueryOrm(new String[]{selectClause}, serviceCatalog, clazz, alias);
        return query;
    }

    @Override
    public final CustomFindQuery findQuery(final String[] selectFields, final Class<?> clazz, final String alias ) throws OrmException {
        final CustomFindQueryOrm query = new CustomFindQueryOrm(selectFields, serviceCatalog, clazz, alias);
        return query;
    }

    public final ServiceCatalog getOrmClassToolMap() {
        return serviceCatalog;
    }

    @Override
    public <BEAN> Save<BEAN> save(final BEAN bean) {
        return new ASave<BEAN>() {
            @Override
            public BEAN now() {
                if (bean!=null) {
                    serviceCatalog.getValidatorService().validator(bean).validateThrowException();
                    Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
                    final OrmClassTool<BEAN> ormClassTool = getOrmClassToolMap().getOrmClassTool(clazz);
                    BEAN newBean = ormClassTool.getOrmPersistor().clone(bean);
                    return new SaveQueryOrm<BEAN>(newBean, serviceCatalog)
                            .cascade(getCascade()).now();
                }
                return null;
            }
        };
    }

    @Override
    public <BEAN> Save<List<BEAN>> save(final Collection<BEAN> beans) throws OrmException {
        return new ASave<List<BEAN>>() {
            @Override
            public List<BEAN> now() {
                final List<BEAN> result = new ArrayList<BEAN>();
                for (final BEAN bean : beans) {
                    result.add(save(bean).cascade(getCascade()).now());
                }
                return result;
            }
        };
    }

    @Override
    public <BEAN> SaveOrUpdate<BEAN> saveOrUpdate(final BEAN bean) throws OrmException {
        return saveOrUpdate(bean, CascadeType.ALWAYS.getInfo());
    }

    public <BEAN> SaveOrUpdate<BEAN> saveOrUpdate(final BEAN bean, final CascadeInfo cascadeInfo) throws OrmException {
        return new ASaveOrUpdate<BEAN>() {
            @Override
            public BEAN now() {
                serviceCatalog.getValidatorService().validator(bean).validateThrowException();
                Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
                final OrmClassTool<BEAN> ormClassTool = getOrmClassToolMap().getOrmClassTool(clazz);

                if (ormClassTool.getOrmPersistor().hasGenerator()) {
                    if(ormClassTool.getOrmPersistor().useGenerators(bean)) {
                        if (cascadeInfo.onSave()) {
                            return new SaveQueryOrm<BEAN>(ormClassTool.getOrmPersistor().clone(bean), serviceCatalog)
                                    .cascade(getCascade()).saveOrUpdate(SaveOrUpdateType.SAVE_OR_UPDATE).now();
                        }
                    } else {
                        if (cascadeInfo.onUpdate()) {
                            return new UpdateQueryOrm<BEAN>(ormClassTool.getOrmPersistor().clone(bean), serviceCatalog)
                                    .cascade(getCascade()).saveOrUpdate(SaveOrUpdateType.SAVE_OR_UPDATE).now();
                        }
                    }
                } else {
                    if (find(bean).exist()) {
                        if (cascadeInfo.onUpdate()) {
                            return new UpdateQueryOrm<BEAN>(ormClassTool.getOrmPersistor().clone(bean), serviceCatalog)
                                    .cascade(getCascade()).saveOrUpdate(SaveOrUpdateType.SAVE_OR_UPDATE).now();
                        }
                    }else {
                        if (cascadeInfo.onSave()) {
                            return new SaveQueryOrm<BEAN>(ormClassTool.getOrmPersistor().clone(bean), serviceCatalog)
                                    .cascade(getCascade()).saveOrUpdate(SaveOrUpdateType.SAVE_OR_UPDATE).now();
                        }
                    }
                }
                return bean;
            }
        };
    }

    @Override
    public <BEAN> SaveOrUpdate<List<BEAN>> saveOrUpdate(final Collection<BEAN> beans) throws OrmException {
        return saveOrUpdate(beans, CascadeType.ALWAYS.getInfo());
    }

    public <BEAN> SaveOrUpdate<List<BEAN>> saveOrUpdate(final Collection<BEAN> beans, final CascadeInfo cascadeInfo) throws OrmException {
        return new ASaveOrUpdate<List<BEAN>>() {
            @Override
            public List<BEAN> now() {
                final List<BEAN> result = new ArrayList<BEAN>();
                for (final BEAN bean : beans) {
                    result.add(saveOrUpdate(bean, cascadeInfo).cascade(getCascade()).now());
                }
                return result;
            }
        };
    }

    @Override
    public final ScriptExecutor scriptExecutor() throws OrmException {
        return new ScriptExecutorImpl(this);
    }

    @Override
    public SqlExecutor sqlExecutor() throws OrmException {
        return new SqlExecutorImpl(sessionProvider.sqlPerformerStrategy(), serviceCatalog);
    }

    @Override
    public final Transaction transaction() throws OrmException {
        return this.transaction(new OrmTransactionDefinition());
    }

    @Override
    public Transaction transaction(
            final TransactionDefinition transactionDefinition) throws OrmException {
        return sessionProvider.getTransaction(transactionDefinition);
    }

    @Override
    public <BEAN> Update<BEAN> update(final BEAN bean) throws OrmException {
        return new AUpdate<BEAN>() {
            @Override
            public BEAN now() {
                serviceCatalog.getValidatorService().validator(bean).validateThrowException();
                Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
                final OrmClassTool<BEAN> ormClassTool = getOrmClassToolMap().getOrmClassTool(clazz);
                BEAN newBean = ormClassTool.getOrmPersistor().clone(bean);
                return new UpdateQueryOrm<BEAN>(newBean, serviceCatalog)
                        .cascade(getCascade()).now();
            }
        };
    }

    @Override
    public <BEAN> Update<List<BEAN>> update(final Collection<BEAN> beans) throws OrmException {
        return new AUpdate<List<BEAN>>(){
            @Override
            public List<BEAN> now() {
                final List<BEAN> result = new ArrayList<BEAN>();
                for (final BEAN bean : beans) {
                    result.add(update(bean).cascade(getCascade()).now());
                }
                return result;
            }
        };
    }

    @Override
    public final <BEAN> CustomUpdateQuery updateQuery(final Class<BEAN> clazz) throws OrmException {
        final CustomUpdateQueryImpl update = new CustomUpdateQueryImpl(clazz, serviceCatalog);
        return update;
    }

    /**
     * @return the sessionProvider
     */
    public SessionProvider getSessionProvider() {
        return sessionProvider;
    }

}
