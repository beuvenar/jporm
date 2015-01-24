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
package com.jporm.query.find;

import com.jporm.exception.OrmException;
import com.jporm.query.clause.Where;

/**
 * 
 * @author ufo
 *
 */
public interface CustomFindQueryWhere extends Where<CustomFindQueryWhere>, CustomFindQueryCommon {

    CustomFindQuery query();

    /**
     * Set the order by clause.
     * @return
     */
    CustomFindQueryOrderBy orderBy() throws OrmException;

    /**
     * Set the GROUP BY clause
     * @param fields the fields to group by
     * @return
     * @throws OrmException
     */

    CustomFindQueryGroupBy groupBy(String... fields) throws OrmException;

}