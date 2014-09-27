jleaser
=======

JLeaser is a central registry that manages exclusive leases on external resources across threads.
 
##Sample Use Case 
You have integration tests that you want to run in parallel but some of those tests require a single port 3306 (mysql). 
Running these tests in parallel would cause race conditions causing intermittent test failures.

JLeaser provides the ability to take a lease on a resource in this case port 3306. Once a lease is taken on a resources 
all other process trying to take a lease on port 3306 will wait until the resource is free before the continue processing. 

Take a lease on port 3360 
```java
    Resource mySqlPort = JLeaser.getLeaseOnPort("3306");
    mySqlPort.getResourceId(); //"3306
```

#Usage

##Localhost
_Coming Soon_
##Ports
_Coming Soon_
##Docker
_Coming Soon_