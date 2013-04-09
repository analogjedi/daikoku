CREATE TABLE 'amount' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'value' REAL NOT NULL,
	'unit' TEXT NOT NULL
);

CREATE TABLE 'nutrition_info' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'reference_amount' INTEGER NOT NULL
);

CREATE TABLE 'nutrient' (
	'nutrition_info' INTEGER NOT NULL,
	'type' TEXT NOT NULL,
	'amount' INTEGER NOT NULL
);

CREATE TABLE 'product' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'label' TEXT NOT NULL,
	'nutrition_info' INTEGER NOT NULL,
	'amount' INTEGER NOT NULL,
	'units' INTEGER
);

CREATE TABLE 'ingredient' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'label' TEXT NOT NULL,
	'product' INTEGER NOT NULL,
	'price' INTEGER NOT NULL,
	'total' REAL NOT NULL,
	'available' REAL NOT NULL,
	'reserved' REAL NOT NULL,
	'consumed' REAL NOT NULL,
	'unit' TEXT NOT NULL
);

CREATE TABLE 'recipe' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'label' TEXT NOT NULL
);

CREATE TABLE 'recipe_ingredient' (
	'recipe' INTEGER NOT NULL,
	'product' INTEGER NOT NULL,
	'amount' INTEGER NOT NULL
);

CREATE TABLE 'meal' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'label' TEXT NOT NULL,
	'recipe' INTEGER NOT NULL,
	'state' INTEGER NOT NULL,
	'due' TEXT NOT NULL
);

CREATE TABLE 'meal_nutrient' (
	'meal' INTEGER NOT NULL,
	'type' TEXT NOT NULL,
	'amount' INTEGER NOT NULL
);

CREATE TABLE 'vendor' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'label' TEXT NOT NULL
);

CREATE TABLE 'offer' (
	'_id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'vendor' INTEGER NOT NULL,
	'product' INTEGER NOT NULL,
	'price' INTEGER NOT NULL
);
