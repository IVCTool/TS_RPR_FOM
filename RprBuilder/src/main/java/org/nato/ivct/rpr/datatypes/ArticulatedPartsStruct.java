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

public class ArticulatedPartsStruct extends HLAfixedRecordStruct { 
    
    /** 
     *  (see RPR-Base_v2.0.xml)
    */

    enum AttributeName {
        Class,                   //  ArticulatedPartsTypeEnum32        -> RPR-Enumerations_v2.0.xml
        TypeMetric           //  ArticulatedTypeMetricEnum32        -> RPR-Enumerations_v2.0.xml
    }

    public ArticulatedPartsStruct () throws RTIinternalError {
        super();
        
        // TODO how to use Enums here  ArticulatedPartsTypeEnum32
        add(AttributeName.Class.name(),  ArticulatedPartsTypeEnum32. );  
        
        // TODO how to use Enums here
        add( (AttributeName.TypeMetric.name(), ArticulatedTypeMetricEnum32.values()    )  ; 
       
       
    }
    
    
    
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
