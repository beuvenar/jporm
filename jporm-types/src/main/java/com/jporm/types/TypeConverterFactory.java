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
package com.jporm.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.types.exception.JpoWrongTypeException;
import com.jporm.types.ext.BooleanToBigDecimalConverter;
import com.jporm.types.ext.ByteToBigDecimalConverter;
import com.jporm.types.ext.CharacterToStringConverter;
import com.jporm.types.ext.DoubleToBigDecimalConverter;
import com.jporm.types.ext.FloatToBigDecimalConverter;
import com.jporm.types.ext.InstantToTimestampConverter;
import com.jporm.types.ext.IntegerToBigDecimalConverter;
import com.jporm.types.ext.LocalDateTimeToSqlTimestampConverter;
import com.jporm.types.ext.LocalDateToSqlTimestampConverter;
import com.jporm.types.ext.LongToBigDecimalConverter;
import com.jporm.types.ext.ShortToBigDecimalConverter;
import com.jporm.types.ext.UtilDateToSqlTimestampConverter;
import com.jporm.types.ext.ZoneDateTimeToSqlTimestampConverter;
import com.jporm.types.jdbc.ArrayJdbcIO;
import com.jporm.types.jdbc.ArrayNullConverter;
import com.jporm.types.jdbc.BigDecimalJdbcIO;
import com.jporm.types.jdbc.BigDecimalNullConverter;
import com.jporm.types.jdbc.BlobJdbcIO;
import com.jporm.types.jdbc.BlobNullConverter;
import com.jporm.types.jdbc.BooleanPrimitiveJdbcIO;
import com.jporm.types.jdbc.BooleanPrimitiveNullConverter;
import com.jporm.types.jdbc.BytePrimitiveJdbcIO;
import com.jporm.types.jdbc.BytePrimitiveNullConverter;
import com.jporm.types.jdbc.BytesJdbcIO;
import com.jporm.types.jdbc.BytesNullConverter;
import com.jporm.types.jdbc.ClobJdbcIO;
import com.jporm.types.jdbc.ClobNullConverter;
import com.jporm.types.jdbc.DateJdbcIO;
import com.jporm.types.jdbc.DateNullConverter;
import com.jporm.types.jdbc.DoublePrimitiveJdbcIO;
import com.jporm.types.jdbc.DoublePrimitiveNullConverter;
import com.jporm.types.jdbc.FloatPrimitiveJdbcIO;
import com.jporm.types.jdbc.FloatPrimitiveNullConverter;
import com.jporm.types.jdbc.InputStreamJdbcIO;
import com.jporm.types.jdbc.InputStreamNullConverter;
import com.jporm.types.jdbc.IntegerPrimitiveJdbcIO;
import com.jporm.types.jdbc.IntegerPrimitiveNullConverter;
import com.jporm.types.jdbc.LongPrimitiveJdbcIO;
import com.jporm.types.jdbc.LongPrimitiveNullConverter;
import com.jporm.types.jdbc.NClobJdbcIO;
import com.jporm.types.jdbc.NClobNullConverter;
import com.jporm.types.jdbc.ObjectJdbcIO;
import com.jporm.types.jdbc.ObjectNullConverter;
import com.jporm.types.jdbc.ReaderJdbcIO;
import com.jporm.types.jdbc.ReaderNullConverter;
import com.jporm.types.jdbc.RefJdbcIO;
import com.jporm.types.jdbc.RefNullConverter;
import com.jporm.types.jdbc.RowIdJdbcIO;
import com.jporm.types.jdbc.RowIdNullConverter;
import com.jporm.types.jdbc.SQLXMLJdbcIO;
import com.jporm.types.jdbc.SQLXMLNullConverter;
import com.jporm.types.jdbc.ShortPrimitiveJdbcIO;
import com.jporm.types.jdbc.ShortPrimitiveNullConverter;
import com.jporm.types.jdbc.StringJdbcIO;
import com.jporm.types.jdbc.StringNullConverter;
import com.jporm.types.jdbc.TimeJdbcIO;
import com.jporm.types.jdbc.TimeNullConverter;
import com.jporm.types.jdbc.TimestampJdbcIO;
import com.jporm.types.jdbc.TimestampNullConverter;
import com.jporm.types.jdbc.URLJdbcIO;
import com.jporm.types.jdbc.URLNullConverter;

/**
 *
 * @author ufo
 *
 */
public class TypeConverterFactory {

	private final Map<Class<?>, JdbcIO<?>> jdbcIOs = new HashMap<Class<?>, JdbcIO<?>>();
	private final Map<Class<?>, TypeConverterBuilder<?,?>> typeConverterBuilders = new HashMap<Class<?>, TypeConverterBuilder<?,?>>();

	public TypeConverterFactory() {
		registerJdbcType();
		registerExtendedType();
	}

