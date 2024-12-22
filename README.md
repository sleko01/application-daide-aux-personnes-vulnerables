# Application d'aide aux personnes vulnérables

## Description
This project is an application written entirely in Java that helps vulnerable people find volunteers to help them with
anything they might need. Vulnerable people can add a request for help and volunteers can then accept the request. On
the other hand, volunteers can add an offer which vulnerable people can accept.

The application is divided into two parts: the client and the server. The client is a Java Swing application that
allows users to interact with the server. The server is a Java Spring Boot application that manages the requests, offers
and does the communication with the database.

## Features
After registration, each user must be approved by an admin before being able to use the application. The admin can approve
or deny the user registration. After approval, the user can log in, use the application and submit requests or offers
depending on if they are a vulnerable person or volunteer.

After submitting a request/offer, it is marked as PENDING before the admin approves it. The admin can also deny the request
or offer. If the request/offer is approved, it is marked as APPROVED and other users can see it and accept it. After the
user accepts the request/offer, it is marked as IN PROGRESS and other users can no longer accept it. The user who accepted
the request/offer can then mark it as COMPLETED and in that process rate the user who submitted the request/offer.

Each user has a rating that is calculated based on the ratings they received from other users. Each request can be rated
between 1 and 10 and the user rating is based off of the average of all the ratings they received.

**There are 3 accounts already created for the purposes of demonstrating the application. You must use the admin account
to approve any newly created accounts, apart from these 3 accounts which are all already approved**
1. Admin account: username = admin, password = admin
2. Volunteer account: username = volunteer, password = volunteer
3. Vulnerable account: username = vulnerable, password = vulnerable

**The application currently lacks the ability for the other side to rate the user who completed their request/offer, 
meaning only one side can rate the request (the user that completed the request).**

## Running the application
Since the application itself is divided into two separate parts, the startup is also divided into two parts.
To run the Java Swing application, you must position yourself within the `Swing` directory and run the following commands:
```mvn clean compile```
```mvn exec:java```

To run the Spring Boot application, you must position yourself within the `Server` directory and run the following command:
```mvn clean compile```
```mvn spring-boot:run```
