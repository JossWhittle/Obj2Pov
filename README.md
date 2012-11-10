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

### Using generated files in Pov-Ray

To use the generated files in Pov-Ray, simply #declare them into the head your scene. Then to call the mesh within the scene create a union and place that name within.

	#include "colors.inc"
	#declare TEAPOT = union { #include "teapot.pov" }

	// Scene Setup
	camera { 
        location <-5,4,-16> 
        look_at <0,2,0>
        angle 60
	}

	light_source {
	   <-7, 11, -12> color <252/255,255/255,194/255>
	}  

	plane {
	    y, 0
	    pigment { checker Gray, White }
	}

	union {
		TEAPOT
 		pigment { Red }
	}

<center>![Render of the above sample scene.](https://raw.github.com/L2Program/Obj2Pov/master/teapot.png)</center>