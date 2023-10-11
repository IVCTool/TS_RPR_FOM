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
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.encoding.HLAfloat32BE;

import hla.rti1516e.exceptions.RTIinternalError;

public class ArticulatedPartsStruct extends HLAfixedRecordStruct { 
    
    /** 
     *  (see RPR-Base_v2.0.xml)    Definition see below
    */

    enum AttributeName {
        Class,                   //  ArticulatedPartsTypeEnum32        -> RPR-Enumerations_v2.0.xml
        TypeMetric,           //  ArticulatedTypeMetricEnum32        -> RPR-Enumerations_v2.0.xml
        Value                  //   Float32       #####################################              
    }

    public ArticulatedPartsStruct () throws RTIinternalError {
        super();
        
        // TODO how to use Enums here  ArticulatedPartsTypeEnum32
        add(AttributeName.Class.name(),  ArticulatedPartsTypeEnum32.Other.getDataElement() );  
        
        // TODO how to use Enums here
        add( AttributeName.TypeMetric.name() ,  ArticulatedTypeMetricEnum32.Position.getDataElement()  )   ;      
                
        add(AttributeName.Value.name() , encoderFactory.createHLAfloat32BE() )  ;  
     // add(AttributeName.ZAcceleration.name(), encoderFactory.createHLAfloat32BE()); 
       
    }
    
 
    public HLAinteger32BE getAttribut_Class() {                                              // TODO  in this Way  ?????  or incorrect
         return (HLAinteger32BE) get(AttributeName.Class.name()  );
    }

    public void setAttribut_Class(int  para) {
         (  (HLAinteger32BE) get(AttributeName.Class.name() ) ).setValue(para );   
    }

    
    public HLAinteger32BE getTypeMetric() {                                           // TODO  in this Way  ?????  or incorrect
        return  (HLAinteger32BE) get(AttributeName.TypeMetric.name()  );
    }
    
    public void setTypeMetric(int typeMetric ) {
        ( (HLAinteger32BE) get(AttributeName.TypeMetric.name() ) ).setValue(typeMetric);
    } 
    
       
    public float  getValue() {
        return ( (HLAfloat32BE) get(AttributeName.Value.name() ) ).getValue() ;
    }
    
    public void setValue (float  alue) {      
        ((HLAfloat32BE) get(AttributeName.Value.name())).setValue(alue);        
    }    
   
}

/*    from  RPR-Base_v2.0.xml   
       <fixedRecordData notes="RPRnoteBase7">
            <name>ArticulatedPartsStruct</name>
            <encoding>HLAfixedRecord</encoding>
            <semantics>Structure to represent the state of a movable part of an entity.</semantics>
            <field>
                <name>Class</name>
                <dataType>ArticulatedPartsTypeEnum32</dataType>
                <semantics>The type class uniquely identifies a particular articulated part on a given entity type. Guidance for uniquely assigning type classes to an entity's articulated parts is given in section 4.8 of the enumeration document (SISO-REF-010).</semantics>
            </field>
            <field>
                <name>TypeMetric</name>
                <dataType>ArticulatedTypeMetricEnum32</dataType>
                <semantics>The type metric uniquely identifies the transformation to be applied to the articulated part.</semantics>
            </field>
            <field>
                <name>Value</name>
                <dataType>Float32</dataType>
                <semantics>Value of the transformation to be applied to the articulated part.</semantics>
            </field>
        </fixedRecordData>
*/



