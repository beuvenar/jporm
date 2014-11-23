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
package com.jporm.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple cache based on {@link ConcurrentHashMap}.
 * @author Francesco Cina
 *
 * 23 Sep 2011
 */
public class SimpleCache extends ACache {

    private final Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();

    @Override
    public Object getValue(final Object key) {
        return map.get(key);
    }

    @Override
    public void put(final Object key, final Object value) {
        map.put(key, value);
    }

    @Override
    public void remove(final Object key) {
        map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(final Object key) {
        return map.containsKey(key);
    }

}