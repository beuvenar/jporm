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
package com.jporm.test.domain.section06;

import com.jporm.annotation.Generator;
import com.jporm.annotation.GeneratorType;
import com.jporm.annotation.Id;
import com.jporm.annotation.Table;
import com.jporm.annotation.Version;

/**
 * 
 * @author Francesco Cina'
 *
 *         26/ago/2011
 */
@Table(tableName = "DATA_VERSION_INT")
public class DataVersionInteger {

    @Id
    @Generator(generatorType = GeneratorType.AUTOGENERATED_FALLBACK_SEQUENCE, name = "seq_data_version_int")
    private int id = -1;
    private String data;

    @Version
    private Integer version;

    public String getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

}
