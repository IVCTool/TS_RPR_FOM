package org.nato.rpr;

import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

import java.util.HashMap;
import java.util.Map;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;


/**
 * Container class for RTI interactions
 */
public class Interaction {

    public static Map<ParameterHandle, DataElement> knownDataElements = new HashMap<>();

    private final InteractionClassHandle classHandle;   public InteractionClassHandle getClassHandle() { return classHandle; }
    private final ParameterHandleValueMap parameters;   public ParameterHandleValueMap getParameters() { return parameters; }
    protected final HashMap<String,ParameterHandle> parameterHandles;

    public Interaction(InteractionClassHandle messageId, ParameterHandleValueMap parameters, HashMap<String,ParameterHandle> handles) {
        this.classHandle = messageId;
        this.parameters = parameters;
        this.parameterHandles = handles;
    }

    public void clear() {
        parameters.clear();
    }

    public void setParameter(String keyName, DataElement value) throws Exception {
        ParameterHandle handle = parameterHandles.get(keyName);
        if (handle == null) {
            throw new Exception("key not found");
        }
        parameters.put(handle, value.toByteArray());
        knownDataElements.put(handle, value);
    }

    public ByteWrapper getParameter(String keyName) throws Exception {
        ParameterHandle handle = parameterHandles.get(keyName);
        if (handle == null) {
            throw new Exception("key not found");
        }
        return parameters.getValueReference(handle);
    }

    public void subscribe (RTIambassador rtiAmbassador) throws FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError  {
        rtiAmbassador.subscribeInteractionClass(classHandle);
    }

    public void publish (RTIambassador rtiAmbassador) throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        rtiAmbassador.publishInteractionClass(classHandle);
    }

    public void send (RTIambassador rtiAmbassador) throws InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
        rtiAmbassador.sendInteraction(classHandle, parameters, null);
    }
}