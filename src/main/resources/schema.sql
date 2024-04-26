/* USE prueba;
DROP DATABASE dental_moovi;
CREATE DATABASE dental_moovi;

USE dental_moovi; */

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL UNIQUE,
    id_parent_category BIGINT, FOREIGN KEY (id_parent_category) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL,
    content_type VARCHAR(10) NOT NULL,
    data LONGBLOB NOT NULL,
    id_product BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL UNIQUE,
    description VARCHAR(255),
    short_description VARCHAR(70),
    unit_price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    open_to_public BOOLEAN NOT NULL,
    id_category BIGINT NOT NULL, FOREIGN KEY (id_category) REFERENCES categories(id),
    id_main_image BIGINT, FOREIGN KEY (id_main_image) REFERENCES images(id)
);

/* ALTER TABLE images
ADD CONSTRAINT fk_images_product_id FOREIGN KEY (id_product) REFERENCES products(id); */

CREATE TABLE IF NOT EXISTS users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(70) NOT NULL UNIQUE,
    cel_phone VARCHAR(13) NOT NULL,
    birthdate DATE,
    gender ENUM('FEMALE', 'MALE', 'OTHER', 'UNDEFINED') NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users_roles(
    id_user BIGINT,
    id_role BIGINT,
    PRIMARY KEY (id_user, id_role),
    FOREIGN KEY (id_user) REFERENCES users(id),
    FOREIGN KEY (id_role) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS departaments(
    id INT PRIMARY KEY,
    name VARCHAR(70) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS municipaly_city(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL,
    id_departament INT NOT NULL, FOREIGN KEY (id_departament) REFERENCES departaments(id)
);

CREATE TABLE IF NOT EXISTS addresses(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(13) NOT NULL,
    id_municipaly_city INT NOT NULL, FOREIGN KEY (id_municipaly_city) REFERENCES municipaly_city(id),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users_addresses(
    id_user BIGINT,
    id_address BIGINT,
    PRIMARY KEY (id_user, id_address),
    FOREIGN KEY (id_user) REFERENCES users(id),
    FOREIGN KEY (id_address) REFERENCES addresses(id)
);

CREATE TABLE IF NOT EXISTS orders(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_file LONGBLOB,
    status ENUM('COMPLETE', 'CANCEL', 'PENDING') NOT NULL,
    id_user BIGINT NOT NULL, FOREIGN KEY (id_user) REFERENCES users(id),
    id_address BIGINT NOT NULL, FOREIGN KEY (id_address) REFERENCES addresses(id)
);

CREATE TABLE IF NOT EXISTS orders_products(
    id_order BIGINT,
    id_product BIGINT,
    PRIMARY KEY (id_order, id_product),
    amount INT NOT NULL,
    FOREIGN KEY (id_order) REFERENCES orders(id),
    FOREIGN KEY (id_product) REFERENCES products(id)
);

/* INSERT INTO departaments (id, name) VALUES
(5, 'Antioquia'),
(15, 'Boyacá'),
(23, 'Córdoba'),
(27, 'Chocó'),
(52, 'Nariño'),
(68, 'Santander'),
(50, 'Meta'),
(8, 'Atlántico'),
(13, 'Bolívar'),
(17, 'Caldas'),
(18, 'Caquetá'),
(19, 'Cauca'),
(20, 'Cesar'),
(25, 'Cundinamarca'),
(41, 'Huila'),
(44, 'La Guajira'),
(47, 'Magdalena'),
(63, 'Quindío'),
(66, 'Risaralda'),
(70, 'Sucre'),
(73, 'Tolima'),
(81, 'Arauca'),
(85, 'Casanare'),
(86, 'Putumayo'),
(91, 'Amazonas'),
(94, 'Guainía'),
(97, 'Vaupés'),
(99, 'Vichada'),
(95, 'Guaviare'),
(88, 'Archipiélago de San Andrés, Providencia y Santa Catalina'),
(11, 'Bogotá D.C.'),
(54, 'Norte de Santander'),
(76, 'Valle del Cauca');

INSERT INTO municipaly_city (name, id_departament) VALUES
('Medellín ',5),
('Abejorral ',5),
('Abriaquí ',5),
('Alejandría ',5),
('Amagá ',5),
('Amalfi ',5),
('Andes ',5),
('Angelópolis ',5),
('Angostura ',5),
('Anorí ',5),
('Tununguá ',15),
('Anza ',5),
('Apartadó ',5),
('Arboletes ',5),
('Argelia ',5),
('Armenia ',5),
('Barbosa ',5),
('Bello ',5),
('Betania ',5),
('Betulia ',5),
('Ciudad Bolívar ',5),
('Briceño ',5),
('Buriticá ',5),
('Cáceres ',5),
('Caicedo ',5),
('Caldas ',5),
('Campamento ',5),
('Cañasgordas ',5),
('Caracolí ',5),
('Caramanta ',5),
('Carepa ',5),
('Motavita ',15),
('Carolina ',5),
('Caucasia ',5),
('Chigorodó ',5),
('Cisneros ',5),
('Cocorná ',5),
('Concepción ',5),
('Concordia ',5),
('Copacabana ',5),
('Dabeiba ',5),
('Don Matías ',5),
('Ebéjico ',5),
('El Bagre ',5),
('Entrerrios ',5),
('Envigado ',5),
('Fredonia ',5),
('San Bernardo del Viento ',23),
('Giraldo ',5),
('Girardota ',5),
('Gómez Plata ',5),
('Istmina ',27),
('Guadalupe ',5),
('Guarne ',5),
('Guatapé ',5),
('Heliconia ',5),
('Hispania ',5),
('Itagui ',5),
('Ituango ',5),
('Belmira ',5),
('Jericó ',5),
('La Ceja ',5),
('La Estrella ',5),
('La Pintada ',5),
('La Unión ',5),
('Liborina ',5),
('Maceo ',5),
('Marinilla ',5),
('Montebello ',5),
('Murindó ',5),
('Mutatá ',5),
('Nariño ',5),
('Necoclí ',5),
('Nechí ',5),
('Olaya ',5),
('Peñol ',5),
('Peque ',5),
('Pueblorrico ',5),
('Puerto Berrío ',5),
('Puerto Nare ',5),
('Puerto Triunfo ',5),
('Remedios ',5),
('Retiro ',5),
('Rionegro ',5),
('Sabanalarga ',5),
('Sabaneta ',5),
('Salgar ',5),
('Ciénega ',15),
('Santacruz ',52),
('San Francisco ',5),
('San Jerónimo ',5),
('Puerto Wilches ',68),
('Puerto Parra ',68),
('San Luis ',5),
('San Pedro ',5),
('San Rafael ',5),
('San Roque ',5),
('San Vicente ',5),
('Santa Bárbara ',5),
('Santo Domingo ',5),
('El Santuario ',5),
('Segovia ',5),
('Sopetrán ',5),
('Uribe ',50),
('Támesis ',5),
('Tarazá ',5),
('Tarso ',5),
('Titiribí ',5),
('Toledo ',5),
('Turbo ',5),
('Uramita ',5),
('Urrao ',5),
('Valdivia ',5),
('Valparaíso ',5),
('Vegachí ',5),
('Venecia ',5),
('Yalí ',5),
('Yarumal ',5),
('Yolombó ',5),
('Yondó ',5),
('Zaragoza ',5),
('Barranquilla ',8),
('Baranoa ',8),
('Candelaria ',8),
('Galapa ',8),
('Luruaco ',8),
('Malambo ',8),
('Manatí ',8),
('Piojó ',8),
('Polonuevo ',8),
('Sabanagrande ',8),
('Sabanalarga ',8),
('Santa Lucía ',8),
('Santo Tomás ',8),
('Soledad ',8),
('Suan ',8),
('Tubará ',8),
('Usiacurí ',8),
('Achí ',13),
('Arenal ',13),
('Arjona ',13),
('Arroyohondo ',13),
('Calamar ',13),
('Cantagallo ',13),
('Cicuco ',13),
('Córdoba ',13),
('Clemencia ',13),
('El Guamo ',13),
('Magangué ',13),
('Mahates ',13),
('Margarita ',13),
('Montecristo ',13),
('Mompós ',13),
('Morales ',13),
('Norosí ',13),
('Pinillos ',13),
('Regidor ',13),
('Río Viejo ',13),
('San Estanislao ',13),
('San Fernando ',13),
('San Juan Nepomuceno ',13),
('Santa Catalina ',13),
('Santa Rosa ',13),
('Simití ',13),
('Soplaviento ',13),
('Talaigua Nuevo ',13),
('Tiquisio ',13),
('Turbaco ',13),
('Turbaná ',13),
('Villanueva ',13),
('Tunja ',15),
('Almeida ',15),
('Aquitania ',15),
('Arcabuco ',15),
('Berbeo ',15),
('Betéitiva ',15),
('Boavita ',15),
('Boyacá ',15),
('Briceño ',15),
('Buena Vista ',15),
('Busbanzá ',15),
('Caldas ',15),
('Campohermoso ',15),
('Cerinza ',15),
('Chinavita ',15),
('Chiquinquirá ',15),
('Chiscas ',15),
('Chita ',15),
('Chitaraque ',15),
('Chivatá ',15),
('Cómbita ',15),
('Coper ',15),
('Corrales ',15),
('Covarachía ',15),
('Cubará ',15),
('Cucaita ',15),
('Cuítiva ',15),
('Chíquiza ',15),
('Chivor ',15),
('Duitama ',15),
('El Cocuy ',15),
('El Espino ',15),
('Firavitoba ',15),
('Floresta ',15),
('Gachantivá ',15),
('Gameza ',15),
('Garagoa ',15),
('Guacamayas ',15),
('Guateque ',15),
('Guayatá ',15),
('Güicán ',15),
('Iza ',15),
('Jenesano ',15),
('Jericó ',15),
('Labranzagrande ',15),
('La Capilla ',15),
('La Victoria ',15),
('Macanal ',15),
('Maripí ',15),
('Miraflores ',15),
('Mongua ',15),
('Monguí ',15),
('Moniquirá ',15),
('Muzo ',15),
('Nobsa ',15),
('Nuevo Colón ',15),
('Oicatá ',15),
('Otanche ',15),
('Pachavita ',15),
('Páez ',15),
('Paipa ',15),
('Pajarito ',15),
('Panqueba ',15),
('Pauna ',15),
('Paya ',15),
('Pesca ',15),
('Pisba ',15),
('Puerto Boyacá ',15),
('Quípama ',15),
('Ramiriquí ',15),
('Ráquira ',15),
('Rondón ',15),
('Saboyá ',15),
('Sáchica ',15),
('Samacá ',15),
('San Eduardo ',15),
('San Mateo ',15),
('Santana ',15),
('Santa María ',15),
('Santa Sofía ',15),
('Sativanorte ',15),
('Sativasur ',15),
('Siachoque ',15),
('Soatá ',15),
('Socotá ',15),
('Socha ',15),
('Sogamoso ',15),
('Somondoco ',15),
('Sora ',15),
('Sotaquirá ',15),
('Soracá ',15),
('Susacón ',15),
('Sutamarchán ',15),
('Sutatenza ',15),
('Tasco ',15),
('Tenza ',15),
('Tibaná ',15),
('Tinjacá ',15),
('Tipacoque ',15),
('Toca ',15),
('Tópaga ',15),
('Tota ',15),
('Turmequé ',15),
('Tutazá ',15),
('Umbita ',15),
('Ventaquemada ',15),
('Viracachá ',15),
('Zetaquira ',15),
('Manizales ',17),
('Aguadas ',17),
('Anserma ',17),
('Aranzazu ',17),
('Belalcázar ',17),
('Chinchiná ',17),
('Filadelfia ',17),
('La Dorada ',17),
('La Merced ',17),
('Manzanares ',17),
('Marmato ',17),
('Marulanda ',17),
('Neira ',17),
('Norcasia ',17),
('Pácora ',17),
('Palestina ',17),
('Pensilvania ',17),
('Riosucio ',17),
('Risaralda ',17),
('Salamina ',17),
('Samaná ',17),
('San José ',17),
('Supía ',17),
('Victoria ',17),
('Villamaría ',17),
('Viterbo ',17),
('Florencia ',18),
('Albania ',18),
('Curillo ',18),
('El Doncello ',18),
('El Paujil ',18),
('Morelia ',18),
('Puerto Rico ',18),
('Solano ',18),
('Solita ',18),
('Valparaíso ',18),
('Popayán ',19),
('Almaguer ',19),
('Argelia ',19),
('Balboa ',19),
('Bolívar ',19),
('Buenos Aires ',19),
('Cajibío ',19),
('Caldono ',19),
('Caloto ',19),
('Corinto ',19),
('El Tambo ',19),
('Florencia ',19),
('Guachené ',19),
('Guapi ',19),
('Inzá ',19),
('Jambaló ',19),
('La Sierra ',19),
('La Vega ',19),
('López',	19	),
('Mercaderes ',19),
('Miranda ',19),
('Morales ',19),
('Padilla ',19),
('Patía ',19),
('Piamonte ',19),
('Piendamó ',19),
('Puerto Tejada ',19),
('Puracé ',19),
('Rosas ',19),
('Santa Rosa ',19),
('Silvia ',19),
('Sotara ',19),
('Suárez ',19),
('Sucre ',19),
('Timbío ',19),
('Timbiquí ',19),
('Toribio ',19),
('Totoró ',19),
('Villa Rica ',19),
('Valledupar ',20),
('Aguachica ',20),
('Agustín Codazzi ',20),
('Astrea ',20),
('Becerril ',20),
('Bosconia ',20),
('Chimichagua ',20),
('Chiriguaná ',20),
('Curumaní ',20),
('El Copey ',20),
('El Paso ',20),
('Gamarra ',20),
('González ',20),
('La Gloria ',20),
('Manaure ',20),
('Pailitas ',20),
('Pelaya ',20),
('Pueblo Bello ',20),
('La Paz ',20),
('San Alberto ',20),
('San Diego ',20),
('San Martín ',20),
('Tamalameque ',20),
('Montería ',23),
('Ayapel ',23),
('Buenavista ',23),
('Canalete ',23),
('Cereté ',23),
('Chimá ',23),
('Chinú ',23),
('Cotorra ',23),
('Lorica ',23),
('Los Córdobas ',23),
('Momil ',23),
('Moñitos ',23),
('Planeta Rica ',23),
('Pueblo Nuevo ',23),
('Puerto Escondido ',23),
('Purísima ',23),
('Sahagún ',23),
('San Andrés Sotavento ',23),
('San Antero ',23),
('San Pelayo ',23),
('Tierralta ',23),
('Tuchín ',23),
('Valencia ',23),
('Anapoima ',25),
('Arbeláez ',25),
('Beltrán ',25),
('Bituima ',25),
('Bojacá ',25),
('Cabrera ',25),
('Cachipay ',25),
('Cajicá ',25),
('Caparrapí ',25),
('Caqueza ',25),
('Chaguaní ',25),
('Chipaque ',25),
('Choachí ',25),
('Chocontá ',25),
('Cogua ',25),
('Cota ',25),
('Cucunubá ',25),
('El Colegio ',25),
('El Rosal ',25),
('Fomeque ',25),
('Fosca ',25),
('Funza ',25),
('Fúquene ',25),
('Gachala ',25),
('Gachancipá ',25),
('Gachetá ',25),
('Girardot ',25),
('Granada ',25),
('Guachetá ',25),
('Guaduas ',25),
('Guasca ',25),
('Guataquí ',25),
('Guatavita ',25),
('Guayabetal ',25),
('Gutiérrez ',25),
('Jerusalén ',25),
('Junín ',25),
('La Calera ',25),
('La Mesa ',25),
('La Palma ',25),
('La Peña ',25),
('La Vega ',25),
('Lenguazaque ',25),
('Macheta ',25),
('Madrid ',25),
('Manta ',25),
('Medina ',25),
('Mosquera ',25),
('Nariño ',25),
('Nemocón ',25),
('Nilo ',25),
('Nimaima ',25),
('Nocaima ',25),
('Venecia ',25),
('Pacho ',25),
('Paime ',25),
('Pandi ',25),
('Paratebueno ',25),
('Pasca ',25),
('Puerto Salgar ',25),
('Pulí ',25),
('Quebradanegra ',25),
('Quetame ',25),
('Quipile ',25),
('Apulo ',25),
('Ricaurte ',25),
('San Bernardo ',25),
('San Cayetano ',25),
('San Francisco ',25),
('Sesquilé ',25),
('Sibaté ',25),
('Silvania ',25),
('Simijaca ',25),
('Soacha ',25),
('Subachoque ',25),
('Suesca ',25),
('Supatá ',25),
('Susa ',25),
('Sutatausa ',25),
('Tabio ',25),
('Tausa ',25),
('Tena ',25),
('Tenjo ',25),
('Tibacuy ',25),
('Tibirita ',25),
('Tocaima ',25),
('Tocancipá ',25),
('Topaipí ',25),
('Ubalá ',25),
('Ubaque ',25),
('Une ',25),
('Útica ',25),
('Vianí ',25),
('Villagómez ',25),
('Villapinzón ',25),
('Villeta ',25),
('Viotá ',25),
('Zipacón ',25),
('Quibdó ',27),
('Acandí ',27),
('Alto Baudo ',27),
('Atrato ',27),
('Bagadó ',27),
('Bahía Solano ',27),
('Bajo Baudó ',27),
('Bojaya ',27),
('Cértegui ',27),
('Condoto ',27),
('Juradó ',27),
('Lloró ',27),
('Medio Atrato ',27),
('Medio Baudó ',27),
('Medio San Juan ',27),
('Nóvita ',27),
('Nuquí ',27),
('Río Iro ',27),
('Río Quito ',27),
('Riosucio ',27),
('Sipí ',27),
('Unguía ',27),
('Neiva ',41),
('Acevedo ',41),
('Agrado ',41),
('Aipe ',41),
('Algeciras ',41),
('Altamira ',41),
('Baraya ',41),
('Campoalegre ',41),
('Colombia ',41),
('Elías ',41),
('Garzón ',41),
('Gigante ',41),
('Guadalupe ',41),
('Hobo ',41),
('Iquira ',41),
('Isnos ',41),
('La Argentina ',41),
('La Plata ',41),
('Nátaga ',41),
('Oporapa ',41),
('Paicol ',41),
('Palermo ',41),
('Palestina ',41),
('Pital ',41),
('Pitalito ',41),
('Rivera ',41),
('Saladoblanco ',41),
('Santa María ',41),
('Suaza ',41),
('Tarqui ',41),
('Tesalia ',41),
('Tello ',41),
('Teruel ',41),
('Timaná ',41),
('Villavieja ',41),
('Yaguará ',41),
('Riohacha ',44),
('Albania ',44),
('Barrancas ',44),
('Dibula ',44),
('Distracción ',44),
('El Molino ',44),
('Fonseca ',44),
('Hatonuevo ',44),
('Maicao ',44),
('Manaure ',44),
('Uribia ',44),
('Urumita ',44),
('Villanueva ',44),
('Santa Marta ',47),
('Algarrobo ',47),
('Aracataca ',47),
('Ariguaní ',47),
('Cerro San Antonio ',47),
('Chivolo ',47),
('Concordia ',47),
('El Banco ',47),
('El Piñon ',47),
('El Retén ',47),
('Fundación ',47),
('Guamal ',47),
('Nueva Granada ',47),
('Pedraza ',47),
('Pivijay ',47),
('Plato ',47),
('Remolino ',47),
('Salamina ',47),
('San Zenón ',47),
('Santa Ana ',47),
('Sitionuevo ',47),
('Tenerife ',47),
('Zapayán ',47),
('Zona Bananera ',47),
('Villavicencio ',50),
('Acacias ',50),
('Cabuyaro ',50),
('Cubarral ',50),
('Cumaral ',50),
('El Calvario ',50),
('El Castillo ',50),
('El Dorado ',50),
('Granada ',50),
('Guamal ',50),
('Mapiripán ',50),
('Mesetas ',50),
('La Macarena ',50),
('Lejanías ',50),
('Puerto Concordia ',50),
('Puerto Gaitán ',50),
('Puerto López ',50),
('Puerto Lleras ',50),
('Puerto Rico ',50),
('Restrepo ',50),
('San Juanito ',50),
('San Martín ',50),
('Vista Hermosa ',50),
('Pasto ',52),
('Albán ',52),
('Aldana ',52),
('Ancuyá ',52),
('Barbacoas ',52),
('Colón ',52),
('Consaca ',52),
('Contadero ',52),
('Córdoba ',52),
('Cuaspud ',52),
('Cumbal ',52),
('Cumbitara ',52),
('El Charco ',52),
('El Peñol ',52),
('El Rosario ',52),
('El Tambo ',52),
('Funes ',52),
('Guachucal ',52),
('Guaitarilla ',52),
('Gualmatán ',52),
('Iles ',52),
('Imués ',52),
('Ipiales ',52),
('La Cruz ',52),
('La Florida ',52),
('La Llanada ',52),
('La Tola ',52),
('La Unión ',52),
('Leiva ',52),
('Linares ',52),
('Los Andes ',52),
('Magüí ',52),
('Mallama ',52),
('Mosquera ',52),
('Nariño ',52),
('Olaya Herrera ',52),
('Ospina ',52),
('Francisco Pizarro ',52),
('Policarpa ',52),
('Potosí ',52),
('Providencia ',52),
('Puerres ',52),
('Pupiales ',52),
('Ricaurte ',52),
('Roberto Payán ',52),
('Samaniego ',52),
('Sandoná ',52),
('San Bernardo ',52),
('San Lorenzo ',52),
('San Pablo ',52),
('Santa Bárbara ',52),
('Sapuyes ',52),
('Taminango ',52),
('Tangua ',52),
('Túquerres ',52),
('Yacuanquer ',52),
('Armenia ',63),
('Buenavista ',63),
('Circasia ',63),
('Córdoba ',63),
('Filandia ',63),
('La Tebaida ',63),
('Montenegro ',63),
('Pijao ',63),
('Quimbaya ',63),
('Salento ',63),
('Pereira ',66),
('Apía ',66),
('Balboa ',66),
('Dosquebradas ',66),
('Guática ',66),
('La Celia ',66),
('La Virginia ',66),
('Marsella ',66),
('Mistrató ',66),
('Pueblo Rico ',66),
('Quinchía ',66),
('Santuario ',66),
('Bucaramanga ',68),
('Aguada ',68),
('Albania ',68),
('Aratoca ',68),
('Barbosa ',68),
('Barichara ',68),
('Barrancabermeja ',68),
('Betulia ',68),
('Bolívar ',68),
('Cabrera ',68),
('California ',68),
('Carcasí ',68),
('Cepitá ',68),
('Cerrito ',68),
('Charalá ',68),
('Charta ',68),
('Chipatá ',68),
('Cimitarra ',68),
('Concepción ',68),
('Confines ',68),
('Contratación ',68),
('Coromoro ',68),
('Curití ',68),
('El Guacamayo ',68),
('El Playón ',68),
('Encino ',68),
('Enciso ',68),
('Florián ',68),
('Floridablanca ',68),
('Galán ',68),
('Gambita ',68),
('Girón ',68),
('Guaca ',68),
('Guadalupe ',68),
('Guapotá ',68),
('Guavatá ',68),
('Güepsa ',68),
('Jesús María ',68),
('Jordán ',68),
('La Belleza ',68),
('Landázuri ',68),
('La Paz ',68),
('Lebríja ',68),
('Los Santos ',68),
('Macaravita ',68),
('Málaga ',68),
('Matanza ',68),
('Mogotes ',68),
('Molagavita ',68),
('Ocamonte ',68),
('Oiba ',68),
('Onzaga ',68),
('Palmar ',68),
('Páramo ',68),
('Piedecuesta ',68),
('Pinchote ',68),
('Puente Nacional ',68),
('Rionegro ',68),
('San Andrés ',68),
('San Gil ',68),
('San Joaquín ',68),
('San Miguel ',68),
('Santa Bárbara ',68),
('Simacota ',68),
('Socorro ',68),
('Suaita ',68),
('Sucre ',68),
('Suratá ',68),
('Tona ',68),
('Vélez ',68),
('Vetas ',68),
('Villanueva ',68),
('Zapatoca ',68),
('Sincelejo ',70),
('Buenavista ',70),
('Caimito ',70),
('Coloso ',70),
('Coveñas ',70),
('Chalán ',70),
('El Roble ',70),
('Galeras ',70),
('Guaranda ',70),
('La Unión ',70),
('Los Palmitos ',70),
('Majagual ',70),
('Morroa ',70),
('Ovejas ',70),
('Palmito ',70),
('San Benito Abad ',70),
('San Marcos ',70),
('San Onofre ',70),
('San Pedro ',70),
('Sucre ',70),
('Tolú Viejo ',70),
('Alpujarra ',73),
('Alvarado ',73),
('Ambalema ',73),
('Armero ',73),
('Ataco ',73),
('Cajamarca ',73),
('Chaparral ',73),
('Coello ',73),
('Coyaima ',73),
('Cunday ',73),
('Dolores ',73),
('Espinal ',73),
('Falan ',73),
('Flandes ',73),
('Fresno ',73),
('Guamo ',73),
('Herveo ',73),
('Honda ',73),
('Icononzo ',73),
('Mariquita ',73),
('Melgar ',73),
('Murillo ',73),
('Natagaima ',73),
('Ortega ',73),
('Palocabildo ',73),
('Piedras ',73),
('Planadas ',73),
('Prado ',73),
('Purificación ',73),
('Rio Blanco ',73),
('Roncesvalles ',73),
('Rovira ',73),
('Saldaña ',73),
('Santa Isabel ',73),
('Venadillo ',73),
('Villahermosa ',73),
('Villarrica ',73),
('Arauquita ',81),
('Cravo Norte ',81),
('Fortul ',81),
('Puerto Rondón ',81),
('Saravena ',81),
('Tame ',81),
('Arauca ',81),
('Yopal ',85),
('Aguazul ',85),
('Chámeza ',85),
('Hato Corozal ',85),
('La Salina ',85),
('Monterrey ',85),
('Pore ',85),
('Recetor ',85),
('Sabanalarga ',85),
('Sácama ',85),
('Tauramena ',85),
('Trinidad ',85),
('Villanueva ',85),
('Mocoa ',86),
('Colón ',86),
('Orito ',86),
('Puerto Caicedo ',86),
('Puerto Guzmán ',86),
('Leguízamo ',86),
('Sibundoy ',86),
('San Francisco ',86),
('San Miguel ',86),
('Santiago ',86),
('Leticia ',91),
('El Encanto ',91),
('La Chorrera ',91),
('La Pedrera ',91),
('La Victoria ',91),
('Puerto Arica ',91),
('Puerto Nariño ',91),
('Puerto Santander ',91),
('Tarapacá ',91),
('Inírida ',94),
('Barranco Minas ',94),
('Mapiripana ',94),
('San Felipe ',94),
('Puerto Colombia ',94),
('La Guadalupe ',94),
('Cacahual ',94),
('Pana Pana ',94),
('Mitú ',97),
('Carurú ',97),
('Taraira ',97),
('Papunahua ',97),
('Yavaraté ',97),
('Pacoa ',97),
('Morichal ',94),
('Puerto Carreño ',99),
('La Primavera ',99),
('Santa Rosalía ',99),
('Cumaribo ',99),
('San José del Fragua ',18),
('Barranca de Upía ',50),
('Palmas del Socorro ',68),
('San Juan de Río Seco ',25),
('Juan de Acosta ',8),
('Fuente de Oro ',50),
('San Luis de Gaceno ',85),
('El Litoral del San Juan ',27),
('Villa de San Diego de Ubate ',25),
('Barranco de Loba ',13),
('Togüí ',15),
('Santa Rosa del Sur ',13),
('El Cantón del San Pablo ',27),
('Villa de Leyva ',15),
('San Sebastián de Buenavista ',47),
('Paz de Río ',15),
('Hatillo de Loba ',13),
('Sabanas de San Angel ',47),
('Calamar ',95),
('Río de Oro ',20),
('San Pedro de Uraba ',5),
('San José del Guaviare ',95),
('Santa Rosa de Viterbo ',15),
('Santander de Quilichao ',19),
('Miraflores ',95),
('Santafé de Antioquia ',5),
('San Carlos de Guaroa ',50),
('Palmar de Varela ',8),
('Santa Rosa de Osos ',5),
('San Andrés de Cuerquía ',5),
('Valle de San Juan ',73),
('San Vicente de Chucurí ',68),
('San José de Miranda ',68),
('Providencia ',88),
('Santa Rosa de Cabal ',66),
('Guayabal de Siquima ',25),
('Belén de Los Andaquies ',18),
('Paz de Ariporo ',85),
('Santa Helena del Opón ',68),
('San Pablo de Borbur ',15),
('La Jagua del Pilar ',44),
('La Jagua de Ibirico ',20),
('San Luis de Sincé ',70),
('San Luis de Gaceno ',15),
('El Carmen de Bolívar ',13),
('El Carmen de Atrato ',27),
('San Juan de Betulia ',70),
('Pijiño del Carmen ',47),
('Vigía del Fuerte ',5),
('San Martín de Loba ',13),
('Altos del Rosario ',13),
('Carmen de Apicala ',73),
('San Antonio del Tequendama ',25),
('Sabana de Torres ',68),
('El Retorno ',95),
('San José de Uré ',23),
('San Pedro de Cartago ',52),
('Campo de La Cruz ',8),
('San Juan de Arama ',50),
('San José de La Montaña ',5),
('Cartagena del Chairá ',18),
('San José del Palmar ',27),
('Agua de Dios ',25),
('San Jacinto del Cauca ',13),
('San Agustín ',41),
('El Tablón de Gómez ',52),
('San Andrés ',88),
('San José de Pare ',15),
('Valle de Guamez ',86),
('San Pablo de Borbur ',13),
('Santiago de Tolú ',70),
('Bogotá D.C. ',11),
('Carmen de Carupa ',25),
('Ciénaga de Oro ',23),
('San Juan de Urabá ',5),
('San Juan del Cesar ',44),
('El Carmen de Chucurí ',68),
('El Carmen de Viboral ',5),
('Belén de Umbría ',66),
('Belén de Bajira ',27),
('Valle de San José ',68),
('San Luis ',73),
('San Miguel de Sema ',15),
('San Antonio ',73),
('San Benito ',68),
('Vergara ',25),
('San Carlos ',23),
('Puerto Alegría ',91),
('Hato ',68),
('San Jacinto ',13),
('San Sebastián ',19),
('San Carlos ',5),
('Tuta ',15),
('Silos ',54),
('Cácota ',54),
('El Dovio ',76),
('Toledo ',54),
('Roldanillo ',76),
('Mutiscua ',54),
('Argelia ',76),
('El Zulia ',54),
('Salazar ',54),
('Sevilla ',76),
('Zarzal ',76),
('Cucutilla ',54),
('El Cerrito ',76),
('Cartago ',76),
('Caicedonia ',76),
('Puerto Santander ',54),
('Gramalote ',54),
('El Cairo ',76),
('El Tarra ',54),
('La Unión ',76),
('Restrepo ',76),
('Teorama ',54),
('Dagua ',76),
('Arboledas ',54),
('Guacarí ',76),
('Lourdes ',54),
('Ansermanuevo ',76),
('Bochalema ',54),
('Bugalagrande ',76),
('Convención ',54),
('Hacarí ',54),
('La Victoria ',76),
('Herrán ',54),
('Ginebra ',76),
('Yumbo ',76),
('Obando ',76),
('Tibú ',54),
('San Cayetano ',54),
('San Calixto ',54),
('Bolívar ',76),
('La Playa ',54),
('Cali ',76),
('San Pedro ',76),
('Guadalajara de Buga ',76),
('Chinácota ',54),
('Ragonvalia ',54),
('La Esperanza ',54),
('Villa del Rosario ',54),
('Chitagá ',54),
('Calima ',76),
('Sardinata ',54),
('Andalucía ',76),
('Pradera ',76),
('Abrego ',54),
('Los Patios ',54),
('Ocaña ',54),
('Bucarasica ',54),
('Yotoco ',76),
('Palmira ',76),
('Riofrío ',76),
('Santiago ',54),
('Alcalá ',76),
('Versalles ',76),
('Labateca ',54),
('Cachirá ',54),
('Villa Caro ',54),
('Durania ',54),
('El Águila ',76),
('Toro ',76),
('Candelaria ',76),
('La Cumbre ',76),
('Ulloa ',76),
('Trujillo ',76),
('Vijes ',76),
('Chimá ',68),
('Sampués ',70),
('Nunchía ',85),
('Pamplona ',54),
('Albán ',25),
('Montelíbano ',23),
('Puerto Asís ',86),
('Corozal ',70),
('Buesaco ',52),
('Maní ',85),
('El Peñón ',13),
('Tuluá ',76),
('Casabianca ',73),
('Anolaima ',25),
('Chía ',25),
('San Andrés de Tumaco ',52),
('Milán ',18),
('Capitanejo ',68),
('Anzoátegui ',73),
('Florida ',76),
('Repelón ',8),
('Frontino ',5),
('El Peñón ',25),
('Pamplonita ',54),
('Miriti Paraná ',91),
('Támara ',85),
('Tibasosa ',15),
('Páez ',19),
('Ibagué ',73),
('Puerto Colombia ',8),
('Belén ',52),
('Sopó ',25),
('Carmen del Darien ',27),
('Gama ',25),
('Sasaima ',25),
('Chachagüí ',52),
('Cúcuta ',54),
('Cartagena ',13),
('Granada ',5),
('Santa Bárbara de Pinto ',47),
('María la Baja ',13),
('La Montañita ',18),
('San Vicente del Caguán ',18),
('El Peñón ',68),
('Jardín ',5),
('Jamundí ',76),
('Tadó ',27),
('Orocué ',85),
('Líbano ',73),
('Yacopí ',25),
('Calarcá ',63),
('Sonsón ',5),
('El Carmen ',54),
('Lérida ',73),
('La Apartada ',23),
('San Cristóbal ',13),
('Fusagasugá ',25),
('Zambrano ',13),
('La Uvita ',15),
('Zipaquirá ',25),
('Génova ',63),
('Suárez ',73),
('Castilla la Nueva ',50),
('Belén ',15),
('Unión Panamericana ',27),
('Pueblo Viejo ',47),
('Villagarzón ',86),
('Facatativá ',25),
('Puerto Libertador ',23),
('Marquetalia ',17),
('Arboleda ',52),
('Buenaventura ',76),
('Ciénaga ',47),
('Ponedera ',8); */

