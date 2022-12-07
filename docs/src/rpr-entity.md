# RPR FOM Entity Module Badge

Interoperability Requirements for the RPR FOM Base Module. 

![RPR-ENTITY-2.0.png](images/RPR-ENTITY-2.0.png)

| Short&nbsp;Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; | Description |
| ---------- | ----------- |
| IR-RPR2-0008 | SuT shall define at least one leaf object class of `BaseEntity.PhysicalEntity` as published and/or subscribed in CS/SOM. |
| IR-RPR2-0009 | SuT shall in CS specify the use of Articulated Parts for all published and subscribedBaseEntity.PhysicalEntity and subclasses. |
| IR-RPR2-0010 | SuT shall in CS specify the use of Dead-Reckoning algorithms for all published and subscribed BaseEntity.PhysicalEntity and subclasses. |
| IR-RPR2-0011 | SuT shall update the following required attributes for PhysicalEntity subclass object instances registered by SuT: EntityIdentifier, EntityType, Spatial. |
| IR-RPR2-0012 | SuT shall not update non-applicable PhysicalEntity Attributes as specified in Domain Appropriateness table in SISO-STD-001-2015. |
| IR-RPR2-0013 | SuT updates of instance attributes shall, for BaseEntity.PhysicalEntity and subclasses, be valid according to SISO-STD-001-2015 and SISO-STD-001.1-2015. |
| IR-RPR2-0014 | SuT updates of instance attribute Spatial shall, for BaseEntity.PhysicalEntity and subclasses, include valid Dead-Reckoning parameters for supported algorithms as specified in CS. |
| IR-RPR2-0015 | SuT shall assume default values for optional attributes on instances of BaseEntity.PhysicalEntity and subclasses according to SISO-STD-001-2015. |
| IR-RPR2-0016 | SuT shall not rely on updates of optional attributes on instances of BaseEntity.PhysicalEntity and subclasses. |
