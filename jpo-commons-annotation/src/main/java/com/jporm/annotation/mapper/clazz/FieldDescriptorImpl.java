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
package com.jporm.annotation.mapper.clazz;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.jporm.annotation.introspector.column.ColumnInfo;
import com.jporm.annotation.introspector.generator.GeneratorInfo;
import com.jporm.annotation.introspector.version.VersionInfo;

/**
 *
 * @author cinafr
 *
 * @param
 *            <P>
 */
public class FieldDescriptorImpl<BEAN, P> implements FieldDescriptor<BEAN, P> {

    private VersionInfo versionInfo;
    private GeneratorInfo generatorInfo;
    private ColumnInfo columnInfo;
    private final String fieldName;
    private final Class<P> type;
    private boolean identifier = false;
    private Method getter;
    private Method setter;
    private final Field field;

    public FieldDescriptorImpl(final Field field, final Class<P> type) {
        this.field = field;
        this.type = type;
        this.fieldName = field.getName();
    }

    @Override
    public final ColumnInfo getColumnInfo() {
        return this.columnInfo;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public final String getFieldName() {
        return this.fieldName;
    }

    @Override
    public GeneratorInfo getGeneratorInfo() {
        return this.generatorInfo;
    }

    @Override
    public Method getGetter() {
        return getter;
    }

    @Override
    public Method getSetter() {
        return setter;
    }

    @Override
    public final Class<P> getType() {
        return this.type;
    }

    @Override
    public VersionInfo getVersionInfo() {
        return this.versionInfo;
    }

    @Override
    public final boolean isIdentifier() {
        return this.identifier;
    }

    public final void setColumnInfo(final ColumnInfo columnInfo) {
        this.columnInfo = columnInfo;
    }

    public void setGeneratorInfo(final GeneratorInfo generatorInfo) {
        this.generatorInfo = generatorInfo;
    }

    public void setGetter(final Method getter) {
        this.getter = getter;
    }

    public final void setIdentifier(final boolean identifier) {
        this.identifier = identifier;
    }

    public void setSetter(final Method setter) {
        this.setter = setter;
    }

    public void setVersionInfo(final VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

}
