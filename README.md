# java-hoi4-parser

A program for parsing Hearts of Iron 4 save game files (only text files for now).

## Prerequisites

Save game files must be in text format.

This can be changed in the `settings.txt` file located in your Hearts of Iron IV folder (`%USERPROFILE%\Documents\Paradox Interactive\Hearts of Iron IV\` by default). In `settings.txt` change `save_as_binary=yes` to `save_as_binary=no`.

## Start options

#### Required

```
-game <path|name>             path to a .hoi4 file - if -hoi4_folder is specified or the hearts of iron 4 folder is in the default location only a file name can be used
```

#### Optional

```
-json                    saves the .hoi4 as a .json file
-json_limit <limit>      saves only json objects that are smaller than <limit>
-hoi4_folder <path>      path to the hearts of iron IV folder that has save games (%USERPROFILE%\Documents\Paradox Interactive\Hearts of Iron IV by default)
-map                     creates a map with all -map_<option> options
-map_highlight           highlights countries specified with -country
-country <COUNTRY> ...   countries specified with this are analyzed and their data is either highlight on the map or printed out depending on other option
-debug                   prints more info about the program
```

## Basic usage

### Saving .hoi4 file as JSON

This will save the .hoi4 as HOI4-game.json

`java -jar HOI4.jar -game GAME.hoi4 -json`

### Saving .hoi4 file as JSON but limiting the size so you can actually open it without crashing your editor

This will ignore all JSON objects with more than 100 values and **only** save ones smaller than that.

`java -jar HOI4.jar -game GAME.hoi4 -json -json_limit 100`

### Saving .hoi4 file as JSON with non-default folder path

If your game doesn't use the default `%USERPROFILE\Documents\Paradox Interactive\Hearts of Iron IV\` folder you can use -folder to specify a correct folder. This will look for a `GAME.hoi4` file in `G:\Hearts of Iron IV\save games\`

`java -jar HOI4.jar -game GAME.hoi4 -json -folder G:\Hearts of Iron IV`