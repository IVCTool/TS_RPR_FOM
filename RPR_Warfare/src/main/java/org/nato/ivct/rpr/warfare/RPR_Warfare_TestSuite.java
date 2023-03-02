/**    Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

Licensed under the Apache License, Version 2.0 (the "License")
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http: //www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package org.nato.ivct.rpr.warfare;

import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib.HlaTestSuite;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

public class RPR_Warfare_TestSuite extends HlaTestSuite {

    public RPR_Warfare_TestSuite() throws FileNotFoundException, IOException, ParseException {
        super();
    }

    public static final String TEST_SUITE_ID = "RPR-ENTITY-2_0";
    JSONObject description;

    public static final org.slf4j.Logger log = LoggerFactory.getLogger(RPR_Warfare_TestSuite.class);

    @Override
    public AbstractTestCaseIf getTestCase(String testCaseId) {
        log.trace("got test case id {} to find", testCaseId);
        if (testCaseId.equals(TC_IR_RPR2_0017.class.getName())) {
            return new TC_IR_RPR2_0017();
        } else if (testCaseId.equals(TC_IR_RPR2_0018.class.getName())) {
            return new TC_IR_RPR2_0018();
        }
        log.trace("nothing found");
        return null;
    }

}
