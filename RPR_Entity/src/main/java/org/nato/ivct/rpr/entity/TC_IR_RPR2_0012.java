/**    Copyright 2022, brf (Fraunhofer IOSB)

Licensed under the Apache License, Version 2.0 (the "License")
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http: //www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package org.nato.ivct.rpr.entity;

import org.slf4j.Logger;

import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;

/**
 * SuT shall not update non-applicable PhysicalEntity Attributes 
 * as specified in Domain Appropriateness table in SISO-STD-001-2015."
 */


public class TC_IR_RPR2_0012 extends AbstractTestCaseIf {

	 Logger logger = null;
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void logTestPurpose(Logger logger) {
		logger.info("Test Case Purpose: \n"
		+ "The test case verifies that the SuT do not update non-applicable PhysicalEntity Attributes \n"
		+ "as specified in Domain Appropriateness table in SISO-STD-001-2015."); 
		this.logger = logger;


	}

	@Override
	protected void preambleAction(Logger logger) throws TcInconclusiveIf {
		// TODO Auto-generated method stub

	}

	@Override
	protected void performTest(Logger logger) throws TcInconclusiveIf, TcFailedIf {
		// TODO Auto-generated method stub

	}

	@Override
	protected void postambleAction(Logger logger) throws TcInconclusiveIf {
		// TODO Auto-generated method stub

	}

}
