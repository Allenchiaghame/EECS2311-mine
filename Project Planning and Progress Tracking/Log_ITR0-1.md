# Meeting Minutes

## Jan 25th
- Approx 20 minute meeting with all group members at the end of class.
- Organized day when group members can meet to discuss with our user for the project.
- Discussed creating a log file.


## Jan 31st 
- Quick discussion after class with professor to discuss how to move forward with the next iterations of the project. 
- Also discussed potential alternatives to utilizing a DB, such as reading from JSON file.
- Discussed with team members about what to name the project, and the potential GUI options

## Feb 3rd
- Met the customer and heard her requests
- Discussed what we could do by the first delivery
- Provided estimate delivery time

## Feb 10th
- Meeting over discord to discuss designation of work.
- Finalized user stories and software used to diagram work 
- Created User Story Cards
- Organizing potential classes needed for Iteration 0,1. 

## Feb 14th 
- Over discord messaging, further discussed the needed requirements for the Itr1 deadline. 

--- 

## Itr 1 Summary

### Big Design Decisions
- We decided to begin building out the UI's with Java.swing as opposed to JavaFx. More team members were familiar with it so it would be quicker to begin development. Having to install an additional package as opposed to using the built in java.swing, was another consideration.
- In order to develop the UI, we went with a model with components and Views. The views display the GUI while the components make up the view. This sort of separation of concerns made development much easier to understand.

### Concerns/Obstacles
- Since every member has varying levels of knowledge on git and github, some basic operations needed a bit more explanation. 

### User Stories
1. Create Home Class (Sarah)
Dev Tasks..
- Create the container class. A container represents a pantry/fridge of items that users can add and delete from. 
- Create Home and ContainerView, using java.swing to develop the UI.
- Setup the home to receive user input (name of container to be created).
- Create stub db class. 

Estimated Working time: 2 Days
Actual Working time: 7 Days
- Setting up this user story took longer than estimated. It also served as the foundation for other user stories as there must be consistent logic for setting up the UI and data binding.


2. Add Items Class (Nina)
Dev Tasks..
- Create item class to represent a food added to a user's pantry.
- Setup the AddItemPanel view to receive user input. 
- Implement error handling on user input.
- Populate stub db with items. 
- Test implementation of adding an item

Estimated Working time: 2 Days
Actual Working time: 3 Days

3. Remove Item from Pantry (Michel)
- Create DeleteItemView to prompt user for the item they want to remove from their pantry.
- Implement error handling on user input to ensure that items actually exist in the database.
- Test implementation of deleting an item 

Estimated Working time: 2 Days
Actual Working time: 3 Days
 
4. Tagging Food Groups (Edison)
- Create tag class and several "tag" classes.
- Implement tagging test cases 

5. See Full List of items (Allen)
- Create ItemsList view to display list of items 
- Display this view over top the containerView

Estimated Working time: 2 Days
Actual Working time: 1 Days

6. Add User (Ning)
- Setup user class to implement user logins in future iterations

Estimated Working time: 2 Days
Actual Working time: 1 Days


