CREATE TABLE people(
	p_id serial primary key,
	name varchar,
	password varchar
);

CREATE TABLE friends(
	f_id serial primary key,
	friend1 int references people(p_id),
	friend2 int references people(p_id)
);

CREATE TABLE messages(
	m_id serial primary key,
	sender int references people(p_id),
	receiver int references people(p_id),
	message varchar
);