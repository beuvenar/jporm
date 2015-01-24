/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.core.session.datasource;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.core.BaseTestApi;
import com.jporm.core.domain.People;
import com.jporm.session.Session;

public class DataSourceSessionProviderTest extends BaseTestApi {

	private final Logger logger = LoggerFactory.getLogger(getClass());


	@Test
	public <T> void testJPOWithLambdaSession() throws InterruptedException {
		com.jporm.JPO jpo = getJPO();

		final Long id = jpo.session().txNow(session -> {
			long _id = create(session, "");
			logger.info("Saved people with id [{}] and name [{}]", _id, "");
			People people = session.find(People.class, _id).getOptional().get();
			assertNotNull(people);
			return _id;
		});

		jpo.session().txVoidNow(session -> {
			People found = session.find(People.class, id).getOptional().get();
			logger.info("Found: " + found);
			assertNotNull( found );
		});

	}


	long create(final Session conn, final String firstName) {

		//		final long id = new Date().getTime();
		People people = new People();
		//		people.setId( id );
		people.setFirstname( firstName );
		people.setLastname("Wizard"); //$NON-NLS-1$

		// CREATE
		people = conn.saveQuery(people).now();

		logger.info("People [" + firstName + "] saved with id: " + people.getId()); //$NON-NLS-1$ //$NON-NLS-2$
		//		assertFalse( id == people.getId() );
		return people.getId();

	}

}
