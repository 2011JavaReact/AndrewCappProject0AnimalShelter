DROP TABLE IF EXISTS animals;

CREATE TABLE animals (
	animal_id int GENERATED ALWAYS AS IDENTITY,
	animal_name varchar(30),
	species varchar(50),
	breed varchar(50),
	sex varchar(6),
	color varchar(50),
	animal_age int,
	weight int,
	temperament varchar(255),
	PRIMARY KEY(animal_id)
);

INSERT INTO animals (animal_name, species, breed, sex, color, animal_age, weight, temperament)
VALUES ('Honey', 'dog', 'Golden Retreiver', 'female', 'red', 7, 85, 'loving - needs attention'),
('Lily', 'dog', 'Pomeranian', 'female', 'brown', 5, 14, 'shy, but loving'),
('Snowball', 'cat', 'American Shorthair', 'male', 'white', 12, 15, 'standoffish!'),
('Tiger', 'lion', 'African', 'male', 'brown and black', 3, 300, 'sweet, but deadly'),
('Tucker', 'dog', 'German Sheppard', 'male', 'black and brown', 8, 185, 'big bark, but a baby if he knows you'),
('Alvin', 'cat', 'Longhair', 'female', 'black', 1, 6, 'hungry a lot!');

SELECT * FROM animals;

SELECT * FROM animals WHERE lower(species) = 'dog';

SELECT * FROM animals WHERE lower(breed) = 'golden retreiver';

SELECT * FROM animals WHERE animal_id = 1 LIMIT 1;