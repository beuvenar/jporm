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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jporm.session.ResultSetReader;
import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.AggregatedUserManyJob;
import com.jporm.test.domain.section08.User;

/**
 *
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
public class QueryGroupByHavingTest extends BaseTestAllDB {

	public QueryGroupByHavingTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private final int firstnameOneQuantity = 50;
	private final String firstnameOne = UUID.randomUUID().toString();

	private final int firstnameTwoQuantity = 60;
	private final String firstnameTwo = UUID.randomUUID().toString();

	private final int firstnameThreeQuantity = 70;
	private final String firstnameThree = UUID.randomUUID().toString();

	@Before
	public void setUp() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				session.deleteQuery(AggregatedUserManyJob.class).now();

				for (int i=0; i<firstnameOneQuantity; i++) {
					User user = new User();
					user.setUserAge(Long.valueOf(i));
					user.setFirstname(firstnameOne);
					user.setLastname("surname");
					user = session.save(user).now();
				}

				for (int i=0; i<firstnameTwoQuantity; i++) {
					User user = new User();
					user.setUserAge(Long.valueOf(i));
					user.setFirstname(firstnameTwo);
					user.setLastname("surname");
					user = session.save(user).now();
				}

				for (int i=0; i<firstnameThreeQuantity; i++) {
					User user = new User();
					user.setUserAge(Long.valueOf(i));
					user.setFirstname(firstnameThree);
					user.setLastname("surname");
					user = session.save(user).now();
				}

				return null;
			}
		});
	}

	@Test
	public void testGroupBy() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				final Map<String, Integer> firstnameCount = new HashMap<String, Integer>();

				session.findQuery(new String[]{"u.firstname", "count(*) as countName"}, User.class, "u").groupBy("u.firstname").get(new ResultSetReader<Void>() {
					@Override
					public Void read(final ResultSet resultSet) throws SQLException {
						while (resultSet.next()) {
							String rsFirstname = resultSet.getString("u.firstname");
							Integer rsCount = resultSet.getInt("countName");
							getLogger().debug("Found firstname [{}] count [{}]", rsFirstname, rsCount);
							firstnameCount.put(rsFirstname, rsCount);
						}
						return null;
					}
				});

				assertFalse(firstnameCount.isEmpty());
				assertEquals( 3, firstnameCount.size());
				assertTrue( firstnameCount.containsKey(firstnameOne) );
				assertTrue( firstnameCount.containsKey(firstnameTwo) );
				assertTrue( firstnameCount.containsKey(firstnameThree) );
				assertEquals( Integer.valueOf(firstnameOneQuantity), firstnameCount.get(firstnameOne));
				assertEquals( Integer.valueOf(firstnameTwoQuantity), firstnameCount.get(firstnameTwo));
				assertEquals( Integer.valueOf(firstnameThreeQuantity), firstnameCount.get(firstnameThree));

				return null;
			}
		});
	}

	@Test
	public void testGroupByWithOrderBy() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				final Map<String, Integer> firstnameCount = new HashMap<String, Integer>();

				session.findQuery(new String[]{"u.firstname", "count(*) as countName"}, User.class, "u").groupBy("u.firstname").orderBy().asc("u.firstname").get(new ResultSetReader<Void>() {
					@Override
					public Void read(final ResultSet resultSet) throws SQLException {
						while (resultSet.next()) {
							String rsFirstname = resultSet.getString("u.firstname");
							Integer rsCount = resultSet.getInt("countName");
							getLogger().debug("Found firstname [{}] count [{}]", rsFirstname, rsCount);
							firstnameCount.put(rsFirstname, rsCount);
						}
						return null;
					}
				});

				assertFalse(firstnameCount.isEmpty());
				assertEquals( 3, firstnameCount.size());
				assertTrue( firstnameCount.containsKey(firstnameOne) );
				assertTrue( firstnameCount.containsKey(firstnameTwo) );
				assertTrue( firstnameCount.containsKey(firstnameThree) );
				assertEquals( Integer.valueOf(firstnameOneQuantity), firstnameCount.get(firstnameOne));
				assertEquals( Integer.valueOf(firstnameTwoQuantity), firstnameCount.get(firstnameTwo));
				assertEquals( Integer.valueOf(firstnameThreeQuantity), firstnameCount.get(firstnameThree));

				return null;
			}
		});
	}

	@Test
	public void testGroupByHaving() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				final Map<String, Integer> firstnameCount = new HashMap<String, Integer>();

				session.findQuery(new String[]{"u.firstname", "count(*) as countName"}, User.class, "u").groupBy("u.firstname").having("count(*) > ?", firstnameOneQuantity).get(new ResultSetReader<Void>() {
					@Override
					public Void read(final ResultSet resultSet) throws SQLException {
						while (resultSet.next()) {
							String rsFirstname = resultSet.getString("u.firstname");
							Integer rsCount = resultSet.getInt("countName");
							getLogger().debug("Found firstname [{}] count [{}]", rsFirstname, rsCount);
							firstnameCount.put(rsFirstname, rsCount);
						}
						return null;
					}
				});

				assertFalse(firstnameCount.isEmpty());
				assertEquals( 2, firstnameCount.size());
				assertFalse( firstnameCount.containsKey(firstnameOne) );
				assertTrue( firstnameCount.containsKey(firstnameTwo) );
				assertTrue( firstnameCount.containsKey(firstnameThree) );
				assertEquals( Integer.valueOf(firstnameTwoQuantity), firstnameCount.get(firstnameTwo));
				assertEquals( Integer.valueOf(firstnameThreeQuantity), firstnameCount.get(firstnameThree));

				return null;
			}
		});
	}

	@Test
	@Ignore
	public void testGroupByHavingWithAlias() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				final Map<String, Integer> firstnameAge = new HashMap<String, Integer>();

				session.findQuery(new String[]{"u.firstname", "sum(userAge) as sumAge"}, User.class, "u").groupBy("u.firstname").having("sum(userAge) > ?", 100).get(new ResultSetReader<Void>() {
					@Override
					public Void read(final ResultSet resultSet) throws SQLException {
						while (resultSet.next()) {
							String rsFirstname = resultSet.getString("u.firstname");
							Integer rsCount = resultSet.getInt("sumAge");
							getLogger().info("Found firstname [{}] sumAge [{}]", rsFirstname, rsCount);
							firstnameAge.put(rsFirstname, rsCount);
						}
						return null;
					}
				});

				assertFalse(firstnameAge.isEmpty());
				assertEquals( 3, firstnameAge.size());
				assertTrue( firstnameAge.containsKey(firstnameOne) );
				assertTrue( firstnameAge.containsKey(firstnameTwo) );
				assertTrue( firstnameAge.containsKey(firstnameThree) );
				assertTrue( firstnameAge.get(firstnameOne) > 100 );
				assertTrue( firstnameAge.get(firstnameTwo) > 100 );
				assertTrue( firstnameAge.get(firstnameThree) > 100 );

				return null;
			}
		});
	}

}
