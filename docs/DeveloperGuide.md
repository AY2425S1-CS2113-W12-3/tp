# WheresMyMoney Developer Guide


## Table of Contents
- [Developer Guide](#wheresmymoney-developer-guide)
    - [Table of Contents](#table-of-contents)
    - [Acknowledgements](#acknowledgements)
    - [Design & Implementation](#design--implementation)
    - [Product Scope](#product-scope)
        - [Target User Profile](#target-user-profile)
        - [Value Proposition](#value-proposition)
    - [User Stories](#user-stories)
    - [Non-Functional Requirements](#non-functional-requirements)
    - [Glossary](#glossary)
    - [Instructions for Testing](#instructions-for-testing)
        - [Manual Testing](#manual-testing)
        - [JUnit Testing](#junit-testing)

<div style="page-break-after: always;"></div>

---

## Acknowledgements

WheresMyMoney uses the following libraries

1. [OpenCSV](https://opencsv.sourceforge.net/) - Used for saving/ loading expenses

WheresMyMoney uses the following tools for development:

1. [JUnit](https://junit.org/junit5/) - Used for testing.
2. [Gradle](https://gradle.org/) - Used for build automation.

---

## Design & Implementation


Design and Implementation has been broken down into various sections, each tagged for ease of reference:

- [Architecture](#architecture)
- [UI and Parser](#ui-and-parser)
- [Commands](#commands)
- [Storage](#storage)
- [Expense and Expense List](#expense-and-expense-list)
- [Expense Filter](#expense-filter)
- [Date and Time Handling](#date-and-time-handling)
- [Exceptions and Logging](#exceptions-and-logging)
- [Recurring Expense and Recurring Expense List](#recurring-expense-and-recurring-expense-list)
- [Category Package](#category-package)

### Architecture
A high-level overview of the system is shown in the Architecture Diagram below.

![ArchitectureDiagram.png](diagrams%2Fimages%2FArchitectureDiagram.png "Architecture Diagram of WheresMyMoney")

This architecture consist of: 
1. `UI`, `Main`, `Parser`, and `Command` classes: These classes stand between the user and the internal processing of the software.
2. `Expense`, `ExpenseList`, `ExpenseFilter` classes: Model expenses that commands can interact with.
3. `Storage` class: Stores information between sessions.
4. Logger and other utility classes: Provide extra functionalities for the software.
### UI and Parser

<u>Overview</u>

The Ui class handles I/O operations such as displaying messages and reading user input.
The Parser parses user input and returns the relevant Command Object. 
Both these classes are important for allowing the User to interact with the application.

<u>Methods</u>

The `Ui` class has the following key methods:

| Method           | Description                   |
|------------------|-------------------------------|
| `displayMessage` | Displays a message (`String`) |
| `getUserInput`   | Reads User Input              |

The Parser class has the following key method:

| Method                 | Description                                               |
|------------------------|-----------------------------------------------------------|
| `parseInputToCommand`  | Parses a given user input and returns the related Command |


<u>Design Considerations</u>

Low-level I/O operations (eg. stdio) are consolidated in the Ui class such that we can easily switch the I/O methods by 
modifying only the Ui class. This would make it easier to port the application to other platforms if needed.

Ui class is used as part of exception handling for displaying of error messages to the user for feedback.

The Parser also has some considerations such as
1. Restricted arguments which should not be used by other developers in their commands. These include
    1. `/command` -> used for the main command keyword
    2. `/main` -> used for the main text argument right after the command keyword
3. Any duplicate arguments will throw an InvalidInputException
4. All `/` in the argument values should be escaped
    1. Examples
       1. `command /argument \/value` -> `argument`: `/value`
       2. `command /argument value\/value` -> `argument`:`value/value`
       3. `command /argument value/value` -> `argument`:`value/value` (this is accepted for now, but not recommended)
       4. `command /argument value\\/value` -> `argument`:`value\/value`
   2. `/` doesn't need to be escaped for
       1. commands -> e.g. `/command /argument value1` -> the command is `/command`
           1. It is discouraged to do so, but the option is left for potential expandability
       2. arguments -> e.g. `command /argument/param value` -> the argument name is `argument/param`
    3. Leading and Trailing spaces are ignored, but additional spaces within values (eg. `main  value`) are counted 

### Commands

#### Overview

The abstract Command class has been implemented to introduce an additional layer of abstraction between I/O and command execution, allowing for separation of handling command keywords and executing commands.


<u>Implementation Details</u>

The following diagram is a class diagram for Command and its children classes. 
This has been heavily simplified and only shows the key commands.

![CommandInheritance.png](diagrams%2Fimages%2FCommandInheritance.png)


The following diagram is a sequence diagram for execution of Command.

![CommandExecutionSequence.png](diagrams%2Fimages%2FCommandExecutionSequence.png)

Commands interact with `UI` and `Parser` classes via `Main`, as illustrated in the following class diagram:

![UiToCommand.png](diagrams%2Fimages%2FUiToCommand.png)

### Storage

<u>Overview</u>

Storage is mostly handled by the different states themselves (`ExpenseList`, `RecurringExpenseList`, `CategoryStorage`).
This is to keep the storage tightly coupled with the data and ensures that when the data format is updated,
the storage format is updated accordingly, increasing cohesion.
The current implementation abstracted out common Csv functions into the CsvUtils class, but this implementation
also allows more flexible file formats between different classes, instead of relying solely on a certain format. 
This might help for future expandability.


However, we also do have a Storage class which handles how these file handling methods interact with one another. 
This is to consolidate the overall file loading and saving logic in the program. 
This is useful for certain cases, such as standardising default file paths 
It would be modified when there is a change of interaction between the various loading/ saving methods of the classes.

The LoadCommand and SaveCommand would reference the Storage class, so ideally they would not need to be changed much 
for feature changes.

<u>Implementation Details</u>

Here is an illustration of the related classes involved.

![StorageInteraction.png](diagrams%2Fimages%2FStorageInteraction.png)

<u>Design Considerations</u>

OpenCSV is used for the storage format to allow for reliable handling of CSV files.
The CSV file format was used to allow expenses to be easily exported/ imported to and from other programs (like Excel).

Storage is also meant to be an intentional act by the user. 
This means functionality such as autosave was not implemented due to the fact that it is not an intentional act.


### Expense and Expense List

<u>Overview</u>

The `Expense` class represents an individual expense with a price, description, category and the date added.

The `ExpenseList` class manages a collection of `Expense` objects. 
It allows for the addition, editing and deletion of expenses.

<u>Methods</u>

The `Expense` class has no notable methods.

The `ExpenseList` class has the following key methods: 

|     Method      |            Description            |
|:---------------:|:---------------------------------:|
|  `addExpense`   |    Adds an expense to the list    |
| `deleteExpense` | Removes an expense from the list  |
|  `editExpense`  |   Edits an expense in the list    |

<u>Design Considerations</u>

The setters in `Expense` class checks for null and blank.
The `Expense` constructors also do, as they use those setters.

The `ExpenseList` class contains exception handling when attempting to edit or delete an expense that is not in the list.

<u>Implementation Details</u>

UML class diagram to show the dependency between `Command` and `ExpenseList` classes:

![CommandAndExpenseList.png](diagrams%2Fimages%2FCommandAndExpenseList.png)

The following diagram is a UML class diagram for `Expense` and `ExpenseList`:

![ExpenseAndExpenseList.png](diagrams%2Fimages%2FExpenseAndExpenseList.png "UML Class Diagram for Expense and ExpenseList")

### Expense Filter

The `ExpenseFilter` class provides utility methods for selecting expenses based on their category and time range.

Its interaction with `ExpenseList` is demonstrated in the following UML Class Diagram:

![ExpenseListAndFilter.png](diagrams%2Fimages%2FExpenseListAndFilter.png)

`ExpenseFilter` is a prerequisite to implementing other features, e.g. Expense statistics and visualization.

<u>Implementation Details</u>

`ExpenseFilter` filters expenses by 3 criteria: Category, From (date), To (date).
Each criterion is taken care of by a helper method.
Since these filter fields are optional, if they are `null`, helper methods will evaluate to `true`.

Given a list of expenses, `ExpenseFilter` iterates through each expense, applying three criteria checks on them; and 
it would add the expense to a new `ArrayList` if all three checks are satisfied.

The `ArrayList` is then returned to the caller.

### Date and Time Handling

The `DateUtils` class provides utility methods to handle date formatting, validation and conversion. 

The `DateUtils` class has no notable methods.

<u>Implementation Details</u>

The `DateUtils` class is implemented as a Singleton as its methods are common to all other classes that require it.

### Recurring Expense and Recurring Expense List

<u>Overview</u>

The `RecurringExpense` class extends from the `Expense` class and it represents an indivual recurring expense with a price, description, category, last date added and a frequency.

The `RecurringExpenseList` class extends from the `ExpenseList` class and it manages a collection of `RecurringExpense` objects.
It allows for addition, editing and deletion of expenses.

<u>Methods</u>

The `RecurringExpense` class has no notable methods.

The `RecurringExpenseList` class has the following key methods:

|          Method          |                Description                 |
|:------------------------:|:------------------------------------------:|
|  `addRecurringExpense`   |    Adds a recurring expense to the list    |
| `deleteRecurringExpense` | Removes a recurring expense from the list  |
|  `editRecurringExpense`  |   Edits a recurring expense in the list    |
| `loadFromCsv` | Adds the appropriate amount of `Expense` objects with the correct date to the `ExpenseList`|


<u>Design Considerations</u>

Since the programme does not have an auto-save function upon closing the programme or auto-load when starting the programme, it is up to the user to save their work and to load it again.

To minimise the amount of checks that need to be done, the recurring expenses are only added after the user calls the `load` command.

<u>Implementation Details</u>

Below is the UML class diagram for `RecurringExpense` and `RecurringExpenseList`:

![RecurringExpenseAndRecurringExpenseList.png](diagrams%2Fimages%2FRecurringExpenseAndRecurringExpenseList.png "UML Class Diagram for RecurringExpense and RecurringExpenseList class")

Below is the sequence diagram for when the user calls the `load` command.

![RecurringExpenseLoadFromCsvSequence.png](diagrams%2Fimages%2FRecurringExpenseLoadFromCsvSequence.png "UML Sequence Diagram for calling load command")



### Category Package

<u>Overview</u>

The `CategoryFacade` class serves as an interface that simplifies the interaction with various category-related classes (`CategoryTracker` and `CategoryFilter`), providing a unified API for the rest of the application (namely the `Command` classes).

The `CategoryTracker` class manages a collection of Category-`CategoryData` pairs. It allows for the addition, editing and deletion of category-related information.

The `CategoryData` class contains category-related information, namely the cumulative expenditure (the sum of all prices of expenses with that category) and the spending limits for that category. 

The `CategoryFilter` class is responsible for filtering categories based on various criteria.

<u>Methods</u>

The `CategoryFacade` class has key methods for:

|          Method           |                                                    Description                                                    |
|:-------------------------:|:-----------------------------------------------------------------------------------------------------------------:|
|        addCategory        |                           The interface for AddCommand when the user adds a new Expense                           |
|      deleteCategory       |                         The interface for DeleteCommand when the user deletes an Expense.                         |
|       editCategory        |                           The interface for EditCommand when the user edits an Expense.                           |
|     loadCategoryInfo      |                    The interface for LoadCommand to load category information from a CSV file.                    |
| displayFilteredCategories |                The interface for LoadCommand to show filtered categories based on spending limits.                |
|     saveCategoryInfo      |                 The interface for SaveCommand to save current category information to a CSV file.                 |
| setCategorySpendingLimit  |                  The interface for SetCommand to set a spending limit for a specified category.                   |

The `CategoryTracker` class has the following key methods: 

|       Method        |                                                            Description                                                            |
|:-------------------:|:---------------------------------------------------------------------------------------------------------------------------------:|
|     addCategory     | Adds a new category to the tracker. If already in the tracker, then the total expenditure for that category is increased instead. |
|   deleteCategory    |    Decreases total expenditure of a category. If that total drops to zero or below, the category is removed from the tracker.     |
|    editCategory     |                   Updates the old and new category's total expenditure when an `Expense`'s category is changed.                   |
| setSpendingLimitFor |                                         Sets a spending limit for a particular category.                                          |

The `CategoryData` class has no notable methods.

The `CategoryFilter` class has key methods for:

|          Method           |                                                    Description                                                    |
|:-------------------------:|:-----------------------------------------------------------------------------------------------------------------:|
|        initMaxHeap        |              Initialises a custom max heap that sorts categories by their current total expenditure               |
|   getCategoriesFiltered   | Sorts categories in the tracker, which are nearing or have exceeded the designated spending limit, into max-heaps |
| displayFilteredCategories |              Displays the categories in the provided category-filtered max-heap, in a preset format.              |
| displayExceededCategories |                          Displays the categories that have exceeded its spending limits.                          |
| displayNearingCategories  |                 Displays the categories that are nearing, but not exceeded, its spending limits.                  |

After the user adds or edits an `Expense`, it alerts the user if the spending limit is approached or exceeded for that `Expenses`'s category.

After the user loads from file, all categories that have exceeded its designated spending limit will be displayed to the user, followed by all categories that have not exceeded its designated spending limit but are close to it.

<u>Design Considerations</u>

Each of the classes in this package handle a separate concern relating to category management (achieving Separation of Concerns Principle) and each focuses on a specific responsibility (achieving Single Responsibility Principle).

- `CategoryFacade` class
  - Acts as a singleton because only one instance of this exists throughout the program's lifetime. The program passes this instance around through method parameters.
  - Acts as a facade because it provides the Command classes with a unified interface to the underlying category management classes, without the Command classes knowing of the complexity of the interactions between the category-related classes. 
    - This abstracts and encapsulates the category management information with an interface. 
    - This decouples classes outside the package from the classes inside.
  - Acts as a mediator because the category classes access information from the other classes through this class.
    - `CategoryFilter` accesses the tracker in `CategoryTracker` via the instance attribute in `CategoryFacade`.
    - `CategoryStorage` accesses the tracker in `CategoryTracker` via the instance attribute in `CategoryFacade`.
- `CategoryTracker` class
  - Contains exception handling when attempting to edit or delete a category that is not in the tracker. 
  - Uses a hashmap data structure to store `CategoryData` objects, providing quick access and updating of total expenses.
- `CategoryData` class
  - Setter methods checks for null inputs. Constructor methods also do, as they use those setters. 
- `CategoryFilter` class
  -  Uses priority queue data structures as max heaps to sort the category expenditure information

<u>Implementation Details</u>

The following diagram is a UML class diagram for `CategoryData`, `CategoryTracker`, `CategoryFilter` and `CategoryFacade`:

![Category Classes ClassUML.png](diagrams%2Fimages%2FCategory%20Classes%20ClassUML.png)



### Exceptions and Logging

<u>Overview</u>

The program implements Exception handling and Logging with the WheresMyMoneyException and Logging classes.

<u>Implementation Details</u>

WheresMyMoneyException has various children classes, such as `StorageException` and `InvalidInputException`. 
These children classes are meant to provide more information on the error to the developer (beyond the message) such 
that exception handling in the program could be better targeted in the future.

The Logging class is implemented as a Singleton for ease of use. 
Developers can log down certain actions in the program by simply calling the relevant class method `log(Level, String)`. 

---

## Product scope

### Target user profile

Our target user profile is frugal and tech-savvy university students.

### Value proposition

The application will track how much a user is spending and what they are spending it on. 
The application can provide summaries and statistical insights to spending habits, optimised for people who prefer a CLI.

---

## User Stories

| Version | As a ...    | I want to ...                                                                  | So that I can ...                                                  |
|---------|-------------|--------------------------------------------------------------------------------|--------------------------------------------------------------------|
| v1.0    | user        | add expenses                                                                   | track how much money I have spent so far                           |
| v1.0    | user        | delete expenses                                                                | clear wrong expenses to ensure expense tracking is accurate        |
| v1.0    | user        | edit expenses                                                                  | correct inaccurate expenses to ensure expense tracking is accurate |
| v1.0    | user        | list expenses                                                                  | track my spending                                                  |
| v1.0    | new user    | see usage instructions                                                         | refer to them when I forget how to use the application             |
| v2.0    | user        | save and load my expenses from a file                                          | retain memory of past expenses from past runs of the program       |
| v2.0    | frugal user | set spending limits for each category and month                                | control my spending                                                |
| v2.0    | frugal user | be alerted when I exceed spending limits for each category and month           | control my spending                                                |
| v2.0    | user        | visualise my spending in the form of graphs                                    | better conceptualise and understand spending trends and patterns   |
| v2.0    | user        | detailed statistical information about my spending (such as mean, median etc.) | better quantify and understand spending trends and patterns        |
| v2.0    | user        | add recurring expenses                                                         | automate expense tracking and make it more convenient              |

## Non-Functional Requirements

1. Technical Requirements: Any mainstream OS, i.e. Windows, macOS or Linux, with Java 17 installed.
2. Project Scope Constraints: The application should only be used for tracking. It is not meant to be involved in any form of monetary transaction.
3. Project Scope Constraints: Data storage is only to be performed locally.
4. Quality Requirements: The application should be able to be used effectively by a novice with little experience with CLIs.

---

## Glossary

* *Expense* - Payment made for various purposes. It has a price, category, description and the date added.

---

## Instructions for Testing

## Manual Testing

View the [User Guide](UserGuide.md) for the full list of UI commands and their related use case and expected outcomes.

## JUnit Testing

JUnit tests are written in the subdirectory `test` and serve to test key methods part of the application.
