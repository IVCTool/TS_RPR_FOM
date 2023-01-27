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

import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.encoding.HLAvariantRecord;
import hla.rti1516e.exceptions.RTIinternalError;


public class SpatialVariantStruct extends HLAvariantRecordStruct<HLAoctet> {

    public enum AttributeName {
        Other           ((byte) 0),
        SpatialStatic   ((byte) 1),
        SpatialFPW      ((byte) 2),
        SpatialRPW      ((byte) 3),
        SpatialRVW      ((byte) 4),
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

    public void decode (byte[] bytes) throws DecoderException {
        HLAvariantRecord<DataElement> decoder = encoderFactory.createHLAvariantRecord(encoderFactory.createHLAoctet());

        try {
            decoder.setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialStatic.getValue()), (new SpatialStaticStruct()).getDataElement());
            decoder.setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialFPW.getValue()), (new SpatialFPStruct()).getDataElement());
            decoder.setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialRPW.getValue()), (new SpatialRPStruct()).getDataElement());
            decoder.setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialRVW.getValue()), (new SpatialRVStruct()).getDataElement());
            decoder.setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialFVW.getValue()), (new SpatialFVStruct()).getDataElement());
            decoder.setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialFPB.getValue()), (new SpatialFPStruct()).getDataElement());
            decoder.setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialRPB.getValue()), (new SpatialRPStruct()).getDataElement());
            decoder.setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialRVB.getValue()), (new SpatialRVStruct()).getDataElement());
            decoder.setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialFVB.getValue()), (new SpatialFVStruct()).getDataElement());

            decoder.decode(bytes);
            switch (((HLAoctet) decoder.getDiscriminant()).getValue()) {
                
                case 1: // SpatialStatic   ((byte) 1)
                    setSpatialStatic(new SpatialStaticStruct((HLAfixedRecord) decoder.getValue()));
                    break;
                
                case 2: // SpatialFPW      ((byte) 2)
                    setSpatialFPW(new SpatialFPStruct((HLAfixedRecord) decoder.getValue())); 
                    break;
                
                case 3: // SpatialRPW   ((byte) 3)
                    setSpatialRPW(new SpatialRPStruct((HLAfixedRecord) decoder.getValue())); 
                    break;
                
                case 4: // SpatialRVW   ((byte) 4)
                    setSpatialRVW(new SpatialRVStruct((HLAfixedRecord) decoder.getValue())); 
                    break;
                
                case 5: // SpatialFVW   ((byte) 5)
                    setSpatialFVW(new SpatialFVStruct((HLAfixedRecord) decoder.getValue())); 
                    break;
                
                case 6:// SpatialFPB    ((byte) 6)
                    setSpatialFPB(new SpatialFPStruct((HLAfixedRecord) decoder.getValue())); 
                    break;
                
                case 7:// SpatialRPB    ((byte) 7)
                    setSpatialRPB(new SpatialRPStruct((HLAfixedRecord) decoder.getValue())); 
                    break;

                case 8: // SpatialRVB   ((byte) 8)
                    setSpatialRVB(new SpatialRVStruct((HLAfixedRecord) decoder.getValue())); 
                    break;
                
                case 9:// SpatialFVB    ((byte) 9)
                    setSpatialFVB(new SpatialFVStruct((HLAfixedRecord) decoder.getValue())); 
                    break;
                default: break;
            }
                    
        } catch (RTIinternalError e) {
            throw new DecoderException(e.getMessage());
        }
   }

    public void reset() {
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
        if (discriminant == null) {
            // initialize if discriminant has not been defined
            setSpatialStatic(new SpatialStaticStruct());
        } else if (this.discriminant.getValue() != AttributeName.SpatialStatic.getValue()) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialStaticStruct) dataElement;            
    }

    public void setSpatialStatic(SpatialStaticStruct SpatialStatic) {
        setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialStatic.getValue()), SpatialStatic);
    }    

    /**
     * Variant for representing an object with a constant velocity (or low acceleration) 
     * linear motion in world coordinates.
     * 
     * @return
     * @throws RTIinternalError
     */
    public SpatialFPStruct getSpatialFPW() throws RTIinternalError {
        if (discriminant == null) {
            // initialize if discriminant has not been defined
            setSpatialFPW(new SpatialFPStruct());
        } else if (this.discriminant.getValue() != AttributeName.SpatialFPW.getValue()) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialFPStruct) dataElement;            
    }
    
    public void setSpatialFPW(SpatialFPStruct SpatialFPW) {
        setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialFPW.getValue()), SpatialFPW);
    }
    
    /**
     * Variant for representing an object with a constant velocity (or low acceleration) linear motion, 
     * including rotation information, in world coordinates.
     * 
     * @return
     * @throws RTIinternalError
     */
    public SpatialRPStruct getSpatialRPW() throws RTIinternalError {
        if (discriminant == null) {
            // initialize if discriminant has not been defined
            setSpatialRPW(new SpatialRPStruct());
        } else if (this.discriminant.getValue() != AttributeName.SpatialRPW.getValue()) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialRPStruct) dataElement;            
    }
    
    public void setSpatialRPW(SpatialRPStruct SpatialRPW) {
        setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialRPW.getValue()), SpatialRPW);
    }

    /**
     * Variant for representing an object with a constant velocity (or low acceleration) linear motion, 
     * including rotation information, in body axis coordinates.
     * 
     * @return
     * @throws RTIinternalError
     */
    public SpatialRPStruct getSpatialRPB() throws RTIinternalError {
        if (discriminant == null) {
            // initialize if discriminant has not been defined
            setSpatialRPB(new SpatialRPStruct());
        } else if (this.discriminant.getValue() != AttributeName.SpatialRPB.getValue()) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialRPStruct) dataElement;            
    }

    public void setSpatialRPB(SpatialRPStruct SpatialRPB) {
        setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialRPB.getValue()), SpatialRPB);
    }
    
    /**
     * Variant for representing an object with high speed or maneuvering at any speed, 
     * including rotation information, in world coordinates.
     * 
     * @return
     * @throws RTIinternalError
     */
    public SpatialRVStruct getSpatialRVW() throws RTIinternalError {
        if (discriminant == null) {
            // initialize if discriminant has not been defined
            setSpatialRVW(new SpatialRVStruct());
        } else if (this.discriminant.getValue() != AttributeName.SpatialRVW.getValue()) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialRVStruct) dataElement;            
    }
    
    public void setSpatialRVW(SpatialRVStruct SpatialRVW) {
        setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialRVW.getValue()), SpatialRVW);
    }
    
    /**
     * Variant for representing an object with high speed or maneuvering at any speed in world coordinates.
     * 
     * @return
     * @throws RTIinternalError
     */
    public SpatialFVStruct getSpatialFVW() throws RTIinternalError {
        if (discriminant == null) {
            // initialize if discriminant has not been defined
            setSpatialFVW(new SpatialFVStruct());
        } else if (this.discriminant.getValue() != AttributeName.SpatialFVW.getValue()) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialFVStruct) dataElement;            
    }
    
    public void setSpatialFVW(SpatialFVStruct SpatialFVW) {
        setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialFVW.getValue()), SpatialFVW);
    }
    
    /**
     * Variant for representing an object with a constant velocity (or low acceleration) 
     * linear motion in body axis coordinates.
     * 
     * @return
     * @throws RTIinternalError
     */
    public SpatialFPStruct getSpatialFPB() throws RTIinternalError {
        if (discriminant == null) {
            // initialize if discriminant has not been defined
            setSpatialFPB(new SpatialFPStruct());
        } else if (this.discriminant.getValue() != AttributeName.SpatialFPB.getValue()) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialFPStruct) dataElement;            
    }
    
    public void setSpatialFPB(SpatialFPStruct SpatialFPB) {
        setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialFPB.getValue()), SpatialFPB);
    }

    /**
     * Variant for representing an object with a constant velocity (or low acceleration) 
     * linear motion in body axis coordinates.
     * @throws RTIinternalError
     */    
    public SpatialRVStruct getSpatialRVB() throws RTIinternalError {
        if (discriminant == null) {
            // initialize if discriminant has not been defined
            setSpatialRVB(new SpatialRVStruct());
        } else if (this.discriminant.getValue() != AttributeName.SpatialFPB.getValue()) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialRVStruct) dataElement;            
    }
    
    public void setSpatialRVB(SpatialRVStruct SpatialRVB) {
        setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialRVB.getValue()), SpatialRVB);
    }
    
    /**
     * Variant for representing an object with high speed or maneuvering at any speed in body axis coordinates.
     * @return
     * @throws RTIinternalError
     */
    public SpatialFVStruct getSpatialFVB() throws RTIinternalError {
        if (discriminant == null) {
            // initialize if discriminant has not been defined
            setSpatialFVB(new SpatialFVStruct());
        } else if (this.discriminant.getValue() != AttributeName.SpatialFPB.getValue()) {
            // return null if asking for wrong discriminant
            return null;
        }
        return (SpatialFVStruct) dataElement;            
    }

    public void setSpatialFVB(SpatialFVStruct SpatialFVB) {
        setVariant(encoderFactory.createHLAoctet((byte) AttributeName.SpatialFVB.getValue()), SpatialFVB);
    }
    
}

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
    </variantRecordData>
 */
