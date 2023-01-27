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

public class AccelerationVectorStruct extends HLAfixedRecordStruct {

    public enum AttributeName {
        XAcceleration,
        YAcceleration,
        ZAcceleration
    }

    public AccelerationVectorStruct() throws RTIinternalError {
        super();
        add(AttributeName.XAcceleration.name(), encoderFactory.createHLAfloat32BE());
        add(AttributeName.YAcceleration.name(), encoderFactory.createHLAfloat32BE());
        add(AttributeName.ZAcceleration.name(), encoderFactory.createHLAfloat32BE());    
    }


    /** Acceleration component along the X axis. */
    public float getXAcceleration() {
        return ((HLAfloat32BE) get(AttributeName.XAcceleration.name())).getValue();
    }
    
    public void setXAcceleration(float XAcceleration) {
        ((HLAfloat32BE) get(AttributeName.XAcceleration.name())).setValue(XAcceleration);
    }
    
    /** Acceleration component along the Y axis. */
    public float getYAcceleration() {
        return ((HLAfloat32BE) get(AttributeName.YAcceleration.name())).getValue();
    }
  
    public void setYAcceleration(float YAcceleration) {
        ((HLAfloat32BE) get(AttributeName.YAcceleration.name())).setValue(YAcceleration);
    }
    
    /** Acceleration component along the Z axis. */
    public float getZAcceleration() {
        return ((HLAfloat32BE) get(AttributeName.ZAcceleration.name())).getValue();
    }

    public void setZAcceleration(float ZAcceleration) {
        ((HLAfloat32BE) get(AttributeName.ZAcceleration.name())).setValue(ZAcceleration);
    }    
}


/*
    <fixedRecordData>
        <name>AccelerationVectorStruct</name>
        <encoding>HLAfixedRecord</encoding>
        <semantics>The magnitude of the change in linear velocity over time.</semantics>
        <field>
            <name>XAcceleration</name>
            <dataType>AccelerationMeterPerSecondSquaredFloat32</dataType>
            <semantics>Acceleration component along the X axis.</semantics>
        </field>
        <field>
            <name>YAcceleration</name>
            <dataType>AccelerationMeterPerSecondSquaredFloat32</dataType>
            <semantics>Acceleration component along the Y axis.</semantics>
        </field>
        <field>
            <name>ZAcceleration</name>
            <dataType>AccelerationMeterPerSecondSquaredFloat32</dataType>
            <semantics>Acceleration component along the Z axis.</semantics>
        </field>
    </fixedRecordData>
 */