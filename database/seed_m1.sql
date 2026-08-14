INSERT INTO employees (emp_id, name, role, department)
VALUES (
    gen_random_uuid(),
    'John Smith',
    'DEVELOPER',
    'Engineering'
);

INSERT INTO skills (skill_id, name, category)
VALUES
    (gen_random_uuid(), 'Java', 'TECHNICAL'),
    (gen_random_uuid(), 'Spring Boot', 'TECHNICAL'),
    (gen_random_uuid(), 'Angular', 'TECHNICAL');

-- Link employee to skills with proficiency
-- Use the actual generated UUIDs
-- INSERT INTO employee_skills (id, emp_id, skill_id, proficiency)
-- VALUES (gen_random_uuid(), '<emp-uuid>', '<skill-uuid>', 8);