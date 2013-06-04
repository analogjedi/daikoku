CREATE TABLE 'goal' (
	'goal_type' INTEGER NOT NULL,
	'scope' INTEGER NOT NULL,
	'nutrient_type' TEXT NOT NULL,
	'amount' TEXT NOT NULL,
	PRIMARY KEY ('goal_type','scope','nutrient_type')
);

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

CREATE TABLE 'recipe' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'label' TEXT NOT NULL,
	'is_favorite' INTEGER NOT NULL
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
	'state' INTEGER NOT NULL,
	'due' TEXT NOT NULL,
	FOREIGN KEY ('_id') REFERENCES 'recipe'('_id')
);

CREATE TABLE 'recipe_nutrient' (
	'recipe' INTEGER NOT NULL,
	'type' TEXT NOT NULL,
	'amount' TEXT NOT NULL,
	PRIMARY KEY ('recipe','type'),
	FOREIGN KEY ('recipe') REFERENCES 'recipe'('_id')
);

CREATE TABLE 'shopping_item' (
	'id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'product' INTEGER NOT NULL,
	'amount' TEXT NOT NULL,
	'checked' INTEGER NOT NULL,
	FOREIGN KEY ('product') REFERENCES 'product'('_id')
);
