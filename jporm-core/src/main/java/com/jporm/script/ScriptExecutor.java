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
package com.jporm.script;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.jporm.exception.OrmException;

/**
 * 
 * @author Francesco Cina
 *
 * 02/lug/2011
 * 
 * Executes statements from a standard sql script;
 * The script can contain comments using the double hyphen symbol -- only if the comment is the 
 * only thing in a row, e.g.:
 * this is valid:
 *     insert into TABLE_NAME 
 *     -- valid comment
 *     values (1,2,3);
 * this isn't valid:
 *     insert into TABLE_NAME -- NOT valid comment, this can break the parser!!
 *     values (1,2,3);
 * 
 */
public interface ScriptExecutor {

	/**
	 * Executes the script.
	 * @param script
	 */
	void execute(String script) throws OrmException;
	
	
	/**
	 * Executes the script.
	 * @param script
	 */
	void execute(InputStream scriptStream) throws IOException, OrmException;
	
	/**
	 * Executes the script.
	 * The passed Charset will be used to read the stream.
	 * @param script
	 * @param charset
	 */
	void execute(InputStream scriptStream, Charset charset) throws IOException, OrmException;
	
}