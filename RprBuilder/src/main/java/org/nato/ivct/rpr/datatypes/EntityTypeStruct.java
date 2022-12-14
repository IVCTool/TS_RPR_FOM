/**        Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

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
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.exceptions.RTIinternalError;

public class EntityTypeStruct extends AttributeStruct{

    public enum AttributeName {
        EntityKind, 
        Domain,
        CountryCode,
        Category,
        Subcategory,
        Specific,
        Extra
    }

    public EntityTypeStruct (EncoderFactory encoderFactory) {
        add(AttributeName.EntityKind.name(), encoderFactory.createHLAoctet());
        add(AttributeName.Domain.name(), encoderFactory.createHLAoctet());
        add(AttributeName.CountryCode.name(), encoderFactory.createHLAinteger16BE());
        add(AttributeName.Category.name(), encoderFactory.createHLAoctet());
        add(AttributeName.Subcategory.name(), encoderFactory.createHLAoctet());
        add(AttributeName.Specific.name(), encoderFactory.createHLAoctet());
        add(AttributeName.Extra.name(), encoderFactory.createHLAoctet());

    }

      
    public void decode (byte[] data) throws DecoderException, RTIinternalError {
        HLAfixedRecord record = RtiFactoryFactory.getRtiFactory().getEncoderFactory().createHLAfixedRecord();
        // record.decode(data);
        // byte[] _b = record.get(0).toByteArray();
        // DataElement _e = get(AttributeName.EntityKind.name());
        // _e.decode(_b);

        ByteWrapper bw = new ByteWrapper(data);

        (get(AttributeName.EntityKind.name())).decode(bw);
        (get(AttributeName.Domain.name())).decode(bw);
        (get(AttributeName.CountryCode.name())).decode(bw);
        (get(AttributeName.Category.name())).decode(bw);
        (get(AttributeName.Subcategory.name())).decode(bw);
        (get(AttributeName.Specific.name())).decode(bw);
        (get(AttributeName.Extra.name())).decode(bw);

        // (get(AttributeName.EntityKind.name())).decode(record.get(0).toByteArray());
        // (get(AttributeName.Domain.name())).decode(record.get(1).toByteArray());
        // (get(AttributeName.CountryCode.name())).decode(record.get(2).toByteArray());
        // (get(AttributeName.Category.name())).decode(record.get(3).toByteArray());
        // (get(AttributeName.Subcategory.name())).decode(record.get(4).toByteArray());
        // (get(AttributeName.Specific.name())).decode(record.get(5).toByteArray());
        // (get(AttributeName.Extra.name())).decode(record.get(6).toByteArray());
    }


    public byte getEntityKind() {
        return ((HLAoctet) get(AttributeName.EntityKind.name())).getValue();
    }

    public void setEntityKind(byte EntityKind) {
        ((HLAoctet) get(AttributeName.EntityKind.name())).setValue(EntityKind);
    }

    public byte getDomain() {
        return ((HLAoctet) get(AttributeName.Domain.name())).getValue();
    }

    public void setDomain(byte Domain) {
        ((HLAoctet) get(AttributeName.Domain.name())).setValue(Domain);
    }

    public short getCountryCode() {
        return ((HLAinteger16BE) get(AttributeName.CountryCode.name())).getValue();
    }

    public void setCountryCode(short CountryCode) {
        ((HLAinteger16BE) get(AttributeName.CountryCode.name())).setValue(CountryCode);
    }

    public byte getCategory() {
        return ((HLAoctet) get(AttributeName.Category.name())).getValue();
    }

    public void setCategory(byte Category) {
        ((HLAoctet) get(AttributeName.Category.name())).setValue(Category);
    }

    public byte getSubcategory() {
        return ((HLAoctet) get(AttributeName.Subcategory.name())).getValue();
    }

    public void setSubcategory(byte Subcategory) {
        ((HLAoctet) get(AttributeName.Subcategory.name())).setValue(Subcategory);
    }

    public byte getSpecific() {
        return ((HLAoctet) get(AttributeName.Specific.name())).getValue();
    }

    public void setSpecific(byte Specific) {
        ((HLAoctet) get(AttributeName.Specific.name())).setValue(Specific);
    }

    public byte getExtra() {
        return ((HLAoctet) get(AttributeName.Extra.name())).getValue();
    }

    public void setExtra(byte Extra) {
        ((HLAoctet) get(AttributeName.Extra.name())).setValue(Extra);
    }
        
}
