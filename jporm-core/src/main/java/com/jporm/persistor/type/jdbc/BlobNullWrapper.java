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
package com.jporm.persistor.type.jdbc;

import java.sql.Blob;

import com.jporm.persistor.type.TypeWrapper;


public class BlobNullWrapper implements TypeWrapper<Blob, Blob> {

	@Override
	public Class<Blob> jdbcType() {
		return Blob.class;
	}

	@Override
	public Class<Blob> propertyType() {
		return Blob.class;
	}

	@Override
	public Blob wrap(final Blob value) {
		return value;
	}

	@Override
	public Blob unWrap(final Blob value) {
		return value;
	}

	@Override
	public Blob clone(final Blob source) {
		return source;
	}

}
