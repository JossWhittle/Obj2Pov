# Obj2Pov

Obj2Pov is a command line utility for converting 3D object (.obj) files to a format usable by Pov-Ray (.pov or .inc)

### Usage

The program reads from Std::Input and writes to Std:Output. An example of how to use Obj2Pov would be as follows:

	// To run the compiled jar
	java -jar obj2pov.jar < teapot.obj > teapot.pov
	
	// To compile from source
	javac obj2pov.java
	java obj2pov < teapot.obj > teapot.pov

Where teapot.obj is the file to convert and teapot.pov is the destination file.