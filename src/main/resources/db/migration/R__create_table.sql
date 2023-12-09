-- checksum 3002030991
--DROP TABLES IF EXISTS
DROP TABLE IF EXISTS public.cart_line CASCADE;

DROP TABLE IF EXISTS public.cart CASCADE;

DROP TABLE IF EXISTS public.order_table CASCADE;

DROP TABLE IF EXISTS public.address CASCADE;

DROP TABLE IF EXISTS public.image_table CASCADE;

DROP TABLE IF EXISTS public.customer CASCADE;

DROP TABLE IF EXISTS public.product CASCADE;

DROP TABLE IF EXISTS public.paid_option_item CASCADE;

DROP TABLE IF EXISTS public.paid_option CASCADE;

DROP TABLE IF EXISTS public.country CASCADE;

-- CREATE TABLES
CREATE TABLE public.paid_option(
  code VARCHAR(50) PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE public.paid_option_item(
  code VARCHAR(50) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  paid_option VARCHAR(50) NOT NULL REFERENCES paid_option(code),
  price NUMERIC NOT NULL
);

CREATE TABLE public.image_table(
  id SERIAL PRIMARY KEY,
  image BYTEA,
  mime_type VARCHAR(50) NOT NULL,
  file_name VARCHAR(50) NOT NULL
);

CREATE TABLE public.product (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price NUMERIC(10, 2) NOT NULL,
  description TEXT,
  quantity INTEGER NOT NULL,
  image INTEGER REFERENCES image_table(id),
  valid BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE public.customer (
  id SERIAL PRIMARY KEY,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL
);

CREATE TABLE public.address (
  id SERIAL PRIMARY KEY,
  street VARCHAR(255) NOT NULL,
  house_number VARCHAR(255) NOT NULL,
  zip_code VARCHAR(10) NOT NULL,
  city VARCHAR(255) NOT NULL,
  country VARCHAR(255) NOT NULL
);

CREATE TABLE public.cart (
  id SERIAL PRIMARY KEY,
  free_shipping BOOLEAN NOT NULL,
  item_count INTEGER NOT NULL,
  cart_price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE public.order_table (
  id SERIAL PRIMARY KEY,
  customer_id INTEGER NOT NULL REFERENCES customer(id),
  delivery_option_item_code VARCHAR(50) NOT NULL REFERENCES paid_option_item(code),
  billing_option_item_code VARCHAR(50) NOT NULL REFERENCES paid_option_item(code),
  created_fe TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  note TEXT,
  shipping_address INTEGER NOT NULL REFERENCES address(id),
  billing_address INTEGER NOT NULL REFERENCES address(id),
  cart INTEGER NOT NULL REFERENCES cart(id)
);

CREATE TABLE public.cart_line (
  id SERIAL PRIMARY KEY,
  product_id INTEGER NOT NULL REFERENCES product(id),
  amount INTEGER NOT NULL,
  line_total NUMERIC(10, 2) NOT NULL,
  cart INTEGER NOT NULL REFERENCES cart(id)
);

CREATE TABLE public.country (
  id SERIAL PRIMARY KEY,
  code VARCHAR(50) NOT NULL,
  name VARCHAR(255) NOT NULL
);

-- ADD CONSTRAINTS
ALTER TABLE
  public.paid_option_item
ADD
  CONSTRAINT fk_paid_option FOREIGN KEY (paid_option) REFERENCES public.paid_option(code);

ALTER TABLE
  public.product
ADD
  CONSTRAINT fk_product_image FOREIGN KEY (image) REFERENCES public.image_table(id);

ALTER TABLE
  public.order_table
ADD
  CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES public.customer(id);

ALTER TABLE
  public.order_table
ADD
  CONSTRAINT fk_order_delivery_option FOREIGN KEY (delivery_option_item_code) REFERENCES public.paid_option_item(code);

ALTER TABLE
  public.order_table
ADD
  CONSTRAINT fk_order_billing_option FOREIGN KEY (billing_option_item_code) REFERENCES public.paid_option_item(code);

ALTER TABLE
  public.order_table
ADD
  CONSTRAINT fk_order_shipping_address FOREIGN KEY (shipping_address) REFERENCES public.address(id);

ALTER TABLE
  public.order_table
ADD
  CONSTRAINT fk_order_billing_address FOREIGN KEY (billing_address) REFERENCES public.address(id);

ALTER TABLE
  public.order_table
ADD
  CONSTRAINT fk_order_cart FOREIGN KEY (cart) REFERENCES public.cart(id);

ALTER TABLE
  public.cart_line
ADD
  CONSTRAINT fk_cart_line_product FOREIGN KEY (product_id) REFERENCES public.product(id);

ALTER TABLE
  public.cart_line
ADD
  CONSTRAINT fk_cart_line_cart FOREIGN KEY (cart) REFERENCES public.cart(id);

-- INSERT MOCK DATA
INSERT INTO
  public.paid_option (code, name)
VALUES
  ('SHIPPING', 'Shipping');

INSERT INTO
  public.paid_option (code, name)
VALUES
  ('PAYMENT', 'Payment');

INSERT INTO
  public.paid_option_item (code, name, paid_option, price)
VALUES
  (
    'CART-PAYMENT',
    'Cart payment',
    'PAYMENT',
    0
  );

INSERT INTO
  public.paid_option_item (code, name, paid_option, price)
VALUES
  (
    'CASH_ON_DELIVERY-PAYMENT',
    'Cash on delivery',
    'PAYMENT',
    1.99
  );

INSERT INTO
  public.paid_option_item (code, name, paid_option, price)
VALUES
  (
    'GROUND-SHIPPING',
    'Ground Shipping',
    'SHIPPING',
    5.99
  );

INSERT INTO
  public.paid_option_item (code, name, paid_option, price)
VALUES
  (
    '2_DAY_SHIPPING-SHIPPING',
    '2-Day Shipping',
    'SHIPPING',
    15.99
  );

INSERT INTO
  public.paid_option_item (code, name, paid_option, price)
VALUES
  (
    'OVERNIGHT_SHIPPING-SHIPPING',
    'Overnight Shipping',
    'SHIPPING',
    29.99
  );

INSERT INTO
  image_table (image, mime_type, file_name)
VALUES
  (
    null,
    'image/jpeg',
    'product1.jpeg'
  );

INSERT INTO
  image_table (image, mime_type, file_name)
VALUES
  (null, 'image/jpeg', 'product2.jpeg');

INSERT INTO
  image_table (image, mime_type, file_name)
VALUES
  (
    null,
    'application/jpeg',
    'product3.jpeg'
  );

INSERT INTO
  public.product (name, price, description, quantity, image, valid)
VALUES
  (
    'Product 1',
    59.99,
    'ما فائدته ؟ هناك حقيقة مثبتة منذ زمن طويل وهي أن المحتوى المقروء لصفحة ما سيلهي القارئ عن التركيز على الشكل الخارجي للنص أو شكل توضع الفقرات في الصفحة التي يقرأها. ولذلك يتم استخدام طريقة لوريم إيبسوم لأنها تعطي توزيعاَ طبيعياَ -إلى حد ما- للأحرف عوضاً عن استخدام \"هنا يوجد محتوى نصي، هنا يوجد محتوى نصي\" فتجعلها تبدو (أي الأحرف) وكأنها نص مقروء. العديد من برامح النشر المكتبي وبرامح تحرير صفحات الويب تستخدم لوريم إيبسوم بشكل إفتراضي كنموذج عن النص، وإذا قمت بإدخال \"lorem ipsum\" في أي محرك بحث ستظهر العديد من المواقع الحديثة العهد في نتائج البحث. على مدى السنين ظهرت نسخ جديدة ومختلفة من نص لوريم إيبسوم، أحياناً عن طريق الصدفة، وأحياناً عن عمد كإدخال بعض العبارات الفكاهية إليها."',
    50,
    1,
    TRUE
  );

INSERT INTO
  public.product (
    name,
    price,
    description,
    quantity,
    image,
    valid
  )
VALUES
  (
    'Product 2',
    69.99,
    'Lorem Ipsum e елементарен примерен текст, използван в печатарската и типографската индустрия. Lorem Ipsum e индустриален стандарт от около 1500 година, когато неизвестен печатар взема няколко печатарски букви и ги разбърква, за да напечата с тях книга с примерни шрифтове. Този начин не само е оцелял повече от 5 века, но е навлязъл и в публикуването на електронни издания като е запазен почти без промяна. Популяризиран е през 60те години на 20ти век със издаването на Letraset листи, съдържащи Lorem Ipsum пасажи, популярен е и в наши дни във софтуер за печатни издания като Aldus PageMaker, който включва различни версии на Lorem Ipsum."',
    30,
    2,
    TRUE
  );

INSERT INTO
  public.product (name, price, description, quantity, image, valid)
VALUES
  (
    'Product 3',
    89.99,
    'A Lorem Ipsum egy egyszerû szövegrészlete, szövegutánzata a betûszedõ és nyomdaiparnak. A Lorem Ipsum az 1500-as évek óta standard szövegrészletként szolgált az iparban; mikor egy ismeretlen nyomdász összeállította a betûkészletét és egy példa-könyvet vagy szöveget nyomott papírra, ezt használta. Nem csak 5 évszázadot élt túl, de az elektronikus betûkészleteknél is változatlanul megmaradt.  Az 1960-as években népszerûsítették a Lorem Ipsum részleteket magukbafoglaló Letraset lapokkal, és legutóbb softwarekkel mint például az Aldus Pagemaker.',
    20,
    3,
    TRUE
  );

INSERT INTO
  public.customer (firstname, lastname, email)
VALUES
  ('John', 'Doe', 'johndoe@example.com');

INSERT INTO
  public.customer (firstname, lastname, email)
VALUES
  ('Jane', 'Smith', 'janesmith@example.com');

INSERT INTO
  public.address (street, house_number, zip_code, city, country)
VALUES
  ('Main Street', '123', '12345', 'New York', 'USA');

INSERT INTO
  public.address (street, house_number, zip_code, city, country)
VALUES
  ('Broadway', '456', '67890', 'Los Angeles', 'USA');

INSERT INTO
  public.cart (
    free_shipping,
    item_count,
    cart_price
  )
VALUES
  (FALSE, 2, 39.98);

INSERT INTO
  public.cart (
    free_shipping,
    item_count,
    cart_price
  )
VALUES
  (FALSE, 1, 59.98);

INSERT INTO
  public.order_table (
    customer_id,
    delivery_option_item_code,
    billing_option_item_code,
    note,
    shipping_address,
    billing_address,
    cart
  )
VALUES
  (
    1,
    'GROUND-SHIPPING',
    'CART-PAYMENT',
    'Please deliver to front porch',
    1,
    2,
    1
  );

INSERT INTO
  public.order_table (
    customer_id,
    delivery_option_item_code,
    billing_option_item_code,
    shipping_address,
    billing_address,
    cart
  )
VALUES
  (
    2,
    'OVERNIGHT_SHIPPING-SHIPPING',
    'CASH_ON_DELIVERY-PAYMENT',
    2,
    1,
    2
  );

INSERT INTO
  public.cart_line (product_id, amount, line_total, cart)
VALUES
  (1, 2, 39.98, 1);

INSERT INTO
  public.cart_line (product_id, amount, line_total, cart)
VALUES
  (3, 1, 89.99, 2);

INSERT INTO public.country (code, name) VALUES ('AFGHA', 'Afghanistan');
INSERT INTO public.country (code, name) VALUES ('ALBAN', 'Albania  ');
INSERT INTO public.country (code, name) VALUES ('ALGER', 'Algeria');
INSERT INTO public.country (code, name) VALUES ('ANDOR', 'Andorra');
INSERT INTO public.country (code, name) VALUES ('ANGOL', 'Angola');
INSERT INTO public.country (code, name) VALUES ('ANGUI', 'Anguilla');
INSERT INTO public.country (code, name) VALUES ('ANTIG&AMP;BARBU', 'Antigua &amp; Barbuda');
INSERT INTO public.country (code, name) VALUES ('ARGEN', 'Argentina');
INSERT INTO public.country (code, name) VALUES ('ARMEN', 'Armenia');
INSERT INTO public.country (code, name) VALUES ('ARUBA', 'Aruba');
INSERT INTO public.country (code, name) VALUES ('AUSTR', 'Australia');
INSERT INTO public.country (code, name) VALUES ('AUSTR', 'Austria');
INSERT INTO public.country (code, name) VALUES ('AZERB', 'Azerbaijan');
INSERT INTO public.country (code, name) VALUES ('BAHAM', 'Bahamas');
INSERT INTO public.country (code, name) VALUES ('BAHRA', 'Bahrain');
INSERT INTO public.country (code, name) VALUES ('BANGL', 'Bangladesh');
INSERT INTO public.country (code, name) VALUES ('BARBA', 'Barbados');
INSERT INTO public.country (code, name) VALUES ('BELAR', 'Belarus');
INSERT INTO public.country (code, name) VALUES ('BELGI', 'Belgium');
INSERT INTO public.country (code, name) VALUES ('BELIZ', 'Belize');
INSERT INTO public.country (code, name) VALUES ('BENIN', 'Benin');
INSERT INTO public.country (code, name) VALUES ('BERMU', 'Bermuda');
INSERT INTO public.country (code, name) VALUES ('BHUTA', 'Bhutan');
INSERT INTO public.country (code, name) VALUES ('BOLIV', 'Bolivia');
INSERT INTO public.country (code, name) VALUES ('BOSNI&AMP;HERZE', 'Bosnia &amp; Herzegovina');
INSERT INTO public.country (code, name) VALUES ('BOTSW', 'Botswana');
INSERT INTO public.country (code, name) VALUES ('BRAZI', 'Brazil');
INSERT INTO public.country (code, name) VALUES ('BRITIVIRGIISLAN', 'British Virgin Islands');
INSERT INTO public.country (code, name) VALUES ('BRUNE', 'Brunei');
INSERT INTO public.country (code, name) VALUES ('BULGA', 'Bulgaria');
INSERT INTO public.country (code, name) VALUES ('BURKIFASO', 'Burkina Faso');
INSERT INTO public.country (code, name) VALUES ('BURUN', 'Burundi');
INSERT INTO public.country (code, name) VALUES ('CAMBO', 'Cambodia');
INSERT INTO public.country (code, name) VALUES ('CAMER', 'Cameroon');
INSERT INTO public.country (code, name) VALUES ('CANAD', 'Canada');
INSERT INTO public.country (code, name) VALUES ('CAPEVERDE', 'Cape Verde');
INSERT INTO public.country (code, name) VALUES ('CAYMAISLAN', 'Cayman Islands');
INSERT INTO public.country (code, name) VALUES ('CHAD', 'Chad');
INSERT INTO public.country (code, name) VALUES ('CHILE', 'Chile');
INSERT INTO public.country (code, name) VALUES ('CHINA', 'China');
INSERT INTO public.country (code, name) VALUES ('COLOM', 'Colombia');
INSERT INTO public.country (code, name) VALUES ('CONGO', 'Congo');
INSERT INTO public.country (code, name) VALUES ('COOKISLAN', 'Cook Islands');
INSERT INTO public.country (code, name) VALUES ('COSTARICA', 'Costa Rica');
INSERT INTO public.country (code, name) VALUES ('COTEDIVOIR', 'Cote D Ivoire');
INSERT INTO public.country (code, name) VALUES ('CROAT', 'Croatia');
INSERT INTO public.country (code, name) VALUES ('CRUISSHIP', 'Cruise Ship');
INSERT INTO public.country (code, name) VALUES ('CUBA', 'Cuba');
INSERT INTO public.country (code, name) VALUES ('CYPRU', 'Cyprus');
INSERT INTO public.country (code, name) VALUES ('CZECHREPUB', 'Czech Republic');
INSERT INTO public.country (code, name) VALUES ('DENMA', 'Denmark');
INSERT INTO public.country (code, name) VALUES ('DJIBO', 'Djibouti');
INSERT INTO public.country (code, name) VALUES ('DOMIN', 'Dominica');
INSERT INTO public.country (code, name) VALUES ('DOMINREPUB', 'Dominican Republic');
INSERT INTO public.country (code, name) VALUES ('ECUAD', 'Ecuador');
INSERT INTO public.country (code, name) VALUES ('EGYPT', 'Egypt');
INSERT INTO public.country (code, name) VALUES ('ELSALVA', 'El Salvador');
INSERT INTO public.country (code, name) VALUES ('EQUATGUINE', 'Equatorial Guinea');
INSERT INTO public.country (code, name) VALUES ('ESTON', 'Estonia');
INSERT INTO public.country (code, name) VALUES ('ETHIO', 'Ethiopia');
INSERT INTO public.country (code, name) VALUES ('FALKLISLAN', 'Falkland Islands');
INSERT INTO public.country (code, name) VALUES ('FAROEISLAN', 'Faroe Islands');
INSERT INTO public.country (code, name) VALUES ('FIJI', 'Fiji');
INSERT INTO public.country (code, name) VALUES ('FINLA', 'Finland');
INSERT INTO public.country (code, name) VALUES ('FRANC', 'France');
INSERT INTO public.country (code, name) VALUES ('FRENCPOLYN', 'French Polynesia');
INSERT INTO public.country (code, name) VALUES ('FRENCWESTINDIE', 'French West Indies');
INSERT INTO public.country (code, name) VALUES ('GABON', 'Gabon');
INSERT INTO public.country (code, name) VALUES ('GAMBI', 'Gambia');
INSERT INTO public.country (code, name) VALUES ('GEORG', 'Georgia');
INSERT INTO public.country (code, name) VALUES ('GERMA', 'Germany');
INSERT INTO public.country (code, name) VALUES ('GHANA', 'Ghana');
INSERT INTO public.country (code, name) VALUES ('GIBRA', 'Gibraltar');
INSERT INTO public.country (code, name) VALUES ('GREEC', 'Greece');
INSERT INTO public.country (code, name) VALUES ('GREEN', 'Greenland');
INSERT INTO public.country (code, name) VALUES ('GRENA', 'Grenada');
INSERT INTO public.country (code, name) VALUES ('GUAM', 'Guam');
INSERT INTO public.country (code, name) VALUES ('GUATE', 'Guatemala');
INSERT INTO public.country (code, name) VALUES ('GUERN', 'Guernsey');
INSERT INTO public.country (code, name) VALUES ('GUINE', 'Guinea');
INSERT INTO public.country (code, name) VALUES ('GUINEBISSA', 'Guinea Bissau');
INSERT INTO public.country (code, name) VALUES ('GUYAN', 'Guyana');
INSERT INTO public.country (code, name) VALUES ('HAITI', 'Haiti');
INSERT INTO public.country (code, name) VALUES ('HONDU', 'Honduras');
INSERT INTO public.country (code, name) VALUES ('HONGKONG', 'Hong Kong');
INSERT INTO public.country (code, name) VALUES ('HUNGA', 'Hungary');
INSERT INTO public.country (code, name) VALUES ('ICELA', 'Iceland');
INSERT INTO public.country (code, name) VALUES ('INDIA', 'India');
INSERT INTO public.country (code, name) VALUES ('INDON', 'Indonesia');
INSERT INTO public.country (code, name) VALUES ('IRAN', 'Iran');
INSERT INTO public.country (code, name) VALUES ('IRAQ', 'Iraq');
INSERT INTO public.country (code, name) VALUES ('IRELA', 'Ireland');
INSERT INTO public.country (code, name) VALUES ('ISLEOFMAN', 'Isle of Man');
INSERT INTO public.country (code, name) VALUES ('ISRAE', 'Israel');
INSERT INTO public.country (code, name) VALUES ('ITALY', 'Italy');
INSERT INTO public.country (code, name) VALUES ('JAMAI', 'Jamaica');
INSERT INTO public.country (code, name) VALUES ('JAPAN', 'Japan');
INSERT INTO public.country (code, name) VALUES ('JERSE', 'Jersey');
INSERT INTO public.country (code, name) VALUES ('JORDA', 'Jordan');
INSERT INTO public.country (code, name) VALUES ('KAZAK', 'Kazakhstan');
INSERT INTO public.country (code, name) VALUES ('KENYA', 'Kenya');
INSERT INTO public.country (code, name) VALUES ('KUWAI', 'Kuwait');
INSERT INTO public.country (code, name) VALUES ('KYRGYREPUB', 'Kyrgyz Republic');
INSERT INTO public.country (code, name) VALUES ('LAOS', 'Laos');
INSERT INTO public.country (code, name) VALUES ('LATVI', 'Latvia');
INSERT INTO public.country (code, name) VALUES ('LEBAN', 'Lebanon');
INSERT INTO public.country (code, name) VALUES ('LESOT', 'Lesotho');
INSERT INTO public.country (code, name) VALUES ('LIBER', 'Liberia');
INSERT INTO public.country (code, name) VALUES ('LIBYA', 'Libya');
INSERT INTO public.country (code, name) VALUES ('LIECH', 'Liechtenstein');
INSERT INTO public.country (code, name) VALUES ('LITHU', 'Lithuania');
INSERT INTO public.country (code, name) VALUES ('LUXEM', 'Luxembourg');
INSERT INTO public.country (code, name) VALUES ('MACAU', 'Macau');
INSERT INTO public.country (code, name) VALUES ('MACED', 'Macedonia');
INSERT INTO public.country (code, name) VALUES ('MADAG', 'Madagascar');
INSERT INTO public.country (code, name) VALUES ('MALAW', 'Malawi');
INSERT INTO public.country (code, name) VALUES ('MALAY', 'Malaysia');
INSERT INTO public.country (code, name) VALUES ('MALDI', 'Maldives');
INSERT INTO public.country (code, name) VALUES ('MALI', 'Mali');
INSERT INTO public.country (code, name) VALUES ('MALTA', 'Malta');
INSERT INTO public.country (code, name) VALUES ('MAURI', 'Mauritania');
INSERT INTO public.country (code, name) VALUES ('MAURI', 'Mauritius');
INSERT INTO public.country (code, name) VALUES ('MEXIC', 'Mexico');
INSERT INTO public.country (code, name) VALUES ('MOLDO', 'Moldova');
INSERT INTO public.country (code, name) VALUES ('MONAC', 'Monaco');
INSERT INTO public.country (code, name) VALUES ('MONGO', 'Mongolia');
INSERT INTO public.country (code, name) VALUES ('MONTE', 'Montenegro');
INSERT INTO public.country (code, name) VALUES ('MONTS', 'Montserrat');
INSERT INTO public.country (code, name) VALUES ('MOROC', 'Morocco');
INSERT INTO public.country (code, name) VALUES ('MOZAM', 'Mozambique');
INSERT INTO public.country (code, name) VALUES ('NAMIB', 'Namibia');
INSERT INTO public.country (code, name) VALUES ('NEPAL', 'Nepal');
INSERT INTO public.country (code, name) VALUES ('NETHE', 'Netherlands');
INSERT INTO public.country (code, name) VALUES ('NETHEANTIL', 'Netherlands Antilles');
INSERT INTO public.country (code, name) VALUES ('NEWCALED', 'New Caledonia');
INSERT INTO public.country (code, name) VALUES ('NEWZEALA', 'New Zealand');
INSERT INTO public.country (code, name) VALUES ('NICAR', 'Nicaragua');
INSERT INTO public.country (code, name) VALUES ('NIGER', 'Niger');
INSERT INTO public.country (code, name) VALUES ('NIGER', 'Nigeria');
INSERT INTO public.country (code, name) VALUES ('NORWA', 'Norway');
INSERT INTO public.country (code, name) VALUES ('OMAN', 'Oman');
INSERT INTO public.country (code, name) VALUES ('PAKIS', 'Pakistan');
INSERT INTO public.country (code, name) VALUES ('PALES', 'Palestine');
INSERT INTO public.country (code, name) VALUES ('PANAM', 'Panama');
INSERT INTO public.country (code, name) VALUES ('PAPUANEWGUINE', 'Papua New Guinea');
INSERT INTO public.country (code, name) VALUES ('PARAG', 'Paraguay');
INSERT INTO public.country (code, name) VALUES ('PERU', 'Peru');
INSERT INTO public.country (code, name) VALUES ('PHILI', 'Philippines');
INSERT INTO public.country (code, name) VALUES ('POLAN', 'Poland');
INSERT INTO public.country (code, name) VALUES ('PORTU', 'Portugal');
INSERT INTO public.country (code, name) VALUES ('PUERTRICO', 'Puerto Rico');
INSERT INTO public.country (code, name) VALUES ('QATAR', 'Qatar');
INSERT INTO public.country (code, name) VALUES ('REUNI', 'Reunion');
INSERT INTO public.country (code, name) VALUES ('ROMAN', 'Romania');
INSERT INTO public.country (code, name) VALUES ('RUSSI', 'Russia');
INSERT INTO public.country (code, name) VALUES ('RWAND', 'Rwanda');
INSERT INTO public.country (code, name) VALUES ('SAINTPIERR&AMP;MIQUE', 'Saint Pierre &amp; Miquelon');
INSERT INTO public.country (code, name) VALUES ('SAMOA', 'Samoa');
INSERT INTO public.country (code, name) VALUES ('SANMARIN', 'San Marino');
INSERT INTO public.country (code, name) VALUES ('SATEL', 'Satellite');
INSERT INTO public.country (code, name) VALUES ('SAUDIARABI', 'Saudi Arabia');
INSERT INTO public.country (code, name) VALUES ('SENEG', 'Senegal');
INSERT INTO public.country (code, name) VALUES ('SERBI', 'Serbia');
INSERT INTO public.country (code, name) VALUES ('SEYCH', 'Seychelles');
INSERT INTO public.country (code, name) VALUES ('SIERRLEONE', 'Sierra Leone');
INSERT INTO public.country (code, name) VALUES ('SINGA', 'Singapore');
INSERT INTO public.country (code, name) VALUES ('SLOVA', 'Slovakia');
INSERT INTO public.country (code, name) VALUES ('SLOVE', 'Slovenia');
INSERT INTO public.country (code, name) VALUES ('SOUTHAFRIC', 'South Africa');
INSERT INTO public.country (code, name) VALUES ('SOUTHKOREA', 'South Korea');
INSERT INTO public.country (code, name) VALUES ('SPAIN', 'Spain');
INSERT INTO public.country (code, name) VALUES ('SRILANKA', 'Sri Lanka');
INSERT INTO public.country (code, name) VALUES ('STKITTS&AMP;NEVIS', 'St Kitts &amp; Nevis');
INSERT INTO public.country (code, name) VALUES ('STLUCIA', 'St Lucia');
INSERT INTO public.country (code, name) VALUES ('STVINCE', 'St Vincent');
INSERT INTO public.country (code, name) VALUES ('ST.LUCIA', 'St. Lucia');
INSERT INTO public.country (code, name) VALUES ('SUDAN', 'Sudan');
INSERT INTO public.country (code, name) VALUES ('SURIN', 'Suriname');
INSERT INTO public.country (code, name) VALUES ('SWAZI', 'Swaziland');
INSERT INTO public.country (code, name) VALUES ('SWEDE', 'Sweden');
INSERT INTO public.country (code, name) VALUES ('SWITZ', 'Switzerland');
INSERT INTO public.country (code, name) VALUES ('SYRIA', 'Syria');
INSERT INTO public.country (code, name) VALUES ('TAIWA', 'Taiwan');
INSERT INTO public.country (code, name) VALUES ('TAJIK', 'Tajikistan');
INSERT INTO public.country (code, name) VALUES ('TANZA', 'Tanzania');
INSERT INTO public.country (code, name) VALUES ('THAIL', 'Thailand');
INSERT INTO public.country (code, name) VALUES ('TIMORL''EST', 'Timor L''Este');
INSERT INTO public.country (code, name) VALUES ('TOGO', 'Togo');
INSERT INTO public.country (code, name) VALUES ('TONGA', 'Tonga');
INSERT INTO public.country (code, name) VALUES ('TRINI&AMP;TOBAG', 'Trinidad &amp; Tobago');
INSERT INTO public.country (code, name) VALUES ('TUNIS', 'Tunisia');
INSERT INTO public.country (code, name) VALUES ('TURKE', 'Turkey');
INSERT INTO public.country (code, name) VALUES ('TURKM', 'Turkmenistan');
INSERT INTO public.country (code, name) VALUES ('TURKS&AMP;CAICO', 'Turks &amp; Caicos');
INSERT INTO public.country (code, name) VALUES ('UGAND', 'Uganda');
INSERT INTO public.country (code, name) VALUES ('UKRAI', 'Ukraine');
INSERT INTO public.country (code, name) VALUES ('UNITEARABEMIRA', 'United Arab Emirates');
INSERT INTO public.country (code, name) VALUES ('UNITEKINGD', 'United Kingdom');
INSERT INTO public.country (code, name) VALUES ('UNITESTATE', 'United States');
INSERT INTO public.country (code, name) VALUES ('UNITESTATEMINOROUTLYISLAN', 'United States Minor Outlying Islands');
INSERT INTO public.country (code, name) VALUES ('URUGU', 'Uruguay');
INSERT INTO public.country (code, name) VALUES ('UZBEK', 'Uzbekistan');
INSERT INTO public.country (code, name) VALUES ('VENEZ', 'Venezuela');
INSERT INTO public.country (code, name) VALUES ('VIETN', 'Vietnam');
INSERT INTO public.country (code, name) VALUES ('VIRGIISLAN(US)', 'Virgin Islands (US)');
INSERT INTO public.country (code, name) VALUES ('YEMEN', 'Yemen');
INSERT INTO public.country (code, name) VALUES ('ZAMBI', 'Zambia');
INSERT INTO public.country (code, name) VALUES ('ZIMBA', 'Zimbabwe');
