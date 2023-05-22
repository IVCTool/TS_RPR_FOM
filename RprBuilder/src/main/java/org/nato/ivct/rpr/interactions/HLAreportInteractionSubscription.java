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

import org.nato.ivct.rpr.OmtBuilder;
import org.nato.ivct.rpr.RprBuilderException;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.encoding.DataElementFactory;
import hla.rti1516e.encoding.HLAbyte;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;


/** The interaction shall be sent by the RTI in response to an interaction of class
    HLAmanager.HLAfederate.HLArequest.HLArequestSubscriptions. It shall report the interaction
    classes subscribed to by the joined federate. This information shall reflect related DDM usage.
    If the joined federate has no subscribed interaction classes, then a single interaction shall be
    sent as a NULL response with the HLAinteractionClassList parameter having an undefined value.
 */
public class HLAreportInteractionSubscription extends HLAreport {

    public enum Attributes {
        HLAinteractionClassList
    }

    public HLAreportInteractionSubscription()
            throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        super();
        DataElementFactory<HLAbyte> byteFactory = new DataElementFactory<HLAbyte>()
        {
            public HLAbyte createElement(int index)
            {
                return OmtBuilder.getEncoderFactory().createHLAbyte();
            }
        };
        addParameter(Attributes.HLAinteractionClassList.name(), OmtBuilder.getEncoderFactory().createHLAvariableArray(byteFactory));
    }

    public static HLAreportInteractionSubscription discover (InteractionClassHandle theInteractionClassHandle) {
        HLAreportInteractionSubscription candidate;
        try {
            candidate = new HLAreportInteractionSubscription();
            if (!candidate.getInteractionClassHandle().equals(theInteractionClassHandle)) {
                candidate = null;
            } 
        } catch (NameNotFound | FederateNotExecutionMember | NotConnected | RTIinternalError | RprBuilderException e) {
            candidate = null;
        }
        return candidate;
    }
}
