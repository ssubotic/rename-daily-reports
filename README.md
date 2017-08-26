A utility for more easily renaming daily updated reports. It works by parsing the names of files within a directory for keywords and renaming them according to keyword matches and the directory's name. 

As the intended use case involves folder names formatted as "2017-07-01-Sat", **proper output requires the housing directory's name to contain the date in the format xxYYxMMxDD.** (x's may be any character)

The GUI requires the housing directory's complete path as input, and the files are renamed to: "{full report type}{mm-dd-yy}"