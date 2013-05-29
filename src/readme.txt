
Richard Cross
CS-451 Spring 2013
Homework 3
05/26/2013
Compression and de-compression of an image using JPEG techniques
Calculation of Compression Ratios achieved by these compression techniques.




-- Entering the file to use ---

When first running the program it will ask the user to select which homework assignment to run.

If the program was given a file name as a command line argument it will use that file.

If a file name is not given or the file doesn't exist, the program will prompt the user to enter one.


When selecting a file the user is given a choice of 2 methods to enter a file name:


1) - The user can type in the file name to use.


2) - The user can elect to use the java file chooser GUI. If the program is being run from Eclipse, or possibly some other IDEs, the GUI file chooser window will open behind the Eclipse window. The Eclipse window will have to be moved to reveal the file chooser. (I'll probably remove this feature for homework 4)




--- Running the program ---

After the program has a file to use, whether command line or entered it will ask the user to choose between two versions of the program to run.

1. version-1 is exactly (I belive) as the homework specified.

2. version-2 is slightly different, but produces the expected output.




-- Similarities between the two versions --

The program will cycle through the quantization values 0-5.
For each quantization value it will compress the image and then calculate and print out the Compression Ratio values. It will then de-compress the image and display it.
It will do this for all 6 quantization values.



-- Differences between the two versions --

     Version 1 is exactly as the homework assignment described. 

Version-1 zigzag pattern:
0.0	1.0	5.0	6.0	14.0	15.0	27.0	28.0	
2.0	4.0	7.0	13.0	16.0	26.0	29.0	42.0	
3.0	8.0	12.0	17.0	25.0	30.0	41.0	43.0	
9.0	11.0	18.0	24.0	31.0	40.0	44.0	53.0	
10.0	19.0	23.0	32.0	39.0	45.0	52.0	54.0	
20.0	22.0	33.0	38.0	46.0	51.0	55.0	60.0	
21.0	34.0	37.0	47.0	50.0	56.0	59.0	61.0	
35.0	36.0	48.0	49.0	57.0	58.0	62.0	63.0	

It Pads the image with 0's (black)

Version-1 results:
It only gives the proper output for the all red image, red.ppm

 

    Version-2 is different from version-1 in two ways. 

    The first difference is that it converts from an 8x8 block to a sequence using the zigzag translator. Instead of first going right and then diagonal down to the left the alternate zigzag starts by first going down then diagonal up to the right. 

Version-2 zigzag pattern:
0.0	2.0	3.0	9.0	10.0	20.0	21.0	35.0	
1.0	4.0	8.0	11.0	19.0	22.0	34.0	36.0	
5.0	7.0	12.0	18.0	23.0	33.0	37.0	48.0	
6.0	13.0	17.0	24.0	32.0	38.0	47.0	49.0	
14.0	16.0	25.0	31.0	39.0	46.0	50.0	57.0	
15.0	26.0	30.0	40.0	45.0	51.0	56.0	58.0	
27.0	29.0	41.0	44.0	52.0	55.0	59.0	62.0	
28.0	42.0	43.0	53.0	54.0	60.0	61.0	63.0

This is not how any of the examples in the lectures showed the zigzag pattern working.


    The second difference is that Version-2 pads the original image with 255's (White).
The chroma sub-sample is still being padded with 0's.


Version-2 results:
The output from version-2 matches the red.ppm example Compression Ratio values exactly.

It also exactly matches the redblack.ppm example for all cost values and Ratios.

Version-2 matches all of the cost values and Compression Ratios for Dune.ppm.

Version-2 matches all of the cost values and Compression Ratios for Ducky.ppm.

 
*******
notes:
I am fairly sure that the zigzag order used to compress the images in the sample data has an error in it. I think it is zigging where it should have zagged. See above outputs for the zigzag expansions. 

Figuring out that I needed to pad the image with white was the hardest part. I had almost everything working by changing the zigzag. Dune.ppm matched exactly but Ducky.ppm was still getting a mismatch with the Luminance value. Then I realized that the difference between these images was that Dune image didn't need padding.

--
I ran a lot of the example data from the lecture slides through my DCT method to try and confirm that it was working by matching it to the results shown. After getting conflicting and confusing results I finally realized that all the examples given are for RGB. None of the examples shown are using DCT on a YCbCr set of values.

It topk me longer than it probably should have, but I finally realized that the DCT algorithm we are using is specifically for encoding values in the YCbCr [-128,127] range. When used on an RGB image it will fail at any point where a value exceeds 127. 

I included some of the demo classes I was using to test the behaviour of different versions of the processes and methods. None of the demos use an image file or commandline argument.

