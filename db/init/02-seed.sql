-- ============================================================================
--  Centurion / NodePoller - datos de ejemplo (ampliados)
--
--  5 regiones, 10 ciudades, 2 plataformas (AURORA / ROSA), 10 chasis/SDS,
--  20 nodos = 10 pares "001 / 002" por ciudad:
--     001 -> siempre UP (icono verde)      state_id = 1
--     002 -> DOWN (icono rojo) en 6 ciudades, UNMANAGED en 2, UP en 2
--  Historial optical_slotdata por nodo (valores sanos si UP, degradados si DOWN).
--  Alarmas (ack=2 visibles, ack=1 ocultas) repartidas por todas las ciudades.
--
--  Recordatorio: NodesDAO.findAlarm() hace var.getAlarmtypeId().getName() sin
--  chequear null -> toda fila de optical_nodealarm lleva alarmtype_id valido.
-- ============================================================================
SET NAMES utf8mb4;

-- ----------------------------------------------------------------- catalogos
INSERT INTO optical_region (id, name) VALUES
  (1, 'CENTRO'), (2, 'OCCIDENTE'), (3, 'COSTA'), (4, 'ORIENTE'), (5, 'EJE CAFETERO');

INSERT INTO optical_city (id, name, region_id) VALUES
  (1,  'BOGOTA',        1),
  (2,  'MEDELLIN',      2),
  (3,  'CALI',          2),
  (4,  'BARRANQUILLA',  3),
  (5,  'CARTAGENA',     3),
  (6,  'BUCARAMANGA',   4),
  (7,  'CUCUTA',        4),
  (8,  'PEREIRA',       5),
  (9,  'MANIZALES',     5),
  (10, 'IBAGUE',        1);

INSERT INTO optical_configfile (id, name) VALUES (1, 'default.cfg');

-- 1 SDS por ciudad (id = id de ciudad)
INSERT INTO optical_sds (id, name, code, city_id, configfile_id) VALUES
  (1,  'SDS-BOG-01', 'BOG01', 1,  1),
  (2,  'SDS-MED-01', 'MED01', 2,  1),
  (3,  'SDS-CAL-01', 'CAL01', 3,  1),
  (4,  'SDS-BAQ-01', 'BAQ01', 4,  1),
  (5,  'SDS-CTG-01', 'CTG01', 5,  1),
  (6,  'SDS-BUC-01', 'BUC01', 6,  1),
  (7,  'SDS-CUC-01', 'CUC01', 7,  1),
  (8,  'SDS-PEI-01', 'PEI01', 8,  1),
  (9,  'SDS-MZL-01', 'MZL01', 9,  1),
  (10, 'SDS-IBG-01', 'IBG01', 10, 1);

INSERT INTO optical_platform (id, name) VALUES (1, 'AURORA'), (2, 'ROSA');

-- 1 chasis por SDS. Chasis impar = AURORA(1), par = ROSA(2)
INSERT INTO optical_chasis (id, name, community, management_ip, platform_id, sds_id) VALUES
  (1,  'CH-BOG-01', 'public', '10.10.1.1',  1, 1),
  (2,  'CH-MED-01', 'public', '10.10.2.1',  2, 2),
  (3,  'CH-CAL-01', 'public', '10.10.3.1',  1, 3),
  (4,  'CH-BAQ-01', 'public', '10.10.4.1',  2, 4),
  (5,  'CH-CTG-01', 'public', '10.10.5.1',  1, 5),
  (6,  'CH-BUC-01', 'public', '10.10.6.1',  2, 6),
  (7,  'CH-CUC-01', 'public', '10.10.7.1',  1, 7),
  (8,  'CH-PEI-01', 'public', '10.10.8.1',  2, 8),
  (9,  'CH-MZL-01', 'public', '10.10.9.1',  1, 9),
  (10, 'CH-IBG-01', 'public', '10.10.10.1', 2, 10);

INSERT INTO optical_slottype (id, name, code, side, platform_id) VALUES
  (1, 'FWD-TX', 'FTX', 'F', 1),
  (2, 'RET-RX', 'RRX', 'R', 1),
  (3, 'FWD-TX', 'FTX', 'F', 2),
  (4, 'RET-RX', 'RRX', 'R', 2);

INSERT INTO optical_state (id, name) VALUES (1, 'UP'), (2, 'DOWN'), (3, 'UNMANAGED');

INSERT INTO optical_alarmtype (id, name) VALUES
  (1, 'CRITICAL'), (2, 'MAJOR'), (3, 'MINOR'), (4, 'WARNING');

INSERT INTO optical_level (id, name) VALUES
  (1, 'FWD-PWR'), (2, 'RET-PWR'), (3, 'INRF'), (4, 'AROP'), (5, 'LOS');

INSERT INTO optical_transponder_model (id, name) VALUES
  (1, 'TXP-100'), (2, 'TXP-200'), (3, 'TXP-400');

INSERT INTO optical_node_model (id, name) VALUES
  (1, 'NODE-A'), (2, 'NODE-B'), (3, 'NODE-C');

