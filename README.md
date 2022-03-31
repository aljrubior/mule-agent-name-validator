## Install the Validator in the Mule Runtime

The corresponding JAR should be added under the lib folder within the mule-agent plugin, which is contained in the plugins folder of the Mule instance.

For example, $MULE_HOME/server-plugins/mule-agent-plugin/lib/mule-agent-properties-validator.jar.

## Mule Agent Name Validator Configuration

In the following configuration, we are going to implement rules for naming applications.

File: $MULE_HOME/conf/mule-agent.yml

```
services:
  mule.agent.artifact.validator.service:
    enabled: true
    validators:
    - type: nameValidator
      name: defaultNameValidator
      enabled: true
      args:
        # Ex. finance-prod-sapAccountsV1
        namePattern: '([a-z]{3,10}-[a-z]{2,4}-[a-zA-Z0-9]{5,20})'
```

### Log4j Configuration

File: $MULE_HOME/conf/log4j2.xml

```
 <AsyncLogger name="com.mycompany.agent" level="INFO"/>
```

### Test the Name Validator

Deploy an application that has an invalid name.

Command

```
curl -X PUT 'http://localhost:9999/mule/applications/finance.prod.sapAccountsV1' \
-H 'Content-Type: application/octet-stream' \
-F 'file=@"/repositories/apps/app-01.jar"'
```

Response

```
{
  "type": "class com.mycompany.agent.exceptions.InvalidNameException",
  "message": "Application 'finance.prod.sapAccountsV1' has an invalid name. Reason: Name 'finance.prod.sapAccountsV1' not match the pattern: '([a-z]{3,10}-[a-z]{2,3}-[a-zA-Z0-9]{5,20})'."
}
```

Logs

```
INFO  2022-03-30 18:41:01,960 [qtp1407261989-66] [processor: ; event: ] com.mulesoft.agent.services.application.AgentApplicationService: Deploying the finance.qa.accountSAPv1 application from jar file.
ERROR 2022-03-30 18:41:02,047 [qtp1407261989-66] [processor: ; event: ] com.mycompany.agent.MuleAgentNameValidator: Application 'finance.prod.sapAccountsV1' has an invalid name. Reason: Name 'finance.prod.sapAccountsV1' not match the pattern: '([a-z]{3,10}-[a-z]{2,3}-[a-zA-Z0-9]{5,20})'.
ERROR 2022-03-30 18:41:02,062 [qtp1407261989-66] [processor: ; event: ] com.mulesoft.agent.external.handlers.deployment.ApplicationsRequestHandler: Error performing the deployment of finance.prod.sapAccountsV1. Cause: InvalidNameException: Application 'finance.prod.sapAccountsV1' has an invalid name. Reason: Name 'finance.prod.sapAccountsV1' not match the pattern: '([a-z]{3,10}-[a-z]{2,3}-[a-zA-Z0-9]{5,20})'.
```