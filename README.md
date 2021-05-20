# dmn-anonymizer

Is a simple tool to anonymize names and type references in your DMN model.
The motivation to create such a tool was to have an option use in public github repository a DMN model coming from enterprise.

## Build
```
mvn clean package
```

## Usage
```
java -jar target/dmn-anonymizer-1.0-SNAPSHOT.jar /path/to/your/model.dmn
``` 

## Result
Is stored in
```
./anonym.dmn
```