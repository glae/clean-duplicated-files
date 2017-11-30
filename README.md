# clean-duplicated-files

## Purpose

This is a script to help friends to clean up their photos/files directories.

It can delete duplicated files (same name and size) from "mess folder" when found in "reference folder".
It ignore all directory structures (it is not a `diff`-like tool).

 ## How to use
 
1. First time
   
       ./setup.sh
    
2. Then

       sbt "run <Reference folder> <Mess folder> <action: dry-run OR delete-duplicates>"
       
    examples: 

    - It will display all files that **could** be deleted (but will not really run the deletion):

            sbt "run /tmp/pictures_root/reference /tmp/pictures_root/total_mess dry-run"
       
    - It will delete all duplicated files:

            sbt "run /tmp/pictures_root/reference /tmp/pictures_root/total_mess delete-duplicates"
     

## Performance tests


- with files (simple mess + ref), including 20% 
duplicates

```
  files     | time 
------------|------
     720    | 6 sec                      
   7 200    | 3 min
  72 000    | ???
 720 000    | ??? 
 
```

`dry-run` and `delete-duplicates` actions seem to take the same time.

