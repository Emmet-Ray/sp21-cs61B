# Gitlet Design Document

**Name**:

## Classes and Data Structures

### Class 1 Main
1. take arguments 
2. before call the corresponding methods, check the arguments.
3. call corresponding methods in Repository
#### Fields

####  to validate number of arguments of command line.  copied from lab 6.
1. public static void validateNumArgs(String cmd, String[] args, int n)
#### for commands that need initialized gitlet repo, if not initialized, print error message, exit.
2. public static void initializedGitletRepository() 
#### to validate format of command 
3. 


### Class 2 Repository

#### Fields

1. Field 1
2. Field 2

### Class 3 Commit

#### Fields
1. String message : commit msg
2. Date date : commit date
3. String parent : parent SHA-1 ID
4. HashMap<String, String> blobs : used to keep track of blob references
5. String SHA_1 : maybe cache the SHA_1 ID
## Algorithms

## Persistence

