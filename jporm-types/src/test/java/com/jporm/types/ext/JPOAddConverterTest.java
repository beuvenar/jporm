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
package com.jporm.types.ext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.sql.SQLXML;
import java.util.Date;

import org.junit.Test;

import com.jporm.types.BaseTestApi;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.TypeConverter;
import com.jporm.types.exception.JpoWrongTypeException;
import com.jporm.types.ext.UtilDateToSqlTimestampConverter;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class JPOAddConverterTest extends BaseTestApi {

	@Test
	public void tesRegisterTypeConverter() {
		TypeConverterFactory typeFactory = new TypeConverterFactory();
		assertNotNull(typeFactory);

		try {
			typeFactory.getTypeConverter(Mock.class);
			fail("An OrmException should be thrown"); //$NON-NLS-1$
		} catch (JpoWrongTypeException e) {
			// do nothing
		}

		typeFactory.addTypeConverter(new MockTypeConverter());

		assertEquals(MockTypeConverter.class, typeFactory.getTypeConverter(Mock.class).getTypeConverter().getClass());
		assertEquals(new MockTypeConverter().propertyType(), typeFactory.getTypeConverter(Mock.class).propertyType());
	}

	@Test
	public void testOverrideTypeConverter() {
		TypeConverterFactory typeFactory = new TypeConverterFactory();
		assertNotNull(typeFactory);

		assertEquals(UtilDateToSqlTimestampConverter.class, typeFactory.getTypeConverter(java.util.Date.class).getTypeConverter().getClass());

		typeFactory.addTypeConverter(new DateTypeConverter());

		assertEquals(DateTypeConverter.class, typeFactory.getTypeConverter(java.util.Date.class).getTypeConverter().getClass());
		assertEquals(new DateTypeConverter().jdbcType(), typeFactory.getTypeConverter(java.util.Date.class).getJdbcIO().getDBClass());

	}

	class Mock {// do nothing

	}

	class MockTypeConverter implements TypeConverter<Mock, InputStream> {
		@Override
		public Class<InputStream> jdbcType() {
			return InputStream.class;
		}
		@Override
		public Class<Mock> propertyType() {
			return Mock.class;
		}
		@Override
		public Mock fromJdbcType(final InputStream value) {
			return null;
		}
		@Override
		public InputStream toJdbcType(final Mock value) {
			return null;
		}
		@Override
		public Mock clone(final Mock source) {
			return source;
		}
	}

	class DateTypeConverter implements TypeConverter<Date, SQLXML> {
		@Override
		public Class<SQLXML> jdbcType() {
			return SQLXML.class;
		}
		@Override
		public Class<Date> propertyType() {
			return Date.class;
		}
		@Override
		public Date fromJdbcType(final SQLXML value) {
			return null;
		}
		@Override
		public SQLXML toJdbcType(final Date value) {
			return null;
		}
		@Override
		public Date clone(final Date source) {
			return source;
		}
	}
}