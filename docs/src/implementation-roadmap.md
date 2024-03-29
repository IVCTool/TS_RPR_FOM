# Test Case Implementation Roadmap

Implementation convention is to provide a dedicated test case for each _Interoperability Requirement_ (IR). A test case will be names like _TC\_\<IR id\>_.

Specification of Implementation Status 

- complete: test case logic complete 
- basic: draft implementation of basic test logic 
- template: Implementation template provided

The prioritization of the test case implementation is given by the requirements of the Bw-Lw badge. 

![Bw.Lw](images/badge-rpr.drawio.svg)


## RPR FOM Entity Module


| TC | Status | Comment |
| --- | --- | --- |
| TC_IR_RPR_0008 | [complete](/RPR_Entity/src/main/java/org/nato/ivct/rpr/entity/TC_IR_RPR2_0008.java) | Current implementation verifies the publication of a _PhysicalEntity_ object. Subscription is missing. |
| TC_IR_RPR_0009 | template | Test case template provided. |
| TC_IR_RPR_0010 | [complete](/RPR_Entity/src/main/java/org/nato/ivct/rpr/entity/TC_IR_RPR2_0011.java) | Registration of _PhysicalEntity_ subclass and publication of _EntityIdentifier_, _EntityType_, and _Spatial_ is tested. |
| TC_IR_RPR_0011 | template |   |
| TC_IR_RPR_0012 | [basic](/RPR_Entity/src/main/java/org/nato/ivct/rpr/entity/TC_IR_RPR2_0012.java)| prepared for Aircraft and AmphibiousVehicle, testet some Attributes with Aircraft   |
| TC_IR_RPR_0013 | template |   |
| TC_IR_RPR_0014 | template |   |
| TC_IR_RPR_0015 | template |   |
| TC_IR_RPR_0016 | template |   |

### Discussion

- TC_IR_RPR_0015
  - brfkar \"SuT shall assume default values for optional attributes\"  How can we know in a Test, what SUT assumes ? May be not testable in this way

## RPR FOM Warfare Module

| TC | Status | Comment | Further Information|
| -- | ------ | ------- | ------------------ |
| TC_IR_RPR_0017 | [basic](/RPR_Warfare/src/main/java/org/nato/ivct/rpr/warfare/TC_IR_RPR2_0017.java)  | Current implementation tests if _Munition_ objects are published. Subscription test and verification for tracked munition is missing. | [sequence diagram](images/tc-ir-rpr2-0017.drawio.png)
| TC_IR_RPR_0018 | [basic](/RPR_Warfare/src/main/java/org/nato/ivct/rpr/warfare/TC_IR_RPR2_0018.java) |Current implementation requests MOM reports for interaction subscriptions and publications. Decoding and verification of received reports is missing. | [sequence diagram](images/tc-ir-rpr2-0018.drawio.png) |
| TC_IR_RPR_0019 | -  | - |
| TC_IR_RPR_0020 |  -  | - |
| TC_IR_RPR_0021 |  -  | - |
| TC_IR_RPR_0022 |  -  | - |
| TC_IR_RPR_0023 |  -  | - |
| TC_IR_RPR_0024 |  -  | - |
| TC_IR_RPR_0025 |  -  | - |
| TC_IR_RPR_0026 |  -  | - |
| TC_IR_RPR_0027 |  -  | - |
| TC_IR_RPR_0028 |  -  | - |
| TC_IR_RPR_0029 |  -  | - |
| TC_IR_RPR_0030 |  -  | - |
| TC_IR_RPR_0031 |  -  | - |
| TC_IR_RPR_0032 |  -  | - |
| TC_IR_RPR_0033 |  -  | - |
| TC_IR_RPR_0034 |  -  | - |



## RPR FOM Physical Module 


| TC | Status | Description | Further Information|
| -- |------- | ----------- |------- |
| TC_IR_PRP_PHY_0001 |[basic](/RPR_Physical/src/main/java/org/nato/ivct/rpr/physical/TC_IR_RPR2_PHY_0001.java)| TC creates/changes randomly (nearly) all of the PhysicalEntity attributes in a short Time (10ms) and e.g. 200 times. It may be the job of the operator to see if the federate get problems. |[sequence diagram](/docs/src/sequencediagrams/TC_IR_PRP_PHY_0001.drawio.png)|
| TC_IR_PRP_PHY_0002 | - | - |
| TC_IR_PRP_PHY_0003 |[basic](/RPR_Physical/src/main/java/org/nato/ivct/rpr/physical/TC_IR_RPR2_PHY_0003.java) | TC changes randomly all of the Platform attributes (16) in a short Time (10ms) and e.g. 200 times. It may be the job of the operator to see if the federate get problems. | [sequence diagram](/docs/src/sequencediagrams/TC_IR_PRP_PHY_0003.drawio.png)|
| TC_IR_PRP_PHY_0004 |[basic](/RPR_Physical/src/main/java/org/nato/ivct/rpr/physical/TC_IR_RPR2_PHY_0004.java) | prepared for Aircraft and other,  testet some Attributes with AircraftApp |
| TC_IR_PRP_PHY_1001 | - | - |
| TC_IR_PRP_PHY_1002 | - | - |
| TC_IR_PRP_PHY_1003 | - | - |
| TC_IR_PRP_PHY_1004 | - | - |

### Discussion

