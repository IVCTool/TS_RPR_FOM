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

import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public class Platform extends PhysicalEntity{
    
    public enum Attributes {
        AfterburnerOn,
        AntiCollisionLightsOn,
        BlackOutBrakeLightsOn,
        BlackOutLightsOn,
        BrakeLightsOn,
        FormationLightsOn,
        HatchState,
        HeadLightsOn,
        InteriorLightsOn,
        LandingLightsOn,
        LauncherRaised,
        NavigationLightsOn,
        RampDeployed,
        RunningLightsOn,
        SpotLightsOn,
        TailLightsOn
    }

    public Platform() throws RprBuilderException {
        super();
    }

    /**
     * TODO: Discuss if class based subscription methods shall be used 
     */
    private static Platform anchor;
    public static void addPub(Attributes attribute) throws Exception {
        if (anchor == null) anchor = new Platform();
        anchor.addPubAttribute(attribute.name());
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
    public void subscribeAfterburnerOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(Attributes.AfterburnerOn.name());
    }
    public void publishAfterburnerOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addPubAttribute(Attributes.AfterburnerOn.name());
    }

    // TODO: add remaining sub/pub helpers

    // TODO: add attribute setter and getter
}

