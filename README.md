# RPR FOM Test Suite

This repository is intended as in incubating project for the development of the RPR FOM interoperability badge test cases. Currently it is only a draft and contains some initial implementation examples.

## Interoperability Requirements

The test suite is designed to test the [Interoperability Requirements](docs/src/interoperability-requirements.md) defined RPR FOM modules.

## RPR FOM Builder

In order to ease the use of the extensive RPR FOM model elements, several [encoding helper](docs/src/rpr-builder-encoding.md) are implemented. Currently, only very few OMT helper elements are available. 

## Test Suite Modules

The test suite modules are aligned with the RPR FOM modules.

- [Entity](docs/src/ts-entity.md)

## Reference Federates

In order to demonstrate and verify the interoperability test cases, a *System under Test (SuT)* is required. This repository contains a simple placeholder for such a SuT, which is the [Aircraft federate](docs/src/rf-aircraft.md). 

## IVCT Compliance

The current test suite design uses the ServiceLoader interface feature of the IVCT framework. This is only available in the latest snapshot releases and not yet included in the production release. 


## LICENCE

Copyright 2022 NATO/OTAN

Licensed under the Apache License, [Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
