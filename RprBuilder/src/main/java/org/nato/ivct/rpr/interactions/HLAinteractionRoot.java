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

package org.nato.ivct.rpr.interactions;

import org.nato.ivct.rpr.HLAroot;
import org.nato.ivct.rpr.RprBuilderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map.Entry;

import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.InvalidParameterHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.InteractionClassHandle;

public class HLAinteractionRoot extends HLAroot {

    class ParameterHolder {
        ParameterHandle handle;
        DataElement data;
        Boolean isUpdated;
        public ParameterHolder(DataElement value) {
            data = value;
            isUpdated = false;
            handle = null;
        }
    }

    protected static final Logger log = LoggerFactory.getLogger(HLAinteractionRoot.class);
    private static HashMap<String,ParameterHandle> knownParameterHandles = new HashMap<>();  // known attribute handles
    private HashMap<String, ParameterHolder> parameterMap = new HashMap<>();
    private InteractionClassHandle interactionClassHandle = null;


    public InteractionClassHandle getInteractionClassHandle() {
        return interactionClassHandle;
    }


    /**
     * Root constructor for all derived interactions. Must be called
     * within each subclass constructor (via 'super()')
     */
    public HLAinteractionRoot() throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        if (interactionClassHandle == null) {
            interactionClassHandle = HLAroot.getRtiAmbassador().getInteractionClassHandle(getHlaClassName());
        }
        log.trace("interaction {} created", interactionClassHandle);
    }

    public void decode (ParameterHandleValueMap theParameters) throws DecoderException {
        log.trace("decoding interaction class {} ", getHlaClassName());
        for (Entry<ParameterHandle, byte[]> entry : theParameters.entrySet()) {
            ParameterHolder holder = parameterMap.get(getHandleString(entry.getKey()));
            if (holder == null) {
                log.warn("unknown parameter handle {}. Decoding skipped.", entry.getKey());
                continue;
            }
            holder.data.decode(entry.getValue());
            holder.isUpdated = true;
        }
    }

    protected void setParameter (String parameterName, byte[] value) {
        ParameterHolder dataHolder = parameterMap.get(parameterName);
        if (dataHolder != null) {
            log.trace("set parameter {}->{} = {}", this.getHlaClassName(), parameterName, value.toString());
            try {
                dataHolder.data.decode(value);
                dataHolder.isUpdated = true;
            } catch (DecoderException e) {
                log.error("set failed to decode for parameter {}->{}", this.getHlaClassName(), parameterName);
            }
        } else {
            log.error("set failed for unknown parameter {}->{}", this.getHlaClassName(), parameterName);
        }
    }

    protected void addParameter (String parameterName, DataElement value) {
        parameterMap.put(parameterName, new ParameterHolder(value));
        log.trace("add parameter {}->{} = {}", this.getHlaClassName(), parameterName, value.toString());
    }

    protected DataElement getParameter (String parameterName) {
        ParameterHolder holder = parameterMap.get(parameterName);
        if (holder != null) {
            return holder.data;
        }
        return null;
    }

    /**
     * The clear method will reset the update status of all known parameter holders. Values encoded 
     * in the DataElements will be kept. 
     */
    public void clear() {
        log.trace("reset update status {} ", this.getHlaClassName());
        for (Entry<String, ParameterHolder> parameter: parameterMap.entrySet()) {
            parameter.getValue().isUpdated = false;
        }
    }

    protected ParameterHandleValueMap getParameterHandleValueMap() throws FederateNotExecutionMember, NotConnected, RprBuilderException {
        int nbValues = 0;
        for (Entry<String, ParameterHolder> entry: parameterMap.entrySet()) {
            if (entry.getValue().isUpdated) {
                nbValues++;
            }
        }
        ParameterHandleValueMap attributeValues = HLAroot.getRtiAmbassador().getParameterHandleValueMapFactory().create(nbValues);
        for (Entry<String, ParameterHolder> entry: parameterMap.entrySet()) {
            if (entry.getValue().isUpdated) {
                attributeValues.put(getParameterHandle(entry.getKey()), entry.getValue().data.toByteArray());
            }
        }
        return attributeValues;        
    }


    public void send() throws InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        HLAroot.getRtiAmbassador().sendInteraction(interactionClassHandle, getParameterHandleValueMap(), null);
    }

    public void subscribe() throws FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        HLAroot.getRtiAmbassador().subscribeInteractionClass(interactionClassHandle);
    }

    public void publish() throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        HLAroot.getRtiAmbassador().publishInteractionClass(interactionClassHandle);
    }

    public ParameterHandle getParameterHandle(String name) {
        ParameterHandle handle = knownParameterHandles.get(name);
        if (handle == null) {
            try {
                handle = HLAroot.getRtiAmbassador().getParameterHandle(interactionClassHandle, name);
            } catch (NameNotFound | InvalidInteractionClassHandle | FederateNotExecutionMember | NotConnected
                    | RTIinternalError | RprBuilderException e) {
                log.error("Parameter name not found", e);
            }
            knownParameterHandles.put(name,handle);
        }
        return handle;
    }


    /**
     * Helper function to get the name of a cached attribute handle. If handle is unknown, a null string is returned.
     * 
     * @param handle
     * @return
     */
    protected String getHandleString(ParameterHandle handle) {
        for (Entry<String, ParameterHandle> entry : knownParameterHandles.entrySet()) {
            if (handle.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        try {
            String handleName = HLAroot.getRtiAmbassador().getParameterName(interactionClassHandle, handle);
            knownParameterHandles.put(handleName, handle);
            return handleName;
        } catch (InteractionParameterNotDefined | InvalidParameterHandle | InvalidInteractionClassHandle
                | FederateNotExecutionMember | NotConnected | RTIinternalError | RprBuilderException e) {
            log.error("ParameterHandle not found", e);
        }
        return null;
    }

    
    /**
     * Get class name in HLA-style as full path with '.' separators. 
     * 
     * @return
     */
    public String getHlaClassName() {
        return getHlaClassName("HLAinteractionRoot");
    }    

}
