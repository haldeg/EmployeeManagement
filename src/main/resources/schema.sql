CREATE TABLE IF NOT EXISTS employeemanagement.employee
(
    id serial primary key,
    name varchar(255) NOT NULL UNIQUE,
    age integer,
    state varchar(255)
);

CREATE TABLE IF NOT EXISTS employeemanagement.employee_state
(
    employee_id integer,
    state varchar(255)
);