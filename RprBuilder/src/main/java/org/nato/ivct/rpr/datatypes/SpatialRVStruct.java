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

import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.exceptions.RTIinternalError;

/** 
 * Spatial structure for Dead Reckoning Algorithm RVW (4) and RVB (8). 
 */
public class SpatialRVStruct extends HLAfixedRecordStruct {

    public enum AttributeName {
        WorldLocation,
        IsFrozen,
        Orientation,
        VelocityVector,
        AccelerationVector,
        AngularVelocity
    }

    public SpatialRVStruct () throws RTIinternalError {
        super();
        add(AttributeName.WorldLocation.name(), new WorldLocationStruct());
        add(AttributeName.IsFrozen.name(), encoderFactory.createHLAboolean());
        add(AttributeName.Orientation.name(), new OrientationStruct());  
        add(AttributeName.VelocityVector.name(), new VelocityVectorStruct());  
        add(AttributeName.AccelerationVector.name(), new AccelerationVectorStruct());  
        add(AttributeName.AngularVelocity.name(), new AngularVelocityVectorStruct());  
    }

   
    public SpatialRVStruct(HLAfixedRecord value) throws RTIinternalError {
        this();
        setWorldLocation((WorldLocationStruct) value.get(0));
        setIsFrozen(((HLAboolean) value.get(1)).getValue());
        setOrientation((OrientationStruct) value.get(2));
        setVelocityVector((VelocityVectorStruct) value.get(3));
        setAccelerationVector((AccelerationVectorStruct) value.get(4));
        setAngularVelocity((AccelerationVectorStruct) value.get(5));
    }


    /** Location of the object. */
    public WorldLocationStruct getWorldLocation() {
        return ((WorldLocationStruct) get(AttributeName.WorldLocation.name()));
    }
    
    public void setWorldLocation(WorldLocationStruct WorldLocation) {
        add(AttributeName.WorldLocation.name(), WorldLocation);
    }

    /** Whether the object is frozen or not. */
    public boolean  getIsFrozen() {
        return ((HLAboolean ) get(AttributeName.IsFrozen.name())).getValue();
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
    
    /** The rate at which an object's position is changing over time. */
    public VelocityVectorStruct getVelocityVector() {
        return ((VelocityVectorStruct) get(AttributeName.VelocityVector.name()));
    }
    
    public void setVelocityVector(VelocityVectorStruct VelocityVector) {
        add(AttributeName.VelocityVector.name(), VelocityVector);
    }
    
    /** The magnitude of the change in linear velocity of an object over time. */
    public AccelerationVectorStruct getAccelerationVector() {
        return ((AccelerationVectorStruct) get(AttributeName.AccelerationVector.name()));
    }

    public void setAccelerationVector(AccelerationVectorStruct AccelerationVector) {
        add(AttributeName.AccelerationVector.name(), AccelerationVector);
    }
    
    /** The rate at which an object's orientation is changing over time. */
    public AngularVelocityVectorStruct getAngularVelocity() {
        return ((AngularVelocityVectorStruct) get(AttributeName.AngularVelocity.name()));
    }

    public void setAngularVelocity(AccelerationVectorStruct AngularVelocity) {
        add(AttributeName.AngularVelocity.name(), AngularVelocity);
    }
}


/*
            <fixedRecordData notes="RPRnoteBase15">
                <name>SpatialRVStruct</name>
                <encoding>HLAfixedRecord</encoding>
                <semantics>Spatial structure for Dead Reckoning Algorithm RVW (4) and RVB (8).</semantics>
                <field>
                    <name>WorldLocation</name>
                    <dataType>WorldLocationStruct</dataType>
                    <semantics>Location of the object.</semantics>
                </field>
                <field notes="RPRnoteBase19">
                    <name>IsFrozen</name>
                    <dataType>RPRboolean</dataType>
                    <semantics>Whether the object is frozen or not.</semantics>
                </field>
                <field>
                    <name>Orientation</name>
                    <dataType>OrientationStruct</dataType>
                    <semantics>The angles of rotation around the coordinate axes between the object's attitude and the reference coordinate system axes (calculated as the Tait-Bryan Euler angles specifying the successive rotations needed to transform from the world coordinate system to the entity coordinate system).</semantics>
                </field>
                <field>
                    <name>VelocityVector</name>
                    <dataType>VelocityVectorStruct</dataType>
                    <semantics>The rate at which an object's position is changing over time.</semantics>
                </field>
                <field>
                    <name>AccelerationVector</name>
                    <dataType>AccelerationVectorStruct</dataType>
                    <semantics>The magnitude of the change in linear velocity of an object over time.</semantics>
                </field>
                <field>
                    <name>AngularVelocity</name>
                    <dataType>AngularVelocityVectorStruct</dataType>
                    <semantics>The rate at which an object's orientation is changing over time.</semantics>
                </field>
            </fixedRecordData>

 */