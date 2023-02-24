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

import org.slf4j.Logger;

import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;


/**
 * IR-RPR2-0011:
 * 
 * SuT shall update the following required attributes for PhysicalEntity subclass object instances 
 * registered by SuT: EntityIdentifier, EntityType, Spatial.
 */
public class TC_IR_RPR2_0018 extends AbstractTestCaseIf {
    
    Logger logger = null;

    @Override
    protected void logTestPurpose(Logger logger) {
        String msg =    "empty implementation";
        logger.info(msg);
        this.logger = logger;
    }

    @Override
    protected void preambleAction(Logger logger) throws TcInconclusiveIf {
        logger.info("placeholder for preamble action for test {}", this.getClass().getName());
    }

    @Override
    protected void performTest(Logger logger) throws TcInconclusiveIf, TcFailedIf {
        logger.info("placeholder for perform test {}", this.getClass().getName());
        // DODO: implement test logic here
        logger.info("test {} passed", this.getClass().getName());
    }

    @Override
    protected void postambleAction(Logger logger) throws TcInconclusiveIf {
        logger.info("placeholder for postamble action for test {}", this.getClass().getName());
    }
}
