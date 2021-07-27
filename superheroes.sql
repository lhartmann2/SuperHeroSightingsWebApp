DROP DATABASE IF EXISTS superHeroes;
CREATE DATABASE superHeroes;

USE superHeroes;

CREATE TABLE superPower(
	id INT PRIMARY KEY AUTO_INCREMENT,
    superPowerName VARCHAR(30) NOT NULL
);

CREATE TABLE hero(
	id INT PRIMARY KEY AUTO_INCREMENT,
    heroName VARCHAR(30) NOT NULL,
    heroDescription VARCHAR(255) NOT NULL,
    superPowerId INT NOT NULL,
    FOREIGN KEY (superPowerId) REFERENCES superPower(id)
);

CREATE TABLE org(
	id INT PRIMARY KEY AUTO_INCREMENT,
    orgName VARCHAR(30) NOT NULL,
    orgDescription VARCHAR(255) NOT NULL,
    orgAddress VARCHAR(128) NOT NULL,
    orgEmail VARCHAR(64) NOT NULL
);

CREATE TABLE location(
	id INT PRIMARY KEY AUTO_INCREMENT,
    locName VARCHAR(30) NOT NULL,
    locDescription VARCHAR(255) NOT NULL,
    locAddress VARCHAR(128),
    lat VARCHAR(24) NOT NULL,
    lon VARCHAR(24) NOT NULL
);

CREATE TABLE sighting(
	id INT PRIMARY KEY AUTO_INCREMENT,
	heroId INT NOT NULL,
    locId INT NOT NULL,
    sightingDate DATETIME(6) NOT NULL,
    FOREIGN KEY (heroId) REFERENCES hero(id),
    FOREIGN KEY (locId) REFERENCES location(id)
);

CREATE TABLE org_hero(
	orgId INT NOT NULL,
    heroId INT NOT NULL,
    PRIMARY KEY(orgId, heroId),
    FOREIGN KEY (orgId) REFERENCES org(id),
    FOREIGN KEY (heroId) REFERENCES hero(id)
);
    
    
    