# ImageApproximation
Task: approximate an image with 50 polygones <br />
This projected was made for AI class. In Mona_Lisa_100_results.png you can find results of approximating an image: monaLisa-100.jpg. <br />

![Results](https://github.com/DBankiewicz/ImageApproximation/blob/master/monaLisa-100_results.png) 

<br/>
This is a greedy algorithm and this program can find an approximation of an image in 2-4 min for residuum error level at 16 for small image (100x149). For the same image, it takes around 15 min for program to find approximation with residuum error level at 12. <br/>
This is an implementation of greedy algorithm, which tries to find the best approximation for given image. The basic idea is for given approximation mutate randomly one of 50 polygons until the better approximation is found. <br />
Test.java - main class, where the input image is read and approximation is executed. <br />
FindImage.java - class with algorithm to approximate an image. <br />
