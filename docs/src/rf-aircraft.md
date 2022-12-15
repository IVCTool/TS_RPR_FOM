# RPR FOM Reference Federate Aircraft

The [aircraft application](/RefFedAircraft/src/main/java/org/nato/ivct/refFed/AircraftApp.java) is a simple HLA federate. It can be configurable to perform correct and incorrect behavior patterns.

The aircraft app is a command line tool with some configuration options. 

```
    [-rtiHost rtiHost], e.g. -rtiHost localhost or -rtiHost localhost:8989, default: localhost
    [-federationName federationName], default: TestFederation
    [-federateName federateName], default: RefFedAircraft
    [-pFA          | -FlyAircraft]
    [-pMMA         | -provokeMissingMandatoryAttribute]
    [-pDRE         | -provokeDeadReckoningError]
```
