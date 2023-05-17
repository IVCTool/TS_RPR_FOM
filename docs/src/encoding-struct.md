# Custom Struct Types

The actual encoding and decoding of data happens in the object class attributes and also in the interaction parameters. These attributes and parameters can be simple basic data types, like a boolean or an integer, but they can also be structure types. A structured typ can composed from basic types and again from other structured types. The HLA API provides encoders for common basic types, like *HLAinteger16BE*, *HLAfloat64BE*, *HLAbyte*, etc. 

Complex types can be constructed by using *HLAfixedRecord*, or *HLAvariantRecord* structs, where the subelements can be again basic types or even complex types. Such complex types will be named and can be used as attribute types. 

An example for such a complex typ is the *OrientationStruct*, which is used to describe the orientation of a physical entity within a virtual space.

```xml
<fixedRecordData>
    <name>OrientationStruct</name>
    <encoding>HLAfixedRecord</encoding>
    <semantics>The orientation of an object in the world coordinate system, as specified in IEEE Std 1278.1-1995 section 1.3.2.</semantics>
    <field>
        <name>Psi</name>
        <dataType>AngleRadianFloat32</dataType>
        <semantics>Rotation about the Z axis.</semantics>
    </field>
    <field>
        <name>Theta</name>
        <dataType>AngleRadianFloat32</dataType>
        <semantics>Rotation about the Y axis.</semantics>
    </field>
    <field>
        <name>Phi</name>
        <dataType>AngleRadianFloat32</dataType>
        <semantics>Rotation about the X axis.</semantics>
    </field>
</fixedRecordData>
```

The HLA API standard does not provide a direct mapping between such structures and a specific programming language.  Only encoders are provided for the build-in types, which need to be applied according to the definition in the OMT module. For modules with complex data types, or with a large number of different types, this becomes a huge effort for the application programmer and it is also error prone in picking the correct encoders in the right order. 

The OMTbuilder some helper classes to support the construction of Java helper classes to work with such custom structures. The resulting help class for the example of the  *OrientationStruct* will look the this:

```java
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
```

The *[HLAfixedRecordStruct](/RprBuilder/src/main/java/org/nato/ivct/rpr/datatypes/HLAfixedRecordStruct.java)* is a helper class within the OmtBuilder library, which support the construction of custom fixed record structs. It is implementing the *DataElement* interface to support the encoding and decoding, and it supports the use of named record elements.  


> For more information on the Object Model Template (OMT) Specification, see the standard document, [IEEE Std 1516.2(tm)-2010](https://ieeexplore.ieee.org/stamp/stamp.jsp?arnumber=5553440).