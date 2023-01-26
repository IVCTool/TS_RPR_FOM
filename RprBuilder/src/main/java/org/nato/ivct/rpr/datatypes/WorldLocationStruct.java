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
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.exceptions.RTIinternalError;

/**
 * The location of an object in the world coordinate system, as specified in IEEE Std 1278.1-1995
 * section 1.3.2.
 */
public class WorldLocationStruct extends HLAfixedRecordStruct {

    public enum AttributeName {
        X,
        Y,
        Z
    }

    public WorldLocationStruct () throws RTIinternalError {
      EncoderFactory encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();

      add(AttributeName.X.name(), encoderFactory.createHLAfloat64BE());
      add(AttributeName.Y.name(), encoderFactory.createHLAfloat64BE());
      add(AttributeName.Z.name(), encoderFactory.createHLAfloat64BE());
    }

    /**
     * 
     * @return Distance from the origin along the X axis.
     */
    public double getX() {
      return ((HLAfloat64BE) get(AttributeName.X.name())).getValue();
    }

    public void setX(double X) {
      ((HLAfloat64BE) get(AttributeName.X.name())).setValue(X);
    }

    /**
     * 
     * @return Distance from the origin along the Y axis.
     */
    public double getY() {
      return ((HLAfloat64BE) get(AttributeName.Y.name())).getValue();
    }

    public void setY(double Y) {
      ((HLAfloat64BE) get(AttributeName.Y.name())).setValue(Y);
    }

    /**
     * 
     * @return Distance from the origin along the Z axis.
     */
    public double getZ() {
      return ((HLAfloat64BE) get(AttributeName.Z.name())).getValue();
    }

    public void setZ(double Z) {
      ((HLAfloat64BE) get(AttributeName.Z.name())).setValue(Z);
    }

}
