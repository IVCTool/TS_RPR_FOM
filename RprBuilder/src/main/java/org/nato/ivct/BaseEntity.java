package org.nato.ivct;

import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public class BaseEntity extends HLAobjectRoot {

    public String getClassName() { return "HLAobjectRoot.BaseEntity"; }

    public enum Attributes {
        EntityType,
        EntityIdentifier,
        IsPartOf,
        Spatial,
        RelativeSpatial
    }

    private HLAfixedRecord aEntityType = null;
    private HLAfixedRecord aEntityIdentifier = null;
    // TODO aIsPartOf
    // TODO aSpatial
    // TODO aRelativeSpatical


    public BaseEntity() throws Exception {
        super();
    }
    
    public void addSubscribe(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }
    public void addPublish(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(attribute.name()); 
    }

    
    // the following methods are providing an alternate implementation approach, 
    // by adding explicit methods for each attribute. 
    //
    public void subscribeEntityType() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addSubAttribute(Attributes.EntityType.name()); 
    }
    public void publishEntityType() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(Attributes.EntityType.name()); 
    }
    public void subscribeEntityIdentifier() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addSubAttribute(Attributes.EntityIdentifier.name()); 
    }
    public void publishEntityIdentifier() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(Attributes.EntityIdentifier.name()); 
    }
    public void subscribeIsPartOf() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addSubAttribute(Attributes.IsPartOf.name()); 
    }
    public void publishIsPartOf() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(Attributes.IsPartOf.name()); 
    }
    public void subscribeSpatial() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addSubAttribute(Attributes.Spatial.name()); 
    }
    public void publishSpatial() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(Attributes.Spatial.name()); 
    }
    public void subscribeRelativeSpatial() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addSubAttribute(Attributes.RelativeSpatial.name()); 
    }
    public void publishRelativeSpatial() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(Attributes.RelativeSpatial.name()); 
    }

    public void setEntityType (HLAfixedRecord value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        setAttributeValue (Attributes.EntityType.name(), value);
    }
    public HLAfixedRecord getEntityType() {
        if (aEntityType == null) {
            aEntityType = encoderFactory.createHLAfixedRecord();
            aEntityType.add(encoderFactory.createHLAoctet());       // EntityKind
            aEntityType.add(encoderFactory.createHLAoctet());       // Domain
            aEntityType.add(encoderFactory.createHLAinteger16BE()); // CountryCode
            aEntityType.add(encoderFactory.createHLAoctet());       // Category
            aEntityType.add(encoderFactory.createHLAoctet());       // Subcategory
            aEntityType.add(encoderFactory.createHLAoctet());       // Specific
            aEntityType.add(encoderFactory.createHLAoctet());       // Extra
        }
        return aEntityType;
    }

    public void setEntityIdentifier(HLAfixedRecord value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        setAttributeValue(Attributes.EntityIdentifier.name(), value);
    }
    public HLAfixedRecord getEntityIdentifier() {
        if (aEntityIdentifier == null) {
            aEntityIdentifier = encoderFactory.createHLAfixedRecord(); 
            aEntityIdentifier.add(encoderFactory.createHLAfixedRecord()); // Federate Identifier
            aEntityIdentifier.add(encoderFactory.createHLAoctet());       // EntityNumber
        }
        return aEntityIdentifier;
    }
}
