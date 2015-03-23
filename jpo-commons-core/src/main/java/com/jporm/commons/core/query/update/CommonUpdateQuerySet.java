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
package com.jporm.commons.core.query.update;

import com.jporm.commons.core.query.QueryRoot;
import com.jporm.commons.core.query.clause.Set;

/**
 *
 * @author ufo
 *
 */
public interface CommonUpdateQuerySet<UPDATE extends CommonUpdateQuery<UPDATE, WHERE, SET>,
										WHERE extends CommonUpdateQueryWhere<UPDATE, WHERE, SET>,
										SET extends CommonUpdateQuerySet<UPDATE, WHERE, SET>>
								extends Set<SET>, QueryRoot {

	/**
	 * Create or modify the "WHERE" clause of the statement.
	 * @return
	 */
	WHERE where();

	/**
	 * Return the root query object
	 * @return
	 */
	UPDATE query();
}