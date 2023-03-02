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
import java.util.Map.Entry;

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
import hla.rti1516e.encoding.DecoderException;
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


/** 
 * The HLAobjectRoot class is the base for all OMT helper classes. It provides
 * support services to handle subscription and publication settings on OMT
 * attributes. The class need to be initialized with an RTIambassador, before
 * any services can be used.
 */
public class HLAobjectRoot {

    private static final Logger log = LoggerFactory.getLogger(HLAobjectRoot.class);
    private static RTIambassador rtiAmbassador;
    private static HashMap<String,AttributeHandle> knownAttributeHandles = null;  // known attribute handles
    private static HashMap<String, AttributeHandleSet> publishedAttributes = new HashMap<>();
    private static HashMap<String, AttributeHandleSet> subscribedAttributes = new HashMap<>();

    private Boolean isPublished = false;
    private Boolean isSubscribed = false;
    private ObjectClassHandle thisClassHandle = null;
    private ObjectInstanceHandle thisObjectHandle;
    private AttributeHandleValueMap attributeValues;  // (handle,value) map for updates
    private Boolean isRegistered = false;

    protected EncoderFactory encoderFactory;
    
    public static void initialize(RTIambassador rtiAmbassador2Use) {
        rtiAmbassador = rtiAmbassador2Use;
    }
    
    public HLAobjectRoot() throws RprBuilderException {
        if (rtiAmbassador == null) { throw new RprBuilderException("HLAobjectRoot not initialized"); } 
        if (thisClassHandle == null) { 
            try {
                thisClassHandle = rtiAmbassador.getObjectClassHandle(getHlaClassName());
                if (knownAttributeHandles == null) { knownAttributeHandles = new HashMap<>(); }
                thisObjectHandle = null; // undefined until object is registered
                this.attributeValues = rtiAmbassador.getAttributeHandleValueMapFactory().create(0);
                encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
            } catch (Exception e) {
                throw new RprBuilderException("unhandled HLA exception", e);
            }
        }
        log.trace("created {} object created", this);
    }

