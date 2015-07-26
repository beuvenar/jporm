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
package com.jporm.rx.sync.query.find;

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.commons.core.query.find.CommonFindQueryGroupBy;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
@Suspendable
public interface CustomFindQueryGroupBy extends CommonFindQueryGroupBy<CustomFindQuery, CustomFindQueryWhere, CustomFindQueryOrderBy, CustomFindQueryGroupBy>, CustomFindQueryCommon {

}
