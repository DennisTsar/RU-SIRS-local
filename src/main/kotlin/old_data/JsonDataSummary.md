# JSON Summary
### Summary of the changes between each JSON dir
#### Note much of this info comes from using `compareDataDirs()`

## Important Notice
* For `json-data-1` through `json-data-6`, the below describes what is found in the `old-json/` directory, NOT the ones in the main directory
  * Info about the stuff in the main directory can be found by examining `old_data/Overwrite.kt`

## json-data-1
* Contains data up to Fall 2021 semester
* Has all and only all schools/depts from Fall 2021 (`courseFilter.php`)
* Originally used `EntryOld1` - meaning no note or question data
* Only has entries where `scores.size==100`

## json-data-2
* Switched to using `EntryOld2` which has note but no question data
* Removed files containing only empty list
  * This caused the removal of schools `[DNTp, SGS, SGS;, SPH, xx]`

## json-data-3
* Added entries with `scores.size!=100`
  * This caused schools `[DNTp, SGS, SGS;, SPH]` to be added back since they contained data now
    * Only school not added back was `xx` - probably because that data was junk and was removed from SIRS


## json-data-4
* Switched to using `EntryOld3` which has `extraQs` parameter 
  * data in this parameter was not entirely accurate since it assumed that first 10 qs were always the same, which is not always true
  * accidentally, only first 9 qs were dropped instead of first 10, so most entries have `"extraQs":["I rate the overall quality of the course as"]`

## json-data-5
* Adds Spring 2022 data 
  * still using Fall 2021 courseFilter schools/depts
  * note that Spring 2014 data, despite being removed from SIRS, is carried over
* Switched to using `Entry` class which has `question` parameter instead of `extraQs`
  * Default 10 questions are represented by number 0-9 (as Strings), other questions are written in full
  * Note that `questions=null` represents the default set of Qs (`["0","1",...,"9"]`)

## json-data-6
* Assigned all existing questions a number (`index` in `QsMap`) to be used in `questions` parameter
  * Despite all values in `questions` being Ints, they are kept as Strings to conform to `Entry` class

## json-data-7
* Adds data from all `courseFilter.php` over the years (excluding Spring 2014 as that was removed)
* Uses Schedule of Classes to not include some schools, specifically `[65, 66, 76, DNTp, SGS, SGS;, SoN, SPH]`
* Combines dept `37:575;` (extra semicolon) into `37:575`
* Has some new/changed entries due to SIRS updates & changes in semicolon handling
* Accidentally removes usage of `questions=null` for default question group
  * Meaning most entries have `questions=["0","1",...,"9"]`

## json-data-8
* Freshly retrieved data from SIRS as of 09/28/22
* Includes all entries available from SIRS at the time
  * Or, at least, that's the hope
* Note that Spring 2014 data is not included

## json-data-9
* Result of running `semicolonCleanup()` on `json-data-8`+`spring-2014-entries`

