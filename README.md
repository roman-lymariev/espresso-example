# Description
Basic test automation framework for Android based on Espresso, gradle and jUnit. 

#Structure
Espresso tests are instrumented tests that have to be located in the test application repo. 
Tests themselves are located under /src/androidTest/ folder. build.gradle file also contains some 
dependencies with "androidTestImplementation" scope.
 - /pages/ - contains PageObjects
 - /tests/ - contains jUnit tests
 - /utils/ - for now contains only fluent waits implemented by 3rd party (AzimoLabs).

# Test app
* The cities are listed in ascending alphabetical order
* The cities list is scrollable
* The user of the application can search for specific city(ies) 
* The user of the application can select a city to be displayed on the map
* The user of the application can interact with the map (i.e zoom in/out, move to the left/right)
