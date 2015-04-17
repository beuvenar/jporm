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
package com.jporm.test.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.Random;

import org.junit.Test;

import com.jporm.core.session.Session;
import com.jporm.core.transaction.TransactionCallback;
import com.jporm.sql.query.clause.impl.where.Exp;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section05.AutoId;
import com.jporm.test.domain.section08.CommonUser;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class QueryExcludeFieldTest extends BaseTestAllDB {

	public QueryExcludeFieldTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testExcludeOnFind() {
		getJPO().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				AutoId autoId = new AutoId();
				final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
				autoId.setValue(value);
				autoId = session.saveOrUpdate(autoId);

				AutoId autoIdWithoutValue = session.findQuery(AutoId.class).ignore("value").where(Exp.eq("id", autoId.getId())).getUnique(); //$NON-NLS-1$
				AutoId autoIdWithValue = session.findQuery(AutoId.class).ignore(false, "value").where(Exp.eq("id", autoId.getId())).getUnique(); //$NON-NLS-1$

				assertEquals( autoId.getId(), autoIdWithValue.getId() );
				assertNull( autoIdWithoutValue.getValue() );
				assertEquals( autoId.getId(), autoIdWithValue.getId() );
				assertEquals( value, autoIdWithValue.getValue() );

				return null;
			}
		});

	}

	@Test
	public void testGetShouldReturnFirstResultSetEntry() {
		getJPO().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				long suffix = new Random().nextLong();

				session.deleteQuery(CommonUser.class).now();

				CommonUser user = new CommonUser();
				user.setUserAge(0l);
				user.setFirstname("aaa" + suffix);
				user.setLastname("aaa" + suffix);
				session.save(user);

				user.setFirstname("bbb" + suffix);
				session.save(user);

				user.setFirstname("ccc" + suffix);

				assertEquals(  session.findQuery(CommonUser.class).orderBy().desc("firstname").getList().get(0).getFirstname() ,
						session.findQuery(CommonUser.class).orderBy().desc("firstname").getOptional().get().getFirstname() );

				assertEquals(  session.findQuery(CommonUser.class).orderBy().asc("firstname").getList().get(0).getFirstname() ,
						session.findQuery(CommonUser.class).orderBy().asc("firstname").getOptional().get().getFirstname() );

				return null;
			}
		});

	}
}
