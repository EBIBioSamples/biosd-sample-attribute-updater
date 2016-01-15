# README

The purpose of this command line application is to let user update already existing BioSamples (those who have a sample accession) with new characteristics.
This is just the first step of a multistep process internally documented within EBI.

## What do you need?
In order to make the application working you need a **config.properties** file containing three properties:
- **connectionString**: the url to the database, in the form of *jdbc:oracle:thin:@[db-url]:[db-port]:[db-name]*
- **databaseUser**: the user for access the database
- **databasePassword**: the user password to access database
