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
package com.jporm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jporm.session.Session;

/**
 * 
 * The {@link Cache} annotation identifies a bean that can be automatically cached by JPO
 * when a {@link Session#find(Class, Object)} method is called to get a Bean of this type.
 * If Bean has relations, the relations will be cached as well.
 * 
 * @author Francesco Cina
 *
 * 08/giu/2011
 */

@Target(value=ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
@Inherited
public @interface Cache {

    /**
     * The name of the cache to use.
     * @return
     */
    String cacheName() ;

}
