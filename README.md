# Warehouse software 
Service that implements a simple warehouse digital platform. 

Its main functionalities are processing files to update the inventory stock 
and the product catalog, along with retrieving the maximum purchasable quantity
for each product, removing products from the catalogue and even selling a product if possible.


The product is build on Java, using the Spring Boot framework

It uses JPA to store data into a database.
NOTE: For simplicity, we have used an H2 database that is initialized every time the application is booted.
In a real-world scenario, we would have used an external instance, hosted in a cloud platform, to store all the data.


### Prerequisites
Prerequisites to install

* Java 17
* Gradle 7.4
* Postman

### Build
Step by step build instructions.

1. cd into the project directory ~/../warehouse-software

2. gradle clean build

3. gradle bootRun

### Functional testing

A postman collection called warehouse-endpoints has been included in the root directory of the project. This collection can be imported into 
Postman, and it contains different endpoints that can be used as base to test the different functionalities of the 
application.