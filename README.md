# Project for parsing Hearts of Iron 4 save files in java

## Benchmarks

### First version
DEBUG MODE 4.2s

"RELEASE" MODE 2.1s

### Second version
Stopped creating all tokens at once.
parsing ~1.2s

### "Third" version
Create a HashMap for every ObjectNode for better experience

parsing ~1.3s

save game build ~40ms (don't really know if this matters since it's going to change in the future when I add more data)

### 4th version
Speed up parsing by ~300ms.

Changed to using precompiled regex and removed unnecessary string comparison

parsing ~1s