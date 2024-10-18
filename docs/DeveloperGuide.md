# WheresMyMoney Developer Guide


## Table of Contents
- [Developer Guide](#wheresmymoney-developer-guide)
    - [Table of Contents](#table-of-contents)
    - [Acknowledgements](#acknowledgements)
    - [Design \& Implementation](#design--implementation)
        - [UI and Parser](#ui-and-parser)
    - [User Stories](#user-stories)
    - [Product scope](#product-scope)
        - [Target user profile](#target-user-profile)
        - [Value proposition](#value-proposition)
    - [Non-Functional Requirements](#non-functional-requirements)
    - [Glossary](#glossary)
    - [Instructions for Testing](#instructions-for-testing)
        - [Manual Testing](#manual-testing)
        - [JUnit Testing](#junit-testing)

<div style="page-break-after: always;"></div>

## Acknowledgements

WheresMyMoney uses the following libraries

1. [OpenCSV](https://opencsv.sourceforge.net/) - Used for saving/ loading expenses

WheresMyMoney uses the following tools for development:

1. [JUnit](https://junit.org/junit5/) - Used for testing.
2. [Gradle](https://gradle.org/) - Used for build automation.

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

### UI and Parser


## Product scope

### Target user profile

Our target user profile is frugal and tech-savvy university students.

### Value proposition

The application will track how much a user is spending and what they are spending it on. 
The application can provide summaries and statistical insights to spending habits, optimised for people who prefer a CLI.

## User Stories

| Version | As a ... | I want to ...             | So that I can ...                                                  |
|---------|----------|---------------------------|--------------------------------------------------------------------|
| v1.0    | user     | add expenses              | track how much money I have spent so far                           |
| v1.0    | user     | delete expenses           | clear wrong expenses to ensure expense tracking is accurate        |
| v1.0    | user     | edit expenses             | correct inaccurate expenses to ensure expense tracking is accurate |
| v1.0    | user     | list expenses             | track my spending                                                  |
| v1.0    | new user | see usage instructions    | refer to them when I forget how to use the application             |


## Non-Functional Requirements

1. Technical Requirements: Any mainstream OS, i.e. Windows, macOS or Linux, with Java 17 installed.
2. Project Scope Constraints: The application should only be used for tracking. It is not meant to be involved in any form of monetary transaction.
3. Project Scope Constraints: Data storage is only to be performed locally.
4. Quality Requirements: The application should be able to be used effectively by a novice with little experience with CLIs.

## Glossary

* *Expense* - Payment made for various purposes. It has a price, category and description.

## Instructions for Testing

## Manual Testing

View the [User Guide](UserGuide.md) for the full list of UI commands and their related use case and expected outcomes.

## JUnit Testing

JUnit tests are written in the subdirectory `test` and serve to test key methods part of the application.