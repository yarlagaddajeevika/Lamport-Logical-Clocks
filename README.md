# Lamport-Logical-Clocks

### Summary
Most of us use physical time to order events. For example, we say that an event at 8:15 AM occurs before an event at 8:16 AM. In distributed systems, physical clocks are not always precise, so we can't rely on physical time to order events.
Instead, we can use logical clocks to create a partial or total ordering of events. For such case, We need to identify the Lamport logical clock timestamp for each message that was sent and received. This will be achieved by calculating the lamport logical value of an event using the Lamport Calculate algorithm and this can be verified using Lamport Verify algorithm.

### Pseudo Codes
### Algorithm Calculate:
Send events starts with ‘s’ and receiver events are ‘r’, ‘Null’ represents nothing, rest all alphabets are considered as internal events.
   Step 1: Enter the input
	  Enter the number of processes(rows) and the number of events in a process(columns)
	  Store input in the form of a matrix array
  Initialize values in output matrix with -2(some initial value)
   Step 2: Run the loop for the input matrix
          •	If the event is an internal event or send event and in the first column, then the value of the event is 1. Update the output matrix with value.
          •	Increment the column position and move on to the next element.
          •	If the event is a sender event or internal event but not in the first column, value will be the of the previous event value plus 1. Update the output matrix with value.
          •	If the event is a receive event and in the first column, find the corresponding send event and add 1 to it. This will be the value of the receive event.
          •	If the event is a receive event not in the first column, hold the loop there and extract the number of the receiver event and run one more for loop to find the corresponding send event. The value of the send event depends on the previous value. This previous value can be found by running the above 2 conditions in a recursive flow.
          •	Once you find the send event value, then find the maximum of the send event and the previous event of receive plus 1, this will be the value of the receive event.
          •	Continue the loop after the receive event.
          •	All these steps will be run in a recursive flow when you try to find the receive event value.
          •	All the values will be updated in the output matrix.
          •	If the value is “NULL”, 0 will be updated in the output matrix.
   Step 3:  Print the output
            Loop comes to an end when it reaches the number of processes and the events in the process,   the execution is completed. Now, Print the output matrix which has the updated values.

### Algorithm Verify:
All the positive integers are treated as events and there is an event with 0, it is considered as NULL.
   Step 1: Enter the input in a file called verify.txt
   Step 2: Read the input from file and store it in a matrix. 
   Step 3: Find the difference between the events in a row. If the difference is huge, mark it as receiver. 
          •	Receiver value minus 1 will be the sender event value. Loop the matrix and find the send event.
          •	Ensure the receiver and sender event are not in the same row(process).
          •	The number of the first receiver event will be maximum number of rows(process) and this number will be reduced by 1 when it finds another receive event.
          •	Repeat the same process for the number of process and find all the receiver and sender events correspondingly. 
          •	If the value encountered is 0 from input, put it as NULL.
          •	Rest all the other events are internal events. Define a int value corresponding to the alphabet and increment the count when an internal event is occurred.
          •	If there are receive events without send events, it means that the given input is incorrect.
   Step 4: Print the output 
           Loop comes to an end when it reaches the number of processes and the events in the process,   the execution is completed. Now, Print the output matrix which has the updated values.

