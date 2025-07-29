CREATE TABLE team (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL UNIQUE,
                      country VARCHAR(255) NOT NULL,
                      balance DECIMAL(15,2) DEFAULT 0.00,
                      commission_rate DOUBLE PRECISION NOT NULL CHECK (commission_rate >= 0 AND commission_rate <= 10)
);

CREATE TABLE player (
                        id BIGSERIAL PRIMARY KEY,
                        first_name VARCHAR(255) NOT NULL,
                        last_name VARCHAR(255) NOT NULL,
                        age INT NOT NULL CHECK (age >= 16 AND age <= 45),
                        months_of_experience INT NOT NULL CHECK (
                            months_of_experience >= 0 AND
                            months_of_experience <= (age - 6) * 12
                            ),
                        team_id BIGINT NOT NULL,
                        FOREIGN KEY(team_id) REFERENCES team(id)
);

INSERT INTO team(name, country, balance, commission_rate)
VALUES
    ('FC Barcelona', 'Spain', 200000000, 5.47),
    ('Manchester United', 'England', 95000000, 3.45),
    ('Paris Saint-Germain', 'France', 120000000, 6.23),
    ('Real Madrid', 'Spain', 180000000, 5.05),
    ('Liverpool', 'England', 110000000, 3.05);

INSERT INTO player(first_name, last_name, age, months_of_experience, team_id)
VALUES
    ('Lionel', 'Messi', 37, 240, 1),
    ('Cristiano', 'Ronaldo', 40, 276, 2),
    ('Kylian', 'Mbappe', 26, 120, 3),
    ('Kevin', 'DeBruyne', 32, 180, 2),
    ('Robert', 'Lewandowski', 36, 228, 1),
    ('Mohamed', 'Salah', 33, 190, 5),
    ('Erling', 'Haaland', 25, 84, 3),
    ('Virgil', 'Van Dijk', 34, 192, 5),
    ('Harry', 'Kane', 31, 168, 2);
