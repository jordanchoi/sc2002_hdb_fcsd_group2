# SC2002 - OBJECTED-ORIENTED DESIGN & PROGRAMMING | FCSD Group 2
This project was developed as part of our SC2002 - Object-Oriented Design & Programming assignment at NTU, Semester 2 AY25/26.
This system simulates the backend operations of Singapore's HDB Build-to-Order Flat application, booking and management process across various user roles, such as Applicants, Officers and Managers, where we applied OOP concepts, SOLID principles and basic software design patterns like MVC, Factory, Singleton, etc to build a modular, scalable CLI-based application.

## Team Members
[Choi Shu Yih, Jordan](https://github.com/jordanchoi)
[Chin Jiaqi](https://github.com/ch11nn)
[Desmond Koh Chye Ang](https://github.com/DesmondKCA)
[Leong Hao Jie](https://github.com/Haxane)
[Low Zhi Yang Nicholas](https://github.com/LZYNicholas)

## Tech Stack & Technologies Used
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)![Maven]((https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white))![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)![Lucid](https://www.google.com/imgres?imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fen%2Fthumb%2Ff%2Ff2%2FLucidchart_logo_%2528September_2021%2529.svg%2F2560px-Lucidchart_logo_%2528September_2021%2529.svg.png&tbnid=-6mgsjJfQlgqHM&vet=10CAIQxiAoAGoXChMIqNvz2YX4jAMVAAAAAB0AAAAAEAc..i&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FFile%3ALucidchart_logo_(September_2021).svg&docid=F17CLUV6ZBD1GM&w=2560&h=384&itg=1&q=lucidchart%20badge&ved=0CAIQxiAoAGoXChMIqNvz2YX4jAMVAAAAAB0AAAAAEAc)![Microsoft Excel](https://img.shields.io/badge/Microsoft_Excel-217346?style=for-the-badge&logo=microsoft-excel&logoColor=white)

## Features
### General
- User Authentication (CORE FEATURE)
  - User Login using credentials stored in Excel data files
  - Change Password functionality, with updates reflected in Excel data
- File Input & Output Requirements
  - Read (Output), Create, Update, Delete operations on Excel files
- Project Browsing
  - Filter and view visible BTO projects
  - Projects sorted in alphabetical order by default
- System Operations
  - User Logout
  - System Termination

### Applicants
- View Projects
  - View visible projects based on marital status and age
- Application Submission
  - Submit application with restrictions:
    - 2-Room Flats for Singles aged ≥ 35 years
    - 2-Room or 3-Room Flats for Married individuals
  - Only one active application allowed at a time
- Application Management
  - View submitted applications (including hidden projects)
  - View application/project details and statuses (Pending / Successful / Unsuccessful / Booked)
  - Book a flat upon "SUCCESSFUL" status (through Officer assistance)
  - Request withdrawal of applications if eligible
  - Resubmit applications if status permits
- Enquiry System
  - Submit enquiry for visible projects
  - View, edit, and delete submitted enquiries
  - Enquiries support multiple messages (threaded form)
  - Delete enquiry only if no reply received

### Officers
- Inherits All Applicant Functions
- Additional Officer Functions
  - Book flats on behalf of applicants
  - View status of Booking Officer Applications
  - View all projects regardless of status
  - Manage enquiries for assigned projects:
    - View and reply to enquiries
  - Search and view BTO Applications by applicant NRIC
  - Process and update application statuses
  - Facilitate flat bookings:
    - Update applicant booking status
    - Update number of available flats
    - Assign selected flat type
  - Generate and export Booking Receipt as PDF

### HDB Managers
- Exercise and Project Management
  - Create, Read, Update, Delete BTO Exercises
  - Create, Read, Update, Delete BTO Projects
  - Toggle visibility of BTO Projects
  - One-project management restriction enforced
- View and Process Applications
  - View all BTO Projects (regardless of visibility)
  - View own created BTO Projects
  - View and process Officer project applications (Approve/Reject)
  - Assign Officers to projects if slots are available
  - View BTO Applications submitted by Applicants
  - Process Applicant Applications (Approve/Reject) based on flat availability
- Reporting and Enquiries
  - Generate Flat Booking Report in PDF, filtered by:
    - Flat Type
    - Project Attributes
    - Applicant Age or Marital Status
  - Manage enquiries for all projects:
    - View all project enquiries
    - View and reply to enquiries for managed projects

### Additional System Features
- Data Management
  - Full CRUD operations for:
    - BTO Projects
    - BTO Applications
    - BTO Project Applications
    - HDB Managers
    - HDB Applicants
    - HDB Officers
    - HDB Block Lists
    - BTO Enquiries
    - BTO Flats List
- PDF Export
  - Generate Booking Receipts (by Officer)
  - Generate Flat Booking Reports (by Manager)
- Booking Management
  - Select specific Block and Flat Unit during booking
- Realistic Modeling
  - One BTO Exercise can launch multiple BTO Projects (1:M relationship)

 ## Project Structure

```
├── output
│   └── ApplicantReport.pdf
├── pom.xml
├── src
│   └── main
│       ├── java
│       │   └── sg
│       │       └── edu
│       │           └── ntu
│       │               └── sc2002
│       │                   └── ay2425
│       │                       └── fcsdGroup2
│       │                           ├── Main.java
│       │                           ├── TestEnquiryRepository.java
│       │                           ├── controller
│       │                           │   ├── ApplicantController.java
│       │                           │   ├── ApplicationController.java
│       │                           │   ├── BTOProjsController.java
│       │                           │   ├── BookingController.java
│       │                           │   ├── EnquiryController.java
│       │                           │   ├── HDBBTOExerciseController.java
│       │                           │   ├── HDBFlatsController.java
│       │                           │   ├── HDBManagerController.java
│       │                           │   ├── HDBOfficerController.java
│       │                           │   ├── OfficerProjectApplicationController.java
│       │                           │   ├── UserAuthController.java
│       │                           │   └── interfaces
│       │                           │       ├── ManagerEnquiryController.java
│       │                           │       ├── ProjNameFilter.java
│       │                           │       └── canApplyFlat.java
│       │                           ├── data
│       │                           ├── factory
│       │                           │   └── FlatTypeFactory.java
│       │                           ├── model
│       │                           │   ├── entities
│       │                           │   │   ├── Application.java
│       │                           │   │   ├── BTOExercise.java
│       │                           │   │   ├── BTOProj.java
│       │                           │   │   ├── Block.java
│       │                           │   │   ├── Enquiry.java
│       │                           │   │   ├── Flat.java
│       │                           │   │   ├── FlatType.java
│       │                           │   │   ├── HDBApplicant.java
│       │                           │   │   ├── HDBManager.java
│       │                           │   │   ├── HDBOfficer.java
│       │                           │   │   ├── OfficerProjectApplication.java
│       │                           │   │   ├── ProjectMessage.java
│       │                           │   │   └── User.java
│       │                           │   └── enums
│       │                           │       ├── ApplicationStatus.java
│       │                           │       ├── AssignStatus.java
│       │                           │       ├── FilterOption.java
│       │                           │       ├── FlatBookingStatus.java
│       │                           │       ├── FlatTypes.java
│       │                           │       ├── MaritalStatus.java
│       │                           │       ├── Neighbourhoods.java
│       │                           │       ├── ProjStatus.java
│       │                           │       └── UserRoles.java
│       │                           ├── repository
│       │                           │   ├── ApplicationRepository.java
│       │                           │   ├── BTORepository.java
│       │                           │   ├── BTOStorageProvider.java
│       │                           │   ├── BlockListRepository.java
│       │                           │   ├── EnquiryRepository.java
│       │                           │   ├── FlatsListRepository.java
│       │                           │   ├── ProjectApplicationRepository.java
│       │                           │   └── UserRepository.java
│       │                           ├── service
│       │                           │   ├── ApplicantEnquiryService.java
│       │                           │   ├── ApplicationService.java
│       │                           │   ├── BaseEnquiryService.java
│       │                           │   ├── EnquiryServiceImpl.java
│       │                           │   ├── ManagerEnquiryService.java
│       │                           │   ├── OfficerEnquiryService.java
│       │                           │   └── ProjectFilterService.java
│       │                           ├── util
│       │                           │   ├── FileIO.java
│       │                           │   └── SessionStateManager.java
│       │                           └── views
│       │                               ├── ApplicantView.java
│       │                               ├── BTOExercisesView.java
│       │                               ├── BTOProjectsView.java
│       │                               ├── EnquiryView.java
│       │                               ├── LoginView.java
│       │                               ├── ManagerView.java
│       │                               ├── OfficerView.java
│       │                               ├── ReportView.java
│       │                               ├── handlers
│       │                               │   ├── ApplicantProjectViewHandler.java
│       │                               │   ├── ApplicantViewHandler.java
│       │                               │   ├── ManagerViewHandler.java
│       │                               │   └── OfficerViewHandler.java
│       │                               └── interfaces
│       │                                   ├── RoleHandler.java
│       │                                   └── UserView.java
│       └── resources
│           ├── data
│           │   ├── ApplicantList.xlsx
│           │   ├── ApplicationLists.xlsx
│           │   ├── BlocksList.xlsx
│           │   ├── Enquiries.xlsx
│           │   ├── ExerciseList.xlsx
│           │   ├── FlatsList.xlsx
│           │   ├── ManagerList.xlsx
│           │   ├── OfficerList.xlsx
│           │   ├── ProjectApplicationList.xlsx
│           │   └── ProjectList.xlsx
│           └── log4j2.xml

```
---

## Architecture & Key Design Highlight
The application follows the MVC (Model-View-Controller) to achieve the separation of concerns between Controllers, Models, and Views.
- SOLID Principles
  - Single Responsibility (SRP) across classes, for instances, UserAuthController handles only Authentication-related operations, and FileIO only handles file access input & output operations.
  - Open-Closed Principle (OCP) (BTOStorageProvider interface to allow support various data storage mechanism in the future)
  - Liskov-Substitution Principle (LSP) - Inheritance guidelines were properly followed, as demonstrated in User, HDBManager, and other subclasses.
  - Interface Segregation (Base interfaces for Enquiry handling)
  - Dependency Inversion (Repositories injected into Controllers)
- Design Patterns Used:
  - Singleton: For Repositories and Session management
  - Factory: FlatType creation based on project requirements
  - Repository: Through the usage of numerous repository.


### UML Class Diagram
-

### Sequence Diagram



## How to Run
1. Clone or download this repository.
2. Ensure you have Java 17 and Maven installed.
3. Navigate to the project directory.
4. Run the following commands:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="sg.edu.ntu.sc2002.ay2425.fcsdGroup2.Main"

## Acknowledgments
- NTU SC2002 Teaching Team & Lecture Material
- Team 2 Members
- Stack Overflow
- Visual Paradigm Class Diagram Tutorial
- Open-source libraries (Apachie POI, etc)
