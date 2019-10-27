# Instant-Insanity

Algorithm description

Finding first thread:
 When searching for the first thread, each cube starts at the 1st opposite pair. Then the count of both colors is incremented (if the pair is two of the same color, that color is incremented twice). If one of them is greater than 2, then the 2nd opposite pair is checked. If one of the colors in that pair is greater than 2, then the 3rd opposite pair is checked. If one of the colors is greater than 2 for all opposite pairs, the opposite pair for the previous cube is moved to the right, and the current cube is tried again.

Finding second thread:
Everything is the same for the second thread except it must make sure it has not chosen the same opposite pair as the first thread chose for each cube. Both threads record the index of the pair they chose for each cube. If the second thread chooses the same index as the first thread for a specific cube, then it must choose a different opposite pair.

Finding minimal obstacle:
The algorithm starts by searching for a first thread through all the cubes. If a first thread is found, it starts searching for a second thread.

Case 1- No first thread can be found (obstacle):
The furthest cube that could be reached is recorded and the algorithm tries to find a first 	thread with the new smaller set of cubes.
	
An obstacle is recorded here, it can be changed if a smaller one is found later.

Case 2 - First thread found, no second thread can be found:
The first thread is adjusted by going to the last cube and shifting the opposite pair to the right. If it is the third pair, then it will move to the cube above it and shift that pair. Then the algorithm tries to find a new second thread.

Case 3 - First and second thread found (solution):
If a solution for a certain number of cubes is found, then every other possible set of the same size is searched.
-	If an obstacle is found, Case 1 is repeated.

-	If a solution is found for every set of the same size, then the obstacle recorded from the previous larger set size is the minimal one. 

