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

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.encoding.HLAvariantRecord;
import hla.rti1516e.exceptions.RTIinternalError;

public class HLAvariantRecordStruct<T extends DataElement> implements HLAvariantRecord<T> {

    DataElement discriminant;
    DataElement dataElement;

    @Override
    public int getOctetBoundary() {
        return dataElement.getOctetBoundary();
    }

    @Override
    public void encode(ByteWrapper byteWrapper) throws EncoderException {
        dataElement.encode(byteWrapper);        
    }

    @Override
    public int getEncodedLength() {
        return dataElement.getEncodedLength();
    }

    @Override
    public byte[] toByteArray() throws EncoderException {
        HLAvariantRecord<HLAoctet> value;
        try {
            EncoderFactory _encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
            HLAoctet spatialDiscriminant = _encoderFactory.createHLAoctet((byte)1);
            value = _encoderFactory.createHLAvariantRecord(spatialDiscriminant);
        } catch (RTIinternalError e) {
            throw new EncoderException(e.getLocalizedMessage());
        }        

        // TODO Auto-generated method stub
        return value.toByteArray();
    }

    @Override
    public void decode(ByteWrapper byteWrapper) throws DecoderException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void decode(byte[] bytes) throws DecoderException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setVariant(T discriminant, DataElement dataElement) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDiscriminant(T discriminant) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public T getDiscriminant() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DataElement getValue() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
