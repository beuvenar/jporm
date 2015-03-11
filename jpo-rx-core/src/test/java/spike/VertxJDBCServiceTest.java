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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 13, 2013
 * ----------------------------------------------------------------------------
 */
package spike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JdbcService;
import io.vertx.ext.sql.SqlConnection;
import io.vertx.ext.sql.UpdateResult;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javax.sql.DataSource;

import org.junit.Test;

import com.jporm.rx.core.BaseTestApi;
import com.jporm.sql.query.clause.Insert;
import com.jporm.sql.query.clause.impl.SelectImpl;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 13, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class VertxJDBCServiceTest extends BaseTestApi {

	@Test
	public void testQuery() throws Exception {
		Vertx vertx = Vertx.vertx();

		JsonObject config = new JsonObject();
		DataSource dataSource = getH2DataSource();

		JdbcService jdbcService = JdbcService.create(vertx, config, dataSource);
		jdbcService.start();

		CountDownLatch latch = new CountDownLatch(1);
		jdbcService.getConnection(handler -> {

			handler.result().query("select count(*) as count from users", result -> {
				try {
					getLogger().info("Found {} columns", result.result().getNumColumns());
					getLogger().info("Columns: {}", result.result().getColumnNames());
					getLogger().info("Found {} rows", result.result().getNumRows());
					getLogger().info("Results size {}", result.result().getResults().size());
					getLogger().info("Rows size {}", result.result().getRows().size());

					for (JsonArray res : result.result().getResults()) {
						getLogger().info("Results at position: {}", res.getInteger(0));
					}

					int count = result.result().getRows().size();
					getLogger().info("Found {} users", count);
				} finally {
					latch.countDown();
				}
			});

		});

		latch.await();

	}


	@Test
	public void testUpdate() throws Exception {
		Vertx vertx = Vertx.vertx();

		JsonObject config = new JsonObject();
		DataSource dataSource = getH2DataSource();

		JdbcService jdbcService = JdbcService.create(vertx, config, dataSource);
		jdbcService.start();

		final String firstname = UUID.randomUUID().toString();
		final String lastname = UUID.randomUUID().toString();
		final Insert insertUser = getSqlFactory().insert(User.class);
		insertUser.values().eq("firstname", firstname);
		insertUser.values().eq("lastname", lastname);

		CountDownLatch latch = new CountDownLatch(1);
		jdbcService.getConnection(handler -> {
			final SqlConnection connection = handler.result();

			List<Object> values = new ArrayList<>();
			insertUser.appendValues(values);
			JsonArray params = new JsonArray(values);

			getLogger().info("Execute query: {}", insertUser.renderSql());

			connection.updateWithParams(insertUser.renderSql(), params, handler2 -> {
				try {
					getLogger().info("Insert succeeded: {}", handler2.succeeded());
					UpdateResult updateResult = handler2.result();
					getLogger().info("Updated {} rows", updateResult.getUpdated());
					getLogger().info("Keys {}", updateResult.getKeys());

//					for (JsonArray res : result.result().getResults()) {
//						getLogger().info("Results at position: {}", res.getInteger(0));
//					}

				} finally {
					latch.countDown();
				}
			});

		});

		latch.await();

	}

}
