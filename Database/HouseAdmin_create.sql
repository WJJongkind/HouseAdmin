-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2018-11-12 16:03:15.27

-- tables
-- Table: Expenses
CREATE TABLE Expenses (
    ID nvarchar(300)  NOT NULL,
    Email nvarchar(100)  NOT NULL,
    Group_ID nvarchar(300)  NOT NULL,
    Date date  NOT NULL,
    Amount decimal(10,2)  NOT NULL,
    Comments nvarchar(max)  NULL,
    Category nvarchar(100)  NOT NULL,
    Type nvarchar(100)  NOT NULL,
    CONSTRAINT Expenses_pk PRIMARY KEY  (ID)
);

-- Table: Groups
CREATE TABLE Groups (
    ID nvarchar(300)  NOT NULL,
    Name nvarchar(100)  NOT NULL,
    Description nvarchar(200)  NOT NULL,
    Admin nvarchar(100)  NOT NULL,
    CONSTRAINT Groups_pk PRIMARY KEY  (ID)
);

-- Table: Income
CREATE TABLE Income (
    ID nvarchar(300)  NOT NULL,
    Date date  NOT NULL,
    Amount decimal(10,2)  NOT NULL,
    Comments nvarchar(300)  NULL,
    Group_ID nvarchar(300)  NOT NULL,
    Email nvarchar(100)  NOT NULL,
    Category nvarchar(100)  NOT NULL,
    CONSTRAINT Income_pk PRIMARY KEY  (ID)
);

-- Table: Users
CREATE TABLE Users (
    Email nvarchar(100)  NOT NULL,
    Hash nvarchar(300)  NOT NULL,
    Salt nvarchar(300)  NOT NULL,
    Name nvarchar(50)  NOT NULL,
    ActivationID nvarchar(300)  NOT NULL,
    CONSTRAINT Users_pk PRIMARY KEY  (Email)
);

-- Table: Users_Groups
CREATE TABLE Users_Groups (
    ID nvarchar(300)  NOT NULL,
    Email nvarchar(100)  NOT NULL,
    CONSTRAINT Users_Groups_pk PRIMARY KEY  (ID,Email)
);

-- foreign keys
-- Reference: Expenses_Users_Groups (table: Expenses)
ALTER TABLE Expenses ADD CONSTRAINT Expenses_Users_Groups
    FOREIGN KEY (Group_ID,Email)
    REFERENCES Users_Groups (ID,Email);

-- Reference: Groups_Users (table: Groups)
ALTER TABLE Groups ADD CONSTRAINT Groups_Users
    FOREIGN KEY (Admin)
    REFERENCES Users (Email);

-- Reference: Income_Users_Groups (table: Income)
ALTER TABLE Income ADD CONSTRAINT Income_Users_Groups
    FOREIGN KEY (Group_ID,Email)
    REFERENCES Users_Groups (ID,Email);

-- Reference: Users_Groups_Groups (table: Users_Groups)
ALTER TABLE Users_Groups ADD CONSTRAINT Users_Groups_Groups
    FOREIGN KEY (ID)
    REFERENCES Groups (ID);

-- Reference: Users_Groups_Users (table: Users_Groups)
ALTER TABLE Users_Groups ADD CONSTRAINT Users_Groups_Users
    FOREIGN KEY (Email)
    REFERENCES Users (Email);

-- End of file.

