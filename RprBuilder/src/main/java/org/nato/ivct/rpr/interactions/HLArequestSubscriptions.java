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

package org.nato.ivct.rpr.interactions;

import org.nato.ivct.rpr.RprBuilderException;

import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;


/** Request that the RTI send report interactions that contain the subscription data of a
    joined federate. It shall result in one interaction of class
    HLAmanager.HLAfederate.HLAreport.HLAreportInteractionSubscription and one interaction of class
    HLAmanager.HLAfederate.HLAreport.HLAreportObjectClassSubscription for each different combination
    of (object class, passive subscription indicator) values that are subscribed. If the joined
    federate is subscribed to no object classes, then one interaction of class
    HLAmanager.HLAfederate.HLAreport.HLAreportObjectClassSubscription shell be sent as a NULL
    response with the HLAobjectClassCount parameter having a value of 0.
 */
public class HLArequestSubscriptions extends HLArequest {

    public HLArequestSubscriptions()
            throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        super();
    }
    
}
