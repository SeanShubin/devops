# Developer Operations Utilities
The intent is to provide libraries in order to easily automate provisioning and installation.
Anything you can do with the following, you should be able to do with this library. 
- local console commands
- ssh console commands
- amazon web services

## Amazon Web Services EC2
Here I provision an EC2 instance, then terminate it.
The region, keypair, and image template (Amazon Machine Image) are all hardcoded constants, so to that extent the integration test is not portable.
The function that lists the instances is named listInstancesNotTerminated, instead of listInstances.
Once an instance is terminated it is effectively inaccessible for any useful purpose, yet it still shows up for a while in the Amazon Management Console.
While the name I choose leaks some amazon implementation details from the abstraction, the name also makes it very clear why there might be a discrepancy between what the API shows and what the Amazon Management Console shows.  

- [Integration Test](src/test/kotlin/com/seanshubin/devops/Ec2ProvisionIntegrationTest.kt)
- [Contract](src/main/kotlin/com/seanshubin/devops/Ec2Api.kt)
- [Implmementation](src/main/kotlin/com/seanshubin/devops/Ec2ApiWithAmazonEC2Async.kt)
