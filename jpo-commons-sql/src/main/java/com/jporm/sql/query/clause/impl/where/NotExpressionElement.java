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
package com.jporm.sql.query.clause.impl.where;

import java.util.List;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.ASqlSubElement;
import com.jporm.sql.query.clause.WhereExpressionElement;
import com.jporm.sql.query.namesolver.NameSolver;

/**
 *
 * @author Francesco Cina
 *
 *         26/giu/2011
 */
public class NotExpressionElement extends ASqlSubElement implements WhereExpressionElement {

    protected final WhereExpressionElement expression;

    public NotExpressionElement(final WhereExpressionElement expression) {
        this.expression = expression;
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        expression.appendElementValues(values);
    }

    @Override
    public final void renderSqlElement(final DBProfile dbProfile, final StringBuilder query, final NameSolver nameSolver) {
        query.append("NOT ( "); //$NON-NLS-1$
        expression.renderSqlElement(dbProfile, query, nameSolver);
        query.append(") "); //$NON-NLS-1$
    }
}
