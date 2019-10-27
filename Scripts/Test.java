import java.util.*;

public class Test {
		
	static Cube[] cube = new Cube[26];
	static List<Cube> cubeList = new ArrayList<Cube>();
	
	static int[] thread1 = new int[52];
	static int[] thread2 = new int[52];
	
	//Holds solution threads if there is a solution for all cubes
	static int[] solutionthread1 = new int[52];
	static int[] solutionthread2 = new int[52];
		
	//Keeps count of how much of each color shows up per thread
	static int[] colorCount1 = new int[26];	
	static int[] colorCount2 = new int[26];
	
	//For each cube; was the 1st, 2nd, or 3rd pair chosen
	static int[] oppositePairIndex1 = new int[26];
	static int[] oppositePairIndex2 = new int[26];
	
	static List<Cube> minObstacle = new ArrayList<Cube>();

	static boolean firstThread = true;
	static boolean firstThreadIncomplete = false;
	static boolean secondThreadIncomplete = false;
	static boolean noSolution = false;
	
	static boolean consecutiveSolution = false;
	
	static int furthestCube = 0;
	
	static int maxSize = 26;
				
	public static void main (String args[])
	{	
		AssignColors();
		Searchthread();
		PrintResults();
	}
	
	static int n;
			
	static void AssignColors() {
		
		for (int n = 0; n < 26; n++)
		{
			cube[n] = new Cube();
		}
		
		int i = 0;
		
		for (n = 1; n < 157; n++) 
		{
			//Math.exp(1) is e^1
			
			int solution = (int)(1 + ((Math.floor(n * Math.PI) % 26)));		
			//int solution = (int)(1 + ((Math.floor(n * Math.exp(1)) % 26)));
			//int solution = (int)(1 + ((Math.floor(n * Math.sqrt(3)) % 26))); 
			//int solution = (int)(1 + ((Math.floor(n * Math.sqrt(5)) % 26)));	
			
			//int solution = (int)(1 + ((Math.floor(n * Math.sqrt(8)) % 8)));	
			//int solution = (int)(1 + ((Math.floor(n * Math.sqrt(Math.exp(1))) % 26))); 
			//int solution = (int)(1 + ((Math.floor(n * Math.sqrt(4)) % 26))); 
			//int solution = (int)(1 + ((Math.floor(n * Math.sqrt(7)) % 26))); 
			//int solution = (int)(1 + ((Math.floor(n * Math.sqrt(17)) % 26))); 
			//int solution = (int)(1 + ((Math.floor(n * Math.sqrt(24)) % 26))); 
			//int solution = (int)(1 + ((Math.floor(n * 4) % 26)));
			//int solution = (int)(1 + ((Math.floor(n * 3) % 26))); 
			
			
			if (n % 6 == 1)
			{
				cube[i].left1 = solution;
			}

			if (n % 6 == 2)
			{
				cube[i].right1 = solution;			
			}

			if (n % 6 == 3)
			{
				cube[i].left2 = solution;
			}

			if (n % 6 == 4)
			{
				cube[i].right2 = solution;
			}

			if (n % 6 == 5)
			{
				cube[i].left3 = solution;
			}

			if (n % 6 == 0)
			{
				cube[i].right3 = solution;
				cube[i].index = i;
				cubeList.add(cube[i]);
				i++;
			}
		} 
	}
						
	static void Searchthread() {
								
		for (n = 0; n < maxSize; n++)  
		{		
			if (n > furthestCube)
			{
				furthestCube = n;
			}
	
			//Case 1 - No first thread can be found (obstacle)
			if (noSolution == true)
			{									
				minObstacle.clear();
				
				maxSize = furthestCube;
						
				//Keeps record of obstacle met for the size of the set of cubes
				for (int i = 0; i < maxSize + 1; i++)	
				{										
					minObstacle.add(cubeList.get(i));
				}
					
				System.out.print("Obstacle Found\n");
				System.out.print("Furthest Cube  " + (furthestCube + 1) + "\n");
				System.out.print("Searching all sets of " + furthestCube + " cubes for an obstacle\n");
				System.out.print("...\n");
				
				consecutiveSolution = false;
				firstThreadIncomplete = false;
				noSolution = false;
				
				furthestCube = 0;
				
				ResetFirstThread();
				ResetSecondThread();
				Searchthread();
				return;
			}
			
			//Case 2 - First thread found, no second thread can be found; adjust first thread
			if (secondThreadIncomplete == true && firstThread == false)	
			{								
				firstThread = true;
				secondThreadIncomplete = false;
				
				ResetSecondThread();
				
				n = maxSize - 1;
							
				if (oppositePairIndex1[n] == 0)
				{
					OppositePair2();
					firstThreadIncomplete = false;
				}
				else if (oppositePairIndex1[n] == 1)
				{		
					OppositePair3();
					firstThreadIncomplete = false;
				}
				else if (oppositePairIndex1[n] == 2)
				{
					if (n != 0)
					{
						BackTrack();
						firstThreadIncomplete = false;
					}
					else
					{
						noSolution = true;
						firstThreadIncomplete = true;
					}
				}
			}
			else
			{	
				OppositePair1();
			}	
		}		
		
		//Found first thread, starting search for second thread
		if (firstThread == true && firstThreadIncomplete == false)
		{
			firstThread = false;
			Searchthread();
			return;
		}	
		
		//Case 3 - First and second thread found (solution)
		if (firstThreadIncomplete == false && secondThreadIncomplete == false)
		{	
			firstThread = true;
			noSolution = false;
			
			if (maxSize == 26)
			{
				System.arraycopy(thread1, 0, solutionthread1, 0, 52);
				System.arraycopy(thread2, 0, solutionthread2, 0, 52);
			}
			
			ResetFirstThread();
			ResetSecondThread();
			
			//If a solution for a set of cubes is found, search every other possile set
			//for that number of cubes
					
			if (consecutiveSolution == false)			
			{					
				consecutiveSolution = true;				
				Cube temp[] = new Cube[maxSize];
				CubeCombinations(temp, 0, 0, maxSize);
			}
		}
	}
	
