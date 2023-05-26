/**
 * Copyright 2022, brf (Fraunhofer IOSB)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http: //www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License. 
 */

package org.nato.ivct.rpr.objects;

import org.nato.ivct.rpr.RprBuilderException;

public class Sensor extends PhysicalEntity {
    
    public enum Attributes {
    AntennaRaised,
    BlackoutLightsOn,
    LightsOn,
    InteriorLightsOn,
    MissionKill    
    }
      
    public Sensor() throws RprBuilderException {
        super();
        
        // TODO  set the attributes        
    }
    
    // TODO   we need getter and setter for the attributes
    
    
}
