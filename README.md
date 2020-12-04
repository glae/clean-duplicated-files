

# clean-duplicated-files [![example workflow name](https://github.com/glae/clean-duplicated-files/workflows/build/badge.svg)](https://github.com/glae/clean-duplicated-files/actions)
 
## Purpose

This is a very simple script to help friends to clean up their photos/files directories.

It can delete duplicated files (same name and size) in **mess folder** when found in **reference folder**.
It ignores all directory structures (it is not a `diff`-like tool).

## How to use
 
### With Docker 

- There is a [`Dockerfile`](Dockerfile).
- There is also an image here : https://hub.docker.com/r/davecloud/docker_clean-duplicated-files

### From the sources
 
1. First time

`sbt` is required. On a Debian, you can do:

       git clone https://github.com/glae/clean-duplicated-files.git && cd clean-duplicated-files && ./setup.sh
    
2. Then

       sbt "run <Reference folder> <Mess folder> <action: dry-run OR delete-duplicates>"
       
    examples: 

    - It will display all files that **could** be deleted (but will not really run the deletion):

            sbt "run /tmp/pictures_root/reference /tmp/pictures_root/total_mess dry-run"
       
    - It will delete all duplicated files:

            sbt "run /tmp/pictures_root/reference /tmp/pictures_root/total_mess delete-duplicates"
     

## Performance tests

- with files (simple mess + ref), including 25% of duplicates

```
  files     | time 
------------|------
     720    | 6 sec                      
   7 200    | 3 min
  72 000    | 5 hours
 720 000    | ???
 
```

`dry-run` and `delete-duplicates` actions seem to take the same time.

