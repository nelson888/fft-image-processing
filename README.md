# Desktop File Sharing Tool

This is an app that show an use case of Fourier series: image processing

## Screenshots

![alt text](https://raw.githubusercontent.com/nelson888/fft-image-processing/master/screenshots/sample0.png)

![alt text](https://raw.githubusercontent.com/nelson888/fft-image-processing/master/screenshots/sample1.png)

![alt text](https://raw.githubusercontent.com/nelson888/fft-image-processing/master/screenshots/sample2.png)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine and assume that you are in a linux environment.

### Prerequisite

You must have JavaFX installed (version 8.0.171)

### Installing

You can run this app from your IDE or generate the jar file by running
```
mvn install
```

### Running
To start the program, run the generated jar file
```
java -jar target/fft-2d-1.0-SNAPSHOT-jar-with-dependencies.jar
```
Or if you want to run it from your IDE, add "--download.path=path/to/download/files/" in program arguments. 

Then go to your web browser at localhost:8080 and from there, you can setup the receive/send procedure

## Built With

* [Maven](https://maven.apache.org/)
* [JavaFX](https://gluonhq.com/products/scene-builder/)