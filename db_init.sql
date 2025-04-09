DROP TABLE IF EXISTS manager;

DROP TABLE IF EXISTS vacancy;

DROP TABLE IF EXISTS candidate;

CREATE TYPE user_role AS ENUM ('admin', 'manager');


CREATE TABLE "user"
(
    id   SERIAL PRIMARY KEY,
    role user_role,
    password VARCHAR(128),
    login VARCHAR(128)
);

CREATE TABLE manager
(
    id INT PRIMARY KEY REFERENCES "user" (id) ON DELETE SET NULL,                          -- Уникальный идентификатор менеджера
    first_name VARCHAR(100)        NOT NULL, -- Имя менеджера
    last_name  VARCHAR(100)        NOT NULL, -- Фамилия менеджера
    patronymic VARCHAR(100)        NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL, -- Электронная почта менеджера
    phone VARCHAR(20)                        -- Телефон менеджера
);

CREATE TABLE vacancy
(
    id          SERIAL PRIMARY KEY,                                     -- Уникальный идентификатор вакансии
    title       VARCHAR(255) NOT NULL,                                  -- Название вакансии
    description VARCHAR,                                                -- Описание вакансии
    salary      DECIMAL(10, 2),                                         -- Зарплата по вакансии
    manager_id  INT          REFERENCES manager (id) ON DELETE SET NULL -- Менеджер, который курирует вакансию
);

CREATE TABLE candidate
(
    id         SERIAL PRIMARY KEY,    -- Уникальный идентификатор соискателя
    first_name VARCHAR(100) NOT NULL, -- Имя соискателя
    last_name  VARCHAR(100) NOT NULL, -- Фамилия соискателя
    patronymic VARCHAR(100),          -- Фамилия соискателя
    email      VARCHAR(255) NOT NULL, -- Электронная почта соискателя
    phone      VARCHAR(20),           -- Телефон соискателя
    cv         TEXT,                  -- Ссылка на резюме соискателя
    vacancy_id INT REFERENCES vacancy (id) ON DELETE CASCADE
);



INSERT INTO "user" (id, role, password, login)
VALUES (1, 'admin', 'admin123', 'admin'),
       (2, 'manager', 'manager123', 'manager1'),
       (3, 'manager', 'manager123', 'manager2'),
       (4, 'manager', 'manager123', 'manager3'),
       (5, 'manager', 'manager123', 'manager4');

ALTER SEQUENCE user_id_seq RESTART WITH 6;


INSERT INTO manager (id, first_name, last_name, patronymic, email, phone)
VALUES (2, 'Иван', 'Иванов', 'Иванович', 'ivanov@example.com', '+7 (900) 123-45-67'),
       (3, 'Мария', 'Петрова', 'Петровна', 'petrova@example.com', '+7 (900) 234-56-78'),
       (4, 'Алексей', 'Сидоров', 'Алексеевич', 'sidorov@example.com', '+7 (900) 345-67-89'),
       (5, 'Екатерина', 'Михайлова', 'Викторовна', 'mihailova@example.com', '+7 (900) 456-78-90');

-- Вставка данных в таблицу vacancy
INSERT INTO vacancy (title, description, salary, manager_id)
VALUES ('Менеджер по продажам', 'Поиск и привлечение новых клиентов, ведение переговоров, заключение контрактов.',
        50000.00, 2),
       ('Системный администратор',
        'Настройка и обслуживание серверного оборудования, настройка сетевой инфраструктуры.', 60000.00, 3),
       ('Маркетолог', 'Разработка и внедрение маркетинговых стратегий, анализ рынка и конкурентов.', 70000.00, 4),
       ('HR-менеджер', 'Подбор персонала, проведение собеседований, участие в разработке внутренней политики компании.',
        55000.00, 4);

INSERT INTO candidate (first_name, last_name, patronymic, email, phone, cv, vacancy_id)
VALUES ('Дмитрий', 'Кузнецов', 'Сергеевич', 'kuznetsov@example.com', '+7 (900) 567-89-01',
        'https://example.com/resume1.pdf', 1),
       ('Ольга', 'Смирнова', 'Владимировна', 'smirnova@example.com', '+7 (900) 678-90-12',
        'https://example.com/resume2.pdf', 2),
       ('Петр', 'Ильин', 'Дмитриевич', 'iljin@example.com', '+7 (900) 789-01-23', 'https://example.com/resume3.pdf', 3),
       ('Анна', 'Васильева', 'Михайловна', 'vasilieva@example.com', '+7 (900) 890-12-34',
        'https://example.com/resume4.pdf', 4);



