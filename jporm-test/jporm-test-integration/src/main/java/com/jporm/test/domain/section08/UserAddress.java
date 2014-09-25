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

import com.jporm.annotation.Column;
import com.jporm.annotation.FK;
import com.jporm.annotation.Table;

@Table(tableName="USER_ADDRESS")
public class UserAddress {

	@FK(references=User.class)
	private Long userId;

	@FK
	@Column(name="COUNTRY_ID")
	private UserCountry country;

	public Long getUserId() {
		return userId;
	}
	public void setUserId(final Long userId) {
		this.userId = userId;
	}
	public UserCountry getCountry() {
		return country;
	}
	public void setCountry(final UserCountry country) {
		this.country = country;
	}

}
