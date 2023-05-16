/**
 * Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http: //www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License. 
 */

package org.nato.ivct.rpr.objects;

import org.nato.ivct.rpr.RprBuilderException;

import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public class Munition extends PhysicalEntity{
    
    public enum Attributes {
        LauncherFlashPresent
    }

    public Munition() throws RprBuilderException {
        super();
        try {
            setAttributeValue(Attributes.LauncherFlashPresent.name(), encoderFactory.createHLAboolean());
        } catch (NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | RTIinternalError
                | EncoderException e) {
        throw new RprBuilderException("error while creating member attributes", e);
       }
    }


    /**
     * TODO: Discuss if the pub/sub methods shall be made type safe. In that case the following two methods shall be private.
     */
    public void addSubscribe(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }
    public void addPublish(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(attribute.name()); 
    }

    /*
     * Type Safe pub/sub Methods
     */
    public void subscribeLauncherFlashPresent() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(Attributes.LauncherFlashPresent.name());
    }
    public void publishLauncherFlashPresent() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addPubAttribute(Attributes.LauncherFlashPresent.name());
    }

    // TODO: add remaining sub/pub helpers

    // attribute setter and getter

    public void setLauncherFlashPresent(Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getLauncherFlashPresent();
        holder.setValue(value);
        setAttributeValue(Attributes.LauncherFlashPresent.name(), holder);
    }
    public HLAboolean getLauncherFlashPresent() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.LauncherFlashPresent.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.LauncherFlashPresent.name(), attribute);
        }
        return attribute;
    }

}

