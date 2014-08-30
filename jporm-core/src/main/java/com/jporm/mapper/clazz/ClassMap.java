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
package com.jporm.mapper.clazz;

import java.util.List;

import com.jporm.annotation.cache.CacheInfo;
import com.jporm.annotation.table.TableInfo;
import com.jporm.mapper.relation.ClassFKMap;
import com.jporm.mapper.relation.RelationInnerFK;
import com.jporm.mapper.relation.RelationOuterFK;


/**
 * 
 * @author Francesco Cina
 *
 * 22/mag/2011
 */
public interface ClassMap<BEAN>  {

    CacheInfo getCacheInfo();

    TableInfo getTableInfo();

    Class<BEAN> getMappedClass();

    ClassFKMap<BEAN> getFKs();

    <P> ClassFieldImpl<BEAN, P> getClassFieldByJavaName(String javaName);

    String[] getAllColumnJavaNames();

    String[] getAllNotGeneratedColumnJavaNames();

    String[] getAllGeneratedColumnJavaNames();

    String[] getAllGeneratedColumnDBNames();

    String[] getPrimaryKeyColumnJavaNames();

    String[] getNotPrimaryKeyColumnJavaNames();

    String[] getPrimaryKeyAndVersionColumnJavaNames();

    List<RelationOuterFK<BEAN,?, ?>> getOuterRelations();

    List<RelationInnerFK<BEAN, ?>> getInnerRelations();

}
