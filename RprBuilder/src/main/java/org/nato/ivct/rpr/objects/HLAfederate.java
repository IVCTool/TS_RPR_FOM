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

package org.nato.ivct.rpr.objects;

import java.util.Map.Entry;
import java.util.HashMap;

import org.nato.ivct.rpr.OmtBuilder;
import org.nato.ivct.rpr.RprBuilderException;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.FederateHandleFactory;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.CouldNotDecode;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

/**
 * This object class shall contain RTI state variables relating to a joined
 * federate. The RTI
 * shall publish it and shall register one object instance for each joined
 * federate in a federation.
 * Dynamic attributes that shall be contained in an object instance shall be
 * updated periodically, where
 * the period should be determined by an interaction of the class
 * HLAmanager.HLAfederate.HLAadjust.HLAsetTiming. If this value is never set or
 * is set to zero, no
 * periodic update shall be performed by the RTI.
 * 
 * The RTI shall respond to the invocation, by any federate, of the Request
 * Attribute Value Update
 * service for this object class or for any instance attribute of an object
 * instance of this class by
 * supplying values via the normal instance attribute update mechanism,
 * regardless of whether the
 * attribute has a data type of static, periodic, or conditional. In addition to
 * its responsibility to
 * update attributes of object instances of this class when those updates are
 * explicitly requested, the
 * RTI shall automatically update instance attributes of object instances of
 * this class according to the
 * update policy of the attribute, which is determined by the update type of the
 * class attribute in Table
 * 6. For those attributes that have an update type of Periodic, the update
 * wall-clock time interval
 * shall be determined by the HLAreportPeriod parameter in an interaction of
 * classHLAmanager.HLAfederate.
 * HLAadjust.HLAsetTiming. If this value is never set or is set to zero, no
 * periodic updates shall be
 * performed by the RTI. Those attributes that have an update type of
 * Conditional shall have update
 * conditions as defined in the Table 6.
 */
public class HLAfederate extends HLAmanager {

    public enum Attributes {
        HLAfederateHandle,
        HLAfederateName,
        HLAfederateType,
        HLAfederateHost,
        HLARTIversion,
        HLAFOMmoduleDesignatorList,
        HLAtimeConstrained,
        HLAtimeRegulating,
        HLAasynchronousDelivery,
        HLAfederateState,
        HLAtimeManagerState,
        HLAlogicalTime,
        HLAlookahead,
        HLAGALT,
        HLALITS,
        HLAROlength,
        HLATSOlength,
        HLAreflectionsReceived,
        HLAupdatesSent,
        HLAinteractionsReceived,
        HLAinteractionsSent,
        HLAobjectInstancesThatCanBeDeleted,
        HLAobjectInstancesUpdated,
        HLAobjectInstancesReflected,
        HLAobjectInstancesDeleted,
        HLAobjectInstancesRemoved,
        HLAobjectInstancesRegistered,
        HLAobjectInstancesDiscovered,
        HLAtimeGrantedTime,
        HLAtimeAdvancingTime,
        HLAconveyRegionDesignatorSets,
        HLAconveyProducingFederate
    }

    private static HLAfederate anchor;
    public static HashMap<ObjectInstanceHandle, HLAfederate> knownObjects = new HashMap<>();

    private FederateHandleFactory  aHLAfederateHandleFactory;
    private byte[]  aHLAfederateHandle;
    private HLAunicodeString aHLAfederateName;
    private HLAunicodeString aHLAfederateType;
    private HLAunicodeString aHLAfederateHost;
    private HLAunicodeString aHLARTIversion;

    public byte[] getHLAfederateHandle() {
        return aHLAfederateHandle;
    }

    public String getHLAfederateName() {
        return aHLAfederateName.getValue();
    }

    public String getHLAfederateType() {
        return aHLAfederateType.getValue();
    }

    public String getHLAfederateHost() {
        return aHLAfederateHost.getValue();
    }

    public String getHLARTIversion() {
        return aHLARTIversion.getValue();
    }

    public HLAfederate() throws RprBuilderException {
        super();
        try {
            aHLAfederateHandleFactory = OmtBuilder.getRtiAmbassador().getFederateHandleFactory();
            aHLAfederateHandle = null;
        } catch (FederateNotExecutionMember | NotConnected e) {
            // TODO Auto-generated catch block
            throw new RprBuilderException(e.getMessage());
        }
        aHLAfederateName = OmtBuilder.getEncoderFactory().createHLAunicodeString();
        aHLAfederateType = OmtBuilder.getEncoderFactory().createHLAunicodeString();
        aHLAfederateHost = OmtBuilder.getEncoderFactory().createHLAunicodeString();
        aHLARTIversion = OmtBuilder.getEncoderFactory().createHLAunicodeString();    
    }

    public void decode(AttributeHandleValueMap theAttributes) throws DecoderException {
        for (Entry<AttributeHandle, byte[]> entry : theAttributes.entrySet()) {
            AttributeHandle attributeHandle = entry.getKey();
            Attributes attribute = Attributes.valueOf(getHandleString(attributeHandle));
            switch (attribute) {
                case HLAfederateHandle:
                    aHLAfederateHandle = theAttributes.get(entry.getKey());
                    break;
                case HLAfederateName:
                    aHLAfederateName.decode(entry.getValue());
                break;
                case HLAfederateType:
                    aHLAfederateType.decode(entry.getValue());
                    break;
                case HLAfederateHost:
                    aHLAfederateHost.decode(entry.getValue());
                    break;
                case HLARTIversion:
                    aHLARTIversion.decode(entry.getValue());
                    break;
            }
        }
    }

    public static void addPub(Attributes attribute) throws RprBuilderException, NameNotFound, InvalidObjectClassHandle,
            FederateNotExecutionMember, NotConnected, RTIinternalError {
        if (anchor == null)
            anchor = new HLAfederate();
        anchor.addPubAttribute(attribute.name());
    }

    public static void addSub(Attributes attribute) throws RprBuilderException, NameNotFound, InvalidObjectClassHandle,
            FederateNotExecutionMember, NotConnected, RTIinternalError {
        if (anchor == null)
            anchor = new HLAfederate();
        anchor.addSubAttribute(attribute.name());
    }

    public static void sub() throws RprBuilderException, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress,
            RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        if (anchor == null)
            anchor = new HLAfederate();
        anchor.subscribe();
    }

    public static HLAfederate discover(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass)
            throws InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError,
            RprBuilderException {
        String receivedClass = OmtBuilder.getRtiAmbassador().getObjectClassName(theObjectClass);
        if (receivedClass.equalsIgnoreCase(anchor.getHlaClassName())) {
            return get(theObject);
        }
        return null;
    }

    public static HLAfederate get(ObjectInstanceHandle theObject) throws RprBuilderException {
        HLAfederate localRef = null;
        localRef = knownObjects.get(theObject);
        if (localRef == null) {
            localRef = new HLAfederate();
            localRef.setObjectHandle(theObject);
            knownObjects.put(theObject, localRef);
        }
        return localRef;
    }
}