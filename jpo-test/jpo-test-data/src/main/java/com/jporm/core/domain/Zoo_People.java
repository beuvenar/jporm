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
package com.jporm.core.domain;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Date;

import com.jporm.annotation.Generator;
import com.jporm.annotation.GeneratorType;
import com.jporm.annotation.Table;

/**
 *
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
@Table(tableName = "PEOPLE", schemaName="ZOO")
public class Zoo_People {

	@Generator(generatorType = GeneratorType.SEQUENCE, name = "ZOO_SEQ_PEOPLE")
	private long id;
	private String firstname;
	private String lastname;
	private Date birthdate;
	private Date deathdate;
	private InputStream firstblob;
	private InputStream secondblob;
	private Reader firstclob;

	public long getId() {
		return id;
	}
	public void setId(final long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(final String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(final String lastname) {
		this.lastname = lastname;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(final Date birthdate) {
		this.birthdate = birthdate;
	}
	public Date getDeathdate() {
		return deathdate;
	}
	public void setDeathdate(final Date deathdate) {
		this.deathdate = deathdate;
	}
	public InputStream getFirstblob() {
		return firstblob;
	}
	public void setFirstblob(final InputStream firstblob) {
		this.firstblob = firstblob;
	}
	public InputStream getSecondblob() {
		return secondblob;
	}
	public void setSecondblob(final InputStream secondblob) {
		this.secondblob = secondblob;
	}
	public void setFirstclob(final Reader firstclob) {
		this.firstclob = firstclob;
	}
	public Reader getFirstclob() {
		return firstclob;
	}

}
