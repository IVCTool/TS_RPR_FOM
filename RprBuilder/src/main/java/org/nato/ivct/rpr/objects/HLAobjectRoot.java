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

package org.nato.ivct.rpr.objects;

import java.util.HashMap;
import java.util.Map.Entry;

import org.nato.ivct.rpr.HLAroot;
import org.nato.ivct.rpr.OmtBuilder;
import org.nato.ivct.rpr.RprBuilderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
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
public class HLAobjectRoot extends HLAroot {

    class AttributeHolder {
        AttributeHandle handle;
        DataElement data;
        Boolean isUpdated;
        public AttributeHolder(DataElement value) {
            data = value;
            isUpdated = false;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(HLAobjectRoot.class);
    private static HashMap<ObjectClassHandle,HashMap<String,AttributeHandle>> knownAttributeHandles = null;
    private static HashMap<String, AttributeHandleSet> publishedAttributes = new HashMap<>();
    private static HashMap<String, AttributeHandleSet> subscribedAttributes = new HashMap<>();
    private Boolean isPublished = false;
    private Boolean isSubscribed = false;
    private ObjectClassHandle thisClassHandle = null;
    private ObjectInstanceHandle thisObjectHandle;
    private HashMap<String, AttributeHolder> attributeMap = new HashMap<>();
    private Boolean isRegistered = false;
    protected EncoderFactory encoderFactory;
    
    /**
     * Base constructor for HLAobject elements. It will initialize the generic attribute support elements
     * and shall be called by all derived subclasses. As a good practice, a subclass shall add and initialize 
     * class specific attributes, by adding the appropriate DataElement encoders. 
     * 
     * @throws RprBuilderException
     */
    public HLAobjectRoot() throws RprBuilderException {
        if (rtiAmbassador == null) {
            rtiAmbassador = OmtBuilder.getRtiAmbassador();
            if (rtiAmbassador == null) throw new RprBuilderException("HLAobjectRoot not initialized"); 
        } 
        if (thisClassHandle == null) { 
            try {
                thisClassHandle = rtiAmbassador.getObjectClassHandle(getHlaClassName());
                if (knownAttributeHandles == null) { 
                    knownAttributeHandles = new HashMap<>();
                }
                if (!knownAttributeHandles.containsKey(thisClassHandle)) {
                    knownAttributeHandles.put(thisClassHandle, new HashMap<>());
                }
                thisObjectHandle = null; // undefined until object is registered
                // this.attributeValues = rtiAmbassador.getAttributeHandleValueMapFactory().create(0);
                encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
            } catch (Exception e) {
                throw new RprBuilderException("unhandled HLA exception", e);
            }
        }
        log.trace("created {} object created", getHlaClassName());
    }

    public ObjectClassHandle getClassHandle() throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
        return thisClassHandle;
    }

    /**
     * The clear method will reset the update status of all known attribute holders. Values encoded 
     * in the DataElements will be kept. 
     */
    public void clear() {
        log.trace("reset update status {} ", this.getObjectHandle());
        for (Entry<String, AttributeHolder> attribute: attributeMap.entrySet()) {
            attribute.getValue().isUpdated = false;
        }
    }

    /**
     * Generic decode method to iterate through an AttributeHandleValueMap, typically received by the RTI.
     * The AttributeValues with the ValueMap will be decoded with the DataElement associated to the attribute
     * handle. Attributes with unknown Handles will be skipped. 
     *  
     * @param theAttributes
     * @throws NameNotFound
     * @throws InvalidObjectClassHandle
     * @throws FederateNotExecutionMember
     * @throws NotConnected
     * @throws RTIinternalError
     * @throws DecoderException
     */
    public void decode(AttributeHandleValueMap theAttributes) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, DecoderException {
        log.trace("decoding class {} ", getHlaClassName());
        for (Entry<AttributeHandle, byte[]> entry : theAttributes.entrySet()) {
            AttributeHolder holder = attributeMap.get(getHandleString(entry.getKey()));
            if (holder == null) {
                log.warn("unknown attribute handle {}. Decoding skipped.", entry.getKey());
                continue;
            }
            holder.data.decode(entry.getValue());
            holder.isUpdated = true;
        }
    }

    /**
     * 
     * Add a given attribute name to the set of published attributes. This publication set
     * will be valid for all instances of the current object class.
     * @param attributeName
     * @throws NameNotFound
     * @throws InvalidObjectClassHandle
     * @throws FederateNotExecutionMember
     * @throws NotConnected
     * @throws RTIinternalError
     */
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

    /**
     * Add a given attribute name to the set of subscribed attributes. This subscription set
     * will be valid for all instances of the current object class.
     * 
     * @param attributeName
     * @throws NameNotFound
     * @throws InvalidObjectClassHandle
     * @throws FederateNotExecutionMember
     * @throws NotConnected
     * @throws RTIinternalError
     */
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

    /**
     * Get the list of subscribed attributes for the objects class of the current instance. 
     * 
     * @return
     * @throws FederateNotExecutionMember
     * @throws NotConnected
     */
    public AttributeHandleSet getSubscribedAttributes() throws FederateNotExecutionMember, NotConnected {
        AttributeHandleSet attr = subscribedAttributes.get(this.getClass().getSimpleName());
        if (attr == null) { 
            attr = rtiAmbassador.getAttributeHandleSetFactory().create();
            subscribedAttributes.put(this.getClass().getSimpleName(), attr);
        }
        return attr;        
    }
    
