Contribution
============
#Etiquette
Contributors are welcome. 

[Open Source Contribution Etiquette](http://tirania.org/blog/archive/2010/Dec-31.html) is a great article to read on things to consider.

## Submitting Helpful Issues
_Coming Soon_
## Creating Good Pull Requests
_Coming Soon_

# Development
## Setup
_Coming Soon_

## Running Tests
Tests can be run as apart of the maven life cycle
### Unit Tests
```
mvn test
```

### Integration Tests
```
mvn verify
```

### Other Tests
There are some tests that aren't included in the unit or integration tests because they are unreliable or are platform
dependant.

#### Unit
- quarantined: currently there is nothing in this suite but can be used to isolated tests from the main unit tests.
Possible reasons might be failing, unreliable, slow unit tests.

```
mvn test -DunitTestSuite=quarantined
```

#### Integration
- unix: specific tests for unix platforms
- macOsx: specific tests for Mac OSX
- windows: specific tests for Windows

```
mvn verify -DintegrationTestSuite=unix
```