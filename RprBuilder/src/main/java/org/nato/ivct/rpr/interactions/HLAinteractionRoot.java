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
import org.nato.ivct.rpr.OmtBuilder;
import org.nato.ivct.rpr.RprBuilderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;

import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.InteractionClassHandle;

public class HLAinteractionRoot extends HLAroot {

    protected static final Logger log = LoggerFactory.getLogger(HLAinteractionRoot.class);

    private InteractionClassHandle interactionClassHandle = null;

    public InteractionClassHandle getInteractionClassHandle() {
        return interactionClassHandle;
    }

    /** Private Field **/
    private static HashMap<String,ParameterHandle> knownParameterHandles = new HashMap<>();  // known attribute handles
    private ParameterHandleValueMap parameters = null;


    /**
     * Root constructor for all derived interactions. Must be called
     * within each subclass constructor (via 'super()')
     */
    public HLAinteractionRoot() throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        if (interactionClassHandle == null) {
            interactionClassHandle = OmtBuilder.getRtiAmbassador().getInteractionClassHandle(getHlaClassName());
        }
        parameters = OmtBuilder.getRtiAmbassador().getParameterHandleValueMapFactory().create(0);
        log.trace("interaction {} created", interactionClassHandle);
    }

    protected void setParameter (String parameterName, byte[] value) throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException, RprBuilderException {
        parameters.put(getParameterHandle(parameterName), value);
        log.trace("set parameter {}->{} = {}", this.getHlaClassName(), parameterName, value.toString());
    }

    protected byte[] getParameter (String parameterName) throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        return parameters.get(getParameterHandle(parameterName));
    }

    public void send() throws InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        OmtBuilder.getRtiAmbassador().sendInteraction(interactionClassHandle, parameters, null);
    }

    public void subscribe() throws FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        OmtBuilder.getRtiAmbassador().subscribeInteractionClass(interactionClassHandle);
    }

    public void publish() throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        OmtBuilder.getRtiAmbassador().publishInteractionClass(interactionClassHandle);
    }

    public ParameterHandle getParameterHandle(String name) throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        ParameterHandle handle = knownParameterHandles.get(name);
        if (handle == null) {
            handle = OmtBuilder.getRtiAmbassador().getParameterHandle(interactionClassHandle, name);
        }
        return handle;
    }
    
    public String getHlaClassName() {
        return getHlaClassName("HLAinteractionRoot");
    }


}
