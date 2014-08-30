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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 25, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.query.crud;

import com.jporm.factory.ObjectBuilder;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 25, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public abstract class AFind<BEAN> implements Find<BEAN> {

    private boolean _lazy = false;
    private String cache = ObjectBuilder.EMPTY_STRING;
    private String[] _ignoredFields = ObjectBuilder.EMPTY_STRING_ARRAY;

    @Override
    public final Find<BEAN> lazy(final boolean lazy) {
        this._lazy = lazy;
        return this;
    }

    public boolean isLazy() {
        return _lazy;
    }

    protected String getCache() {
        return cache;
    }

    @Override
    public final Find<BEAN> cache(final String cacheName) {
        this.cache = cacheName;
        return this;
    }

    @Override
    public final Find<BEAN> ignore(final String... fields) {
        return ignore(true, fields);
    }

    @Override
    public final Find<BEAN> ignore(final boolean excludeFieldsCondition, final String... fields) {
        if(excludeFieldsCondition) {
            _ignoredFields = fields;
        }
        return this;
    }

    /**
     * @return the _ecludeFields
     */
    public String[] getIgnoredFields() {
        return _ignoredFields;
    }

}
