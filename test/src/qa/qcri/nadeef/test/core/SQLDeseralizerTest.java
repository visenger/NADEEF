/*
 * Copyright (C) Qatar Computing Research Institute, 2013.
 * All rights reserved.
 */

package qa.qcri.nadeef.test.core;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import qa.qcri.nadeef.core.datamodel.CleanPlan;
import qa.qcri.nadeef.core.datamodel.Rule;
import qa.qcri.nadeef.core.operator.SQLDeseralizer;
import qa.qcri.nadeef.test.TestDataRepository;

import java.util.List;

/**
 * SQLDeseralizer Test.
 */
@RunWith(JUnit4.class)
public class SQLDeseralizerTest {

    @Test
    public void projectSQLStatementTest() {
        try {
            CleanPlan cleanPlan = TestDataRepository.getFDCleanPlan();
            List<Rule> rules = cleanPlan.getRules();
            SQLDeseralizer deseralizer = new SQLDeseralizer(cleanPlan);
            String sqlStatement = deseralizer.getSQLStatement(rules.get(0));
            Assert.assertTrue(
                "SELECT location.zip,location.state,location.city from location"
                        .equalsIgnoreCase(sqlStatement)
            );
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