	public boolean isConvertedType(final Class<?> clazz) {
		if (typeConverterBuilders.containsKey(clazz)) {
			return true;
		}
		checkAssignableFor(clazz);
		return typeConverterBuilders.containsKey(clazz);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <P, DB> TypeConverterJdbcReady<P,DB> getTypeConverter(final Class<P> clazz) {
		if (isConvertedType(clazz)) {
			TypeConverter<P,DB> typeConverter = (TypeConverter<P, DB>) typeConverterBuilders.get(clazz).build((Class) clazz);
			JdbcIO<DB> jdbcIO = (JdbcIO<DB>) jdbcIOs.get(typeConverter.jdbcType());
			return new TypeConverterJdbcReady<P, DB>(typeConverter, jdbcIO);
		}

		throw new JpoWrongTypeException("Cannot manipulate properties of type [" + clazz + "]. Allowed types [" + Arrays.toString( typeConverterBuilders.keySet().toArray() ) + "]. Use another type or register a custom " + TypeConverter.class.getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private void registerJdbcType() {
		addType(new ArrayJdbcIO(), new ArrayNullConverter());
		addType(new BigDecimalJdbcIO(), new BigDecimalNullConverter());
		addType(new BlobJdbcIO(), new BlobNullConverter());
		addType(new BooleanPrimitiveJdbcIO(), new BooleanPrimitiveNullConverter());
		addType(new BytesJdbcIO(), new BytesNullConverter());
		addType(new BytePrimitiveJdbcIO(), new BytePrimitiveNullConverter());
		addType(new ClobJdbcIO(), new ClobNullConverter());
		addType(new DateJdbcIO(), new DateNullConverter());
		addType(new DoublePrimitiveJdbcIO(), new DoublePrimitiveNullConverter());
		addType(new FloatPrimitiveJdbcIO(), new FloatPrimitiveNullConverter());
		addType(new InputStreamJdbcIO(), new InputStreamNullConverter());
		addType(new IntegerPrimitiveJdbcIO(), new IntegerPrimitiveNullConverter());
		addType(new LongPrimitiveJdbcIO(), new LongPrimitiveNullConverter());
		addType(new NClobJdbcIO(), new NClobNullConverter());
		addType(new ObjectJdbcIO(), new ObjectNullConverter());
		addType(new ReaderJdbcIO(), new ReaderNullConverter());
		addType(new RefJdbcIO(), new RefNullConverter());
		addType(new RowIdJdbcIO(), new RowIdNullConverter());
		addType(new ShortPrimitiveJdbcIO(), new ShortPrimitiveNullConverter());
		addType(new SQLXMLJdbcIO(), new SQLXMLNullConverter());
		addType(new StringJdbcIO(), new StringNullConverter());
		addType(new TimeJdbcIO(), new TimeNullConverter());
		addType(new TimestampJdbcIO(), new TimestampNullConverter());
		addType(new URLJdbcIO(), new URLNullConverter());
	}

	private void registerExtendedType() {
		addTypeConverter(new BooleanToBigDecimalConverter());
		addTypeConverter(new ByteToBigDecimalConverter());
		addTypeConverter(new CharacterToStringConverter());
		addTypeConverter(new DoubleToBigDecimalConverter());
		addTypeConverter(new FloatToBigDecimalConverter());
		addTypeConverter(new InstantToTimestampConverter());
		addTypeConverter(new IntegerToBigDecimalConverter());
		addTypeConverter(new LocalDateTimeToSqlTimestampConverter());
		addTypeConverter(new LocalDateToSqlTimestampConverter());
		addTypeConverter(new LongToBigDecimalConverter());
		addTypeConverter(new ShortToBigDecimalConverter());
		addTypeConverter(new UtilDateToSqlTimestampConverter());
		addTypeConverter(new TypeConverterBuilderEnum() );
		addTypeConverter(new ZoneDateTimeToSqlTimestampConverter());
	}

	/**
	 * This method assures that for every {@link JdbcIO} there is a correspondent {@link TypeConverter} that
	 * convert from and to the same type.
	 * @param jdbcIO
	 * @param typeConverter
	 */
	private <DB> void addType( final JdbcIO<DB> jdbcIO, final TypeConverter<DB, DB> typeConverter) {
		jdbcIOs.put(jdbcIO.getDBClass(), jdbcIO);
		addTypeConverter(typeConverter.propertyType(), new TypeConverterBuilderDefault<DB, DB>(typeConverter) );
	}

	private <TYPE, DB> void addTypeConverter( final Class<TYPE> clazz , final TypeConverterBuilder<TYPE, DB> typeConverterbuilder) {
		if (!jdbcIOs.containsKey(typeConverterbuilder.jdbcType())) {
			throw new JpoWrongTypeException("Cannot register typeConverter " + typeConverterbuilder.getClass() + ". The specified jdbc type " + typeConverterbuilder.jdbcType() + " is not a valid type for the ResultSet and PreparedStatement getters/setters"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		typeConverterBuilders.put(clazz, typeConverterbuilder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <TYPE> void addTypeConverter( final TypeConverter<TYPE, ?> typeConverter) {
		addTypeConverter(typeConverter.propertyType(), new TypeConverterBuilderDefault(typeConverter));
	}

	public <TYPE> void addTypeConverter( final TypeConverterBuilder<TYPE, ?> typeConverter) {
		addTypeConverter(typeConverter.propertyType(), typeConverter);
	}

	synchronized private <TYPE> void checkAssignableFor(final Class<TYPE> versusClass) {
		TypeConverterBuilder<TYPE, ?> candidate = null;
		for (Entry<Class<?>, TypeConverterBuilder<?, ?>> twEntry : typeConverterBuilders.entrySet()) {
			if (twEntry.getKey().isAssignableFrom(versusClass) && !twEntry.getKey().equals(Object.class) ) {
				candidate = (TypeConverterBuilder<TYPE, ?>) twEntry.getValue();
				break;
			}
		}
		if (candidate != null) {
			addTypeConverter(versusClass, candidate);
		}
	}
}
