Richard Cross
cs-451 spring 2013

Homework 2

Image Aliesing

Notes on the results of using different values of M, N, and K
-----------------------------
M=1, N=20, K=2

At these sttings the best is the 3x3-ver2 filter.

Average comes in second with some slight jaggedness showing

3x3-ver1 gives an image with slight grey radial banding

no Filter is showings gaps and the circles are starting to break up into individual dots

-----------------------------
M=1, N=20, K=4

The averaging method works the best at these settings with no noticable artifacts.

3x3-ver1 gives some noticable white radial banding.

3x3-ver2 gives the white banding and appears to be showing more seperate dots along the lines of the circles.

no filter gives a ragged series of seperate dots aproximatly in the form of concentric rings.


-----------------------------
M=3, N=20, K=2

In this case the three filtered images look good.

the 3x3-ver2 is the best

3x3-ver1 is very close with just a bit of jaggedness on the cardinal axis

Average is showing some minor but noticable jaggedness

No Filter has some obvious artifacting with jagged thick and thin areas on the circle, but is still a continous black line.


-----------------------------
M=3, N=20, K=4

Here we have the Average filter looking the best with minor grey radial bands

3x3-ver1 and 3x3-ver2 are indistinguahable and are showing minor jaggedness

No Filter is showing gaps, dots and strait bars around the circles


-----------------------------
M=5, N=40, K=2

All of the filters are looking good here.

I would order the quality as 3x3-ver2 -> 3x3-ver1 -> Average -> None

No Filter is showing some jagged stair-stepping along the curves of the circles


-----------------------------
M=5, N=40, K=4

At these settings the Average works best followed closely by 3x3-ver1.

3x3-ver2 is showing a bit of black radial banding while No Filter is showing distinct black radial banding.

##############################################

Part 2 - Dictionary coding

The file input takes a path and filename string.

As well as the simple compression ratio I also included a calculation for the ratio based on the understanding that the initial dictionary containing the individual sysmbols to be used would have to be sent along with the encoded data.




