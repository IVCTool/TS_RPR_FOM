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

import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.exceptions.RTIinternalError;
import org.nato.ivct.rpr.datatypes.StationEnum32;


public class AttachedPartsStruct extends HLAfixedRecordStruct { 
    
    /**
     * (see PRP-Base_v2.0.xml
     */

    enum AttributeName {
        Station,                              // StationEnum32
        StoreType                          // EntityTypeStruct        
    }

    public AttachedPartsStruct () throws RTIinternalError {
        super();        
        // TODO  create StationEnum32 
        
        //add(AttributeName.Station.name(), new StationEnum32( ).getDataElement()  );
        
        add(AttributeName.Station.name(), StationEnum32.values()   );      /// ################
        
        add(AttributeName.StoreType.name(),  new EntityTypeStruct() ) ;              
    }
    
        
    public StationEnum32 getStation() {      
        return ( (StationEnum32)  get(AttributeName.Station.name().  )  );
         //return ((ParameterValueVariantStruct) get(AttributeName.ParameterValue.name()));
    }

    public void setStation(StationEnum32  station) {
        add(AttributeName.Station.name(), station);
        //add(AttributeName.ParameterValue.name(), parameterValue);      
    }

    
    public EntityTypeStruct getStoreType() {
        return ( (EntityTypeStruct) get(AttributeName.StoreType.name() ) ) ;       
    }
    
    public void setStoreType(EntityTypeStruct storeType) {
        add(AttributeName.StoreType.name(), storeType) ;
    } 
        
        
}