	static void OppositePair1(){
							
		if (firstThread == true)   		//First thread
		{	
			thread1[n * 2] = cubeList.get(n).left1;
			thread1[(n * 2) + 1] = cubeList.get(n).right1;
			
			colorCount1[cubeList.get(n).left1 - 1]++;
			colorCount1[cubeList.get(n).right1 - 1]++;
			
			oppositePairIndex1[n] = 0;

			if (colorCount1[cubeList.get(n).left1 - 1] > 2 || colorCount1[cubeList.get(n).right1 - 1] > 2)
			{
				OppositePair2();
				return;
			}
		}
		else						//Second thread
		{						
			thread2[n * 2] = cubeList.get(n).left1;
			thread2[(n * 2) + 1] = cubeList.get(n).right1;
			
			colorCount2[cubeList.get(n).left1 - 1]++;
			colorCount2[cubeList.get(n).right1 - 1]++;	
			
			oppositePairIndex2[n] = 0;
			
			if (n == 0)
			{
				OppositePair2();
				return;
			}
			else if (oppositePairIndex2[n] == oppositePairIndex1[n])
			{
				OppositePair2();
				return;
			}
			else if (colorCount2[cubeList.get(n).left1 - 1] > 2 || colorCount2[cubeList.get(n).right1 - 1] > 2)
			{
				OppositePair2();
				return;
			}
		}
	}
	
	static void OppositePair2(){
		
		if (firstThread == true)		//First thread
		{			
			colorCount1[cubeList.get(n).left1 - 1]--;
			colorCount1[cubeList.get(n).right1 - 1]--;
		
			thread1[n * 2] = cubeList.get(n).left2;
			thread1[(n * 2) + 1] = cubeList.get(n).right2;
			
			colorCount1[cubeList.get(n).left2 - 1]++;
			colorCount1[cubeList.get(n).right2 - 1]++;
			
			oppositePairIndex1[n] = 1;
				
			if (colorCount1[cubeList.get(n).left2 - 1] > 2 || colorCount1[cubeList.get(n).right2 - 1] > 2)
			{
				OppositePair3();
				return;
			}
		}
		else					//Second thread
		{
			colorCount2[cubeList.get(n).left1 - 1]--;
			colorCount2[cubeList.get(n).right1 - 1]--;
		
			thread2[n * 2] = cubeList.get(n).left2;
			thread2[(n * 2) + 1] = cubeList.get(n).right2;
			
			colorCount2[cubeList.get(n).left2 - 1]++;
			colorCount2[cubeList.get(n).right2 - 1]++;
			
			oppositePairIndex2[n] = 1;
			
			if (oppositePairIndex2[n] == oppositePairIndex1[n])
			{
				OppositePair3();
				return;
			}			
			else if (colorCount2[cubeList.get(n).left2 - 1] > 2 || colorCount2[cubeList.get(n).right2 - 1] > 2)
			{
				OppositePair3();
				return;
			}
		}
	}
	
