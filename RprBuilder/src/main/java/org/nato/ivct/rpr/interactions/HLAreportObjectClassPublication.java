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


/** The interaction shall be sent by the RTI in response to an interaction of class
    HLAmanager.HLAfederate.HLArequest.HLArequestPublications. It shall report the attributes of one
    object class published by the joined federate. One of these interactions shall be sent for each
    object class containing attributes that are published by the joined federate. If the joined
    federate is published to no object classes then a single interaction shall be sent as a NULL
    response with the HLAobjectClassCount parameter having a value of 0.
 */
public class HLAreportObjectClassPublication extends HLAreport {

    public enum Attributes {
        HLAnumberOfClasses,
        HLAobjectClass,
        HLAattributeList
    }

    public HLAreportObjectClassPublication()
            throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        super();
    }
    
}
