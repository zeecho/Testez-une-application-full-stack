### Install the database
The project uses mysql. You need to have it installed on your machine.

There is an SQL script for creating the schema and adding some basic data here: `ressources/sql/script.sql`

You can import all of this by creating a database through your mysql console. 

First let's open the mysql console with (using your credentials):
`mysql -u USERNAME -pPASSWORD`

and then you can create a database like this (the user you're using must have the rights to do so):
`CREATE DATABASE test;`

if you want to use another name than "test" for the database you can choose whatever you want).

Once done, you can quit the mysql console (Ctrl+D).

And then you can launch this command (still using your credentials and the name you gave to your DB instead of test if neeeded):
`mysql -u USERNAME -pPASSWORD test < ressources/sql/script.sql`

You should now have everything you need in your db. 

Now think about editing the file `back/src/main/resources/application.properties`, replacing "test" by your database name, "user" by your actual username and password by your actual password:
spring.datasource.url=jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true
spring.datasource.username=user
spring.datasource.password=123456

### Install the app
#### Front
Go into the folder for the front code:
`cd front`

And then you need to install the dependencies:
`npm install`

#### Back
It's in the "back" folder. You must have java installed (tested with version 17).

### Run the app
#### Front
`cd front`
`npm run start`

By default the admin account is (if you imported the database from the script provided):
- login: yoga@studio.com
- password: test!1234

#### Back
`cd back`
`mvn clean install`

### Launch tests
#### Front
You only have to launch this command:
`npm run test`

#### End-to-end
You have to launch this command:
`npm run e2e`

It should open a browser. Choose which one you prefer (it has been tested mostly with firefox). Then you can test different components and/or scenarios. The scenario named "all" launches all the scenarios (useful to generate the coverage report) but you can test specific parts of the app if you want.

#### Back
`cd back`

You can launch the tests (present in src/test) in your IDE or run:
`mvn clean test`

### Check test coverage
#### Front
`cd front`
`npx jest --coverage`

You should get the result in your console.

#### End-to-end
`cd front`

Make sure to launch this first:

`npm run e2e`

It should launch a browser. Then you should launch the tests (file named "all"). You need to wait for this to end before running the coverage command which is this one:

`npm run e2e:coverage`

Then the report is generated here: front/coverage/lcov-report/index.html.
You can open it in your browser.

#### Back
`cd back`
`mvn clean test jacoco:report`
