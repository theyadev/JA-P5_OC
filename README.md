# Database
- MySQL

A script can be found unded `/ressourcs/sql/script.sql` to create the database and the tables.

# Testing

## Backend
- JUnit 5
- Mockito

### Run tests
To run the tests, you can use the following command:
```bash
mvn test
```
### Coverage
The coverage report is generated in the `target/site/jacoco` folder. You can open the `index.html` file to see the report.


## Frontend
- Jest
- Cypress

### Run tests
To run the tests, you can use the following command:
```bash
npm run test
```

### Run e2e tests
To run the e2e tests, you can use the following command:
```bash
npm run e2e
```

### Coverage
To generate the coverage report, you can use the following command:
```bash
npm run test:coverage
```

### e2e Coverage
To generate the e2e coverage report, you can use the following command:
```bash
npm run e2e:coverage
```