-- 1 transponder por nodo (id = id de nodo)
INSERT INTO optical_transponder (id, name, status, model_id)
SELECT x.i,
       CONCAT('TXP-', LPAD(x.i, 3, '0')),
       'OK',
       ((x.i - 1) % 3) + 1
FROM (SELECT 1 i UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION
      SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION
      SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION
      SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20) x;

-- ------------------------------------------------------------------- nodos
-- id i: 1..10 = nodo "001" de la ciudad i  (UP)
--       11..20 = nodo "002" de la ciudad (i-10)
--          ciudades 1..6  -> DOWN (state 2)
--          ciudades 7..8  -> UNMANAGED (state 3)
--          ciudades 9..10 -> UP (state 1)
INSERT INTO optical_node
  (id, name, adminchasis_ip, alarmtype_id, fwdslot_id, model_id, platform_id, retslot_id, state_id, transponder_id) VALUES
  ( 1, 'NODOBOG001', '10.10.1.11',  3,  1, 1, 1,  2, 1,  1),
  ( 2, 'NODOMED001', '10.10.2.11',  3,  3, 2, 2,  4, 1,  2),
  ( 3, 'NODOCAL001', '10.10.3.11',  3,  5, 3, 1,  6, 1,  3),
  ( 4, 'NODOBAQ001', '10.10.4.11',  3,  7, 1, 2,  8, 1,  4),
  ( 5, 'NODOCTG001', '10.10.5.11',  3,  9, 2, 1, 10, 1,  5),
  ( 6, 'NODOBUC001', '10.10.6.11',  3, 11, 3, 2, 12, 1,  6),
  ( 7, 'NODOCUC001', '10.10.7.11',  3, 13, 1, 1, 14, 1,  7),
  ( 8, 'NODOPEI001', '10.10.8.11',  3, 15, 2, 2, 16, 1,  8),
  ( 9, 'NODOMZL001', '10.10.9.11',  3, 17, 3, 1, 18, 1,  9),
  (10, 'NODOIBG001', '10.10.10.11', 3, 19, 1, 2, 20, 1, 10),
  (11, 'NODOBOG002', '10.10.1.12',  1, 21, 2, 1, 22, 2, 11),
  (12, 'NODOMED002', '10.10.2.12',  1, 23, 3, 2, 24, 2, 12),
  (13, 'NODOCAL002', '10.10.3.12',  1, 25, 1, 1, 26, 2, 13),
  (14, 'NODOBAQ002', '10.10.4.12',  1, 27, 2, 2, 28, 2, 14),
  (15, 'NODOCTG002', '10.10.5.12',  1, 29, 3, 1, 30, 2, 15),
  (16, 'NODOBUC002', '10.10.6.12',  1, 31, 1, 2, 32, 2, 16),
  (17, 'NODOCUC002', '10.10.7.12',  4, 33, 2, 1, 34, 3, 17),
  (18, 'NODOPEI002', '10.10.8.12',  4, 35, 3, 2, 36, 3, 18),
  (19, 'NODOMZL002', '10.10.9.12',  3, 37, 1, 1, 38, 1, 19),
  (20, 'NODOIBG002', '10.10.10.12', 3, 39, 2, 2, 40, 1, 20);

