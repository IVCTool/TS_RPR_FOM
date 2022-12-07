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
