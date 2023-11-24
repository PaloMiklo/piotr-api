-- checksum 3002030998
--DROP TABLES IF EXISTS
DROP TABLE IF EXISTS public.cart_line CASCADE;

DROP TABLE IF EXISTS public.cart CASCADE;

DROP TABLE IF EXISTS public.order_table CASCADE;

DROP TABLE IF EXISTS public.address CASCADE;

DROP TABLE IF EXISTS public.image_table CASCADE;

DROP TABLE IF EXISTS public.customer CASCADE;

DROP TABLE IF EXISTS public.product CASCADE;

DROP TABLE IF EXISTS public.payed_option_item CASCADE;

DROP TABLE IF EXISTS public.payed_option CASCADE;

-- CREATE TABLES
CREATE TABLE public.payed_option(
  code VARCHAR(50) PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE public.payed_option_item(
  code VARCHAR(50) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  payed_option VARCHAR(50) NOT NULL REFERENCES payed_option(code),
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
  delivery_option_item_code VARCHAR(50) NOT NULL REFERENCES payed_option_item(code),
  billing_option_item_code VARCHAR(50) NOT NULL REFERENCES payed_option_item(code),
  created_fe TIMESTAMP NOT NULL DEFAULT NOW(),
  comment TEXT,
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

-- ADD CONSTRAINTS
ALTER TABLE
  public.payed_option_item
ADD
  CONSTRAINT fk_payed_option FOREIGN KEY (payed_option) REFERENCES public.payed_option(code);

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
  CONSTRAINT fk_order_delivery_option FOREIGN KEY (delivery_option_item_code) REFERENCES public.payed_option_item(code);

ALTER TABLE
  public.order_table
ADD
  CONSTRAINT fk_order_billing_option FOREIGN KEY (billing_option_item_code) REFERENCES public.payed_option_item(code);

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
  public.payed_option (code, name)
VALUES
  ('SHIPPING', 'Shipping');

INSERT INTO
  public.payed_option (code, name)
VALUES
  ('PAYMENT', 'Payment');

INSERT INTO
  public.payed_option_item (code, name, payed_option, price)
VALUES
  (
    'CART-PAYMENT',
    'Cart payment',
    'PAYMENT',
    0
  );

INSERT INTO
  public.payed_option_item (code, name, payed_option, price)
VALUES
  (
    'CASH_ON_DELIVERY-PAYMENT',
    'Cash on delivery',
    'PAYMENT',
    1.99
  );

INSERT INTO
  public.payed_option_item (code, name, payed_option, price)
VALUES
  (
    'GROUND-SHIPPING',
    'Ground Shipping',
    'SHIPPING',
    5.99
  );

INSERT INTO
  public.payed_option_item (code, name, payed_option, price)
VALUES
  (
    '2_DAY_SHIPPING-SHIPPING',
    '2-Day Shipping',
    'SHIPPING',
    15.99
  );

INSERT INTO
  public.payed_option_item (code, name, payed_option, price)
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
    'T-Shirt',
    19.99,
    'Comfortable cotton t-shirt in black',
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
    'Sweatshirt',
    39.99,
    'Warm fleece sweatshirt in gray',
    30,
    2,
    TRUE
  );

INSERT INTO
  public.product (name, price, description, quantity, image, valid)
VALUES
  (
    'Sneakers',
    89.99,
    'Sporty sneakers with good grip',
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
    comment,
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