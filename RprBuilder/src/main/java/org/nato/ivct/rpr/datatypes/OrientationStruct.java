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
 * The orientation of an object in the world coordinate system, as specified in IEEE Std 1278.1-1995
 * section 1.3.2.
 */
public class OrientationStruct extends HLAfixedRecordStruct {

    public enum AttributeName {
        Psi,
        Theta,
        Phi
    }

    public OrientationStruct () throws RTIinternalError {
        super();
        add(AttributeName.Psi.name(), encoderFactory.createHLAfloat32BE());
        add(AttributeName.Theta.name(), encoderFactory.createHLAfloat32BE());
        add(AttributeName.Phi.name(), encoderFactory.createHLAfloat32BE());
    }

    /**
     * 
     * @return Rotation about the Z axis.
     */
    public float getPsi() {
        return ((HLAfloat32BE) get(AttributeName.Psi.name())).getValue();
    }

    public void setPsi(float Psi) {
        ((HLAfloat32BE) get(AttributeName.Psi.name())).setValue(Psi);
    }

    /**
     * 
     * @return Rotation about the Y axis.
     */
    public float getTheta() {
        return ((HLAfloat32BE) get(AttributeName.Theta.name())).getValue();
    }

    public void setTheta(float Theta) {
        ((HLAfloat32BE) get(AttributeName.Theta.name())).setValue(Theta);
    }

    /**
     * 
     * @return Rotation about the X axis.
     */
    public float getPhi() {
        return ((HLAfloat32BE) get(AttributeName.Phi.name())).getValue();
    }

    public void setPhi(float Phi) {
        ((HLAfloat32BE) get(AttributeName.Phi.name())).setValue(Phi);
    }

}
