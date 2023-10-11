/**        Copyright 2023 brf (Fraunhofer IOSB)

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

import org.nato.ivct.rpr.datatypes.EntityIdentifierStruct.AttributeName;

import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.exceptions.RTIinternalError;

public class ArticulatedParameterStruct extends HLAfixedRecordStruct { 
    
    /** 
     *  (see RPR-Base_v2.0.xml)
     <fixedRecordData>
                <name>ArticulatedParameterStruct</name>
                <encoding>HLAfixedRecord</encoding>
                <semantics>Structure to specify a movable or attached part of an entity. 
                Based on the Articulation Parameter record as specified in IEEE 1278.1-1995 section 5.2.5.               
                Note that usage of this datatype for the PhyscialEntity object class attribute ArticulatedParametersArray
                and MunitionDetonation interaction class parameter ArticulatedPartData shall be in accordance
                 with IEEE 1278.1-1995 Annex A.</semantics>
                <field>
                    <name>ArticulatedParameterChange</name>
                    <dataType>Octet</dataType>
                    <semantics>Indicator of a change to the part. This field shall be set to zero for each exercise and sequentially incremented by one for each change in articulation parameters. In the case where all possible values are exhausted, the numbers shall be reused beginning at zero.</semantics>
                </field>
                <field>
                    <name>PartAttachedTo</name>
                    <dataType>UnsignedInteger16</dataType>
                    <semantics>Identification of the articulated part to which this articulation parameter is attached. This field shall contain the value zero if the articulated part is attached directly to the entity.</semantics>
                </field>
                <field>
                    <name>ParameterValue</name>
                    <dataType>ParameterValueVariantStruct</dataType>
                    <semantics>Details of the parameter: whether it is an articulated or an attached part, and its type and value.</semantics>
                </field>
            </fixedRecordData>
    
     
    */

    enum AttributeName {
        ArticulatedParameterChange,     // Octet
        PartAttachedTo,                        // UnsignedInteger16
        ParameterValue                      // ParameterValueVariantStruct        
    }

    public ArticulatedParameterStruct () throws RTIinternalError {
        super();
        add(AttributeName.ArticulatedParameterChange.name(), encoderFactory.createHLAoctet() );
        
        //TODo  what is a UnsignedInteger16  ? , is HLAinteger16 correct for UnsignedInteger16 ?
        add(AttributeName.PartAttachedTo.name(), encoderFactory.createHLAinteger16BE() );
        
        // TODO   create ParameterValueVariantStruct and its .....
        add(AttributeName.ParameterValue.name(), new ParameterValueVariantStruct() );
    }    
    
    // getter and setter
    public byte getArticulatedParameterChange() {
        return ((HLAoctet) get(AttributeName.ArticulatedParameterChange.name())).getValue();
    }
    
    public void setArticulatedParameterChange(byte parameterChange) {
        ((HLAoctet) get(AttributeName.ArticulatedParameterChange.name())).setValue(parameterChange);
    }

    public short getPartAttachedTo() {
        return ((HLAinteger16BE) get(AttributeName.PartAttachedTo.name())).getValue();
    }
    
    public void setPartAttachedTo(short partAttachedTo) {
        ((HLAinteger16BE) get(AttributeName.PartAttachedTo.name())).setValue(partAttachedTo);
    } 
    
    public ParameterValueVariantStruct getParameterValue() {
        return ((ParameterValueVariantStruct) get(AttributeName.ParameterValue.name()));
    }
    
    public void setParameterValue (ParameterValueVariantStruct parameterValue) {
        add(AttributeName.ParameterValue.name(), parameterValue);
    }    
        
}
