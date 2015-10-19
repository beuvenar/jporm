/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.test.session;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.UserWithTwoIDs;
import com.jporm.test.domain.section08.UserWithTwoIDsAndGenerator;

public class MultipleIDsCRUDTest extends BaseTestAllDB {

	public MultipleIDsCRUDTest(String testName, TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testCRUDsWithMultipleIDsAndGenerator() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(Session session) {

				UserWithTwoIDsAndGenerator user = new UserWithTwoIDsAndGenerator();
				user.setFirstname("firstname");
				user.setLastname("lastname");

				user = session.saveOrUpdate(user).now();
				assertNotNull(user);
				assertNotNull(user.getId());

				user = session.saveOrUpdate(user).now();
				assertNotNull(user);
				assertNotNull(user.getId());

				user = session.find(user).get();
				assertNotNull(user);
				assertNotNull(user.getId());

				assertEquals(1, session.delete(user).now());
				assertNull(session.find(user).get());

				return null;
			}
		});

	}

	@Test
	public void testCRUDsWithMultipleIDsWithoutGenerator() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(Session session) {

				UserWithTwoIDs user = new UserWithTwoIDs();
				user.setId(Long.valueOf(new Random().nextInt(Integer.MAX_VALUE)));
				user.setFirstname("firstname");
				user.setLastname("lastname");

				user = session.saveOrUpdate(user).now();
				assertNotNull(user);
				assertNotNull(user.getId());

				user = session.saveOrUpdate(user).now();
				assertNotNull(user);
				assertNotNull(user.getId());

				user = session.find(user).get();
				assertNotNull(user);
				assertNotNull(user.getId());

				assertEquals(1, session.delete(user).now());
				assertNull(session.find(user).get());

				return null;
			}
		});

	}

}
