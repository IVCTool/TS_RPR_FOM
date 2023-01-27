/**    Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

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

import hla.rti1516e.encoding.HLAfloat32BE;
import hla.rti1516e.exceptions.RTIinternalError;


/**
 * The rate at which the position is changing over time.
 */
public class VelocityVectorStruct extends HLAfixedRecordStruct {

    public enum AttributeName {
        XVelocity,
        YVelocity,
        ZVelocity
    }

    public VelocityVectorStruct() throws RTIinternalError {
        super();
        add(AttributeName.XVelocity.name(), encoderFactory.createHLAfloat32BE());
        add(AttributeName.YVelocity.name(), encoderFactory.createHLAfloat32BE());
        add(AttributeName.ZVelocity.name(), encoderFactory.createHLAfloat32BE());    
    }

    
    /** Velocity component along the X axis. */
    public float getXVelocity() {
        return ((HLAfloat32BE) get(AttributeName.XVelocity.name())).getValue();
    }
  
    public void setXVelocity(float XVelocity) {
        ((HLAfloat32BE) get(AttributeName.XVelocity.name())).setValue(XVelocity);
    }
    
    /** Velocity component along the Y axis. */ 
    public float getYVelocity() {
        return ((HLAfloat32BE) get(AttributeName.YVelocity.name())).getValue();
    }

    public void setYVelocity(float YVelocity) {
        ((HLAfloat32BE) get(AttributeName.YVelocity.name())).setValue(YVelocity);
    }

    /** Velocity component along the Z axis. */
    public float getZVelocity() {
        return ((HLAfloat32BE) get(AttributeName.ZVelocity.name())).getValue();
    }

    public void setZVelocity(float ZVelocity) {
        ((HLAfloat32BE) get(AttributeName.ZVelocity.name())).setValue(ZVelocity);
    }
}
