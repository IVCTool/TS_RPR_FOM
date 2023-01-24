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

import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.exceptions.RTIinternalError;


/**
 * 
            <variantRecordData>
                <name>SpatialVariantStruct</name>
                <discriminant>DeadReckoningAlgorithm</discriminant>
                <dataType>DeadReckoningAlgorithmEnum8</dataType>
                <alternative>
                    <enumerator>Static</enumerator>
                    <name>SpatialStatic</name>
                    <dataType>SpatialStaticStruct</dataType>
                    <semantics>Variant for representing a static object.</semantics>
                </alternative>
                <alternative>
                    <enumerator>DRM_FPW</enumerator>
                    <name>SpatialFPW</name>
                    <dataType>SpatialFPStruct</dataType>
                    <semantics>Variant for representing an object with a constant velocity (or low acceleration) linear motion in world coordinates.</semantics>
                </alternative>
                <alternative>
                    <enumerator>DRM_RPW</enumerator>
                    <name>SpatialRPW</name>
                    <dataType>SpatialRPStruct</dataType>
                    <semantics>Variant for representing an object with a constant velocity (or low acceleration) linear motion, including rotation information, in world coordinates.</semantics>
                </alternative>
                <alternative>
                    <enumerator>DRM_RVW</enumerator>
                    <name>SpatialRVW</name>
                    <dataType>SpatialRVStruct</dataType>
                    <semantics>Variant for representing an object with high speed or maneuvering at any speed, including rotation information, in world coordinates.</semantics>
                </alternative>
                <alternative>
                    <enumerator>DRM_FVW</enumerator>
                    <name>SpatialFVW</name>
                    <dataType>SpatialFVStruct</dataType>
                    <semantics>Variant for representing an object with high speed or maneuvering at any speed in world coordinates.</semantics>
                </alternative>
                <alternative>
                    <enumerator>DRM_FPB</enumerator>
                    <name>SpatialFPB</name>
                    <dataType>SpatialFPStruct</dataType>
                    <semantics>Variant for representing an object with a constant velocity (or low acceleration) linear motion in body axis coordinates.</semantics>
                </alternative>
                <alternative>
                    <enumerator>DRM_RPB</enumerator>
                    <name>SpatialRPB</name>
                    <dataType>SpatialRPStruct</dataType>
                    <semantics>Variant for representing an object with a constant velocity (or low acceleration) linear motion, including rotation information, in body axis coordinates.</semantics>
                </alternative>
                <alternative>
                    <enumerator>DRM_RVB</enumerator>
                    <name>SpatialRVB</name>
                    <dataType>SpatialRVStruct</dataType>
                    <semantics>Variant for representing an object with high speed or maneuvering at any speed, including rotation information, in body axis coordinates.</semantics>
                </alternative>
                <alternative>
                    <enumerator>DRM_FVB</enumerator>
                    <name>SpatialFVB</name>
                    <dataType>SpatialFVStruct</dataType>
                    <semantics>Variant for representing an object with high speed or maneuvering at any speed in body axis coordinates.</semantics>
                </alternative>

 */
public class SpatialVariantStruct extends HLAvariantRecordStruct<HLAoctet> {

    public enum AttributeName {
        Other           ((byte) 0),
        SpatialStatic   ((byte) 1),
        SpatialFPW      ((byte) 2),
        SpatialRPW      ((byte) 3),
        DRM_RVW         ((byte) 4),
        SpatialFVW      ((byte) 5),
        SpatialFPB      ((byte) 6),
        SpatialRPB      ((byte) 7),
        SpatialRVB      ((byte) 8),
        SpatialFVB      ((byte) 9);

        private final byte value;

        private AttributeName(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }

    public SpatialVariantStruct () throws RTIinternalError {
        super();
    }

    public void reset() {
        discriminantName = null;
        discriminant = null;
        dataElement = null;
    }

    /**
     * Variant for representing a static object.
     * 
     * @return
     * @throws RTIinternalError
     */
    public SpatialStaticStruct getSpatialStatic() throws RTIinternalError {
        if (discriminantName == null) {
            // initialize if discriminant has not been defined
            setSpatialStatic(new SpatialStaticStruct());
        } else if (!this.discriminantName.equals(AttributeName.SpatialStatic.name())) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialStaticStruct) dataElement;            
    }

