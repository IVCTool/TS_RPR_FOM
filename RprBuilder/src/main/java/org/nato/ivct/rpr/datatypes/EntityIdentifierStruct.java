/**    Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

Licensed under the Apache License, Version 2.0 (the "License")
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http: //www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package org.nato.ivct.rpr.datatypes;

import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.exceptions.RTIinternalError;

/**
 * Unique, exercise-wide identification of the entity, or a symbolic group address referencing
 * multiple entities or a simulation application. Based on the Entity Identifier record as specified
 * in IEEE 1278.1-1995 section 5.2.14.
 */
public class EntityIdentifierStruct extends HLAfixedRecordStruct {

    enum AttributeName {
        /** Simulation application (federate) identifier. */
        FederateIdentifier,

        /**
         * Each entity in a given simulation application shall be given an entity identifier number unique
         * to all other entities in that application. This identifier number is valid for the duration of
         * the exercise; however, entity identifier numbers may be reused when all possible numbers have
         * been exhausted. No entity shall have an entity identifier number of NO_ENTITY (0), ALL_ENTITIES
         * (0xFFFF), or RQST_ASSIGN_ID (0xFFFE). The entity identifier number need not be registered or
         * retained for future exercises. An entity identifier number equal to zero with valid site and
         * application identification shall address a simulation application. An entity identifier number
         * equal to ALL_ENTITIES shall mean all entities within the specified site and application. An
         * entity identifier number equal to RQST_ASSIGN_ID allows the receiver of the CreateEntity
         * interaction to define the entity identifier number of the new entity. The new entity will
         * specify its entity identifier number in the Acknowledge interaction.
         */
        EntityNumber
    }

    // public void decode (byte[] data) throws DecoderException {
    //     ByteWrapper bw = new ByteWrapper(data);
    //     (get(AttributeName.FederateIdentifier.name())).decode(bw);
    //     (get(AttributeName.EntityNumber.name())).decode(bw);
    // }
    
    public EntityIdentifierStruct () throws RTIinternalError {
        super();
        add(AttributeName.FederateIdentifier.name(), new FederateIdentifierStruct());
        add(AttributeName.EntityNumber.name(), encoderFactory.createHLAinteger16BE());
    }

    public FederateIdentifierStruct getFederateIdentifier() {
        return ((FederateIdentifierStruct) get(AttributeName.FederateIdentifier.name()));
    }

    public void setFederateIdentifier(FederateIdentifierStruct FederateIdentifier) {
        add(AttributeName.FederateIdentifier.name(), FederateIdentifier);
    }

    public short getEntityNumber() {
        return ((HLAinteger16BE) get(AttributeName.EntityNumber.name())).getValue();
    }

    public void setEntityNumber(short EntityNumber) {
        ((HLAinteger16BE) get(AttributeName.EntityNumber.name())).setValue(EntityNumber);
    }
}
