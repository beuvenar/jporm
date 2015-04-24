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
package com.jporm.test.query.forupdate;


import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.JpoRX;
import com.jporm.rx.core.query.find.FindQuery;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;

/**
 *
 * @author Francesco Cina'
 *
 * 30/ago/2011
 */
public class QuerySelectForUpdateExecutionTest extends BaseTestAllDB {

	public QuerySelectForUpdateExecutionTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private final long THREAD_SLEEP = 250l;

	@Test
	public void testQuery1() throws Exception {
		final JpoRX jpOrm = getJPO();

		final Employee employeeLocked = createEmployee(jpOrm);
		final Employee employeeUnlocked = createEmployee(jpOrm);

		final ActorLockForUpdate actor1 = new ActorLockForUpdate(jpOrm, employeeLocked.getId(), "locked1"); //$NON-NLS-1$
		final Thread thread1 = new Thread( actor1 );
		thread1.start();

		Thread.sleep(THREAD_SLEEP / 5);

		final ActorLockForUpdate actor2 = new ActorLockForUpdate(jpOrm, employeeLocked.getId(), "locked2"); //$NON-NLS-1$
		final Thread thread2 = new Thread( actor2 );
		thread2.start();

		thread1.join();
		thread2.join();

		assertFalse(actor1.exception);
		assertFalse(actor2.exception);

		getLogger().info("Threads execution ended. Check results");

		assertEquals( "name_locked1_locked2" ,  jpOrm.session().find(Employee.class, employeeLocked.getId()).getUnique().get().getName() ); //$NON-NLS-1$

		deleteEmployee(jpOrm, employeeLocked);
		deleteEmployee(jpOrm, employeeUnlocked);

	}




	public class ActorLockForUpdate implements Runnable {

		private final JpoRX jpOrm;
		final String actorName;
		private final long employeeId;
		boolean exception = false;

		public ActorLockForUpdate(final JpoRX jpOrm, final long employeeId, final String name) {
			this.jpOrm = jpOrm;
			this.employeeId = employeeId;
			actorName = name;
		}

		@Override
		public void run() {
			System.out.println("Run: " + actorName); //$NON-NLS-1$
			try {

				jpOrm.transaction().isolation(TransactionIsolation.REPEATABLE_READS).now(txSession -> {

					final FindQuery<Employee> query = txSession.findQuery(Employee.class, "Employee"); //$NON-NLS-1$
					query.where().eq("Employee.id", employeeId); //$NON-NLS-1$
					query.forUpdate();

					System.out.println("Thread " + actorName + " executing select query"); //$NON-NLS-1$
					CompletableFuture<Employee> result = query.get()
					.thenCompose(employee -> {
						System.out.println("Thread " + actorName + " - employee.getName() = [" + employee.getName() + "]"); //$NON-NLS-1$
						assertNotNull(employee);

						try {
							Thread.sleep(THREAD_SLEEP);
						} catch (final InterruptedException e) {
							//Nothing to do
						}

						employee.setName( employee.getName() + "_" + actorName); //$NON-NLS-1$
						System.out.println("Thread " + actorName + " updating employee"); //$NON-NLS-1$
						return txSession.update(employee);

					});
					return result;
				})
				.get()
				;

				System.out.println("Thread " + actorName + " execution ended");

			} catch (final Exception e) {
				e.printStackTrace();
				exception = true;
			}
		}

	}



	private Employee createEmployee(final JpoRX jpOrm) throws Exception {
		final int id = new Random().nextInt(Integer.MAX_VALUE);
		final Employee employee = new Employee();
		employee.setId( id );
		employee.setAge( 44 );
		employee.setEmployeeNumber( ("empNumber" + id) ); //$NON-NLS-1$
		employee.setName("name"); //$NON-NLS-1$
		employee.setSurname("Cina"); //$NON-NLS-1$
		return jpOrm.session().save(employee).get();
	}

	private void deleteEmployee(final JpoRX jpOrm, final Employee employee) throws Exception {
		jpOrm.session().delete(employee).get();
	}



}