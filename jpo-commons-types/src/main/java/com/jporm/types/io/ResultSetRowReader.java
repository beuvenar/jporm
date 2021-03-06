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
package com.jporm.types.io;

/**
 *
 * @author ufo
 *
 * @param <T>
 */
@FunctionalInterface
public interface ResultSetRowReader<T> {

    /**
     * @param rs
     *            the {@link ResultEntry} to map (pre-initialized for the
     *            current row)
     * @param rowNum
     *            the number of the current row (starting from 0)
     * @return the result object for the current row
     */
    T readRow(ResultEntry rs, int rowNum);

}
