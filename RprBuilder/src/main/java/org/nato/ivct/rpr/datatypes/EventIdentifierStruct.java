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

import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.encoding.HLAASCIIstring;
import hla.rti1516e.exceptions.RTIinternalError;

public class EventIdentifierStruct extends HLAfixedRecordStruct {
    
    public enum AttributeName {
        EventCount,
        IssuingObjectIdentifier
    }


    public EventIdentifierStruct() throws RTIinternalError {
        super();
        add(AttributeName.EventCount.name(), encoderFactory.createHLAinteger16BE());
        add(AttributeName.IssuingObjectIdentifier.name(), encoderFactory.createHLAASCIIstring());
    }

    /** 
     * Identification of an event. Based on the Event Identifier record as specified in 
     * IEEE 1278.1-1995 section 5.2.18.
     */
    public short getEventCount() {
        return ((HLAinteger16BE) get(AttributeName.EventCount.name())).getValue();
    }
    public void setEventCount(short value) {
        ((HLAinteger16BE) get(AttributeName.EventCount.name())).setValue(value);
    }

    /**
     * Identification of the object issuing the event.
     */
    public String getIssuingObjectIdentifier() {
        return ((HLAASCIIstring) get(AttributeName.IssuingObjectIdentifier.name())).getValue();
    }
    public void setIssuingObjectIdentifier(String value) {
        ((HLAASCIIstring) get(AttributeName.IssuingObjectIdentifier.name())).setValue(value);
    }
}

