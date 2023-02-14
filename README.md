# RPR FOM Test Suite

This repository is intended as in incubating project for the development of the RPR FOM interoperability badge test cases. Currently it is only a draft and contains some initial implementation examples.

The test cases are designed to be used within the IVCT framework, but they can also be executed from within a JUnit test. Each test case is effectively a individual *High Level Architecture* (HLA) federate and will be connected to *System under Test* (SuT) via a HLA *Run-Time Infrastructure* (RTI).

The test cases are designed to verify a single interoperability requirement and they are grouped into test suites. The test suites are mapped to sub-projects, like the [RPR_Entity](/RPR_Entity/). To build and start a specific test suite, the gradle build can be used like this:

> .\gradlew :RPR_Entity:test

Individual test cases may be started with a test filter like this:

> .\gradlew :RPR_Entity:test --tests TC_IR_RPR2_0008Test

The JUnit test pattern is widely used and most IDE will support them. 

## Interoperability Requirements

In order to verify if a RPR FOM federate can be considered standard compliant, several interoperability requirements have been defined. These requirements are structured groups, which are called badges. Such a badge stands for a certain compliance group. A federate is typically not using all of the RPR FOM elements and is therefor only compliant to a some of these badges.  

The test suites are designed to test these [Interoperability Requirements](docs/src/interoperability-requirements.md).

## RPR FOM Builder

In order to ease the use of the extensive RPR FOM model elements, several [encoding helper](docs/src/rpr-builder-encoding.md) are implemented. Currently, only very few OMT helper elements are available. 

## Test Suite Modules

The test suite modules are aligned with the RPR FOM modules.

- [Entity](docs/src/ts-entity.md)

## Reference Federates

In order to demonstrate and verify the interoperability test cases, a *System under Test (SuT)* is required. This repository contains a simple placeholder for such a SuT, which is the [Aircraft federate](docs/src/rf-aircraft.md). 

The *Aircraft* federate can be build and started with the gradle build command:

> .\gradlew :RefFedAircraft:run --args="-provokeFlyAircraft"

## IVCT Compliance

The current test suite design uses the ServiceLoader interface feature of the IVCT framework. This is only available in the latest snapshot releases and not yet included in the production release. 


## LICENSE

Copyright 2022 NATO/OTAN

Licensed under the Apache License, [Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
