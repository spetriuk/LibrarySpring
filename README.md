# Система Бібліотека

**Читач**, щоб взяти книгу, повинен зареєструватись і зробити запит на книгу.
Читач може повенути взяті книги.

**Адміністратор** повинен підтвердити запит, або відхилити. 

Крім того, адміністратор має можливість додавати, редагувати та видаляти книги.

Є можливіть пошуку книг за автором або назвою.

Requirements:
1. DB - MySql
2. Java version up 8.
3. Maven

How to install:
1. Clone project
2. Enter your database settings to application.properties
3. Run libraryDB_dump.sql from resources/scripts folder
4. Run in terminal commands: 
- mvn clean install
- mvn spring-boot:run
5. Go to link localhost:8088

