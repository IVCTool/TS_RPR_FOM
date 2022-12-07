package org.nato.rpr;

import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

public class PhysicalEntityBuilder extends HlaObjectBuilder {

    public static String className = "HLAobjectRoot.BaseEntity.PhysicalEntity";
    public static String AcousticSignatureIndex = "AcousticSignatureIndex";

    /**
     * Wrapper class for PhysicalEntity 
     */
    public class PhysicalEntity extends HlaObjectBuilder.HLAobject {

        private PhysicalEntity(
            ObjectClassHandle classHandle, 
            ObjectInstanceHandle objectHandle, 
            AttributeHandleValueMap attributes, 
            AttributeHandleSet attributeHandles) {
            super(classHandle, objectHandle, attributes, attributeHandles);
        }

        public void setAcousticSignatureIndex(short value) throws Exception {
            // AttributeHandle handle = null;
            // ByteWrapper v = attributeValues.getValueReference(handle);
            // v.setValue(value);

            HLAinteger16BE wrapper = encoderFactory.createHLAinteger16BE();            
            wrapper.setValue(value);
            DataElement dataElement = dataElements.get(AcousticSignatureIndex);
            if (dataElement == null) {
                throw new Exception("data element " + AcousticSignatureIndex + " not found");
            } else {
                ((HLAinteger16BE) dataElement).setValue(value);
            }
        }
    }


    public PhysicalEntityBuilder(RTIambassador rtiAmbassador)
            throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
        super(rtiAmbassador, className);
    }
    
    public PhysicalEntityBuilder addAcousticSignatureIndex () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addAttribute(AcousticSignatureIndex, encoderFactory.createHLAinteger16BE());
        return this;
    }

    public PhysicalEntity build() throws ObjectClassNotPublished, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, AttributeNotDefined {
        this.rtiAmbassador.publishObjectClassAttributes(classHandle, attributeHandles);
        ObjectInstanceHandle objectHandle = this.rtiAmbassador.registerObjectInstance(classHandle);
        return new PhysicalEntity(classHandle, objectHandle, attributeValues, attributeHandles);
    }
}
