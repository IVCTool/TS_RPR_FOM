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

/** 
 *  (see RPR-Base_v2.0.xml)
 *         
  </arrayData>
        <arrayData>
            <name>ArticulatedParameterStructLengthlessArray</name>
            <dataType>ArticulatedParameterStruct</dataType>
            <cardinality>Dynamic</cardinality>
            <encoding>RPRlengthlessArray</encoding>
            <semantics>Dynamic array of ArticulatedParameterStruct elements, may also contain no elements. The array is encoded without array length, containing only the elements.</semantics>
        </arrayData>  
*/

// TODO   it  is  more complicated to implement these datatypes see  SISO-STD-001-....pdf SS 31
// this class has to work in  another manner.

public class ArticulatedParameterStructLengthlessArray extends  ArticulatedParameterStruct { 
    
    public ArticulatedParameterStructLengthlessArray () throws RTIinternalError {
        super();      
        
       add(AttributeName.ArticulatedParameterChange.name(), encoderFactory.createHLAoctet() );
        
        //TODo  what is a UnsignedInteger16  ? , is HLAinteger16 correct for UnsignedInteger16 ?
        add(AttributeName.PartAttachedTo.name(), encoderFactory.createHLAinteger16BE() );
        
        // TODO   create ParameterValueVariantStruct and its .....
        add(AttributeName.ParameterValue.name(), new ParameterValueVariantStruct() );
        
    }
    
    
    
   
   
        
}
