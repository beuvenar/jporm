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
package com.jporm.sql.dialect;

import com.jporm.sql.dialect.querytemplate.QueryTemplate;
import com.jporm.sql.dialect.querytemplate.UnknownQueryTemplate;

/**
 * 
 * @author Francesco Cina
 *
 * 28/giu/2011
 * 
 * This is the default {@link DBProfile} used by the orm.
 * It is supposed that the unknown DB supports all the needed features.
 */
public class UnknownDBProfile implements DBProfile {

    private QueryTemplate queryTemplate = new UnknownQueryTemplate();

    @Override
    public boolean isSequenceSupport() {
        return true;
    }

    @Override
    public boolean isAutogeneratedKeySupport() {
        return true;
    }

    @Override
    public QueryTemplate getQueryTemplate() {
        return queryTemplate;
    }

}
