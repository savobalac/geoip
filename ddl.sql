-- Table: ac_host

-- DROP TABLE ac_host;

CREATE TABLE ac_host
(
  id bigint NOT NULL,
  baseurl character varying(512) NOT NULL,
  description character varying(255),
  key character varying(255) NOT NULL,
  name character varying(255),
  publickey character varying(512) NOT NULL,
  sharedsecret character varying(512) NOT NULL,
  CONSTRAINT ac_host_pkey PRIMARY KEY (id),
  CONSTRAINT uk_b38e192e5ac04e93b54657fa40b UNIQUE (key),
  CONSTRAINT uk_c89b606798e24c2594bc15bfec7 UNIQUE (baseurl)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ac_host
  OWNER TO lgfrcollsoskva;




-- Table: wiki_user

-- DROP TABLE wiki_user;

CREATE TABLE wiki_user
(
  id serial NOT NULL,
  username character varying(255),
  password character varying(255),
  email character varying(255),
  forename character varying(255),
  surname character varying(255),
  CONSTRAINT "wiki_user_PK" PRIMARY KEY (id),
  CONSTRAINT "wiki_user_username_UN" UNIQUE (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wiki_user
  OWNER TO lgfrcollsoskva;




-- Table: user_login

-- DROP TABLE user_login;

CREATE TABLE user_login
(
  id serial NOT NULL,
  username character varying(255),
  login_timestamp timestamp without time zone,
  ip_address character varying(255),
  continent_code character varying(255),
  continent_geoname_id integer,
  continent_name character varying(255),
  country_confidence integer,
  country_iso_code character varying(255),
  country_geoname_id integer,
  country_name character varying(255),
  registered_country_iso_code character varying(255),
  registered_country_geoname_id integer,
  registered_country_name character varying(255),
  represented_country_iso_code character varying(255),
  represented_country_geoname_id integer,
  represented_country_name character varying(255),
  represented_country_type character varying(255),
  city_confidence integer,
  city_geoname_id integer,
  city_name character varying(255),
  postal_code character varying(255),
  postal_confidence integer,
  location_accuracy_radius integer,
  location_latitude double precision,
  location_longitude double precision,
  location_metro_code integer,
  location_time_zone character varying(255),
  most_specific_subdivision_confidence integer,
  most_specific_subdivision_geoname_id integer,
  most_specific_subdivision_iso_code character varying(255),
  most_specific_subdivision_name character varying(255),
  traits_autonomous_system_number integer,
  traits_autonomous_system_organization character varying(255),
  traits_domain character varying(255),
  traits_ip_address character varying(255),
  traits_is_anonymous_proxy boolean,
  traits_is_satellite_provider boolean,
  traits_isp character varying(255),
  traits_organization character varying(255),
  traits_user_type character varying(255),
  CONSTRAINT "user_login_PK" PRIMARY KEY (id),
  CONSTRAINT "wiki_user_FK" FOREIGN KEY (username)
      REFERENCES wiki_user (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "user_login_UN" UNIQUE (username, login_timestamp, ip_address)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_login
  OWNER TO lgfrcollsoskva;
