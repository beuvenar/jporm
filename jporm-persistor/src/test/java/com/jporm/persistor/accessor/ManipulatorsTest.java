/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.persistor.accessor;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerAccessorFactory;
import com.jporm.persistor.accessor.reflection.ReflectionAccessorFactory;

public class ManipulatorsTest extends BaseTestApi {

	private Field privateStringField;
	private Field publicLongPrimitiveField;
	private Field publicLongField;
	private Method stringSetterMethod;
	private Method stringGetterMethod;
	private Method intPrimitiveSetterMethod;
	private Method intPrimitiveGetterMethod;
	private Method integerSetterMethod;
	private Method integerGetterMethod;

	@Before
	public void setUp() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		privateStringField = TestBean.class.getDeclaredField("privateString");

		publicLongPrimitiveField = TestBean.class.getField("publicLongPrimitive");
		publicLongField = TestBean.class.getField("publicLong");

		stringSetterMethod = TestBean.class.getMethod("setString", String.class); //$NON-NLS-1$
		stringGetterMethod = TestBean.class.getMethod("getString"); //$NON-NLS-1$

		intPrimitiveSetterMethod = TestBean.class.getMethod("setIntPrimitive", Integer.TYPE); //$NON-NLS-1$
		intPrimitiveGetterMethod = TestBean.class.getMethod("getIntPrimitive"); //$NON-NLS-1$

		integerSetterMethod = TestBean.class.getMethod("setInteger", Integer.class); //$NON-NLS-1$
		integerGetterMethod = TestBean.class.getMethod("getInteger"); //$NON-NLS-1$
	}

	@Test
	public void testMethodHandler() {
		test(new MethodHandlerAccessorFactory());
	}

	@Test
	public void testReflection() {
		test(new ReflectionAccessorFactory());
	}

	private void test(final AccessorFactory accessorFactory) {

		TestBean testBean = new TestBean();
		{
			GetterSetter<TestBean, String> privateStringFieldAccessor = accessorFactory.build(privateStringField);
			testBean.setInteger(12);
			assertEquals("privateValue", privateStringFieldAccessor.get(testBean) );
			privateStringFieldAccessor.set(testBean, "privateValueNew");
			assertEquals("privateValueNew", privateStringFieldAccessor.get(testBean) );
			privateStringFieldAccessor.set(testBean, null);
			assertNull(privateStringFieldAccessor.get(testBean) );
		}
		{
			GetterSetter<TestBean, Integer> integerGetterMethodGetter = accessorFactory.build(integerGetterMethod, integerSetterMethod);
			testBean.setInteger(12);
			assertEquals(12, testBean.getInteger().intValue());
			assertEquals(12, integerGetterMethodGetter.get(testBean).intValue() );
			integerGetterMethodGetter.set(testBean, 14);
			assertEquals(14, testBean.getInteger().intValue());
			integerGetterMethodGetter.set(testBean, null);
			assertNull(integerGetterMethodGetter.get(testBean) );
		}
		{
			GetterSetter<TestBean, Long> publicLongPrimitiveFieldGetter = accessorFactory.build(publicLongPrimitiveField);
			testBean.publicLongPrimitive = 12l;
			assertEquals(12l, publicLongPrimitiveFieldGetter.get(testBean).longValue() );
			publicLongPrimitiveFieldGetter.set(testBean, 14l);
			assertEquals(14l, testBean.publicLongPrimitive);
		}
		{
			GetterSetter<TestBean, Long> publicLongFieldGetter = accessorFactory.build(publicLongField);
			testBean.publicLong = 12l;
			assertEquals(12l, publicLongFieldGetter.get(testBean).longValue() );
			publicLongFieldGetter.set(testBean, 14l);
			assertEquals(14l, testBean.publicLong.longValue());
		}
		{
			GetterSetter<TestBean, Integer> intPrimitiveGetterMethodGetter = accessorFactory.build(intPrimitiveGetterMethod, intPrimitiveSetterMethod);
			testBean.setIntPrimitive(12);
			assertEquals(12, intPrimitiveGetterMethodGetter.get(testBean).intValue() );
			intPrimitiveGetterMethodGetter.set(testBean, 14);
			assertEquals(14, testBean.getIntPrimitive());
		}
		{
			GetterSetter<TestBean, String> stringGetterMethodGetter = accessorFactory.build(stringGetterMethod, stringSetterMethod);
			testBean.setString("stringValue");
			assertEquals("stringValue", stringGetterMethodGetter.get(testBean) );
			stringGetterMethodGetter.set(testBean, "stringValueNew");
			assertEquals("stringValueNew", testBean.getString());
		}
	}
}
