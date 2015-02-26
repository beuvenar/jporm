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
package com.jporm.types.jdbc;

import java.sql.RowId;

import com.jporm.types.TypeConverter;


public class RowIdNullConverter implements TypeConverter<RowId, RowId> {

	@Override
	public Class<RowId> jdbcType() {
		return RowId.class;
	}

	@Override
	public Class<RowId> propertyType() {
		return RowId.class;
	}

	@Override
	public RowId fromJdbcType(final RowId value) {
		return value;
	}

	@Override
	public RowId toJdbcType(final RowId value) {
		return value;
	}

	@Override
	public RowId clone(final RowId source) {
		return source;
	}

}