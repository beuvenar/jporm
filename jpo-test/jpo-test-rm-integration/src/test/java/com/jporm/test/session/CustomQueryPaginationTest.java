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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.rm.session.Session;
import com.jporm.rm.transaction.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 *
 * @author Francesco Cina
 *
 *         05/giu/2011
 */
public class CustomQueryPaginationTest extends BaseTestAllDB {

    private final int userQuantity = 100;

    private Long firstId;

    public CustomQueryPaginationTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Before
    public void setUp() {
        getJPO().transaction().execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(final Session session) {
                for (int i = 0; i < userQuantity; i++) {
                    CommonUser user = new CommonUser();
                    user.setUserAge(Long.valueOf(i));
                    user.setFirstname("name");
                    user.setLastname("surname");
                    user = session.save(user);

                    if (i == 0) {
                        firstId = user.getId();
                    }

                }

                return null;
            }
        });
        assertNotNull(firstId);
    }

    @Test
    public void testFirstRowPaginationWithOrderAsc() {
        getJPO().transaction().execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(final Session session) {

                int firstRow = new Random().nextInt(userQuantity);

                ResultSetRowReader<Integer> rsrr = new ResultSetRowReader<Integer>() {
                    @Override
                    public Integer readRow(final ResultEntry rs, final int rowNum) {
                        return rs.getInt("userAge");
                    }
                };
                List<Integer> results = session.find("userAge").from(CommonUser.class, "user").offset(firstRow).where().ge("id", firstId).orderBy().asc("id")
                        .fetch(rsrr);

                assertEquals(userQuantity - firstRow, results.size());

                for (Integer age : results) {
                    assertTrue(age >= firstRow);
                }

                return null;
            }
        });
    }

    @Test
    public void testFirstRowPaginationWithOrderDesc() {
        getJPO().transaction().execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(final Session session) {

                int firstRow = new Random().nextInt(userQuantity);

                ResultSetRowReader<Integer> rsrr = new ResultSetRowReader<Integer>() {
                    @Override
                    public Integer readRow(final ResultEntry rs, final int rowNum) {
                        return rs.getInt("userAge");
                    }
                };
                List<Integer> results = session.find("userAge").from(CommonUser.class, "user").offset(firstRow).where().ge("id", firstId).orderBy().desc("id")
                        .fetch(rsrr);

                assertEquals(userQuantity - firstRow, results.size());

                for (Integer age : results) {
                    assertTrue(age < (userQuantity - firstRow));

                }

                return null;
            }
        });
    }

    @Test
    public void testMaxRowsPaginationWithOrderAsc() {
        getJPO().transaction().execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(final Session session) {

                int maxRows = new Random().nextInt(userQuantity) + 1;

                ResultSetRowReader<Integer> rsrr = new ResultSetRowReader<Integer>() {
                    @Override
                    public Integer readRow(final ResultEntry rs, final int rowNum) {
                        return rs.getInt("userAge");
                    }
                };
                List<Integer> results = session.find("userAge").from(CommonUser.class, "user").limit(maxRows).where().ge("id", firstId).orderBy().asc("id")
                        .fetch(rsrr);

                assertEquals(maxRows, results.size());

                for (Integer age : results) {
                    assertTrue(age < maxRows);
                }

                return null;
            }
        });
    }

    @Test
    public void testMaxRowsPaginationWithOrderDesc() {
        getJPO().transaction().execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(final Session session) {

                int maxRows = new Random().nextInt(userQuantity) + 1;

                ResultSetRowReader<Integer> rsrr = new ResultSetRowReader<Integer>() {
                    @Override
                    public Integer readRow(final ResultEntry rs, final int rowNum) {
                        return rs.getInt("userAge");
                    }
                };
                List<Integer> results = session.find("userAge").from(CommonUser.class, "user").limit(maxRows).where().ge("id", firstId).orderBy().desc("id")
                        .fetch(rsrr);

                assertEquals(maxRows, results.size());

                for (Integer age : results) {
                    assertTrue(age >= (userQuantity - maxRows));
                }

                return null;
            }
        });
    }

    @Test
    public void testPaginationWithOrderAsc() {
        getJPO().transaction().execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(final Session session) {

                int firstRow = new Random().nextInt(userQuantity);
                int maxRows = new Random().nextInt(userQuantity - firstRow) + 1;

                ResultSetRowReader<Integer> rsrr = new ResultSetRowReader<Integer>() {
                    @Override
                    public Integer readRow(final ResultEntry rs, final int rowNum) {
                        return rs.getInt("userAge");
                    }
                };
                List<Integer> results = session.find("userAge").from(CommonUser.class, "user").limit(maxRows).offset(firstRow).where().ge("id", firstId)
                        .orderBy().asc("id").fetch(rsrr);

                assertEquals(maxRows, results.size());

                for (Integer age : results) {
                    assertTrue(age >= firstRow);
                    assertTrue(age < (firstRow + maxRows));
                }

                return null;
            }
        });
    }

    @Test
    public void testPaginationWithOrderDesc() {
        getJPO().transaction().execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(final Session session) {

                int firstRow = new Random().nextInt(userQuantity);
                int maxRows = new Random().nextInt(userQuantity - firstRow) + 1;

                ResultSetReader<List<Integer>> rsrr = new ResultSetReader<List<Integer>>() {
                    @Override
                    public List<Integer> read(final ResultSet resultSet) {
                        final List<Integer> results = new ArrayList<Integer>();
                        while (resultSet.next()) {
                            results.add(resultSet.getInt("userAge"));
                        }
                        return results;
                    }
                };
                final List<Integer> results = session.find("userAge").from(CommonUser.class, "user").limit(maxRows).offset(firstRow).where().ge("id", firstId)
                        .orderBy().desc("id").fetch(rsrr);

                assertEquals(maxRows, results.size());

                for (Integer age : results) {
                    assertTrue(age < (userQuantity - firstRow));
                    assertTrue(age >= ((userQuantity - firstRow) - maxRows));

                }

                return null;
            }
        });
    }

}
