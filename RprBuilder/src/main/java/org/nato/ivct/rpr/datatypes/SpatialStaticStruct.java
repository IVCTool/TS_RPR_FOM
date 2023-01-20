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

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.exceptions.RTIinternalError;

/** Spatial structure for Dead Reckoning Algorithm Static (1). */
public class SpatialStaticStruct extends HLAfixedRecordStruct {

    // class RPRboolean extends HLAboolean {}

    public enum AttributeName {
        WorldLocation,
        IsFrozen,
        Orientation
    }

    public SpatialStaticStruct () throws RTIinternalError {
        EncoderFactory encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
        
        add(AttributeName.WorldLocation.name(), new WorldLocationStruct());
        add(AttributeName.IsFrozen.name(), encoderFactory.createHLAboolean());
        add(AttributeName.Orientation.name(), new OrientationStruct());
    }
    
    
    /** 
     * Location of the object. 
    */
    public WorldLocationStruct getWorldLocation() {
        return ((WorldLocationStruct) get(AttributeName.WorldLocation.name()));
    }
    
    public void setWorldLocation(WorldLocationStruct WorldLocation) {
        add(AttributeName.WorldLocation.name(), WorldLocation);
    }

    /**
     * @return Whether the object is frozen or not.
     */
    public boolean getIsFrozen() {
        return ((HLAboolean) get(AttributeName.IsFrozen.name())).getValue();
    }
        
    public void setIsFrozen(boolean IsFrozen) {
        ((HLAboolean) get(AttributeName.IsFrozen.name())).setValue(IsFrozen);
    }
    
    /**
     * The angles of rotation around the coordinate axes between the object's attitude and the
     * reference coordinate system axes (calculated as the Tait-Bryan Euler angles specifying the
     * successive rotations needed to transform from the world coordinate system to the entity
     * coordinate system).
     */
    public OrientationStruct getOrientation() {
        return ((OrientationStruct) get(AttributeName.Orientation.name()));
    }

    public void setOrientation(OrientationStruct Orientation) {
        add(AttributeName.Orientation.name(), Orientation);
    }
}
