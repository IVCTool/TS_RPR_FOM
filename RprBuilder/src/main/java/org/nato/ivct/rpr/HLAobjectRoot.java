/**
 * Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http: //www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License. 
 */

package org.nato.ivct.rpr;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static final Logger log = LoggerFactory.getLogger(HLAobjectRoot.class);

    private static RTIambassador rtiAmbassador;
    private ObjectClassHandle thisClassHandle = null;
    private static HashMap<String,AttributeHandle> knownAttributeHandles = null;  // known attribute handles
    private static AttributeHandleSet attributeHandles4Pub;  // handles set for publish 
    private static AttributeHandleSet attributeHandles4Sub;  // handles set for publish 
    
    private ObjectInstanceHandle thisObjectHandle;
    private AttributeHandleValueMap attributeValues;  // (handle,value) map for updates
    private Boolean isPublished = false;
    private Boolean isSubscribed = false;
    private Boolean isRegistered = false;

    protected EncoderFactory encoderFactory;
    
    public static void initialize(RTIambassador rtiAmbassador2Use) {
        rtiAmbassador = rtiAmbassador2Use;
    }

    public String getHlaClassName() { return "HLAobjectRoot"; }

    public HLAobjectRoot() throws Exception {
        if (rtiAmbassador == null) { throw new Exception("HLAobjectRoot not initialized"); } 
        if (thisClassHandle == null) { thisClassHandle = rtiAmbassador.getObjectClassHandle(getHlaClassName()); }
        if (knownAttributeHandles == null) { knownAttributeHandles = new HashMap<>(); }
        if (attributeHandles4Pub == null) { attributeHandles4Pub = rtiAmbassador.getAttributeHandleSetFactory().create(); }
        if (attributeHandles4Sub == null) { attributeHandles4Sub = rtiAmbassador.getAttributeHandleSetFactory().create(); }
        thisObjectHandle = null; // undefined until object is registered
        this.attributeValues = rtiAmbassador.getAttributeHandleValueMapFactory().create(0);
        encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
        log.trace("created {} object created", this);
    }

    public void clear() {
        attributeValues.clear();
    }

    public void decode(AttributeHandleValueMap theAttributes) {
        log.error("HLAobjectRoot has no attributes to decode");
    }

    protected void addPubAttribute (String attributeName) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        attributeHandles4Pub.add(getAttributeHandle(attributeName));
        isPublished = false;
        log.trace("added publish for {}.{}", this, attributeName);
    }

    protected void addSubAttribute (String attributeName) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        attributeHandles4Sub.add(getAttributeHandle(attributeName));
        isSubscribed = false;
        log.trace("added subscribe for {}.{}", this, attributeName);
    }
    
    protected void setAttributeValue(String attributeName, DataElement value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        attributeValues.put(getAttributeHandle(attributeName), value.toByteArray());
        log.trace("set value {}.{} = {}", this, attributeName, value);
    }

    public void update() throws AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        rtiAmbassador.updateAttributeValues(thisObjectHandle, attributeValues, null);
        log.trace("update {}({}) to RTI", this, thisObjectHandle);
    }

    public void register() throws ObjectClassNotPublished, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, AttributeNotDefined {
        publish();
        if (!isRegistered) {

            thisObjectHandle = rtiAmbassador.registerObjectInstance(thisClassHandle);
            log.trace("register {}({}) as {}", this, thisClassHandle, thisObjectHandle);
            isRegistered = true;
        }
    }

    public void register(ObjectInstanceHandle newHandle) {
        thisObjectHandle = newHandle;
    }

    public void publish() throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        if (!isPublished) {
            rtiAmbassador.publishObjectClassAttributes(thisClassHandle, attributeHandles4Pub);
            log.trace("publish {}({}) -> {} object class", getHlaClassName(), thisClassHandle, attributeHandles4Pub);
            isPublished = true;  
        }
    }
    
    public void subscribe() throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        if (!isSubscribed) {
            rtiAmbassador.subscribeObjectClassAttributes(thisClassHandle, attributeHandles4Sub);
            log.trace("subscribe {}({}) -> {} object class", getHlaClassName(), thisClassHandle, attributeHandles4Pub);
            isSubscribed = true;
        }
    }

    /**
     * Helper function for cached attribute handles. 
     * 
     * @param attributeName The name of the requested attribute handle as defined in the OMT 
     * @return The handle as given by the rti ambassador. 
     * 
     * @throws NameNotFound
     * @throws InvalidObjectClassHandle
     * @throws FederateNotExecutionMember
     * @throws NotConnected
     * @throws RTIinternalError
     */
    protected AttributeHandle getAttributeHandle(String attributeName) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        AttributeHandle handle = knownAttributeHandles.get(attributeName);
        if (handle == null) {
            handle = rtiAmbassador.getAttributeHandle(thisClassHandle, attributeName);
            knownAttributeHandles.put(attributeName, handle);
        }
        return handle;
    }
}
