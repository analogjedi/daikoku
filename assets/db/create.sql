CREATE TABLE 'nutrition' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'reference_amount' TEXT NOT NULL
);

CREATE TABLE 'nutrient' (
	'nutrition' INTEGER NOT NULL,
	'type' TEXT NOT NULL,
	'amount' TEXT NOT NULL,
	PRIMARY KEY ('nutrition','type'),
	FOREIGN KEY ('nutrition') REFERENCES 'nutrition'('_id')
);

CREATE TABLE 'product' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'label' TEXT NOT NULL,
	'nutrition' INTEGER NOT NULL,
	'amount' TEXT NOT NULL,
	'units' REAL NOT NULL,
	FOREIGN KEY ('nutrition') REFERENCES 'nutrition'('_id')
);

CREATE TABLE 'supply' (
	'product' INTEGER NOT NULL PRIMARY KEY,
	'total' REAL NOT NULL,
	'available' REAL NOT NULL,
	'reserved' REAL NOT NULL,
	'consumed' REAL NOT NULL,
	FOREIGN KEY ('product') REFERENCES 'product'('_id')
);

CREATE TABLE 'recipe' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'label' TEXT NOT NULL
);

CREATE TABLE 'recipe_ingredient' (
	'recipe' INTEGER NOT NULL,
	'product' INTEGER NOT NULL,
	'amount' TEXT NOT NULL,
	PRIMARY KEY ('recipe','product'),
	FOREIGN KEY ('recipe') REFERENCES 'recipe'('_id'),
	FOREIGN KEY ('product') REFERENCES 'product'('_id')
);

CREATE TABLE 'meal' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'label' TEXT NOT NULL,
	'recipe' INTEGER NOT NULL,
	'state' INTEGER NOT NULL,
	'due' TEXT NOT NULL,
	FOREIGN KEY ('recipe') REFERENCES 'recipe'('_id')
);

CREATE TABLE 'meal_nutrient' (
	'meal' INTEGER NOT NULL,
	'type' TEXT NOT NULL,
	'amount' TEXT NOT NULL,
	PRIMARY KEY ('meal','type'),
	FOREIGN KEY ('meal') REFERENCES 'meal'('_id')
);

CREATE TABLE 'vendor' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'label' TEXT NOT NULL
);

CREATE TABLE 'offer' (
	'date' TEXT NOT NULL,
	'vendor' INTEGER NOT NULL,
	'product' INTEGER NOT NULL,
	'price' TEXT NOT NULL,
	PRIMARY KEY ('date','vendor','product'),
	FOREIGN KEY ('vendor') REFERENCES 'vendor'('_id'),
	FOREIGN KEY ('product') REFERENCES 'product'('_id')
);
