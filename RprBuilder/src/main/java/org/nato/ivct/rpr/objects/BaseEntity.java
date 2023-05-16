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

import org.nato.ivct.rpr.RprBuilderException;
import org.nato.ivct.rpr.datatypes.EntityIdentifierStruct;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
import org.nato.ivct.rpr.datatypes.SpatialVariantStruct;

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
    
    
    public BaseEntity() throws RprBuilderException {
        super();
        try {
            // initialize attribute holders by calling the getter for each member attribute
            getEntityType();
            getEntityIdentifier();
            getIsPartOf();
            getSpatial();
            getRelativeSpatical();
        } catch (NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected
                | RTIinternalError e) {
            throw new RprBuilderException(e.getMessage());
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
        setAttributeValue(Attributes.EntityType.name(), value);
    }
    public EntityTypeStruct getEntityType() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        EntityTypeStruct aEntityType = (EntityTypeStruct) getAttribute(Attributes.EntityType.name());
        if (aEntityType == null) {
            aEntityType = new EntityTypeStruct();
            setEntityType(aEntityType);
        }
        return (aEntityType);
    }

    public void setEntityIdentifier(EntityIdentifierStruct value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        setAttributeValue(Attributes.EntityIdentifier.name(), value);
    }
    public EntityIdentifierStruct getEntityIdentifier() throws RTIinternalError, NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, EncoderException {
        EntityIdentifierStruct aEntityIdentifier = (EntityIdentifierStruct) getAttribute(Attributes.EntityIdentifier.name());
        if (aEntityIdentifier == null) {
            aEntityIdentifier = new EntityIdentifierStruct();
            setEntityIdentifier(aEntityIdentifier);
        }
        return aEntityIdentifier;
    }

    public void setIsPartOf (HLAfixedRecord value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        setAttributeValue (Attributes.IsPartOf.name(), value);
    }
    public HLAfixedRecord getIsPartOf() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAfixedRecord aIsPartOf = (HLAfixedRecord) getAttribute(Attributes.IsPartOf.name());
        if (aIsPartOf == null) {
            aIsPartOf = encoderFactory.createHLAfixedRecord();
            aIsPartOf.add(encoderFactory.createHLAoctet());       // EntityKind
            aIsPartOf.add(encoderFactory.createHLAoctet());       // Domain
            aIsPartOf.add(encoderFactory.createHLAinteger16BE()); // CountryCode
            aIsPartOf.add(encoderFactory.createHLAoctet());       // Category
            aIsPartOf.add(encoderFactory.createHLAoctet());       // Subcategory
            aIsPartOf.add(encoderFactory.createHLAoctet());       // Specific
            aIsPartOf.add(encoderFactory.createHLAoctet());       // Extra
            setIsPartOf(aIsPartOf);
        }
        return aIsPartOf;
    }

    public void setSpatial (SpatialVariantStruct value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        setAttributeValue (Attributes.Spatial.name(), value);
    }
    public SpatialVariantStruct getSpatial() throws RTIinternalError, NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, EncoderException {
        SpatialVariantStruct aSpatial = (SpatialVariantStruct) getAttribute(Attributes.Spatial.name());
        if (aSpatial == null) {
            aSpatial = new SpatialVariantStruct();
            setSpatial(aSpatial);
        }
        return aSpatial;
    }

    public void setRelativeSpatical (HLAvariantRecord value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        setAttributeValue (Attributes.RelativeSpatial.name(), value);
    }
    public HLAvariantRecord getRelativeSpatical () {
        HLAvariantRecord aRelativeSpatical = (HLAvariantRecord) getAttribute(Attributes.RelativeSpatial.name());
        if (aRelativeSpatical == null) {
            HLAoctet spatialDiscriminant = encoderFactory.createHLAoctet((byte) 1);
            aRelativeSpatical = encoderFactory.createHLAvariantRecord(spatialDiscriminant);
        }
        return aRelativeSpatical;
    }


}