    public void setSpatialStatic(SpatialStaticStruct SpatialStatic) {
        this.discriminantName = AttributeName.SpatialStatic.name();
        discriminant = encoderFactory.createHLAoctet((byte) AttributeName.SpatialStatic.getValue());
        dataElement = SpatialStatic;
    }    

    /**
     * Variant for representing an object with a constant velocity (or low acceleration) 
     * linear motion in world coordinates.
     * 
     * @return
     * @throws RTIinternalError
     */
    public SpatialFPStruct getSpatialFPW() throws RTIinternalError {
        if (discriminantName == null) {
            // initialize if discriminant has not been defined
            setSpatialFPW(new SpatialFPStruct());
        } else if (!this.discriminantName.equals(AttributeName.SpatialFPW.name())) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialFPStruct) dataElement;            
    }
    
    public void setSpatialFPW(SpatialFPStruct SpatialFPW) {
        this.discriminantName = AttributeName.SpatialFPW.name();
        discriminant = encoderFactory.createHLAoctet((byte) AttributeName.SpatialFPW.getValue());
        dataElement = SpatialFPW;
    }
    
    /**
     * Variant for representing an object with a constant velocity (or low acceleration) linear motion, 
     * including rotation information, in world coordinates.
     * 
     * @return
     * @throws RTIinternalError
     */
    public SpatialRPStruct getSpatialRPW() throws RTIinternalError {
        if (discriminantName == null) {
            // initialize if discriminant has not been defined
            setSpatialRPW(new SpatialRPStruct());
        } else if (!this.discriminantName.equals(AttributeName.SpatialRPW.name())) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialRPStruct) dataElement;            
    }
    
    public void setSpatialRPW(SpatialRPStruct SpatialRPW) {
        this.discriminantName = AttributeName.SpatialRPW.name();
        discriminant = encoderFactory.createHLAoctet((byte) AttributeName.SpatialRPW.getValue());
        dataElement = SpatialRPW;
    }

    /**
     * Variant for representing an object with a constant velocity (or low acceleration) linear motion, 
     * including rotation information, in body axis coordinates.
     * 
     * @return
     * @throws RTIinternalError
     */
    public SpatialRPStruct getSpatialRPB() throws RTIinternalError {
        if (discriminantName == null) {
            // initialize if discriminant has not been defined
            setSpatialRPW(new SpatialRPStruct());
        } else if (!this.discriminantName.equals(AttributeName.SpatialRPB.name())) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialRPStruct) dataElement;            
    }

    public void setSpatialRPB(SpatialRPStruct SpatialRPB) {
        this.discriminantName = AttributeName.SpatialRPW.name();
        discriminant = encoderFactory.createHLAoctet((byte) AttributeName.SpatialRPW.getValue());
        dataElement = SpatialRPB;
    }

    
    /** TODO: attribute getter, setter, and structures to be completed
     * 
    public SpatialRVStruct getSpatialRVW() {
        return this.SpatialRVW;
    }

    public void setSpatialRVW(SpatialRVStruct SpatialRVW) {
        this.SpatialRVW = SpatialRVW;
    }

    public SpatialFVStruct getSpatialFVW() {
        return this.SpatialFVW;
    }

    public void setSpatialFVW(SpatialFVStruct SpatialFVW) {
        this.SpatialFVW = SpatialFVW;
    }

    public SpatialFPStruct getSpatialFPB() {
        return this.SpatialFPB;
    }

    public void setSpatialFPB(SpatialFPStruct SpatialFPB) {
        this.SpatialFPB = SpatialFPB;
    }

    public SpatialRVStruct getSpatialRVB() {
        return this.SpatialRVB;
    }

    public void setSpatialRVB(SpatialRVStruct SpatialRVB) {
        this.SpatialRVB = SpatialRVB;
    }

    public SpatialFVStruct getSpatialFVB() {
        return this.SpatialFVB;
    }

    public void setSpatialFVB(SpatialFVStruct SpatialFVB) {
        this.SpatialFVB = SpatialFVB;
    }
    */
    
}