    /**
     * Overwrites a DataElement for a certain attribute with a new value. The attribute shall be given with its 
     * valid name and the DataElement shall an encoder with the correct type as defined in the OMT data model. 
     * 
     * @param attributeName
     * @param value
     * @throws NameNotFound
     * @throws InvalidObjectClassHandle
     * @throws FederateNotExecutionMember
     * @throws NotConnected
     * @throws RTIinternalError
     * @throws EncoderException
     */
    protected void setAttributeValue(String attributeName, DataElement value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        // attributeValues.put(getAttributeHandle(attributeName), value.toByteArray());    // keep attributeValues up to date
        AttributeHolder holder = attributeMap.get(attributeName);
        if (holder == null) {
            holder = new AttributeHolder(value);
        } else {
            holder.data = value;
            holder.isUpdated = true;
        }
        attributeMap.put(attributeName, holder);
        // attributeMap.put(attributeName, value);
        log.trace("set value {}->{} = {}", this, attributeName, value);
    }

    /**
     * Adds a DataElement for a certain attribute. The attribute shall be given with its valid name
     * and the DataElement shall an encoder with the correct type as defined in the OMT data model. 
     * 
     * @param name
     * @param data
     */
    public void addAttribute (String name, DataElement data) {
        attributeMap.put(name, new AttributeHolder(data));
    }

    /**
     * Returns the DataElement encoder element for a given attribute name. If the attribute name
     * is unknown, a null value will be returned.
     * 
     * @param name
     * @return
     */
    public DataElement getAttribute (String name) {
        AttributeHolder holder = attributeMap.get(name);
        if (holder != null) {
            return attributeMap.get(name).data;
        }
        return null;
    }

    /**
     * Test if a given attribute has been added to the object instance. 
     * 
     * @param name
     * @return
     */
    public Boolean hasAttribute (String name) {
        return attributeMap.containsKey(name);
    }

    /**
     * The getAttributeValues created a new AttributeHandleValueMap, containing the attributes
     * known by the object instance. 
     */
    public AttributeHandleValueMap getAttributeValues() throws FederateNotExecutionMember, NotConnected, NameNotFound, InvalidObjectClassHandle, RTIinternalError, EncoderException {
        int nbValues = 0;
        for (Entry<String, AttributeHolder> entry: attributeMap.entrySet()) {
            if (entry.getValue().isUpdated) {
                nbValues++;
            }
        }
        AttributeHandleValueMap attributeValues = rtiAmbassador.getAttributeHandleValueMapFactory().create(nbValues);
        for (Entry<String, AttributeHolder> entry: attributeMap.entrySet()) {
            if (entry.getValue().isUpdated) {
                attributeValues.put(getAttributeHandle(entry.getKey()), entry.getValue().data.toByteArray());
            }
        }
        return attributeValues;
    }

    /**
     * Test is a given attribute is known and has been updated. 
     *  
     * @param name
     * @return
     */
    public Boolean isUpdated (String name) {
        AttributeHolder holder = attributeMap.get(name);
        if ((holder != null) && (holder.isUpdated)) {
            return true;
        }
        return false;
    }


    /** 
     * The update method will send the attribute values of the current attribute instance to the 
     * RTI ambassador. 
     */
    public void update() throws AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, NameNotFound, InvalidObjectClassHandle, EncoderException {
        rtiAmbassador.updateAttributeValues(thisObjectHandle, getAttributeValues(), null);
        log.trace("update {}({}) to RTI", this, thisObjectHandle);
    }

    /**
     * The register method will make sure that all assigned attributes are published and will
     * register the current object instance with the RTI ambassador. 
     *  
     * @throws ObjectClassNotPublished
     * @throws ObjectClassNotDefined
     * @throws SaveInProgress
     * @throws RestoreInProgress
     * @throws FederateNotExecutionMember
     * @throws NotConnected
     * @throws RTIinternalError
     * @throws AttributeNotDefined
     */
    public void register() throws ObjectClassNotPublished, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, AttributeNotDefined {
        publish();
        if (!isRegistered) {
            thisObjectHandle = rtiAmbassador.registerObjectInstance(thisClassHandle);
            log.trace("register {}({}) as {}", this, thisClassHandle, thisObjectHandle);
            isRegistered = true;
        }
    }

    /** 
     * Assign a given object handle to the current object instance. This is required in case
     * the java object is created as mirror object of a remote HLA object.  
     */
    public void setObjectHandle(ObjectInstanceHandle newHandle) {
        thisObjectHandle = newHandle;
    }

    /**
     * Returns the known HLA object handle of the current instance. 
     * @return
     */     
    public ObjectInstanceHandle getObjectHandle() {
        return thisObjectHandle;
    }

    /**
     * Get class name in HLA-style as full path with '.' separators. 
     * 
     * @return
     */
    public String getHlaClassName() {
        return getHlaClassName("HLAobjectRoot");
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
        AttributeHandle handle = knownAttributeHandles.get(thisClassHandle).get(attributeName);
        if (handle == null) {
            handle = rtiAmbassador.getAttributeHandle(thisClassHandle, attributeName);
            knownAttributeHandles.get(thisClassHandle).put(attributeName, handle);
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
        for (Entry<String, AttributeHandle> entry : knownAttributeHandles.get(thisClassHandle).entrySet()) {
            if (handle.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
