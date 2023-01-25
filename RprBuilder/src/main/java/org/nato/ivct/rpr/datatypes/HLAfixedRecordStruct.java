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

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.exceptions.RTIinternalError;

public class HLAfixedRecordStruct implements DataElement {

    private ArrayList<NamedDataElement> data = new ArrayList<>();
    private HLAfixedRecord valueMap;
    EncoderFactory encoderFactory;

    class NamedDataElement {
        String name;
        DataElement element;
        public NamedDataElement(String n, DataElement e) {
            name = n;
            element = e;
        }    
    }     

    public HLAfixedRecordStruct () throws RTIinternalError {
        encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
    }

    public HLAfixedRecordStruct add(String name, DataElement element) {
        data.add(new NamedDataElement(name, element));
        return this;
    }

    public DataElement get(String name) {
        for (NamedDataElement d: data) {
            if (name.equals(d.name)) return d.element;
        }
        return null;
    }

    public void set (String name, DataElement element) throws Exception {
        for (NamedDataElement d: data) {
            if (name.equals(d.name)) {
                d.element = element;
                valueMap = getDataElement();
                return;
            }
        }
        throw new Exception("value " + name + " not found");
    }

    public HLAfixedRecord getDataElement() {
        valueMap = encoderFactory.createHLAfixedRecord();
        for (NamedDataElement d: data) {
            valueMap.add(d.element);
        }
        return valueMap;
    }


    @Override
    public byte[] toByteArray() throws EncoderException {
        HLAfixedRecord valueMap;
        valueMap = encoderFactory.createHLAfixedRecord();
        for (NamedDataElement d: data) {
            valueMap.add(d.element);
        }
        return valueMap.toByteArray();
    }

    @Override
    public int getOctetBoundary() {
        return getDataElement().getOctetBoundary();
    }

    @Override
    public void encode(ByteWrapper byteWrapper) throws EncoderException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getEncodedLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void decode(ByteWrapper byteWrapper) throws DecoderException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void decode(byte[] bytes) throws DecoderException {
        // TODO Auto-generated method stub
        
    }
}
