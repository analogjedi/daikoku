INSERT INTO 'vendor' ('_id','label') VALUES (0,'Aldi');
INSERT INTO 'vendor' ('_id','label') VALUES (1,'Norma');
INSERT INTO 'vendor' VALUES (2,'Netto');

INSERT INTO 'nutrition' VALUES (0,'100g');
INSERT INTO 'nutrient' VALUES (0,'E','336kcal');
INSERT INTO 'nutrient' VALUES (0,'P','23g');
INSERT INTO 'nutrient' VALUES (0,'C','50g');
INSERT INTO 'nutrient' VALUES (0,'Cs','1.1g');
INSERT INTO 'nutrient' VALUES (0,'F','1.6g');
INSERT INTO 'nutrient' VALUES (0,'Fs','0.4g');
INSERT INTO 'nutrient' VALUES (0,'Cf','15g');
INSERT INTO 'nutrient' VALUES (0,'S','10mg');
INSERT INTO 'product' VALUES (0,'Linsen (Muellers Muehle)',0,'10kg',0);

INSERT INTO 'nutrition' VALUES (1,'100g');
INSERT INTO 'nutrient' VALUES (1,'E','55kcal');
INSERT INTO 'product' VALUES (1,'Apples',1,'120g',1);


INSERT INTO 'recipe' VALUES (0,'Linsenrezept');
INSERT INTO 'recipe_ingredient' VALUES (0,0,'500g');

INSERT INTO 'meal' VALUES (0,'Linsenmahlzeit',0,4,'2013-04-10');
INSERT INTO 'meal' VALUES (1,'Linsenmahlzeit',0,1,'2013-04-11');
INSERT INTO 'meal' VALUES (2,'Linsenmahlzeit',0,1,'2013-04-12');
INSERT INTO 'meal' VALUES (3,'Linsenmahlzeit',0,1,'2013-04-13');