- IR-RPR-PHY-0001
  - Vito: Need to investigate how this could be tested. One option is to create/publish all defined attributes to stress the SuT. 
  - Reinhard: As you propose, I could imagine that the  TC creates a physical entity object and permutates all possible attributes and observes if the SuT has any problems with that.
  

- IR-RPR-PHY-0002: 
  - Vito: It seams reasonable to publish only explicitly defined attributes. On the other hand any additional fields published should not have any impact on the simulation.
  - Reinhard: that is the same as TC_IR_RPR_0012. I guess the question is, weather ‘not appropriate’ means ‘not valid’? As I do not know anything else, I would say yes. 

- IR-RPR-PHY-0003: 
  - Vito: Attributes defined in this table are the ones present in the RPR.
Requirement IR-RPR-PHY-0001 already states that all attributes of Physical class and subclasses are optional. Is this requirement redundant?
  - Reinhard: This IR is about attributes from Platform, and also about the default values. And this is probably very hard to test without access to the user interface, as it is only evaluated inside the SuT. I guess the idea behind that requirement is, that a consuming federate must tolerate attributes in any combination. Maybe the permutation approach would be use here as well. 

- IR-RPR-PHY-0004: 
  - Vito: Is seams reasonable to publish only explicitly defined attributes. On the other hand any additional fields published should not have any impact on the simulation.
  - Reinhard: same as IR-RPR-PHY-0002, and I would vote for strict interpretation

- IR-RPR-PHY-1001, IR-RPR-PHY-1002: 
  - Vito: I am wondering, how this could be tested? RPR does not specify any bounding mesh on physical entities. How to test scenario where you have a separate simulation federate used for collision detection and response. Could this be a valid use case and how to approach it? Is IVCT capable of spawning multiple federates acting as a single SuT?

  - Reinhard: good observation. I guess the question is, which federate detects a collision? For example, if the TC creates an object and hit with that object another object owned by the SuT, shall the SuT detect this, or the TC? I have no answer for that. The formulation “there shall be a collision interaction…” is not clear enough. This need further refinements. 



## RPR FOM Distributed Emission Regeneration Module 


| TC | Status | Description |
| ---| ------ | ----------- |
| TC_IR_RPR_DER_0001| - | - |
| TC_IR_RPR_DER_0002| - | - |
| TC_IR_RPR_DER_0003| - | - |
| TC_IR_RPR_DER_0004| - | - |
| TC_IR_RPR_DER_0005| - | - |
| TC_IR_RPR_DER_0006| - | - |
| TC_IR_RPR_DER_0007| - | - |
| TC_IR_RPR_DER_0008| - | - |
| TC_IR_RPR_DER_0009| - | - |
| TC_IR_RPR_DER_0010| - | - |
| TC_IR_RPR_DER_0011| - | - |
| TC_IR_RPR_DER_0012| - | - |
| TC_IR_RPR_DER_0013| - | - |
| TC_IR_RPR_DER_0014| - | - |
| TC_IR_RPR_DER_0015| - | - |
| TC_IR_RPR_DER_0016| - | - |


## RPR FOM Communication Module 


| TC | Status | Description |
| ---| ------ | ----------- |
| RC_IR_RPR_COM_0001 | - | - |
| RC_IR_RPR_COM_0002 | - | - |
| RC_IR_RPR_COM_0003 | - | - |
| RC_IR_RPR_COM_0004 | - | - |
| RC_IR_RPR_COM_0005 | - | - |
| RC_IR_RPR_COM_0006 | - | - |
| RC_IR_RPR_COM_0007 | - | - |
| RC_IR_RPR_COM_0008 | - | - |
| RC_IR_RPR_COM_0009 | - | - |
| RC_IR_RPR_COM_0010 | - | - |
| RC_IR_RPR_COM_0011 | - | - |
| RC_IR_RPR_COM_0012 | - | - |
| RC_IR_RPR_COM_1001 | - | - |
| RC_IR_RPR_COM_1002 | - | - |
| RC_IR_RPR_COM_1003 | - | - |
| RC_IR_RPR_COM_1004 | - | - |


## RPR FOM Aggregate Module 

| TC | Status | Description |
| ---| ------ | ----------- |
| IR-RPR2-0001 | -  | SuT shall comply with SISO-STD-001-2015, Standard for Guidance, Rationale, and Interoperability Modalities for the Real-time Platform Reference Federation Object Model, Version 2.0, 10 August 2015 |
| IR-RPR2-0002 | -  | SuT shall define BaseEntity.AggregateEntity as published or define a subclass of BaseEntity.AggregateEntity as published and/or define BaseEntity.AggregateEntity as subscribed in CS/SOM. |
| IR-RPR2-0003 | -  | SuT shall update the following required attributes for AggregateEntity object instances registered by SuT: AggregateState, Dimensions, EntityIdentifier, EntityType, Spatial. |
| IR-RPR2-0004 | -  | SuT shall assume default values for optional attributes on instances of AggregateEntity object class. |
| IR-RPR2-0005 | -  | SuT shall assume default values for optional attributes on instances of AggregateEntity object class. |
| IR-RPR2-0006 | -  | SuT shall not rely on updates of optional attributes on instances of AggregateEntity object class. |
| IR-RPR2-0007 | -  | SuT shall be configurable for the following parameters: SiteID, ApplicationID. |
