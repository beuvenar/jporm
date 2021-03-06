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

import java.util.List;

import org.junit.Test;

import com.jporm.sql.query.clause.impl.where.Exp;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;

/**
 *
 * @author Francesco Cina
 *
 *         05/giu/2011
 */
public class QueryUnionInterceptExceptTest extends BaseTestAllDB {

    public QueryUnionInterceptExceptTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    private CommonUser createUser(String firstName) {
        CommonUser commonUser = new CommonUser();
        commonUser.setUserAge(10l);
        commonUser.setFirstname(firstName);
        commonUser.setLastname("surname");
        return commonUser;
    }

    private boolean contains(String firstname, List<CommonUser> users) {
        for (CommonUser user : users) {
            if (user.getFirstname().equals(firstname)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testUnion() {
        getJPO().transaction().executeVoid(session -> {

            session.delete(CommonUser.class).execute();

            session.save(createUser("one"));
            session.save(createUser("two"));
            session.save(createUser("three"));

            List<CommonUser> users = session.find(CommonUser.class).where().eq("firstname", "one")
            .union(session.find(CommonUser.class).where().eq("firstname", "two"))
            .fetchList();

            assertEquals(2, users.size());
            assertTrue(contains("one", users));
            assertTrue(contains("two", users));
            assertFalse(contains("three", users));
        });
    }

    @Test
    public void testUnionWithDuplicates() {
        getJPO().transaction().executeVoid(session -> {

            session.delete(CommonUser.class).execute();

            session.save(createUser("one"));
            session.save(createUser("two"));
            session.save(createUser("three"));

            List<CommonUser> users = session.find(CommonUser.class).where().eq("firstname", "one")
            .union(session.find(CommonUser.class).where().or(Exp.eq("firstname", "one"), Exp.eq("firstname", "two")))
            .fetchList();

            assertEquals(2, users.size());
            assertTrue(contains("one", users));
            assertTrue(contains("two", users));
            assertFalse(contains("three", users));
        });
    }

    @Test
    public void testUnionAll() {
        getJPO().transaction().executeVoid(session -> {

            session.delete(CommonUser.class).execute();

            session.save(createUser("one"));
            session.save(createUser("two"));
            session.save(createUser("three"));

            List<CommonUser> users = session.find(CommonUser.class).where().eq("firstname", "one")
            .unionAll(session.find(CommonUser.class).where().or(Exp.eq("firstname", "one"), Exp.eq("firstname", "two")))
            .fetchList();

            assertEquals(3, users.size());
            assertTrue(contains("one", users));
            assertTrue(contains("two", users));
            assertFalse(contains("three", users));
        });
    }

//    @Test
//    public void testIntersect() {
//        getJPO().transaction().executeVoid(session -> {
//
//            session.delete(CommonUser.class).execute();
//
//            session.save(createUser("one"));
//            session.save(createUser("two"));
//            session.save(createUser("three"));
//
//            List<CommonUser> users = session.find(CommonUser.class)
//            .intersect(session.find(CommonUser.class).where().eq("firstname", "two"))
//            .fetchList();
//
//            assertEquals(1, users.size());
//            assertFalse(contains("one", users));
//            assertTrue(contains("two", users));
//            assertFalse(contains("three", users));
//        });
//    }

//    @Test
//    public void testExcept() {
//        getJPO().transaction().executeVoid(session -> {
//
//            session.delete(CommonUser.class).execute();
//
//            session.save(createUser("one"));
//            session.save(createUser("two"));
//            session.save(createUser("three"));
//
//            List<CommonUser> users = session.find(CommonUser.class)
//            .except(session.find(CommonUser.class).where().eq("firstname", "three"))
//            .fetchList();
//
//            assertEquals(2, users.size());
//            assertTrue(contains("one", users));
//            assertTrue(contains("two", users));
//            assertFalse(contains("three", users));
//        });
//    }
}
