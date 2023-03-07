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

/** Request that the RTI send report interactions that contain the publication data of a
    joined federate. It shall result in one interaction of class HLAmanager. HLAfederate.HLAreport.
    HLAreportInteractionPublication and one interaction of class HLAmanager.HLAfederate.
    HLAreport.HLAreportObjectClassPublication for each object class published. If the joined
    federate is published to no object classes then one of class
    HLAmanager.HLAfederate.HLAreport.HLAreportObjectClassPublication shall be sent as a NULL
 */
public class HLArequestPublications extends HLArequest {

    public HLArequestPublications()
            throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        super();
    }
    
}
