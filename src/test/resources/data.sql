INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date)
VALUES ('Java course', 'Course about Spring Framework and Spring Boot', '450', 1036800000000000, '2023-03-16 16:25:21.867392', '2023-03-25 16:25:21.867392');

INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date)
VALUES ('C# course', 'Course about .NET Framework', '400', 1036800000000000, '2023-03-25 16:25:21.867392', '2023-03-25 16:25:21.867392');

INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date)
VALUES ('Python course', 'Course about machine learning and data science', '320', 1036800000000000, '2023-03-19 16:25:21.867392', '2023-03-25 16:25:21.867392');

INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date)
VALUES ('JavaScript course', 'Course about React JS, Angular JS and View JS', '380', 1036800000000000, '2023-03-07 16:25:21.867392', '2023-03-25 16:25:21.867392');


INSERT INTO tags(name)
VALUES ('Java');

INSERT INTO tags(name)
VALUES ('C Sharp');

INSERT INTO tags(name)
VALUES ('Python');

INSERT INTO tags(name)
VALUES ('JavaScript');


INSERT INTO gift_certificates_tags(gift_certificate_id, tag_id)
VALUES (1, 1);

INSERT INTO gift_certificates_tags(gift_certificate_id, tag_id)
VALUES (1, 2);

INSERT INTO gift_certificates_tags(gift_certificate_id, tag_id)
VALUES (1, 3);

INSERT INTO gift_certificates_tags(gift_certificate_id, tag_id)
VALUES (2, 3);

INSERT INTO gift_certificates_tags(gift_certificate_id, tag_id)
VALUES (2, 4);

INSERT INTO gift_certificates_tags(gift_certificate_id, tag_id)
VALUES (3, 2);

INSERT INTO gift_certificates_tags(gift_certificate_id, tag_id)
VALUES (4, 3);
