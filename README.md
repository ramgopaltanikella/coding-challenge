## JSON Library

For encoding/decoding JSON objects, the Google gson library was used.

## Install Java

Install Java, if not already installed. The current code was built and tested using openjdk 18.0.1 on macOS.

## Gradle

The build framework uses gradle to build the project and manage the dependencies. The `gradlew` command will automatically download gradle so there is no need to install anything other than Java, if required.

### Project Layout

All source code is located in the `src/main/java` folder. There is only one source file: `JSONUtils.java`, which contains the flatten method. The testing files are located in the `src/test/java` folder.
Corresponding to the source file, there is one JUnit test file: `JSONUtilsTest.java`, which contains two tests: `testValidJson`, and `testInvalidJson`.
`testValidJson` is a parametrized test, which takes a list of valid inputs and expected flattened outputs, and asserts that the actual flattened output equals the expected output.

### Building the project from the command line

To build the project on Linux or MacOS, in a shell terminal, go to the project root directory, and run the command `./gradlew clean build`.
This will build the source code in `src/main/java`, run the tests in `src/test/java`, and create two output jar files in the `build/libs` folder: `mongodb-coding-challenge-shadow.jar` and `mongodb-coding-challenge.jar`.
To clean out any intermediate files run `./gradlew clean`.  This will remove all files in the `build` folder.

### Test results

After building the project, the test results should be available in `./build/reports/tests/test/classes/com.mongodb.codingchallenge.JSONUtilsTest.html`

### Running the application from the command line

Start the application by running the command from the project root directory:  
`java -jar ./build/libs/mongodb-coding-challenge-shadow.jar`

The input is expected via stdin, so after typing the above command, hit Return. Then input a valid JSON object string, and hit Return. The output is sent to std.out.

##### Valid runs

`java -jar ./build/libs/mongodb-coding-challenge-shadow.jar`  
{  
"a": 1,  
"b": true,  
"c": {  
"d": 3,  
"e": "test"  
}  
}  

{"a":1,"b":true,"c.d":3,"c.e":"test"}  

test.json contains same input as above.  
`cat ./src/test/resources/test.json | java -jar ./build/libs/mongodb-coding-challenge-shadow.jar`  
{"a":1,"b":true,"c.d":3,"c.e":"test"}  

##### Invalid runs

Note the missing brace at the end of the input  
`java -jar ./build/libs/mongodb-coding-challenge-shadow.jar`  
{"a": 3  
Input is not a valid JSON object

empty.json is empty.  
`cat ./src/test/resources/empty.json | java -jar ./build/libs/mongodb-coding-challenge-shadow.jar`  
Input is not a valid JSON object
