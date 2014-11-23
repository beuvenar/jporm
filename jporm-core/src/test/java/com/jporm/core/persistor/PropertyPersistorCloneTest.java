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
package com.jporm.core.persistor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.persistor.reflection.GetManipulator;
import com.jporm.core.persistor.reflection.GetterGetManipulator;
import com.jporm.core.persistor.reflection.SetManipulator;
import com.jporm.core.persistor.reflection.SetterSetManipulator;
import com.jporm.types.TypeFactory;
import com.jporm.types.TypeWrapperJdbcReady;

public class PropertyPersistorCloneTest extends BaseTestApi {

	private final String fieldName = "value";
	private Method setterMethod;
	private Method getterMethod;

	@Before
	public void setUp() throws Exception {
		setterMethod = MockBean.class.getMethod("setValue", Integer.TYPE); //$NON-NLS-1$
		assertNotNull(setterMethod);

		getterMethod = MockBean.class.getMethod("getValue"); //$NON-NLS-1$
		assertNotNull(getterMethod);

	}


	@Test
	public void testCloneProperty() throws Exception {
		final MockBean source = new MockBean();

		GetManipulator<MockBean, Integer> getter = new GetterGetManipulator<MockBean, Integer>(getterMethod);
		SetManipulator<MockBean, Integer> setter = new SetterSetManipulator<MockBean, Integer>(setterMethod);
		TypeWrapperJdbcReady<Integer, Integer> typeWrapper = new TypeFactory().getTypeWrapper(Integer.class);
		PropertyPersistorImpl<MockBean, Integer, Integer > pp = new PropertyPersistorImpl<MockBean, Integer, Integer >(fieldName, getter, setter, typeWrapper, null);

		final MockBean destination = new MockBean();
		source.setValue( new Random().nextInt() );

		pp.clonePropertyValue(source, destination);
		assertEquals( source.getValue() , destination.getValue() );

	}

	public class MockBean {
		private int value = 10;
		public boolean getCalled = false;
		public boolean setCalled = false;

		public int getValue() {
			getCalled = true;
			return value;
		}

		public void setValue(final int value) {
			setCalled = true;
			this.value = value;
		}

	}

}
