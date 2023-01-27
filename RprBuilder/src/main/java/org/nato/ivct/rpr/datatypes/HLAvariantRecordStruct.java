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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAvariantRecord;
import hla.rti1516e.exceptions.RTIinternalError;

public class HLAvariantRecordStruct<T extends DataElement> implements DataElement {
    public static final Logger log = LoggerFactory.getLogger(HLAvariantRecordStruct.class);

    protected T discriminant = null;
    protected DataElement dataElement = null;
    protected EncoderFactory encoderFactory;


    public HLAvariantRecordStruct() throws RTIinternalError {
        encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
    }
    
    public void setVariant(T aDiscriminant, DataElement aDataElement) {
        if (this.discriminant == null) {
            log.trace("setVariant(" + aDiscriminant + ", " + aDataElement.getClass() + ")"); 
        } else if (this.discriminant.equals(aDiscriminant)) {
            log.warn("setVariant(" + aDiscriminant + ", " + aDataElement.getClass() +") reset with (" + this.discriminant + ", " + aDataElement.getClass()); 
        } else {
            log.warn("setVariant(" + aDiscriminant + ", " + aDataElement.getClass() +") overwriting with (" + this.discriminant + ", " + aDataElement.getClass()); 
        }
        this.discriminant = aDiscriminant;
        this.dataElement = aDataElement;
    }

    public T getDiscriminant() {
        return discriminant;
    }

    public HLAvariantRecord<T> getDataElement() {
        HLAvariantRecord<T> value = encoderFactory.createHLAvariantRecord(discriminant);
        value.setVariant(discriminant, dataElement);
        return value;
    }


    /**
     * Inherited methods from DataElement
     */

    @Override
    public int getOctetBoundary() {
        return getDataElement().getOctetBoundary();
    }

    @Override
    public void encode(ByteWrapper byteWrapper) throws EncoderException {
        log.warn("decode(byte[] bytes) not tested");        
        getDataElement().encode(byteWrapper);        
    }

    @Override
    public int getEncodedLength() {
        log.warn("decode(byte[] bytes) not tested");        
        return getDataElement().getEncodedLength();
    }

    @Override
    public byte[] toByteArray() throws EncoderException {
        HLAvariantRecord<T> value;
        value = encoderFactory.createHLAvariantRecord(discriminant);
        value.setVariant(discriminant, dataElement);
        return value.toByteArray();
    }

    @Override
    public void decode(ByteWrapper byteWrapper) throws DecoderException {
        log.warn("decode(ByteWrapper byteWrapper) not tested");       
        HLAvariantRecord<T> value = encoderFactory.createHLAvariantRecord(discriminant);
        value.decode(byteWrapper);
        setVariant(value.getDiscriminant(), value.getValue());
    }

    @Override
    public void decode(byte[] bytes) throws DecoderException {
        log.warn("decode(byte[] bytes) not tested");
        HLAvariantRecord<T> value = encoderFactory.createHLAvariantRecord(discriminant);
        setVariant(value.getDiscriminant(), value.getValue());
    }

}