	static void OppositePair3(){
		
		if (firstThread == true)		//First thread
		{	
			colorCount1[cubeList.get(n).left2 - 1]--;
			colorCount1[cubeList.get(n).right2 - 1]--;
		
			thread1[n * 2] = cubeList.get(n).left3;
			thread1[(n * 2) + 1] = cubeList.get(n).right3;
			
			colorCount1[cubeList.get(n).left3 - 1]++;
			colorCount1[cubeList.get(n).right3 - 1]++;

			
			oppositePairIndex1[n] = 2;
				
			if (colorCount1[cubeList.get(n).left3 - 1] > 2 || colorCount1[cubeList.get(n).right3 - 1] > 2)
			{	
				if (n != 0)
				{
					BackTrack();
					return;
				}
				else
				{
					noSolution = true;
					firstThreadIncomplete = true;
				}
			}		
		}
		else					//Second thread
		{	
			colorCount2[cubeList.get(n).left2 - 1]--;
			colorCount2[cubeList.get(n).right2 - 1]--;
		
			thread2[n * 2] = cubeList.get(n).left3;
			thread2[(n * 2) + 1] = cubeList.get(n).right3;
			
			colorCount2[cubeList.get(n).left3 - 1]++;
			colorCount2[cubeList.get(n).right3 - 1]++;
			
			oppositePairIndex2[n] = 2;
			
			if (oppositePairIndex2[n] == oppositePairIndex1[n])
			{	
				if (n != 0)
				{
					BackTrack();
					return;
				}
				else
				{
					secondThreadIncomplete = true;
				}
			}
			else if (colorCount2[cubeList.get(n).left3 - 1] > 2 || colorCount2[cubeList.get(n).right3 - 1] > 2)
			{	
				if (n != 0)
				{
					BackTrack();
					return;
				}
				else
				{
					secondThreadIncomplete = true;
				}
			}					
		}
	}
	
	static void BackTrack() {		
								
		if (firstThread == true)		//First thread
		{		
			colorCount1[cubeList.get(n).left3 - 1]--;
			colorCount1[cubeList.get(n).right3 - 1]--;
			
			thread1[n * 2] = 0;
			thread1[(n * 2) + 1] = 0;
		}
		else						//Second thread
		{
			colorCount2[cubeList.get(n).left3 - 1]--;
			colorCount2[cubeList.get(n).right3 - 1]--;
			
			thread2[n * 2] = 0;
			thread2[(n * 2) + 1] = 0;
		}
	
		n--;	//Goes to previous cube
				
		if (firstThread == true)			//First thread
		{		
			if (oppositePairIndex1[n] == 0)
			{
				OppositePair2();
				return;
			}
			else if (oppositePairIndex1[n] == 1)
			{	
				OppositePair3();
				return;
			}
			else if (oppositePairIndex1[n] == 2)
			{
				if (n != 0)
				{
					BackTrack();	
					return;
				}
				else
				{
					noSolution = true;
					firstThreadIncomplete = true;
				}
			}
		}	
		else					//Second thread
		{				
			if (oppositePairIndex2[n] == 0)
			{
				OppositePair2();
				return;
			}
			else if (oppositePairIndex2[n] == 1)
			{	
				OppositePair3();
				return;
			}
			else if (oppositePairIndex2[n] == 2)
			{		
				if (n != 0)
				{
					BackTrack();
					return;					
				}
				else
				{
					secondThreadIncomplete = true;
				}
			}
		}
	}
			
	//Searches through all possible sets of a given size	
	static void CubeCombinations(Cube temp[], int start, int index, int size) {
		
		if (index == size)
		{
			cubeList.clear();
			Collections.addAll(cubeList, temp);
			Searchthread();
			return;
		}
		
		for (int i = start; i < 26 && 26 - i + 1 > size - index; i++) 
		{ 			
			temp[index] = cube[i];			
			CubeCombinations(temp, i + 1, index + 1, size);
				
		}
	}
		
	static void ResetFirstThread(){
		
		for (int i = 0; i < 52; i++)
		{
			thread1[i] = 0;
		}
								
		for (int i = 0; i < 26; i++)
		{
			colorCount1[i] = 0;
		}
	}
	
	static void ResetSecondThread(){
		
		for (int i = 0; i < 52; i++)
		{
			thread2[i] = 0;
		}
								
		for (int i = 0; i < 26; i++)
		{
			colorCount2[i] = 0;
		}
	}
	
	static void PrintResults(){
		
		if (furthestCube + 1 == 26) //Solution for puzzle found
		{
			System.out.print("No obstacle found\n\n");
			System.out.print("Solution:\n");
			
			for (int i = 0; i < solutionthread1.length; i++)
			{
				if (i % 2 == 1)
				{
					System.out.print(i / 2 + 1 + ": " + solutionthread1[i - 1]);
					System.out.print(", " + solutionthread1[i] + "    ");
					
					
					System.out.print(solutionthread2[i - 1]);
					System.out.print(", " + solutionthread2[i] + "\n");
				}
			}
		}
		else	//minimal obstacle found
		{
			System.out.print("\nMinimal Obstacle: " + minObstacle.size() + " cubes\n");
		}
		
		for (n = 0; n < minObstacle.size(); n++)
		{
			System.out.print(minObstacle.get(n) + "\n");
		}
		
		System.out.print("\n");
	}
}