    public ObjectClassHandle getClassHandle() throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
        return rtiAmbassador.getObjectClassHandle(getHlaClassName());
    }

    public void clear() {
        attributeValues.clear();
    }

    public void decode(AttributeHandleValueMap theAttributes) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, DecoderException {
        log.error("HLAobjectRoot has no attributes to decode");
    }

    protected void addPubAttribute (String attributeName) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        AttributeHandleSet attr = publishedAttributes.get(this.getClass().getSimpleName());
        if (attr == null) { 
            attr = rtiAmbassador.getAttributeHandleSetFactory().create();
            publishedAttributes.put(this.getClass().getSimpleName(), attr);
        }
        if (attr.add(getAttributeHandle(attributeName))) {
            // mark as not published, because new attribute was added, 
            isPublished = false;
        };
        log.trace("added publish for {}->{}({})", this.getHlaClassName(), attributeName, getAttributeHandle(attributeName));
    }

    protected void addSubAttribute (String attributeName) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        AttributeHandleSet attr = subscribedAttributes.get(this.getClass().getSimpleName());
        if (attr == null) { 
            attr = rtiAmbassador.getAttributeHandleSetFactory().create();
            subscribedAttributes.put(this.getClass().getSimpleName(), attr);
        }
        if (attr.add(getAttributeHandle(attributeName))) {
            // attribute was not yet subscribed
            isSubscribed = false;
        }
        log.trace("added subscribe for {}->{}({})", this.getClass().getSimpleName(), attributeName, getAttributeHandle(attributeName));
    }
    
    protected void setAttributeValue(String attributeName, DataElement value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        attributeValues.put(getAttributeHandle(attributeName), value.toByteArray());
        log.trace("set value {}->{} = {}", this, attributeName, value);
    }

    // protected DataElement getAttributeValue(String attributeName) {
    //     return attributeValues.get(attributeName);
    // }

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

    public void setObjectHandle(ObjectInstanceHandle newHandle) {
        thisObjectHandle = newHandle;
    }

    public ObjectInstanceHandle getObjectHandle() {
        return thisObjectHandle;
    }

    /**
     * Get class name in HLA-style as full path with '.' separators. 
     * 
     * @return
     */
    public String getHlaClassName() {
        String hlaClassName = null;
        if (this.getClass() == HLAobjectRoot.class) {
            hlaClassName = "HLAobjectRoot";
        } else {
            Class cls = this.getClass();
            do {
                if (hlaClassName == null) {
                    hlaClassName = cls.getSimpleName();
                } else {
                    hlaClassName = cls.getSimpleName() + "." + hlaClassName;
                }
                cls = cls.getSuperclass();
            }
            while (cls != HLAobjectRoot.class);
            hlaClassName = cls.getSimpleName() + "." + hlaClassName;
        }
        return hlaClassName;
    }

    /**
     * Publish all attributes which have been added for publication on this or any super class.
     * If the current set of attributes are already published, no new publication is done.
     * 
     * @throws AttributeNotDefined
     * @throws ObjectClassNotDefined
     * @throws SaveInProgress
     * @throws RestoreInProgress
     * @throws FederateNotExecutionMember
     * @throws NotConnected
     * @throws RTIinternalError
     */
    public void publish() throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        if (!isPublished) {
            AttributeHandleSet toPublish = rtiAmbassador.getAttributeHandleSetFactory().create();
            Class cls = this.getClass();
            do {
                AttributeHandleSet attr = publishedAttributes.get(cls.getSimpleName());
                if (attr != null) toPublish.addAll(attr);
                cls = cls.getSuperclass();
            }
            while (cls != HLAobjectRoot.class);
            rtiAmbassador.publishObjectClassAttributes(thisClassHandle, toPublish);
            log.trace("publish {}({}) -> {} object class", getHlaClassName(), thisClassHandle, toPublish);
            isPublished = true;  
        }
    }
    
    /**
     * Subscribe to all attributes which have been added for subscription on this or any super class. 
     * If the current set of attributes are already subscribed, no new subscription is done.
     * 
     * @throws AttributeNotDefined
     * @throws ObjectClassNotDefined
     * @throws SaveInProgress
     * @throws RestoreInProgress
     * @throws FederateNotExecutionMember
     * @throws NotConnected
     * @throws RTIinternalError
     */
    public void subscribe() throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        if (!isSubscribed) {
            AttributeHandleSet toSubscribe = rtiAmbassador.getAttributeHandleSetFactory().create();
            Class cls = this.getClass();
            do {
                AttributeHandleSet attr = subscribedAttributes.get(cls.getSimpleName());
                if (attr != null) toSubscribe.addAll(attr);
                cls = cls.getSuperclass();
            }
            while (cls != HLAobjectRoot.class);
            rtiAmbassador.subscribeObjectClassAttributes(thisClassHandle, toSubscribe);
            log.trace("subscribe {}({}) -> {} object class", getHlaClassName(), thisClassHandle, toSubscribe);
            isSubscribed = true;
        }
    }

    /**
     * Helper function for cached attribute handles visible within the scope of this object. 
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
    public AttributeHandle getAttributeHandle(String attributeName) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        AttributeHandle handle = knownAttributeHandles.get(attributeName);
        if (handle == null) {
            handle = rtiAmbassador.getAttributeHandle(thisClassHandle, attributeName);
            knownAttributeHandles.put(attributeName, handle);
        }
        return handle;
    }

    /**
     * Helper function to get the name of a cached attribute handle. Caching happens when the attribute
     * has been subscribed or published.
     * 
     * @param handle
     * @return
     */
    protected String getHandleString(AttributeHandle handle) {
        for (Entry<String, AttributeHandle> entry : knownAttributeHandles.entrySet()) {
            if (handle.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
