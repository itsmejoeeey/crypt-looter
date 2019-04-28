<img src="misc/cryptlooter_logo.png" width="400">

“Laurel Kraft stars in Crypt Looter as a hero ready to seek out the lost treasures underneath Japan. Featuring thrilling gameplay, this game challenges the player to skillfully slay enemies and sidestep traps to make it to the long-lost treasure.”

**Genre:** Action-adventure, RPG, platformer

# Getting started

## Prerequisites
This application was made to be compiled with OpenJDK 8. We use ANT build files to make compiling the source easier.

## Compiling and running
Perform all sets of commands from the root directory of this repo.

### To build

```
ant
```

### To run
Ensure you follow the build instructions above prior then execute:
```
java -classpath out/production/2019-Java-Group5 crypt_looter.main
```
  
### To create and run a jar
Ensure you follow the build instructions above prior then execute:
```
# Create the jar
jar cmf out/production/2019-Java-Group5/MANIFEST.MF CryptLooter.jar -C out/production/2019-Java-Group5 *
# Execute the jar
java -jar ./CryptLooter.jar
```

# Licenses

For all licenses of the content used in the making of this game, see LICENSE.md
