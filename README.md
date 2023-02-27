<img src="misc/cryptlooter_logo.png">

# Crypt Looter

> Developed in 2019 as a college software project with @minpin04

<br>

“Laurel Kraft stars in Crypt Looter as a hero ready to seek out the lost treasures underneath Japan. Featuring thrilling gameplay, this game challenges the player to skillfully slay enemies and sidestep traps to make it to the long-lost treasure.”

**Genre:** Action-adventure, RPG, platformer

# Brief
- Has to appeal to children aged twelve and up
- Must contain many standard game features - such as a leaderboard, multiple enemies, and a boss.
- Game must be rendered with only the Swing UI library

# Getting started

> See [Releases](https://github.com/itsmejoeeey/crypt-looter/releases) for a pre-compiled binary

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
java -classpath out/production/crypt-looter crypt_looter.Main
```
  
### To create and run a jar
Ensure you follow the build instructions above prior then execute:
```
# Create the jar
jar cmf out/production/crypt-looter/MANIFEST.MF CryptLooter.jar -C out/production/crypt-looter *
# Execute the jar
java -jar ./CryptLooter.jar
```

# Licenses

For all licenses of the content used in the making of this game, see LICENSE.md
