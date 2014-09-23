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
package com.jporm.test.domain.section07;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.jporm.annotation.Generator;
import com.jporm.annotation.Id;
import com.jporm.annotation.generator.GeneratorType;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class WrapperTypeTable {

	@Id
	@Generator(generatorType = GeneratorType.AUTOGENERATED_FALLBACK_SEQUENCE, name="seq_Wrapper_Type_Table")
	private Long id = Long.valueOf(-1);
	private Date now;
	private LocalDateTime startDate;
	private LocalDate endDate;
	private Boolean valid;

	public Long getId() {
		return id;
	}

	/**
	 * @return the now
	 */
	public Date getNow() {
		return now;
	}

	/**
	 * @param now the now to set
	 */
	public void setNow(final Date now) {
		this.now = now;
	}

	/**
	 * @return the startDate
	 */
	public LocalDateTime getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(final LocalDateTime startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(final LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the valid
	 */
	public Boolean getValid() {
		return valid;
	}

	/**
	 * @param valid the valid to set
	 */
	public void setValid(final Boolean valid) {
		this.valid = valid;
	}

}
