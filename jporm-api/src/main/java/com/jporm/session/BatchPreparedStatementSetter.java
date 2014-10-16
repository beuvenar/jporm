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

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * @author Francesco Cina
 *
 * 02/lug/2011
 * 
 * Set the values of a PreparedStatement to be executed in a batch update.
 */
public interface BatchPreparedStatementSetter  {

	/**
	 * 
	 * @param ps the prepared statement
	 * @param i the index of the batch (from 0 to batchSize-1)
	 * @throws SQLException
	 */
	void set(PreparedStatement ps, int i) throws SQLException;

	int getBatchSize();
}