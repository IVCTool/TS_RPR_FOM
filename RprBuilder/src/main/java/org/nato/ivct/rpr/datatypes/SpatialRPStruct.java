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

/** 
 * Spatial structure for Dead Reckoning Algorithm RPW (3) and RPB (7). 
 */
public class SpatialRPStruct extends HLAfixedRecordStruct {

    public enum AttributeName {
        WorldLocation,
        IsFrozen,
        OrientationStruct,
        VelocityVector,
        AngularVelocity
    }

    public SpatialRPStruct () throws RTIinternalError {
        EncoderFactory encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();

        add(AttributeName.WorldLocation.name(), new WorldLocationStruct());
        add(AttributeName.IsFrozen.name(), encoderFactory.createHLAboolean());
        add(AttributeName.OrientationStruct.name(), new OrientationStruct());  
        add(AttributeName.VelocityVector.name(), new VelocityVectorStruct());  
        add(AttributeName.AngularVelocity.name(), new AngularVelocityVectorStruct());  
    }

   
    /** Location of the object. */
    public WorldLocationStruct getWorldLocation() {
        return ((WorldLocationStruct) get(AttributeName.WorldLocation.name()));
    }
    
    public void setWorldLocation(WorldLocationStruct WorldLocation) {
        add(AttributeName.WorldLocation.name(), WorldLocation);
    }

    /** Whether the object is frozen or not. */
    public HLAboolean  getIsFrozen() {
        return ((HLAboolean ) get(AttributeName.IsFrozen.name()));
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
        return ((OrientationStruct) get(AttributeName.OrientationStruct.name()));
    }
    
    public void setOrientation(OrientationStruct Orientation) {
        add(AttributeName.OrientationStruct.name(), Orientation);
    }
    
    /** The rate at which an object's position is changing over time. */
    public VelocityVectorStruct getVelocityVector() {
        return ((VelocityVectorStruct) get(AttributeName.VelocityVector.name()));
    }
    
    public void setVelocityVector(VelocityVectorStruct VelocityVector) {
        add(AttributeName.VelocityVector.name(), VelocityVector);
    }
    
    /** The rate at which an object's orientation is changing over time. */
    public AngularVelocityVectorStruct getAngularVelocity() {
        return ((AngularVelocityVectorStruct) get(AttributeName.AngularVelocity.name()));
    }

    public void setAngularVelocity(AngularVelocityVectorStruct AngularVelocity) {
        add(AttributeName.AngularVelocity.name(), AngularVelocity);
    }
}
