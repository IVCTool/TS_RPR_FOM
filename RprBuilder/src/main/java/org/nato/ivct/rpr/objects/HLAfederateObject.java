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

import javax.accessibility.AccessibleHyperlink;

import org.nato.ivct.rpr.OmtBuilder;
import org.nato.ivct.rpr.RprBuilderException;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.AttributeNotDefined;
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
public class HLAfederateObject extends HLAmanagerObject {

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

    private static HLAfederateObject anchor;
    private static HashMap<ObjectInstanceHandle, HLAfederateObject> knownObjects = new HashMap<>();

    private HLAoctet aHLAfederateHandle;
    private HLAunicodeString aHLAfederateName;
    private HLAunicodeString aHLAfederateType;
    private HLAunicodeString aHLAfederateHost;
    private HLAunicodeString aHLARTIversion;

    public byte getHLAfederateHandle() {
        return aHLAfederateHandle.getValue();
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

    public HLAfederateObject() throws RprBuilderException {
        super();
        aHLAfederateHandle = OmtBuilder.getEncoderFactory().createHLAoctet();
        aHLAfederateName= OmtBuilder.getEncoderFactory().createHLAunicodeString();
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
                    aHLAfederateHandle.decode(entry.getValue());
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
            anchor = new HLAfederateObject();
        anchor.addPubAttribute(attribute.name());
    }

    public static void addSub(Attributes attribute) throws RprBuilderException, NameNotFound, InvalidObjectClassHandle,
            FederateNotExecutionMember, NotConnected, RTIinternalError {
        if (anchor == null)
            anchor = new HLAfederateObject();
        anchor.addSubAttribute(attribute.name());
    }

    public static void sub() throws RprBuilderException, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress,
            RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        if (anchor == null)
            anchor = new HLAfederateObject();
        anchor.subscribe();
    }

    public static HLAfederateObject discover(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass)
            throws InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError,
            RprBuilderException {
        String receivedClass = OmtBuilder.getRtiAmbassador().getObjectClassName(theObjectClass);
        if (receivedClass.equalsIgnoreCase(anchor.getHlaClassName())) {
            return get(theObject);
        }
        return null;
    }

    public static HLAfederateObject get(ObjectInstanceHandle theObject) throws RprBuilderException {
        HLAfederateObject localRef = null;
        localRef = knownObjects.get(theObject);
        if (localRef == null) {
            localRef = new HLAfederateObject();
            localRef.setObjectHandle(theObject);
            knownObjects.put(theObject, localRef);
        }
        return localRef;
    }
}
