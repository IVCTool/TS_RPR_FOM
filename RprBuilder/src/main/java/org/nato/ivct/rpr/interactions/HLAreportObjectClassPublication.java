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
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DataElementFactory;
import hla.rti1516e.encoding.HLAbyte;
import hla.rti1516e.encoding.HLAoctetPairBE;
import hla.rti1516e.encoding.HLAinteger32BE;
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
        DataElementFactory<HLAbyte> byteFactory = new DataElementFactory<HLAbyte>()
        {
            public HLAbyte createElement(int index)
            {
                return OmtBuilder.getEncoderFactory().createHLAbyte();
            }
        };
        DataElementFactory<HLAoctetPairBE> octedFactory = new DataElementFactory<HLAoctetPairBE>()
        {
            public HLAoctetPairBE createElement(int index)
            {
                return OmtBuilder.getEncoderFactory().createHLAoctetPairBE();
            }
        };
        addParameter(Attributes.HLAnumberOfClasses.name(), RtiFactoryFactory.getRtiFactory().getEncoderFactory().createHLAinteger32BE());
        addParameter(Attributes.HLAobjectClass.name(), OmtBuilder.getEncoderFactory().createHLAvariableArray(byteFactory));
        addParameter(Attributes.HLAattributeList.name(), OmtBuilder.getEncoderFactory().createHLAvariableArray(octedFactory));
    }
    
    public static HLAreportObjectClassPublication discover (InteractionClassHandle theInteractionClassHandle) {
        HLAreportObjectClassPublication candidate;
        try {
            candidate = new HLAreportObjectClassPublication();
            if (!candidate.getInteractionClassHandle().equals(theInteractionClassHandle)) {
                candidate = null;
            } 
        } catch (NameNotFound | FederateNotExecutionMember | NotConnected | RTIinternalError | RprBuilderException e) {
            candidate = null;
        }
        return candidate;
    }

    
    public int getaHLAnumberOfClasses() {
        return ((HLAinteger32BE) getParameter(Attributes.HLAnumberOfClasses.name())).getValue();
    }

    public byte[] getHLAobjectClass() {
        return getParameter(Attributes.HLAobjectClass.name()).toByteArray();
    }

    public byte [] getHLAattributeList() {
        return getParameter(Attributes.HLAattributeList.name()).toByteArray();
    }
}
