/**********************************************
 * Please DO NOT MODIFY the format of this file
 **********************************************/

/*************************
 * Team Info & Time spent
 *************************/

	Name1: Will Victor	// Edit this accordingly
	NetId1: wjv4 	// Edit
	Time spent: 15 hours 	// Edit 

	Name2: Michael Ogez 	// Edit this accordingly
	NetId2: mjo17	// Edit
	Time spent: 15 hours	// Edit 

	Name3: Jesse Hu	// Edit this accordingly
	NetId3: jrh52	 	// Edit
	Time spent: 15 hours 	// Edit 

/******************
 * Files to submit
 ******************/

	lab3.jar // An executable jar including all the source files and test cases.
	README	// This file filled with the lab implementation details
	Elevator.input // You can submit a sample input and log file
    Elevator.log   // corresponding to the input but Elevator.log should be 
		       

/************************
 * Implementation details
 *************************/

We started this project by essentially each doing the whole thing on our own. We then convened and figured out
which people's implementations were the most efficient. However, all of us realized that it was obvious that
the best solution would use 2 lists of F event barriers in the building to represent up and down thresholds
and 1 list of F event barriers for each elevator to represent those folks waiting to get off at a particular floor. 

Then, once you get each elevator running the elevator algorithm, its no big deal to throw multiple elevators into the same
Building. The only problem is avoiding the thundering herd problem. To avoid this, we made the building control which of the
idle elevators goes to pick up a person. We do this by prohibiting dispatched floors for idle elevators. 

You can use the .jar by simply "java -jar lab3.jar" or "java -jar lab3.jar PART TEST_FILE" where PART is p1, p2part1, p2part2, or p2part3.

Extra credit was completed by adding additional checks into the methods of Elevator. A time function allows the elevator to find out when a rider is not well-behaved. The elevator will move on without verifying that all users are aboard if the rider has not gotten on.

Note that 

/************************
 * Feedback on the lab
 ************************/

Great lab, really captured a lot of notions in multi-threading. Fun elegant solutions. 

Not enough specification. Spent a lot of time trying to argue design decisions and searching Piazza for more information.


/************************
 * References
 ************************/

We only used outside references for picking up Java syntax for I/O. Done via the oracle docs.


