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
package com.jporm.persistor.type.ext;

import java.sql.Timestamp;

import org.joda.time.DateMidnight;

import com.jporm.persistor.type.TypeWrapper;

/**
 * 
 * @author Francesco Cina'
 *
 * Mar 27, 2012
 */
public class JodaDateMidnightToTimestampWrapper implements TypeWrapper<DateMidnight, Timestamp> {

    @Override
    public Class<Timestamp> jdbcType() {
        return Timestamp.class;
    }

    @Override
    public Class<DateMidnight> propertyType() {
        return DateMidnight.class;
    }

    @Override
    public DateMidnight wrap(final Timestamp value) {
        if (value==null) {
            return null;
        }
        return new DateMidnight(value.getTime());
    }

    @Override
    public Timestamp unWrap(final DateMidnight value) {
        if (value==null) {
            return null;
        }
        return new Timestamp(value.getMillis());
    }

    @Override
    public DateMidnight clone(final DateMidnight source) {
        return source;
    }

}
