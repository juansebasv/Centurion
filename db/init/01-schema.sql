-- ============================================================================
--  Centurion / NodePoller - database schema  (MySQL 5.7)
--  Column names taken 1:1 from the JPA @Entity mappings in
--  src/java/co/com/claro/nodepoller/entities/*.java
--
--  Note: optical_slotdata has NO entity class - it is only hit by the native
--  SQL in NodesDAO (findDateByName*), so it must be created here by hand.
--  persistence.xml is set to schema-generation = none, so THIS file is the
--  single source of truth for the schema.
-- ============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS optical_region (
  id    INT PRIMARY KEY,
  name  VARCHAR(64)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_city (
  id         INT PRIMARY KEY,
  name       VARCHAR(64),
  region_id  INT,
  KEY ix_city_region (region_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_configfile (
  id    INT PRIMARY KEY,
  name  VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_sds (
  id             INT PRIMARY KEY,
  name           VARCHAR(64),
  code           VARCHAR(64),
  city_id        INT,
  configfile_id  INT,
  KEY ix_sds_city (city_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_platform (
  id    INT PRIMARY KEY,
  name  VARCHAR(64)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_chasis (
  id             INT PRIMARY KEY,
  name           VARCHAR(64),
  community      VARCHAR(64),
  management_ip  VARCHAR(64),
  platform_id    INT,
  sds_id         INT,
  KEY ix_chasis_platform (platform_id),
  KEY ix_chasis_sds (sds_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_slottype (
  id           INT PRIMARY KEY,
  name         VARCHAR(64),
  code         VARCHAR(64),
  side         VARCHAR(16),
  platform_id  INT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_state (
  id    INT PRIMARY KEY,
  name  VARCHAR(32)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_alarmtype (
  id    INT PRIMARY KEY,
  name  VARCHAR(64)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_level (
  id    INT PRIMARY KEY,
  name  VARCHAR(64)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_transponder_model (
  id    INT PRIMARY KEY,
  name  VARCHAR(64)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_transponder (
  id        INT PRIMARY KEY,
  name      VARCHAR(64),
  status    VARCHAR(16),
  model_id  INT,
  KEY ix_txp_model (model_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_node_model (
  id    INT PRIMARY KEY,
  name  VARCHAR(64)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_slot (
  id           INT PRIMARY KEY,
  name         VARCHAR(64),
  label1       VARCHAR(64),
  label2       VARCHAR(64),
  title        VARCHAR(128),
  platform_id  INT,
  alarmtype_id INT,
  chasis_id    INT,
  slottype_id  INT,
  state_id     INT,
  KEY ix_slot_chasis (chasis_id),
  KEY ix_slot_slottype (slottype_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_node (
  id              INT PRIMARY KEY,
  name            VARCHAR(32),
  adminchasis_ip  VARCHAR(64),
  fastcity        SMALLINT DEFAULT 0,
  fastcity_phase  SMALLINT DEFAULT 0,
  in_ro           SMALLINT DEFAULT 0,
  in_sds          SMALLINT DEFAULT 0,
  in_tree         SMALLINT DEFAULT 0,
  in_capital      SMALLINT DEFAULT 0,
  fwd_idx         SMALLINT DEFAULT 0,
  ret_idx         SMALLINT DEFAULT 0,
  in_bogota       SMALLINT DEFAULT 0,
  alarmtype_id    INT,
  fwdslot_id      INT,
  model_id        INT,
  platform_id     INT,
  retslot_id      INT,
  state_id        INT,
  transponder_id  INT,
  KEY ix_node_name (name),
  KEY ix_node_state (state_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS optical_nodealarm (
  id             INT PRIMARY KEY AUTO_INCREMENT,
  created        DATETIME NOT NULL,
  ack            INT,
  node_id        VARCHAR(11),
  region_name    VARCHAR(32),
  city_name      VARCHAR(32),
  sds_name       VARCHAR(32),
  slot_name      VARCHAR(32),
  platform_name  VARCHAR(32),
  chasis_name    VARCHAR(32),
  process_id     INT,
  alarmtype_id   INT,
  level_id       INT,
  state_id       INT,
  KEY ix_alarm_ack (ack)
) ENGINE=InnoDB;

-- No entity: consumed only by NodesDAO native queries (forward / retorno history).
CREATE TABLE IF NOT EXISTS optical_slotdata (
  id           INT PRIMARY KEY AUTO_INCREMENT,
  slot_id      INT,
  created      DATETIME,
  inrf         DECIMAL(10,3),
  outputpower  DECIMAL(10,3),
  power1       DECIMAL(10,3),
  power2       DECIMAL(10,3),
  arop         DECIMAL(10,3),
  edfapower    DECIMAL(10,3),
  KEY ix_slotdata_slot (slot_id)
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;
