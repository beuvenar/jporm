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
package com.jporm.test.domain.section08;

import com.jporm.annotation.FK;
import com.jporm.annotation.Generator;
import com.jporm.annotation.Id;
import com.jporm.annotation.Table;
import com.jporm.annotation.generator.GeneratorType;

/**
 * 
 * @author cinafr
 *
 */
@Table(tableName="USER_JOB")
public class UserJob {

	@Id
	@Generator(generatorType=GeneratorType.AUTOGENERATED_FALLBACK_SEQUENCE, name="SEQ_USER_JOB")
	private Long id;
	@FK(references=CommonUser.class)
	private Long userId;
	private String name;

	public UserJob() {
	}

	public UserJob(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(final Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(final Long userId) {
		this.userId = userId;
	}

}
