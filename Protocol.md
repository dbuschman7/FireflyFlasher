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


# Commands 

## Flash 

flash/{duration} 

Turn the test LED on for the given duration of time and then turn it off

## Pause 

pause/{duration} 

Delay for the given duration of time

# Examples 

## CQD - Morse Code 

Code | Command Sequence
---|---
. | `flash/250|pause/750`
- | `flash/750|pause/250`

Example of . . . - - - . . .

is 

`flash/250|pause/750|flash/250|pause/750|flash/250|pause/750|flash/750|pause/250|flash/750|pause/250|flash/750|pause/250|flash/250|pause/750|flash/250|pause/750|flash/250|pause/750`

