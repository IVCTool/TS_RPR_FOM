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

import java.util.ArrayList;
import java.util.Iterator;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.exceptions.RTIinternalError;

public class AttributeStruct {

    class NamedDataElement {
        String name;
        AttributeHandle handle;
        DataElement element;
        public NamedDataElement(String n, DataElement e) {
            name = n;
            element = e;
        }
    } 

    private static RTIambassador rtiAmbassador;
    ArrayList<NamedDataElement> data = new ArrayList<>();

    public static void initialize(RTIambassador rtiAmbassador2Use) {
        rtiAmbassador = rtiAmbassador2Use;
    }

    public AttributeStruct add(String name, DataElement element) {
        data.add(new NamedDataElement(name, element));
        return this;
    }

    public DataElement get(String name) {
        for (NamedDataElement d: data) {
            if (name.equalsIgnoreCase(d.name)) return d.element;
        }
        return null;
    }

    public HLAfixedRecord encode() throws RTIinternalError {
        HLAfixedRecord valueMap = RtiFactoryFactory.getRtiFactory().getEncoderFactory().createHLAfixedRecord();
        for (NamedDataElement d: data) {
            valueMap.add(d.element);
        }
        return valueMap;
    }

    public void decode (byte[] data) throws DecoderException, RTIinternalError {
        HLAfixedRecord record = RtiFactoryFactory.getRtiFactory().getEncoderFactory().createHLAfixedRecord();
        record.decode(data);
    }

    public void decodeHandleValueMap(AttributeHandleValueMap valueMap) throws Exception {
        data.clear();
        for (Iterator<AttributeHandle> i = valueMap.keySet().iterator(); i.hasNext();) {
            AttributeHandle recvHandle = i.next();
            DataElement decoder = getDataElement (recvHandle);
            decoder.decode(valueMap.get(recvHandle));
        }
    }

    private DataElement getDataElement(AttributeHandle recvHandle) throws Exception {
        for (NamedDataElement d: data) {
            if (d.handle.equals(recvHandle)) return d.element;
        }
        throw new Exception("unknown attribute handle");
    }
}
