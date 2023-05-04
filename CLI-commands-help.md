# Helpful CLI commands during Kalix application development and deployment:

## Running application locally on your machine

### First, run the docker file from root of project folder
```sh
docker-compose up
```

### Then run the application
```sh
mvn exec:exec
```

# Deploying application on Kalix environment

### From the application's root folder, run following to compile, package, build a docker image and push to the docker registry defined in pom file.
```sh
mvn clean compile package docker:build docker:push
```

#### In the logs, you should see similar like this: [INFO] DOCKER> Tagging image klx-wrk/loan-application-13420:5.0-SNAPSHOT successful!

#### Now once the image is pushed, let's deploy the microservice in Kalix
#### make sure your cli is authenticated to Kalix console, help doc here: https://docs.kalix.io/kalix/kalix_auth_login.html

### List available projects
```sh
kalix projects list
```

#### kalix-training project is created by admin, and make sure you are added into this project by the admin (reach out to Rushi if you are not already there)
### Set kalix-training active project or switch from another project
```sh
kalix config set project kalix-training
```

### Get services within the project
```sh
kalix services list
```

### Deploy your Kalix microservice, make sure you name 'loanapp' to your unique service name like 'loanapp-{last-4-digits-of-your-id}'' so that service name does not clash and override with other users
```sh
kalix service deploy loanapp-13420 registry.digitalocean.com/klx-wrk/loan-application-13420:3.0-SNAPSHOT
```

### List services again, should show your service 'loanapp-13420' ready in about a minute or so.
```sh
kalix services list
```

#### You can also restart your service, delete your service and do a bunch more stuff, doc here: https://docs.kalix.io/kalix/kalix_services.html

#### Once your service shows ready after issuing 'kalix service list', you can expose your service and test it, make sure you include the ACL as in line #14 in https://github.com/ceecer1/loan-application-13420/blob/step2/src/main/proto/io/kx/loanapp/kalix_policy.proto

#### ACL documentation for Java Protobuf SDK is here: https://docs.kalix.io/java-protobuf/access-control.html

#### You need to recompile, rebuild the image, push and deploy the new version.

### Expose your service, this will auto-generate a hostname and a route to your service.
```sh
kalix service expose loanapp-13420
```

### You can list route and get below response:
```sh
kalix route list
```

```
NAME      		HOSTNAME                                         PATHS        		CORS ENABLED   STATUS   
loanapp-13420   shy-flower-2473.westpac-ap-southeast-2.kalix.app   /->loanapp-13420   false          Ready  
```




#### You can issue following grpc calls from your terminal to access the exposed grpc service endpoints over the Internet.

### Submit a Loan application

```sh
grpcurl -d '{"loan_app_id": "some_uuid", "client_id": "client100", "client_monthly_income_cents": 100000, "loan_amount_cents": 200000, "loan_duration_months": 28}' shy-flower-2473.westpac-ap-southeast-2.kalix.app:443 io.kx.loanapp.api.LoanAppService/Submit
```
```sh
Expected response:
{

}
```



### Retrieve a Loan application
```sh
grpcurl -d '{"loan_app_id": "some_uuid"}' shy-flower-2473.westpac-ap-southeast-2.kalix.app:443 io.kx.loanapp.api.LoanAppService/Get
```

```sh
Expected response:

{
"client_id": "client100",
"client_monthly_income_cents": "1000000",
"loan_amount_cents": "4000000",
"loan_duration_months": 28
}
```
