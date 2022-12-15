# RPR FOM Test Suite

This repository is intended as in incubating project for the development of the RPR FOM interoperability badge test cases. Currently it is only a draft and contains some initial implementation examples.

The test cases are designed to be used within the IVCT framework, but they can also be executed from within a JUnit test. Each test case is effectively a individual *High Level Architecture* (HLA) federate and will be connected to *System under Test* (SuT) via a HLA *Run-Time Infrastructure* (RTI).

The test cases are designed to verify a single interoperability requirement and they are grouped into test suites. The test suites are mapped to sub-projects, like the [RPR_Entity](/RPR_Entity/). To build and start a specific test suite, the gradle build can be used like this:

> .\gradlew :RPR_Entity:test

Individual test cases may be started with a test filter like this:

> .\gradlew :RPR_Entity:test --tests TC_IR_RPR2_0008Test

The JUnit test pattern is widely used and most IDE will support them. 

## Interoperability Requirements

The test suite is designed to test the [Interoperability Requirements](docs/src/interoperability-requirements.md) defined RPR FOM modules.

## RPR FOM Builder

In order to ease the use of the extensive RPR FOM model elements, several [encoding helper](docs/src/rpr-builder-encoding.md) are implemented. Currently, only very few OMT helper elements are available. 

## Test Suite Modules

The test suite modules are aligned with the RPR FOM modules.

- [Entity](docs/src/ts-entity.md)

## Reference Federates

In order to demonstrate and verify the interoperability test cases, a *System under Test (SuT)* is required. This repository contains a simple placeholder for such a SuT, which is the [Aircraft federate](docs/src/rf-aircraft.md). 

The *Aircraft* federate can be build and started with the gradle build command:

> .\gradlew :RefFedAircraft:run

## IVCT Compliance

The current test suite design uses the ServiceLoader interface feature of the IVCT framework. This is only available in the latest snapshot releases and not yet included in the production release. 


## LICENCE

Copyright 2022 NATO/OTAN

Licensed under the Apache License, [Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
