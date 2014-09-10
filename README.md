jleaser
=======

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
- nonThreadSafe: these test need to be run sequentially

```
mvn test -DunitTestSuite=nonThreadSafe
```

#### Integration
- unix: specific tests for unix platforms
- macOsx: specific tests for Mac OSX
- windows: specific tests for Windows

```
mvn verify -DintegrationTestSuite=unix
```