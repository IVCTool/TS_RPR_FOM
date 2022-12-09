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

import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAvariantRecord;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public class BaseEntity extends HLAobjectRoot {

    @Override
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
    private HLAfixedRecord aIsPartOf = null;
    private HLAvariantRecord aSpatial = null;
    private HLAvariantRecord aRelativeSpatical = null;


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
