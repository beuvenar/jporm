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
package com.jporm.test.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.Random;

import org.junit.Test;

import com.jporm.JPO;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section02.People;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class PeopleTest extends BaseTestAllDB {

	public PeopleTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testCrudPeople() {
		final JPO jpOrm = getJPOrm();

		jpOrm.register(People.class);

		final long id = new Random().nextInt(Integer.MAX_VALUE);

		assertFalse( jpOrm.session().find(People.class, id).exist() );


		final Session conn = jpOrm.session();

		People people = conn.txNow((_session) -> {
			// CREATE
			People people_ = new People();
			people_.setId( id );
			people_.setFirstname( "people" ); //$NON-NLS-1$
			people_.setLastname("Wizard"); //$NON-NLS-1$
			return conn.save(people_).now();
		});

		System.out.println("People saved with id: " + people.getId()); //$NON-NLS-1$
		assertTrue( id == people.getId() );

		assertTrue( jpOrm.session().find(people).exist() );


		People peopleLoad1 = conn.txNow((_session) -> {
			// LOAD
			People peopleLoad1_ = conn.find(People.class, id).getOptional().get();
			assertNotNull(peopleLoad1_);
			assertEquals( people.getId(), peopleLoad1_.getId() );
			assertEquals( people.getFirstname(), peopleLoad1_.getFirstname() );
			assertEquals( people.getLastname(), peopleLoad1_.getLastname() );

			//UPDATE
			peopleLoad1_.setFirstname("Wizard name"); //$NON-NLS-1$
			return conn.update(peopleLoad1_).now();
		});

		conn.txVoidNow((_session) -> {
			// LOAD
			final People peopleLoad2 = conn.find(People.class, new Object[]{id}).getUnique();
			assertNotNull(peopleLoad2);
			assertEquals( peopleLoad1.getId(), peopleLoad2.getId() );
			assertEquals( peopleLoad1.getFirstname(), peopleLoad2.getFirstname() );
			assertEquals( peopleLoad1.getLastname(), peopleLoad2.getLastname() );

			//DELETE
			conn.delete(peopleLoad2).now();

			final Optional<People> peopleLoad3 = conn.find(People.class, new Object[]{id}).getOptional();
			assertFalse(peopleLoad3.isPresent());
		});

	}

}
