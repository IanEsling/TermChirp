# TermChirp
a Java 8 twitterlike in a terminal

#Running
TermChirp can be run with the executable JAR file in the project root:
`java -jar TermChirp-0.1.jar`

or you can compile the JAR yourself using JDK 8 and Maven: `mvn package`

or you can run it in your favourite IDE by checking the code out and running the `com.termchirp.TermChirp` class.

#Usage
TermChirp supports 4 commands:

**posting - post a chirp** : `username -> message`

**reading all chirps from a user** : `username`

**following chirps from another user** : `username follows userToFollow`

**user's wall - all chirps from that user and the users they follow** : `username wall`
