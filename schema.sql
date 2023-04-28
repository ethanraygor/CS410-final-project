CREATE DATABASE IF NOT EXISTS gradebook;
USE gradebook;

CREATE TABLE classes (
    class_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    course_number VARCHAR(255) NOT NULL,
    term VARCHAR(255) NOT NULL,
    section INTEGER NOT NULL,
    class_description VARCHAR(255) NOT NULL
);

-- class and category have a one to many relation, and each category belong to one class
CREATE TABLE categories (
    category_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL,
    weight INTEGER NOT NULL,
    
    class_id INTEGER,
	FOREIGN KEY(class_id) REFERENCES classes(class_id)
);

CREATE TABLE students (
    student_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    student_firstname VARCHAR(255) NOT NULL,
    student_lastname VARCHAR(255) NOT NULL
    
);

CREATE TABLE enroll (
	enroll_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    class_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    
	FOREIGN KEY(class_id) REFERENCES classes(class_id),
    FOREIGN KEY(student_id) REFERENCES students(student_id)
    
);

CREATE TABLE assignments (
	assignment_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    assignment_name VARCHAR(255) NOT NULL,
    assignment_description VARCHAR(255) NOT NULL,
    assignment_value INTEGER NOT NULL
);

CREATE TABLE assigned (
	assigned_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    student_id INTEGER NOT NULL,
    assignment_id INTEGER NOT NULL,
    grade INTEGER NOT NULL,
    
	FOREIGN KEY(student_id) REFERENCES students(student_id),
    FOREIGN KEY(assignment_id) REFERENCES assignments(assignment_id)
    
);

-- drop table students; 
-- drop table categories; 
-- drop table classes; 