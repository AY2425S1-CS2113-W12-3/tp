# WheresMyMoney User Guide

## Introduction

WheresMyMoney allows you to keep track of your spending habits and trends with various supporting tools and functionalities.

## Quick Start

1. Ensure you have Java 17 or above installed in your Computer.
2. Download the latest .jar file from the Github Repository, under releases
3. Copy the file to the folder you want to use as the home folder for your expenses
4. Open a command terminal, cd into the folder you put the jar file in, and use the `java -jar tp.jar` command to run the application.


## Features 


### Add an expense: `add`

Use the add command to add an expense

Format:  `add [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]`

Notes:
- Price is a decimal number
- Description and Category are Text

Examples: `add /price 4.50 /description chicken rice /category food`

### Edit your expense: `edit`

Use the edit command to edit an expense

Format: `edit INDEX [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]`

Notes:
- `PRICE` is a decimal number
- `DESCRIPTION` and Category are Text
- All parameters are optional and only the parameters that are inputted will be reflected after the edit
 
Examples: `edit 1 /price 5.50 /description chicken rice /category food`

### Delete an expense: `delete`

Use the delete command to delete an expense

Format:  `delete [INDEX]`

Examples: `delete 2`

### Get a list of all your transactions: `list`

Use the list command to display expenses and gives the sum of all expenses listed

Format:  `list [/category CATEGORY]`

Notes:
- `CATEGORY` is text
- Lists all expenses the user has if the category is not specified
- Lists all expenses with that category if specified

Examples: `list /category food`

### Viewing help: `help`

Lists to the user command formats that the app recognises

Format: help [/command COMMAND]

Notes:
- `COMMAND` is a text
- `COMMAND` exists in our app

Examples:
- `help`              lists all commands the app has if command is not specified
- `help /command add` lists specified command “add” if a command is specified

### Saves expenses to a file: `save`

Saves all expenses to a csv file `./data.csv`, which can then  be loaded by the program.

Format: `save`

### Loads expenses from a file: `load`

Loads all expenses from a csv file `./data.csv`.

Format: `load`


## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: You can save your expenses to a `.csv` file, which you can then transfer and load on another computer 

## Command Summary


| Command     | Format                                                                | 
|-------------|-----------------------------------------------------------------------|
| Add Expense | `add [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]`  |
 | Edit Expense | `edit INDEX [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]` |
| Delete Expense | `delete [INDEX]` |
| List all Expenses |  `list [/category CATEGORY]` | 
| Viewing help | `help` |
| Saves expenses to a file | `save` |
| Loads expenses from a file | `load` |
