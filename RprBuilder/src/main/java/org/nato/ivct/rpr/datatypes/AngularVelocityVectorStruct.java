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
 * The rate at which the orientation is changing over time, in body coordinates.
 */
public class AngularVelocityVectorStruct extends HLAfixedRecordStruct {

    public enum AttributeName {
        XAngularVelocity,
        YAngularVelocity,
        ZAngularVelocity
    }

    public AngularVelocityVectorStruct() throws RTIinternalError {
        super();
        add(AttributeName.XAngularVelocity.name(), encoderFactory.createHLAfloat32BE());
        add(AttributeName.YAngularVelocity.name(), encoderFactory.createHLAfloat32BE());
        add(AttributeName.ZAngularVelocity.name(), encoderFactory.createHLAfloat32BE());
    }

    /** Acceleration component about the X axis. */
    public float getXAngularVelocity() {
        return ((HLAfloat32BE) get(AttributeName.XAngularVelocity.name())).getValue();
    }
    
    public void setXAngularVelocity(float WorldLocation) {
        ((HLAfloat32BE) get(AttributeName.XAngularVelocity.name())).setValue(WorldLocation);
    }

    /** Acceleration component about the Y axis. */
    public float getYAngularVelocity() {
        return ((HLAfloat32BE) get(AttributeName.YAngularVelocity.name())).getValue();
    }
    
    public void setYAngularVelocity(float WorldLocation) {
        ((HLAfloat32BE) get(AttributeName.YAngularVelocity.name())).setValue(WorldLocation);
    }

    /** Acceleration component about the Z axis. */
    public float getZAngularVelocity() {
        return ((HLAfloat32BE) get(AttributeName.ZAngularVelocity.name())).getValue();
    }
    
    public void setZAngularVelocity(float WorldLocation) {
        ((HLAfloat32BE) get(AttributeName.ZAngularVelocity.name())).setValue(WorldLocation);
    }
}

/**
    <fixedRecordData>
        <name>AngularVelocityVectorStruct</name>
        <encoding>HLAfixedRecord</encoding>
        <semantics>The rate at which the orientation is changing over time, in body coordinates.</semantics>
        <field>
            <name>XAngularVelocity</name>
            <dataType>AngularVelocityRadianPerSecondFloat32</dataType>
            <semantics>Acceleration component about the X axis.</semantics>
        </field>
        <field>
            <name>YAngularVelocity</name>
            <dataType>AngularVelocityRadianPerSecondFloat32</dataType>
            <semantics>Acceleration component about the Y axis.</semantics>
        </field>
        <field>
            <name>ZAngularVelocity</name>
            <dataType>AngularVelocityRadianPerSecondFloat32</dataType>
            <semantics>Acceleration component about the Z axis.</semantics>
        </field>
    </fixedRecordData>
 */
