# Encoding Helper

All interactions and object updates are encoded and decoded by HLA-Encoders obtained from the RTI-EncoderFactory. The interaction parameters and object attributes are structured according to the OMT definitions. Before sending the data must be encoded into a handle value map and after receiving the data must decoded into basic Java data types. With that concept, the protocol data units (PDU) are only containing encoded byte arrays.  

Steps to send and receive an interaction:

## Phase 1: Publishing

1. Get the interaction handle: `_messageId = rtiAmbassador.getInteractionClassHandle("Communication")`
2. Get the parameter handles: `_parameterIdText = rtiAmbassador.getParameterHandle(_messageId, "Message")`

## Phase 2: Encoding

3. Create parameter handle value map: `parameters = rtiAmbassador.getParameterHandleValueMapFactory().create(1)`
4. Create encoder: `messageEncoder = _encoderFactory.createHLAunicodeString()`
5. Set the value: `messageEncoder.setValue(_message)`
6. Encode the value: `parameters.put(_parameterIdText, messageEncoder.toByteArray())`
7. Send the interaction: `rtiAmbassador.sendInteraction(_messageId, parameters, null)`


## Phase 3: Receive and Decode

8. Receive the interaction: `receiveInteraction` callback
9. Iterate through received parameter handle value map: `theParameters.keySet().iterator()`
10. Create the correct encoder: `_encoderFactory.createHLAunicodeString()` 
11. Decode the message: `messageDecoder.decode((byte[]) theParameters.get(handle))`
12. Read the as basic Java type: `message = messageDecoder.getValue()`


The alignment between the attribute names and the correct encoders is important in these steps. The OMT definitions must be maintained while selecting the correct encoder types. This can get quite complex, especially when using structured elements for the parameters or attributes.
In order to support type safe programming, the RPR Builder provides helper classes for the parameter and attribute structures. 

The helper class will be specific for a particular parameter or attribute and will provide Java type save getter and setter methods. 

```
// interface to the user
public class Communication.Message extends DataType {
    public setMessage (String msg);
    public String getMessage ();
}

// interface to the interaction and object helper classes
public class DataType {
    public ParameterHandleValueMap encode();
    public decode(ParameterHandleValueMap data);
}
```