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
package com.jporm.commons.core.exception.sql;

/**
 * 
 * Data access exception thrown when a resource fails temporarily and the
 * operation can be retried.
 * 
 * @author cinafr
 *
 */
public class JpoSqlTransientDataAccessResourceException extends JpoSqlException {

    private static final long serialVersionUID = 1L;

    public JpoSqlTransientDataAccessResourceException(final Exception e) {
        super(e);
    }

    public JpoSqlTransientDataAccessResourceException(final String message, final Exception e) {
        super(message, e);
    }

}
