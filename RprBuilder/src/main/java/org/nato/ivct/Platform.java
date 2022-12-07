package org.nato.ivct;

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

    public Platform() throws Exception {
        super();
    }

    public void subscribeAfterburnerOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(Attributes.AfterburnerOn.name());
    }
    public void publishAfterburnerOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addPubAttribute(Attributes.AfterburnerOn.name());
    }

    // TODO: add remaining sub/pub helpers

    // TODO: add attribute setter and getter
}

