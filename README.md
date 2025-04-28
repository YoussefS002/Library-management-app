# Library Management System

This project is a library management system developed as part of the **TP (Travaux Pratiques)** for **CentraleSupélec**. The system manages the loan of books, tracks borrowers, and provides functionalities for both library users and administrators. 

## 🚀 Features

The library management system includes the following features:

### For Borrowers:
- **Borrow books**: Users can borrow books with a limited duration.
- **Consult borrowed books**: View a list of borrowed books and their statuses.
- **Consult available books**: View books that are available for loan.

### For Library Administrators:
- **Manage books**: Administrators can add and edit books, including editions and authors.
- **Manage users**: Admins can view the list of borrowers, manage user data, and restrict borrowing rights if necessary.
- **Track borrowing history**: The system keeps an historical record of all borrowings.
- **Red List**: Users can be restricted from borrowing books, and these restrictions are recorded.

### Data Model
- **Books**: Each book has a title, multiple authors, and an associated edition.
- **Editions**: Each edition of a book has an ISBN, a publisher, and a year of publication.
- **Authors**: Each author has a first name, last name, and year of birth.
- **Users**: Borrowers and administrators have first name, last name, email address, and an ID.
- **Categories**: Users belong to categories that set borrowing limits and durations.
- **Loan History**: Each user has an associated borrowing history.

### Database
- The system uses a relational database to store the data, with tables for books, editions, authors, users, categories, and loans.
- **SQL Database Model**: The data model was designed in previous TP sessions.

### UML Modelling
- The system's design follows **UML diagrams** including use case and class diagrams that were worked on in previous TP sessions.

### Technologies Used:
- **JavaFX**: For the graphical user interface (GUI).
- **Java & Kotlin**: For object-oriented development and backend logic.
- **Spring Boot & JDBC**: For interaction with the relational database.
- **UML**: For system design and modeling.
- **MVC Architecture**: The software follows the Model-View-Controller architecture.

## 📝 Implementation Details

- The implementation is divided into the following phases:
  1. **UML Modelling**: Class, Use Case diagrams.
  2. **Database Modeling**: SQL tables and relationships.
  3. **Object-Oriented Design**: Java/Kotlin classes for each entity.
  4. **Graphical User Interface**: JavaFX interface for interaction.
  5. **Database Integration**: Using JDBC to link the system to the database.
