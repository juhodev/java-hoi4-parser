# java-hoi4-parser

A program for parsing Hearts of Iron 4 game files (only text files for now).

## Prerequisites

Save game files must be in text format.

This can be changed in the `settings.txt` file located in your Hearts of Iron IV folder (`%USERPROFILE%\Documents\Paradox Interactive\Hearts of Iron IV\` by default). In `settings.txt` change `save_as_binary=yes` to `save_as_binary=no`.

## Start options

#### Required

```
-file <path|name>        path to a .hoi4 file - (if -hoi4_folder is specified or the hearts of iron 4 folder is
                         in the default location only a file name can be used)
```

#### Optional

```
-json                    saves the .hoi4 as a .json file
-json_limit <limit>      only saves <limit> amount of values in objects
                         (use this if you want to open files in an editor without breaking it)
-hoi4_folder <path>      path to the hearts of iron IV folder that has save games 
                         (%USERPROFILE%\Documents\Paradox Interactive\Hearts of Iron IV by default)
-help                    prints command options
-debug                   prints more info about the program
```

## Basic usage

### Saving .hoi4 file as JSON

This will save the .hoi4 as GAME.json

`java -jar HOI4Parser.jar -file GAME.hoi4 -json`

or

`java -jar HOI4Parser.jar -file C:/path/to/GAME.hoi4 -json`

### Saving .hoi4 file as JSON but limiting the size so you can actually open it without crashing your editor

This will only save 100 values in JSON objects

`java -jar HOI4Parser.jar -file GAME.hoi4 -json -json_limit 100`

### Saving .hoi4 file as JSON with non-default folder path

If your game doesn't use the default `%USERPROFILE\Documents\Paradox Interactive\Hearts of Iron IV\` folder you can use -folder to specify a correct folder. This will look for a `GAME.hoi4` file in `G:\Hearts of Iron IV\save games\`

`java -jar HOI4Parser.jar -file GAME.hoi4 -json -folder G:\Hearts of Iron IV`
