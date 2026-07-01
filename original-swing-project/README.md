# My Personal Project -Loan Rate Matcher

## Project Overview
This application helps users determine the loan rates they are eligible for based on their financial and credit information. By inputting key details such as *age, income, credit score, and current liabilities*, users can receive an estimated loan rate they might qualify for.

## Target Users
- Individuals looking to compare potential loan offers before applying.
- Financial advisors helping clients evaluate their borrowing options.
- Lenders who want a quick pre-screening tool for applicants.

## Why This Project?
I have several years of experience in **digital loan platforms** and hold the **Chartered Financial Analyst (CFA) designation**. With my background in finance, I want to leverage technology to create a practical tool that simplifies loan rate estimation. This project allows me to integrate my expertise in financial modeling with software development to make financial decisions more accessible.

## User Stories
- As a user, I want to be able to add a client’s financial profile by specifying their id, name and credit score so that I can determine their loan rate eligibility.
- As a user, I want to view a list of all stored client profiles, along with their estimated loan rates, so I can compare different cases.
- As a user, I want to be able to update a client’s financial profile if their credit score changes.
- As a user, I want to delete a client’s profile if it is no longer needed.
- As a user, I want to be able to save my current list of clients’ financial profile (if I so choose)
- As a user, I want to be able to load my current list of clients’ financial profile (if I so choose)

## Instructions for End User

- You can generate the first required action related to the user story **"adding multiple Clients to the Client Database"** by clicking the **"Add Client"** button. You will be prompted to enter a **name** and **credit score**, and the client will be added to the list.
  
- You can generate the second required action related to the user story **"adding multiple Clients to the Client Database"** by clicking the **"Filter Clients"** button. You will be prompted to enter a **minimum credit score**, and only clients meeting the criteria will be displayed.

- You can locate my **visual component** by observing the **splash screen** that appears at the **start of the application** for **3 seconds** and the **interactive client list panel**, which displays each client's **ID, name, credit score, and loan rate**.

- You can **save the state of my application** by clicking the **"Save"** button or confirming the **save prompt when closing the application**.

- You can **reload the state of my application** by clicking the **"Load"** button or selecting **"Yes"** when prompted on startup.

## Phase 4: Task 2

### Sample Event Log Output:
Thu Mar 27 23:54:30 PDT 2025   
Loaded client database from file.

Thu Mar 27 23:54:45 PDT 2025  
Added client: Alice

Thu Mar 27 23:54:51 PDT 2025  
Filtered clients with credit score >= 600

## Phase 4: Task 3

After reviewing the UML class diagram, one design improvement I would consider is refactoring the direct use of JsonReader and JsonWriter in both LoanRateMatcherApp and LoanRateMatcherGUI. Right now, these UI classes are responsible for file loading and saving, which means they are tightly coupled to the JSON format and file system operations.

If I had more time, I would create a new class, perhaps called DataManager, whose sole responsibility would be to handle reading from and writing to files. Both UI classes could then delegate persistence tasks to this class, reducing code duplication and improving separation of concerns. This would make the UI classes more focused on user interaction and presentation, while keeping all persistence logic centralized and easier to test. This change would not add new features, but it would improve maintainability. For example, if the data storage format changed in the future, only the DataManager would need to be updated.