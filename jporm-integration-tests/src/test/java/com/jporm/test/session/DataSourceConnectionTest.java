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

import org.junit.Test;

import com.jporm.mapper.NullServiceCatalog;
import com.jporm.session.Session;
import com.jporm.session.SessionImpl;
import com.jporm.session.SessionProvider;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.transaction.Transaction;

/**
 * 
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
public class DataSourceConnectionTest extends BaseTestAllDB {

	public DataSourceConnectionTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testConnections() {
		final SessionProvider provider = getJPOrm().session().getSessionProvider();
		loopTransaction( provider );
		loopConnection( provider );
	}

	public void loopTransaction(final SessionProvider dsProvider) {
		final Session conn = new SessionImpl(new NullServiceCatalog(), dsProvider);

		final int howMany = 1000;

		for (int i=0; i<howMany; i++) {
			final Transaction tx = conn.transaction();
			tx.commit();
			System.out.println("commit: " + i); //$NON-NLS-1$
		}

		for (int i=0; i<howMany; i++) {
			final Transaction tx = conn.transaction();
			tx.rollback();
			System.out.println("rollback: " + i); //$NON-NLS-1$
		}
	}

	public void loopConnection(final SessionProvider dsProvider) {

		final int howMany = 100;

		for (int i=0; i<howMany; i++) {
			final Session conn = new SessionImpl(new NullServiceCatalog(), dsProvider);
			final Transaction tx = conn.transaction();
			tx.commit();
			System.out.println("commit: " + i); //$NON-NLS-1$
		}

		for (int i=0; i<howMany; i++) {
			final Session conn = new SessionImpl(new NullServiceCatalog(), dsProvider);
			final Transaction tx = conn.transaction();
			tx.rollback();
			System.out.println("rollback: " + i); //$NON-NLS-1$
		}
	}
}