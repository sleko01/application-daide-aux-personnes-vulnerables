CREATE TABLE IF NOT EXISTS Role
(
    roleId INT NOT NULL,
    roleName VARCHAR(100) NOT NULL,
    PRIMARY KEY (roleId)
);

CREATE TABLE IF NOT EXISTS appUser
(
    userId INT NOT NULL,
    roleId INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    rating INT NOT NULL,
    PRIMARY KEY (userId),
    FOREIGN KEY (roleId) REFERENCES Role(roleId)
);

CREATE TABLE IF NOT EXISTS status
(
    statusId INT NOT NULL,
    statusName VARCHAR(100) NOT NULL,
    PRIMARY KEY (statusId)
);

CREATE TABLE IF NOT EXISTS requestType
(
    requestTypeId INT NOT NULL,
    requestTypeName VARCHAR(100) NOT NULL,
    PRIMARY KEY (requestTypeId)
);

CREATE TABLE IF NOT EXISTS request
(
    requestId INT NOT NULL,
    requestTypeId INT NOT NULL,
    userId INT NOT NULL,
    statusId INT NOT NULL,
    rating INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    message VARCHAR(100) NOT NULL,
    PRIMARY KEY (requestId),
    FOREIGN KEY (requestTypeId) REFERENCES requestType(requestTypeId),
    FOREIGN KEY (userId) REFERENCES appUser(userId),
    FOREIGN KEY (statusId) REFERENCES status(statusId)
);

CREATE TABLE IF NOT EXISTS activity
(
    activityId INT NOT NULL,
    requestId INT NOT NULL,
    offerId INT NOT NULL,
    PRIMARY KEY (activityId),
    FOREIGN KEY (requestId) REFERENCES request(requestId),
    FOREIGN KEY (offerId) REFERENCES request(requestId)
);


INSERT INTO role VALUES (1, 'VulnerablePerson'), (2, 'Volunteer'), (3, 'Admin');
INSERT INTO status VALUES (1, 'Pending'), (2, 'Validated'), (3, 'Completed'), (4, 'Rejected');