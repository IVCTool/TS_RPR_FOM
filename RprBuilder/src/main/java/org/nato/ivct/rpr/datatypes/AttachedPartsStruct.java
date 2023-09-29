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
import org.nato.ivct.rpr.objects.PhysicalEntity.Attributes;

import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.exceptions.RTIinternalError;

import org.nato.ivct.rpr.RprBuilderException;
import org.nato.ivct.rpr.datatypes.StationEnum32;


public class AttachedPartsStruct extends HLAfixedRecordStruct { 
    
    /**
     * (see PRP-Base_v2.0.xml
     
     <fixedRecordData>
                <name>AttachedPartsStruct</name>
                <encoding>HLAfixedRecord</encoding>
                <semantics>Structure to represent removable parts that may be attached to an entity.</semantics>
                <field>
                    <name>Station</name>
                    <dataType>StationEnum32</dataType>
                    <semantics>Identification of the location (or station) to which the part is attached.</semantics>
                </field>
                <field>
                    <name>StoreType</name>
                    <dataType>EntityTypeStruct</dataType>
                    <semantics>The entity type of the attached part.</semantics>
                </field>
            </fixedRecordData>   
     */

    enum AttributeName {
        Station,                              // StationEnum32   -->  HLAinteger32BE ?
        StoreType                          // EntityTypeStruct        
    }

    public AttachedPartsStruct () throws RTIinternalError {
        super();        
        
        // TODO   StationEnum32  is not complete
        add(AttributeName.Station.name(), StationEnum32.Nothing_Empty.getDataElement()   );      /// ################        
        add(AttributeName.StoreType.name(),  new EntityTypeStruct() ) ;              
    }
    
    
    public HLAinteger32BE getStation() {                                             // TODO  in this Way  ?????  or incorrect
        return ( (HLAinteger32BE)  get(AttributeName.Station.name() )  );
    }  
    
    public void setStation(int  stat) {                                                 // TODO  in this Way  ?????  or incorrect        
        ( (HLAinteger32BE) get(AttributeName.Station.name() ) ).setValue(stat);         
    }
    
    
    public EntityTypeStruct getStoreType() {
        return ( (EntityTypeStruct) get(AttributeName.StoreType.name() ) ) ;       
    }
    
    public void setStoreType(EntityTypeStruct storeType) {               // TODO  in this Way  ?????  or incorrect
        add(AttributeName.StoreType.name(), storeType) ;
    } 
        
}
