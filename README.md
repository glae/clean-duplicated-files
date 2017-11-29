# clean-duplicated-files

## Purpose

This is a script to help friends to clean up their photos directories.

It can delete duplicated files (same name and filename) from "mess folder" when found from "reference folder".
 
 ## How to use
 
1. First time
   
       ./setup.sh
    
2. Then

       sbt "run <Reference folder> <Mess folder>"
       
    example: 
    
       sbt "run /tmp/pictures_root/reference /tmp/pictures_root/total_mess"
       
       
       
       