package org.nato.ivct;

import java.util.HashMap;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

public class HLAobjectRoot {

    private static RTIambassador rtiAmbassador;
    private ObjectClassHandle thisClassHandle = null;
    private static HashMap<String,AttributeHandle> knownAttributeHandles = null;  // known attribute handles
    private static AttributeHandleSet attributeHandles4Pub;                    // handles set for publish 
    private static AttributeHandleSet attributeHandles4Sub;                    // handles set for publish 
    
    private ObjectInstanceHandle thisObjectHandle;
    private AttributeHandleValueMap attributeValues;                // handle, value map for updates
    private Boolean isPublished = false;
    private Boolean isSubscribed = false;
    private Boolean isRegistered = false;

    protected EncoderFactory encoderFactory;
    
    static void initialize(RTIambassador rtiAmbassador2Use) {
        rtiAmbassador = rtiAmbassador2Use;
    }

    public String getClassName() { return "HLAobjectRoot"; }


    public HLAobjectRoot() throws Exception {
        if (rtiAmbassador == null) { throw new Exception("HLAobjectRoot not initialized"); } 
        if (thisClassHandle == null) { thisClassHandle = rtiAmbassador.getObjectClassHandle(getClassName()); }
        if (knownAttributeHandles == null) { knownAttributeHandles = new HashMap<>(); }
        if (attributeHandles4Pub == null) { attributeHandles4Pub = rtiAmbassador.getAttributeHandleSetFactory().create(); }
        if (attributeHandles4Sub == null) { attributeHandles4Sub = rtiAmbassador.getAttributeHandleSetFactory().create(); }
        thisObjectHandle = null; // unknown until object is registered
        this.attributeValues = rtiAmbassador.getAttributeHandleValueMapFactory().create(0);
        encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
    }

    public void clear() {
        attributeValues.clear();
    }
    
    public void addPubAttribute (String attributeName) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        attributeHandles4Pub.add(getAttributeHandle(attributeName));
    }

    public void addSubAttribute (String attributeName) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        attributeHandles4Sub.add(getAttributeHandle(attributeName));
    }
    
    public void setAttributeValue(String attributeName, DataElement value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        attributeValues.put(getAttributeHandle(attributeName), value.toByteArray());
    }

    public void update() throws AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {

        rtiAmbassador.updateAttributeValues(thisObjectHandle, attributeValues, null);
    }

    public void register() throws ObjectClassNotPublished, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, AttributeNotDefined {
        if (!isPublished) {
            publish();
        }
        if (!isRegistered) {

            thisObjectHandle = rtiAmbassador.registerObjectInstance(thisClassHandle);
            isRegistered = true;
        }
    }

    public void publish() throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        rtiAmbassador.publishObjectClassAttributes(thisClassHandle, attributeHandles4Pub);
        isPublished = true;  
    }
    
    public void subscribe() throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        rtiAmbassador.subscribeObjectClassAttributes(thisClassHandle, attributeHandles4Sub);        
    }

    
    private AttributeHandle getAttributeHandle(String attributeName) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        AttributeHandle handle = knownAttributeHandles.get(attributeName);
        if (handle == null) {
            handle = rtiAmbassador.getAttributeHandle(thisClassHandle, attributeName);
            knownAttributeHandles.put(attributeName, handle);
        }
        return handle;
    }
}


/**
 * 
            <objectClass notes="RPRnoteBase1">
                <name>BaseEntity</name>
                <sharing>Subscribe</sharing>
                <semantics>A base class of aggregate and discrete scenario domain participants. The BaseEntity class is characterized by being located at a particular location in space and independently movable, if capable of movement at all. It specifically excludes elements normally considered to be a component of another element. The BaseEntity class is intended to be a container for common attributes for entities of this type. Since it lacks sufficient class specific attributes that are required for simulation purposes, federates cannot publish objects of this class. Certain simulation management federates, e.g. viewers, may subscribe to this class. Simulation federates will normally subscribe to one of the subclasses, to gain the extra information required to properly simulate the entity.</semantics>
                <attribute notes="RPRnoteBase2">
                    <name>EntityType</name>
                    <dataType>EntityTypeStruct</dataType>
                    <updateType>Static</updateType>
                    <updateCondition>NA</updateCondition>
                    <ownership>DivestAcquire</ownership>
                    <sharing>PublishSubscribe</sharing>
                    <transportation>HLAbestEffort</transportation>
                    <order>Receive</order>
                    <semantics>The category of the entity.</semantics>
                </attribute>
                <attribute notes="RPRnoteBase2">
                    <name>EntityIdentifier</name>
                    <dataType>EntityIdentifierStruct</dataType>
                    <updateType>Static</updateType>
                    <updateCondition>NA</updateCondition>
                    <ownership>DivestAcquire</ownership>
                    <sharing>PublishSubscribe</sharing>
                    <transportation>HLAbestEffort</transportation>
                    <order>Receive</order>
                    <semantics>The unique identifier for the entity instance.</semantics>
                </attribute>
                <attribute notes="RPRnoteBase3">
                    <name>IsPartOf</name>
                    <dataType>IsPartOfStruct</dataType>
                    <updateType>Conditional</updateType>
                    <updateCondition>On change</updateCondition>
                    <ownership>DivestAcquire</ownership>
                    <sharing>PublishSubscribe</sharing>
                    <transportation>HLAbestEffort</transportation>
                    <order>Receive</order>
                    <semantics>Defines if the entity if a constituent part of another entity (denoted the host entity). If the entity is a constituent part of another entity then the HostEntityIdentifier shall be set to the EntityIdentifier of the host entity and the HostRTIObjectIdentifier shall be set to the RTI object instance ID of the host entity. If the entity is not a constituent part of another entity then the HostEntityIdentifier shall be set to 0.0.0 and the HostRTIObjectIdentifier shall be set to the empty string.</semantics>
                </attribute>
                <attribute notes="RPRnoteBase2 RPRnoteBase18">
                    <name>Spatial</name>
                    <dataType>SpatialVariantStruct</dataType>
                    <updateType>Conditional</updateType>
                    <updateCondition notes="RPRnoteBase10 RPRnoteBase11 RPRnoteBase12 RPRnoteBase13 RPRnoteBase14">On change</updateCondition>
                    <ownership>DivestAcquire</ownership>
                    <sharing>PublishSubscribe</sharing>
                    <transportation>HLAbestEffort</transportation>
                    <order>Receive</order>
                    <semantics>Spatial state stored in one variant record attribute.</semantics>
                </attribute>
                <attribute notes="RPRnoteBase3 RPRnoteBase18">
                    <name>RelativeSpatial</name>
                    <dataType>SpatialVariantStruct</dataType>
                    <updateType>Conditional</updateType>
                    <updateCondition notes="RPRnoteBase10 RPRnoteBase11 RPRnoteBase12 RPRnoteBase13 RPRnoteBase14">On change</updateCondition>
                    <ownership>DivestAcquire</ownership>
                    <sharing>PublishSubscribe</sharing>
                    <transportation>HLAbestEffort</transportation>
                    <order>Receive</order>
                    <semantics>Relative spatial state stored in one variant record attribute.</semantics>
                </attribute>
            </objectClass>
 */