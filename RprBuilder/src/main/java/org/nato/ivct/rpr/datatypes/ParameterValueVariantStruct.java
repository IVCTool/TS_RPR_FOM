/**    Copyright 2023,  brf (Fraunhofer IOSB)

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

import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.exceptions.RTIinternalError;

public class ParameterValueVariantStruct extends HLAfixedRecordStruct {
    
    /** 
     *  (see RPR-Base_v2.0.xml)
    */
    
    public enum AttributeName {
        ArticulatedPart,                       // datatype  ArticulatedPartsStruct
        AttachedPart                          // AttachedPartsStruct
    }

    public ParameterValueVariantStruct () throws RTIinternalError {
        super();
        
        // TODO  create ArticulatedPartsStruct
        add(AttributeName.ArticulatedPart.name(), new ArticulatedPartsStruct() );
        
        // ToDo create AttachedPartsStruct
        add(AttributeName.AttachedPart.name(), new AttachedPartsStruct() );
        
    }

    public ArticulatedPartsStruct getArticulatedPart() {
        return ( (ArticulatedPartsStruct) get(AttributeName.ArticulatedPart.name() ) );
    }
    
    public void setArticulatedPart(ArticulatedPartsStruct   articulatedPart) {
        set(AttributeName.ArticulatedPart.name(), articulatedPart);
    }
    
    public AttachedPartsStruct getAttachedPart() {
        return ( (AttachedPartsStruct) get(AttributeName.AttachedPart.name() ) );
    }
    
    public void setAttachedPart(AttachedPartsStruct   attachedPart) {
        set(AttributeName.AttachedPart.name(), attachedPart);
    }
}