-- --------------------------------------------------- slots + historial (proc)
DROP PROCEDURE IF EXISTS seed_slots_and_data;
DELIMITER $$
CREATE PROCEDURE seed_slots_and_data()
BEGIN
  DECLARE s        INT DEFAULT 1;
  DECLARE nodeid   INT;
  DECLARE is_fwd   TINYINT;
  DECLARE chasisid INT;
  DECLARE platid   INT;
  DECLARE sttype   INT;
  DECLARE nstate   INT;
  DECLARE k        INT;
  DECLARE kmax     INT;
  DECLARE ts       DATETIME;
  DECLARE bad      TINYINT;

  WHILE s <= 40 DO
    SET nodeid   = (s + 1) DIV 2;
    SET is_fwd   = (s % 2);                       -- 1 = forward, 0 = retorno
    SET chasisid = ((nodeid - 1) % 10) + 1;
    SET platid   = IF(chasisid % 2 = 1, 1, 2);    -- impar AURORA, par ROSA
    SET sttype   = IF(platid = 1, IF(is_fwd = 1, 1, 2), IF(is_fwd = 1, 3, 4));
    SET nstate   = (SELECT state_id FROM optical_node WHERE id = nodeid);
    SET bad      = IF(nstate = 2, 1, 0);

    INSERT INTO optical_slot
      (id, name, label1, label2, title, platform_id, alarmtype_id, chasis_id, slottype_id, state_id)
    VALUES
      (s,
       CONCAT('S', chasisid, '/', s),
       CONCAT('PORT-', s), CONCAT('LC-', s),
       CONCAT('MARK-', IF(is_fwd = 1, 'FWD', 'RET'), '-', LPAD(nodeid, 3, '0')),
       platid,
       IF(bad = 1, 1, 3),
       chasisid, sttype, nstate);

    SET kmax = IF(is_fwd = 1, 6, 5);
    SET k = 0;
    WHILE k < kmax DO
      SET ts = NOW() - INTERVAL (k * 11 + s) MINUTE;
      IF is_fwd = 1 THEN
        INSERT INTO optical_slotdata
          (slot_id, created, inrf, outputpower, power1, power2, arop, edfapower)
        VALUES
          (s, ts,
           IF(bad = 1, ROUND(0.05 + RAND() * 0.30, 3),  ROUND(11.9 + RAND() * 0.9,  3)),
           IF(bad = 1, ROUND(-28.0 + RAND() * 3.0, 3),  ROUND( 3.05 + RAND() * 0.35, 3)),
           IF(bad = 1, ROUND(-40.0 + RAND() * 2.0, 3),  ROUND(-5.10 + RAND() * 0.40, 3)),
           IF(bad = 1, ROUND(-41.0 + RAND() * 2.0, 3),  ROUND(-6.20 + RAND() * 0.40, 3)),
           IF(bad = 1, ROUND(-42.0 + RAND() * 2.0, 3),  ROUND(-7.40 + RAND() * 0.40, 3)),
           IF(bad = 1, ROUND(  0.0 + RAND() * 0.5, 3),  ROUND(15.10 + RAND() * 0.40, 3)));
      ELSE
        INSERT INTO optical_slotdata
          (slot_id, created, inrf, outputpower, power1, power2, arop, edfapower)
        VALUES
          (s, ts,
           0, 0,
           IF(bad = 1, ROUND(-36.0 + RAND() * 2.0, 3),  ROUND(-18.10 + RAND() * 0.9, 3)),
           IF(bad = 1, ROUND(-37.0 + RAND() * 2.0, 3),  ROUND(-19.30 + RAND() * 0.9, 3)),
           IF(bad = 1, ROUND(-38.0 + RAND() * 2.0, 3),  ROUND(-20.10 + RAND() * 0.9, 3)),
           0);
      END IF;
      SET k = k + 1;
    END WHILE;

    SET s = s + 1;
  END WHILE;
END$$
DELIMITER ;
CALL seed_slots_and_data();
DROP PROCEDURE seed_slots_and_data;

-- ------------------------------------------------------------------ alarmas
-- helper: datos de ubicacion por nodo
DROP VIEW IF EXISTS v_node_loc;
CREATE VIEW v_node_loc AS
SELECT n.id, n.name, n.state_id,
       re.name AS region_name, oc.name AS city_name, sd.name AS sds_name,
       s.name  AS slot_name,   pl.name AS platform_name, c.name AS chasis_name
FROM optical_node n
JOIN optical_slot     s  ON n.fwdslot_id = s.id
JOIN optical_chasis   c  ON s.chasis_id  = c.id
JOIN optical_platform pl ON c.platform_id = pl.id
JOIN optical_sds      sd ON c.sds_id     = sd.id
JOIN optical_city     oc ON sd.city_id   = oc.id
JOIN optical_region   re ON oc.region_id = re.id;

-- 1) alarma principal por cada nodo UP/DOWN  (ack = 2, visible)
INSERT INTO optical_nodealarm
  (created, ack, node_id, region_name, city_name, sds_name, slot_name, platform_name, chasis_name, process_id, alarmtype_id, level_id, state_id)
SELECT NOW() - INTERVAL (v.id * 5) MINUTE, 2, v.name,
       v.region_name, v.city_name, v.sds_name, v.slot_name, v.platform_name, v.chasis_name,
       3000 + v.id,
       IF(v.state_id = 2, 1, 3),          -- CRITICAL si DOWN, MINOR si UP
       IF(v.state_id = 2, 5, 3),          -- LOS si DOWN, INRF si UP
       v.state_id
FROM v_node_loc v
WHERE v.state_id IN (1, 2);

-- 2) alarma secundaria solo para nodos DOWN  (ack = 2, visible)
INSERT INTO optical_nodealarm
  (created, ack, node_id, region_name, city_name, sds_name, slot_name, platform_name, chasis_name, process_id, alarmtype_id, level_id, state_id)
SELECT NOW() - INTERVAL (v.id * 5 + 37) MINUTE, 2, v.name,
       v.region_name, v.city_name, v.sds_name, v.slot_name, v.platform_name, v.chasis_name,
       3500 + v.id, 2, 2, v.state_id       -- MAJOR / RET-PWR
FROM v_node_loc v
WHERE v.state_id = 2;

-- 3) alarmas ya reconocidas  (ack = 1, NO se muestran) para 4 nodos UP
INSERT INTO optical_nodealarm
  (created, ack, node_id, region_name, city_name, sds_name, slot_name, platform_name, chasis_name, process_id, alarmtype_id, level_id, state_id)
SELECT NOW() - INTERVAL (v.id * 13 + 120) MINUTE, 1, v.name,
       v.region_name, v.city_name, v.sds_name, v.slot_name, v.platform_name, v.chasis_name,
       3900 + v.id, 4, 4, v.state_id       -- WARNING / AROP
FROM v_node_loc v
WHERE v.id IN (2, 4, 6, 8);

DROP VIEW v_node_loc;
