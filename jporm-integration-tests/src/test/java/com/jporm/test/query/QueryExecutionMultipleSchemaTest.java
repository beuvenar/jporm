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
package com.jporm.test.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.jporm.JPO;
import com.jporm.JPOrm;
import com.jporm.query.clause.where.LeExpressionElement;
import com.jporm.query.find.FindQuery;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.test.domain.section04.Zoo_People;
import com.jporm.transaction.Transaction;

/**
 * 
 * @author Francesco Cina
 *
 * 23/giu/2011
 */
public class QueryExecutionMultipleSchemaTest extends BaseTestAllDB {

	public QueryExecutionMultipleSchemaTest(final String testName, final TestData testData) {
		super(testName, testData);
	}


	@Test
	public void testQuery2() {

		if (!getTestData().isSupportMultipleSchemas()) {
			return;
		}

		final JPOrm jpOrm = getJPOrm();
		jpOrm.register(Employee.class);
		jpOrm.register(Zoo_People.class);

		final Session session =  jpOrm.session();
		final Employee employee = createEmployee(jpOrm);

		final int maxRows = 4;
		final FindQuery<Employee> query = session.findQuery(Employee.class);
		query.join(Zoo_People.class, "zp"); //$NON-NLS-1$
		query.maxRows(maxRows);
		query.where().not( new LeExpressionElement("id", "0") ); //$NON-NLS-1$ //$NON-NLS-2$
		query.where().ilike("zp.firstname", "%"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println(query.renderSql());

		final List<Employee> employeeList = query.getList();
		assertNotNull( employeeList );

		System.out.println("found employees: " + employeeList.size()); //$NON-NLS-1$
		assertTrue( employeeList.size()<=maxRows );

		deleteEmployee(jpOrm, employee);
	}

	private Employee createEmployee(final JPO jpOrm) {
		final Session ormSession = jpOrm.session();
		final Transaction tx = ormSession.transaction();
		final int id = new Random().nextInt(Integer.MAX_VALUE);
		final Employee employee = new Employee();
		employee.setId( id );
		employee.setAge( 44 );
		employee.setEmployeeNumber( "empNumber" + id ); //$NON-NLS-1$
		employee.setName("Wizard"); //$NON-NLS-1$
		employee.setSurname("Cina"); //$NON-NLS-1$
		ormSession.save(employee).now();
		tx.commit();
		return employee;
	}

	private void deleteEmployee(final JPO jpOrm, final Employee employee) {
		final Session ormSession = jpOrm.session();
		final Transaction tx = ormSession.transaction();
		ormSession.delete(employee).now();
		tx.commit();
	}

}
