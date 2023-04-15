SET TIME ZONE 'Europe/Kiev';

-- Table: public.gift_certificate

DROP TABLE IF EXISTS public.gift_certificate_tag;
DROP TABLE IF EXISTS public.gift_certificate;

CREATE TABLE IF NOT EXISTS public.gift_certificate
(
    id               serial                NOT NULL,
    name             character varying(50) NOT NULL,
    description      character varying(100),
    price            numeric(15, 2) default 0,
    duration         numeric        default 0,
    create_date      timestamptz      default now() ,
    last_update_date timestamptz      default now(),
    CONSTRAINT pk_gift_certificate PRIMARY KEY (id)
)
    TABLESPACE pg_default;
-- timestamp UTC

ALTER TABLE IF EXISTS public.gift_certificate
    OWNER to "user";

CREATE OR REPLACE FUNCTION update_modified_column()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.last_update_date = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_gift_certificate_last_update_date
    BEFORE UPDATE
    ON gift_certificate
    FOR EACH ROW
EXECUTE PROCEDURE update_modified_column();

INSERT INTO gift_certificate (name, description, price, duration)
VALUES ('name1', 'desc1', 11.44, 5);
INSERT INTO gift_certificate (name, description, price, duration)
VALUES ('name2', 'desc2', 12.44, 1);
INSERT INTO gift_certificate (name, description, price, duration)
VALUES ('name3', 'desc3', 13.44, 3);
INSERT INTO gift_certificate (name, description, price, duration)
VALUES ('name4', 'desc4', 14.44, 4);


-- Table: public.tags
DROP TABLE IF EXISTS public.tag;

CREATE TABLE IF NOT EXISTS public.tag
(
    id               serial                NOT NULL,
    name             character varying(50) UNIQUE NOT NULL,
    CONSTRAINT pk_tag PRIMARY KEY (id)
)
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tag
    OWNER to "user";

INSERT INTO tag (name) VALUES ('tag1');
INSERT INTO tag (name) VALUES ('tag2');
INSERT INTO tag (name) VALUES ('tag3');


-- Table: public.gift_certificate_tag
-- DROP TABLE IF EXISTS public.gift_certificate_tag;

CREATE TABLE IF NOT EXISTS public.gift_certificate_tag
(
    id_cert integer NOT NULL,
    id_tag integer NOT NULL,
    CONSTRAINT gift_certificate_tag_pkey PRIMARY KEY (id_cert, id_tag),
    CONSTRAINT id_cert FOREIGN KEY (id_cert)
        REFERENCES public.gift_certificate (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT id_tag FOREIGN KEY (id_tag)
        REFERENCES public.tag (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.gift_certificate_tag
    OWNER to "user";


INSERT INTO gift_certificate_tag (id_cert, id_tag) VALUES ('1', '1');
INSERT INTO gift_certificate_tag (id_cert, id_tag) VALUES ('1', '2');
INSERT INTO gift_certificate_tag (id_cert, id_tag) VALUES ('2', '2');
INSERT INTO gift_certificate_tag (id_cert, id_tag) VALUES ('2', '3');
INSERT INTO gift_certificate_tag (id_cert, id_tag) VALUES ('3', '3');