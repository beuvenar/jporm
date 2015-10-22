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
package com.jporm.test.lob;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;

import com.jporm.JPO;
import com.jporm.dialect.DBType;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section02.Blobclob_String;
import com.jporm.transaction.Transaction;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class BlobClob_String_Test extends BaseTestAllDB {

	public BlobClob_String_Test(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testCrudBlobclob() {

		if (DBType.POSTGRESQL.equals(getTestData().getDBType())) {
			getLogger().info("Skip Test. Postgresql doesn't support this kind of data");
			return;
		}

		final JPO jpOrm =getJPOrm();

		jpOrm.register(Blobclob_String.class);

		long id = new Date().getTime();

		final String text1 = "BINARY STRING TEST 1 " + id; //$NON-NLS-1$

		final String text2 = "BINARY STRING TEST 2 " + id; //$NON-NLS-1$

		Blobclob_String blobclob = new Blobclob_String();
		blobclob.setBlobField(text1.getBytes());
		blobclob.setClobField(text2);

		// CREATE
		final Session conn = jpOrm.session();
		Transaction tx = conn.transaction();
		blobclob = conn.save(blobclob);
		tx.commit();

		System.out.println("Blobclob saved with id: " + blobclob.getId()); //$NON-NLS-1$
		assertFalse( id == blobclob.getId() );
		id = blobclob.getId();

		// LOAD
		tx = conn.transaction();
		final Blobclob_String blobclobLoad1 = conn.find(Blobclob_String.class, id).get();
		assertNotNull(blobclobLoad1);
		assertEquals( blobclob.getId(), blobclobLoad1.getId() );

		final String retrieved1 = new String(blobclobLoad1.getBlobField());
		System.out.println("Retrieved1 String " + retrieved1); //$NON-NLS-1$
		assertEquals( text1 , retrieved1 );

		final String retrieved2 = blobclobLoad1.getClobField();
		System.out.println("Retrieved2 String " + retrieved2); //$NON-NLS-1$
		assertEquals( text2 , retrieved2 );

		//DELETE
		conn.delete(blobclobLoad1);
		final Blobclob_String blobclobLoad2 = conn.find(Blobclob_String.class, id).get();
		assertNull(blobclobLoad2);
		tx.commit();

	}

}
