CREATE ROLE ejp;

CREATE EXTENSION pg_trgm;

CREATE FUNCTION to_tsvector_multilang(text) RETURNS tsvector
    LANGUAGE sql IMMUTABLE
    AS $_$ SELECT to_tsvector('simple', $1) $_$;


ALTER FUNCTION to_tsvector_multilang(text) OWNER TO ejp;


CREATE TABLE diseases (
    id integer NOT NULL,
    name text,
    orphacode text NOT NULL,
    synonyms text,
    codes text
);


ALTER TABLE diseases OWNER TO ejp;


CREATE SEQUENCE diseases_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE diseases_id_seq OWNER TO ejp;


ALTER SEQUENCE diseases_id_seq OWNED BY diseases.id;


CREATE TABLE genes (
    id integer NOT NULL,
    hgnc_id text NOT NULL,
    symbol text,
    name text,
    status text,
    previous_symbols text,
    alias_symbols text,
    alias_names text,
    omim_id text
);

ALTER TABLE genes OWNER TO ejp;

CREATE SEQUENCE genes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE genes_id_seq OWNER TO ejp;

ALTER SEQUENCE genes_id_seq OWNED BY genes.id;

CREATE SEQUENCE my_serial
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE my_serial OWNER TO ejp;

ALTER SEQUENCE my_serial OWNED BY genes.id;

CREATE TABLE notification (
    id integer NOT NULL,
    channel varchar,
    status varchar,
    type varchar,
    resource_id varchar,
    content text,
    "timestamp" timestamp without time zone DEFAULT (now() AT TIME ZONE 'cest'::text) NOT NULL
);

ALTER TABLE notification OWNER TO ejp;

CREATE SEQUENCE notification_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE notification_id_seq OWNER TO ejp;

ALTER SEQUENCE notification_id_seq OWNED BY notification.id;

CREATE TABLE resource_monitor (
    id integer NOT NULL,
    resource_id varchar NOT NULL,
    response_status_code integer,
    response_time integer,
    response_body text,
    "timestamp" timestamp without time zone DEFAULT (now() AT TIME ZONE 'cest'::text) NOT NULL
);


ALTER TABLE resource_monitor OWNER TO ejp;

CREATE SEQUENCE resource_monitor_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE resource_monitor_id_seq OWNER TO ejp;

ALTER SEQUENCE resource_monitor_id_seq OWNED BY resource_monitor.id;

ALTER TABLE ONLY diseases ALTER COLUMN id SET DEFAULT nextval('diseases_id_seq'::regclass);

ALTER TABLE ONLY genes ALTER COLUMN id SET DEFAULT nextval('my_serial'::regclass);

ALTER TABLE ONLY notification ALTER COLUMN id SET DEFAULT nextval('notification_id_seq'::regclass);

ALTER TABLE ONLY resource_monitor ALTER COLUMN id SET DEFAULT nextval('resource_monitor_id_seq'::regclass);


ALTER TABLE ONLY diseases
    ADD CONSTRAINT diseases_pkey PRIMARY KEY (id);

ALTER TABLE ONLY genes
    ADD CONSTRAINT genes_pkey PRIMARY KEY (id);

CREATE INDEX search_index_0 ON genes USING gin (to_tsvector_multilang(hgnc_id));


CREATE INDEX search_index_10 ON diseases USING gin (orphacode gin_trgm_ops);


CREATE INDEX search_index_11 ON diseases USING gin (synonyms gin_trgm_ops);

CREATE INDEX search_index_12 ON diseases USING gin (codes gin_trgm_ops);

CREATE INDEX search_index_2 ON genes USING gin (to_tsvector_multilang(symbol));

CREATE INDEX search_index_3 ON genes USING gin (to_tsvector_multilang(name));

CREATE INDEX search_index_4 ON genes USING gin (to_tsvector_multilang(status));

CREATE INDEX search_index_5 ON genes USING gin (to_tsvector_multilang(previous_symbols));

CREATE INDEX search_index_6 ON genes USING gin (to_tsvector_multilang(alias_symbols));

CREATE INDEX search_index_7 ON genes USING gin (to_tsvector_multilang(alias_names));

CREATE INDEX search_index_8 ON genes USING gin (to_tsvector_multilang(omim_id));

CREATE INDEX search_index_9 ON diseases USING gin (name gin_trgm_ops);
