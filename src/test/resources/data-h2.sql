--First Hotel
insert into hotel_information (address, description, name, hotel_id) values ('Hyderabad', 'My First Hotel', 'MyHotel101', 100);
insert into hotel_inventory_contact_numbers (hotel_inventory_hotel_id, contact_numbers) values (100, 9999999999);

insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (CURRENT_DATE(), CURRENT_DATE()+2, 'SINGLE_BED', 100101);
insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (NULL, NULL, 'DOUBLE_BED', 100102);
insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (CURRENT_DATE(), CURRENT_DATE()+5, 'KING_BED', 100103);
insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (CURRENT_DATE(), CURRENT_DATE()+1, 'DOUBLE_QUEEN', 100104);
insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (CURRENT_DATE(), CURRENT_DATE()+3, 'RUN_OF_THE_HOUSE', 100105);

update rooms_tbl set hotel_id=100  where room_number=100101;
update rooms_tbl set hotel_id=100  where room_number=100102;
update rooms_tbl set hotel_id=100  where room_number=100103;
update rooms_tbl set hotel_id=100  where room_number=100104;
update rooms_tbl set hotel_id=100  where room_number=100105;

--Second Hotel
insert into hotel_information (address, description, name, hotel_id) values ('Bangalore', 'My Second Hotel', 'MyHotel102', 200);
insert into hotel_inventory_contact_numbers (hotel_inventory_hotel_id, contact_numbers) values (200, 9999999999);
insert into hotel_inventory_contact_numbers (hotel_inventory_hotel_id, contact_numbers) values (200, 8888888888);

insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (CURRENT_DATE(), CURRENT_DATE()+2, 'SINGLE_BED', 200101);
insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (NULL, NULL, 'DOUBLE_BED', 200102);
insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (CURRENT_DATE(), CURRENT_DATE()+5, 'KING_BED', 200103);
insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (CURRENT_DATE(), CURRENT_DATE()+1, 'DOUBLE_QUEEN', 200104);
insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (CURRENT_DATE(), CURRENT_DATE()+3, 'RUN_OF_THE_HOUSE', 200105);

update rooms_tbl set hotel_id=200  where room_number=200101;
update rooms_tbl set hotel_id=200  where room_number=200102;
update rooms_tbl set hotel_id=200  where room_number=200103;
update rooms_tbl set hotel_id=200  where room_number=200104;
update rooms_tbl set hotel_id=200  where room_number=200105;

--Third Hotel - to delete
insert into hotel_information (address, description, name, hotel_id) values ('Bangalore', 'My Third Hotel', 'MyHotel103', 300);
insert into hotel_inventory_contact_numbers (hotel_inventory_hotel_id, contact_numbers) values (300, 9999999999);

insert into rooms_tbl (booked_from, booked_until, room_type, room_number) values (CURRENT_DATE(), CURRENT_DATE()+2, 'SINGLE_BED', 300101);

update rooms_tbl set hotel_id=300  where room_number=300101;
