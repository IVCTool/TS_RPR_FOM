package org.nato.ivct;

public class PhysicalEntity extends BaseEntity {

    public enum Attributes {
        AcousticSignatureIndex,
        AlternateEntityType,
        ArticulatedParametersArray,
        CamouflageType,
        DamageState,
        EngineSmokeOn,
        FirePowerDisabled,
        FlamesPresent,
        ForceIdentifier,
        HasAmmunitionSupplyCap,
        HasFuelSupplyCap,
        HasRecoveryCap,
        HasRepairCap,
        Immobilized,
        InfraredSignatureIndex,
        IsConcealed,
        LiveEntityMeasuredSpeed,
        Marking,
        PowerPlantOn,
        PropulsionSystemsData,
        RadarCrossSectionSignatureIndex,
        SmokePlumePresent,
        TentDeployed,
        TrailingEffectsCode,
        VectoringNozzleSystemData        
    }

    // TODO: create fields
    
    public String getClassName() { return "HLAobjectRoot.BaseEntity.PhysicalEntity"; }

    public PhysicalEntity() throws Exception {
        super();
    }

    // TODO: add remaining sub/pub helpers

    // TODO: add attribute setter and getter
}

