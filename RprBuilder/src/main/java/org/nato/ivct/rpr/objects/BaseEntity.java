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

import java.util.Map.Entry;

import org.nato.ivct.rpr.RprBuilderException;
import org.nato.ivct.rpr.datatypes.EntityIdentifierStruct;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
import org.nato.ivct.rpr.datatypes.SpatialVariantStruct;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.encoding.HLAvariantRecord;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public class BaseEntity extends HLAobjectRoot {

    public enum Attributes {
        EntityType,
        EntityIdentifier,
        IsPartOf,
        Spatial,
        RelativeSpatial
    }
    
    private EntityTypeStruct aEntityType = null;
    private EntityIdentifierStruct aEntityIdentifier = null;
    private SpatialVariantStruct aSpatial = null;
    // following are the not-yet typed attributes
    private HLAfixedRecord aIsPartOf = null;
    private HLAvariantRecord<HLAoctet> aRelativeSpatical = null;
    
    public BaseEntity() throws RprBuilderException {
        super();
    }
    
    public void decode(AttributeHandleValueMap theAttributes) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, DecoderException {
        super.decode(theAttributes);
        for (Entry<AttributeHandle, byte[]> entry : theAttributes.entrySet()) {
            AttributeHandle attributeHandle = entry.getKey();
            Attributes attribute = Attributes.valueOf(getHandleString(attributeHandle));
            switch (attribute) {
                case EntityType:
                    getEntityType().decode(entry.getValue());
                    break;
                case EntityIdentifier:
                    getEntityIdentifier().decode(entry.getValue());
                    break;
                case IsPartOf:
                    getIsPartOf().decode(entry.getValue());
                    break;
                case Spatial:
                    getSpatial().decode(entry.getValue());
                    break;
                case RelativeSpatial:
                    getRelativeSpatical().decode(entry.getValue());
                    break;
                default:
                    throw new NameNotFound(attribute.name());
            }
        }
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

    /*
     * setter and getter for BaseEntity attributes
     */
    public void setEntityType (EntityTypeStruct value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        aEntityType = value;
        setAttributeValue(Attributes.EntityType.name(), aEntityType.getDataElement());
    }
    public EntityTypeStruct getEntityType() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        if (aEntityType == null) {
            aEntityType = new EntityTypeStruct();
        }
        return aEntityType;
    }
    public Boolean isSetEntityType() {
        return (aEntityType != null);
    }

    public void setEntityIdentifier(EntityIdentifierStruct value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        aEntityIdentifier = value;
        setAttributeValue(Attributes.EntityIdentifier.name(), value.getDataElement());
    }
    public EntityIdentifierStruct getEntityIdentifier() throws RTIinternalError, NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, EncoderException {
        if (aEntityIdentifier == null) {
            aEntityIdentifier = new EntityIdentifierStruct();
            setAttributeValue(Attributes.EntityIdentifier.name(), aEntityIdentifier);
        }
        return aEntityIdentifier;
    }
    public Boolean isSetEntityIdentifier() {
        return (aEntityIdentifier != null);
    }

    public void setIsPartOf (HLAfixedRecord value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        setAttributeValue (Attributes.IsPartOf.name(), value);
    }
    public HLAfixedRecord getIsPartOf() {
        if (aIsPartOf == null) {
            aIsPartOf = encoderFactory.createHLAfixedRecord();
            aIsPartOf.add(encoderFactory.createHLAoctet());       // EntityKind
            aIsPartOf.add(encoderFactory.createHLAoctet());       // Domain
            aIsPartOf.add(encoderFactory.createHLAinteger16BE()); // CountryCode
            aIsPartOf.add(encoderFactory.createHLAoctet());       // Category
            aIsPartOf.add(encoderFactory.createHLAoctet());       // Subcategory
            aIsPartOf.add(encoderFactory.createHLAoctet());       // Specific
            aIsPartOf.add(encoderFactory.createHLAoctet());       // Extra
        }
        return aIsPartOf;
    }
    public Boolean isSetIsPartOf() {
        return (aIsPartOf != null);
    }

    public void setSpatial (SpatialVariantStruct value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        setAttributeValue (Attributes.Spatial.name(), value);
    }
    public SpatialVariantStruct getSpatial() throws RTIinternalError {
        if (aSpatial == null) {
            aSpatial = new SpatialVariantStruct();
        }
        return aSpatial;
    }
    public Boolean isSetSpatial() {
        return (aSpatial != null);
    }

    public void setRelativeSpatical (HLAvariantRecord value) {
        aRelativeSpatical = value;
    }
    public HLAvariantRecord getRelativeSpatical () {
        if (aRelativeSpatical == null) {
            HLAoctet spatialDiscriminant = encoderFactory.createHLAoctet((byte) 1);
            aRelativeSpatical = encoderFactory.createHLAvariantRecord(spatialDiscriminant);
        }
        return aRelativeSpatical;
    }
    public Boolean isSetRelativeSpatical() {
        return (aRelativeSpatical != null);
    }


}
