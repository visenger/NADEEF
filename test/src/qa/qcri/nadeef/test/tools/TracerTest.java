/*
 * Copyright (C) Qatar Computing Research Institute, 2013.
 * All rights reserved.
 */

package qa.qcri.nadeef.test.tools;

import org.junit.Test;
import qa.qcri.nadeef.tools.Tracer;

/**
 * Tracer unit test.
 */
public class TracerTest {
    @Test
    public void test() {
        Tracer.putStatsEntry(Tracer.StatType.DetectTime, 1);
        Tracer.printDetectSummary("");
    }
}
