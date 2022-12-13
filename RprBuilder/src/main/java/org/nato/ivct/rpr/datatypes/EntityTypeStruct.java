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

import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.encoding.HLAoctet;

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
