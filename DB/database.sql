DROP TABLE messages;
DROP TABLE friends;
DROP TABLE people;

CREATE TABLE people(
	name varchar PRIMARY KEY,
	password varchar
);

CREATE TABLE friends(
	f_id serial primary key,
	friend1 varchar references people(name),
	friend2 varchar references people(name)
);

CREATE TABLE messages(
	m_id serial primary key,
	sender varchar references people(name),
	receiver varchar references people(name),
	message varchar
);