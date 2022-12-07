package org.nato.rpr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger16BE;
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

public class HlaObjectBuilder {

    public class HLAobject {
        private final ObjectClassHandle objectClassHandle;       public ObjectClassHandle getObjectClassHandle()     { return objectClassHandle; }
        private final ObjectInstanceHandle objectInstanceHandle;   public ObjectInstanceHandle getObjectInstanceHandle() { return objectInstanceHandle; }
        private final AttributeHandleValueMap attributeValues;  public AttributeHandleValueMap getAttributs() { return attributeValues; }
        private final AttributeHandleSet attributeHandles;

        protected HLAobject(ObjectClassHandle classHandle, ObjectInstanceHandle objectHandle, AttributeHandleValueMap attributes, AttributeHandleSet attributeHandles) {
            this.objectClassHandle = classHandle;
            this.objectInstanceHandle = objectHandle;
            this.attributeValues = attributes;
            this.attributeHandles = attributeHandles;
        }

        public void clear() {
            attributeValues.clear();
        }

        // public void setAttribute(String keyName, DataElement value) throws Exception {
        //     AttributeHandle handle = attributeHandles.get(keyName);
        //     if (handle == null) {
        //         throw new Exception("attribute key " + keyName + " not found");
        //     }
        //     attributeValues.put(handle, value.toByteArray());
        // }
    
        public void send (RTIambassador rtiAmbassador) throws AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
            rtiAmbassador.updateAttributeValues(objectInstanceHandle, attributeValues, null);
        }
    }


    protected RTIambassador rtiAmbassador;
    protected ObjectClassHandle classHandle;
    protected AttributeHandleValueMap attributeValues;
    protected AttributeHandleSet attributeHandles;
    protected EncoderFactory encoderFactory;
    // protected HashMap<String,AttributeHandle> attributeHandles;
    // protected HashMap<String,ObjectClassHandle> dataHandles;
    protected HashMap<String,DataElement> dataElements;

    public HlaObjectBuilder (RTIambassador rtiAmbassador, String className) throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
        this.rtiAmbassador = rtiAmbassador;
        this.classHandle = rtiAmbassador.getObjectClassHandle(className);
        this.attributeValues = rtiAmbassador.getAttributeHandleValueMapFactory().create(0);
        this.attributeHandles = rtiAmbassador.getAttributeHandleSetFactory().create();
        // this.attributeHandles = new HashMap<>();
        this.encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
        this.dataElements = new HashMap<>();
    }

    public HlaObjectBuilder addAttribute (String attributeName, HLAinteger16BE value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        AttributeHandle handle = this.rtiAmbassador.getAttributeHandle(this.classHandle, attributeName);
        attributeHandles.add(handle);
        attributeValues.put(handle, value.toByteArray());
        dataElements.put(attributeName, value);
        return this;
    }

    public HLAobject build() throws ObjectClassNotPublished, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, AttributeNotDefined {
        this.rtiAmbassador.publishObjectClassAttributes(classHandle, attributeHandles);
        ObjectInstanceHandle objectHandle = this.rtiAmbassador.registerObjectInstance(classHandle);
        return new HLAobject(classHandle, objectHandle, attributeValues, attributeHandles);
    }
}
