package org.nato.rpr;

import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import java.util.HashMap;
import java.util.Map;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;


/**
 * Generic Interaction Builder to create Interaction instances
 */
public class InteractionBuilder {

    public static Map<InteractionClassHandle, InteractionBuilder> knownBuilder = new HashMap<>();

    protected RTIambassador rtiAmbassador;
    protected InteractionClassHandle messageId;
    protected ParameterHandleValueMap parameters;
    protected EncoderFactory encoderFactory;
    protected HashMap<String,ParameterHandle> parameterHandles;

    public InteractionBuilder (RTIambassador rtiAmbassador, String messageName) throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
        this.rtiAmbassador = rtiAmbassador;
        this.messageId = rtiAmbassador.getInteractionClassHandle(messageName);
        this.parameters = rtiAmbassador.getParameterHandleValueMapFactory().create(1);
        this.encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
        this.parameterHandles = new HashMap<>();
        knownBuilder.put(this.messageId, this);
    }
    
    public InteractionBuilder addParameter (String parameterName, byte[] value) throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        ParameterHandle handle = this.rtiAmbassador.getParameterHandle(this.messageId, parameterName);
        parameterHandles.put(parameterName, handle);
        parameters.put(handle, value);
        return this;
    }

    public Interaction build() {
        Interaction aggregate = new Interaction(messageId, parameters, parameterHandles);
        return aggregate;
    }

    public static Interaction parse (InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters) {
        InteractionBuilder builder = knownBuilder.get(interactionClass);
        if (builder != null) {
            return new Interaction(interactionClass,theParameters,null);
        }
        
        return null;
    }
}


