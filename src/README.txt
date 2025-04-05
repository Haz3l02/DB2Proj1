Hazel Green - 330645435

1. To run the code, simply run Main.java, and ensure that the folder with the tests (Project1) is in a directory called "src"
2. As far as I can tell it passes the large majority of the tests, one thing I noticed at the last minute is that when I ran the very
    last test in the document we were provided, after running all the other ones sequentially, my program returned "Evicted file 1 from frame 2"
    where the correct answer was "evicted file 2 from frame 3", so there is likely some sort of small off-by-one error somewhere.
3. I decided to make a "Virtual File" class to act as my "disk" as to avoid the headache of permanently modifying my test data.
    These "virtual files" have file data read to them at the same time when a frame is (and aren't loaded up into memory unless
    a corresponding frame is queried). They have data written to them in the case of a dirty flag on a frame that gets evicted.
    They function pretty identically to using the actual files on disk and could be easily substituted out if needed (to write to files on disk)
    Everything else is pretty standard though. Main.java is the real bulk of the program