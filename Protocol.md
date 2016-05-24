# Flasher Protocol
This document defines the transmission contract between the Arduino and Android App. 

## Arduino App
* The Arduino with be running continuously waiting to commands to run. 
* When it receives a series of basic commands that is will execute immediately 
and then wait for more commands. 
* After a command has been completed, it will transmit that command back to the sender.

## Android App
* Will be responsible for create sets of basic commands to represent more complex behaviors
* It will transmit a sets of commands and watch for the response of each commands execution.
* Many different behaviors can be supported from different screens with the App. 

# Protocol 
## Terms 
Term | Description 
--- | ---
`|` | denotes a separator between commands 
`/` | denotes a separator between parameters 
`{var}` | a variable of data for a given parameters

## Variables 
Variable | Details
---|---
{duration} | in milliseconds
{red-intensity} | a value from 0-255
{green-intensity} | a value from 0-255
{blue-intensity} | a value from 0-255

# Commands 

### Flash - flash/{duration} 
Turn the test LED on for the given duration of time and then turn it off

### Pause - pause/{duration} 
Delay for the given duration of time

### Color - color/{duration}/{red-intensity}/{green-intensity}/{blue-intensity}
Turn the RGB Led on with a given color/intensity combination

### Red - red/{duration}/{red-intensity}
Turn on the Red part of the color led to the given intensity

### Green - green/{duration}/{green-intensity}
Turn on the Green part of the color led to the given intensity

### Blue - blue/{duration}/{blue-intensity}
Turn on the Blue part of the color led to the given intensity

# Examples 
## CQD - Morse Code 
Code | Command Sequence
---|---
. | `flash/250|pause/750`
- | `flash/750|pause/250`

Example of . . . - - - . . .
is 
`flash/250|pause/750|flash/250|pause/750|flash/250|pause/750|flash/750|pause/250|flash/750|pause/250|flash/750|pause/250|flash/250|pause/750|flash/250|pause/750|flash/250|pause/750`
