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

import java.sql.Statement;

import com.jporm.sql.dialect.querytemplate.QueryTemplate;

/**
 * 
 * @author Francesco Cina
 *
 * 28/giu/2011
 * 
 * This class take care of the small differences between different database implementations
 */
public interface DBProfile  {

    /**
     * Return true if the DB has sequence support and the method getGeneratedKeys of the  {@link Statement}
     * correctly return the sequence value generated using a sequence in an insert query.
     * @return
     */
    boolean isSequenceSupport();

    /**
     * Return true if the DB has autogenerated key features.
     * @return
     */
    boolean isAutogeneratedKeySupport();

    QueryTemplate getQueryTemplate();

}
