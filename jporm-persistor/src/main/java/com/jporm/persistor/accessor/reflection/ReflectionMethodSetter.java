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
package com.jporm.persistor.accessor.reflection;

import java.lang.reflect.Method;

import com.jporm.exception.OrmException;
import com.jporm.persistor.accessor.Setter;


/**
 * 
 * Get the value of a field using the related getter method
 * 
 * @author Francesco Cina'
 *
 * Mar 31, 2012
 */
public class ReflectionMethodSetter<BEAN, P> extends Setter<BEAN, P> {

    private final Method setterMethod;

    public ReflectionMethodSetter(final Method setterMethod) {
        this.setterMethod = setterMethod;
    }

    @Override
    public void setValue(final BEAN bean, final P value) {
        try {
            this.setterMethod.invoke(bean, value);
        } catch (Exception e) {
            throw new OrmException(e);
        }
    }

}