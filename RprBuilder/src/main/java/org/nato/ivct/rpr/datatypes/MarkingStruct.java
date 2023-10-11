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

import hla.rti1516e.encoding.HLAfixedArray;
import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.exceptions.RTIinternalError;

public class MarkingStruct extends HLAfixedRecordStruct { 
    
    /** 
     *  (see   RPR-Physical_v2.0.xml )    definition     MarkingStruct see below 
     
   
              MarkingArray11 see   
              
               SISO-STD-001***.pdf   6.8.1.14   ArrayDatatypes  :
               an example Datatype is  "EntityTypeStructLengthlessArray" another OctetArray8
               Datatype Marking Struct ?????
               
               RPR-Physical_v2.0.xml : 
               <arrayData>
                <name>MarkingArray11</name>
                <dataType>Octet</dataType>
                <cardinality>11</cardinality>
                <encoding>HLAfixedArray</encoding>
                <semantics>String of characters represented by an 11 element character string.</semantics>
            </arrayData>
               
            
    */

    enum AttributeName {
        MarkingEncodingType,               // MarkingEncodingEnum8
        MarkingData                        //   MarkingArray11
    }

    public MarkingStruct () throws RTIinternalError {
        super();
        //add(AttributeName.MarkingEncodingType.name(), encoderFactory.createHLAoctet() );
        add(AttributeName.MarkingEncodingType.name(),  MarkingEncodingEnum8.Other.getDataElement() );  
        
        
         // TODO   create MarkingArray11  and its .....  
        //add(AttributeName.MarkingData.name(), new MarkingArray11()   );
        
    }    
    
    // getter and setter
    
    public HLAoctet getMarkingEncodingType() {                // TODO  in this Way  ?????  or incorrect
        return (HLAoctet) get(AttributeName.MarkingEncodingType.name()  );
   }
   public void setMarkingEncodingType(byte  markEncType ) {
        (  (HLAoctet) get(AttributeName.MarkingEncodingType.name() ) ).setValue(markEncType );   
   } 
    
    
    // TODO    MarkingData      //   MarkingArray11 is not correct defined
    public HLAoctet getMarkingData() {
        return (HLAoctet)  get(AttributeName.MarkingData.name()  )  ;
    }
    
    public void setMarkingData(byte markData) {
        ((HLAoctet) get(AttributeName.MarkingData.name())).setValue(markData);
    }        
}

/** 
 *  (see   RPR-Physical_v2.0.xml )  
 
<fixedRecordData notes="RPRnotePhysical18">
            <name>MarkingStruct</name>
            <encoding>HLAfixedRecord</encoding>
            <semantics>Character set used in the marking and the string of characters to be interpreted for display.</semantics>
            <field>
                <name>MarkingEncodingType</name>
                <dataType>MarkingEncodingEnum8</dataType>
                <semantics>Character set representation.</semantics>
            </field>
            <field>
                <name>MarkingData</name>
                <dataType>MarkingArray11</dataType>
                <semantics>11 element character string</semantics>
            </field>
        </fixedRecordData>     
 */


