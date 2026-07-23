-- Deja cargado el torneo únicamente hasta antes de octavos.
-- Conserva partidos FIFA 1-88 y elimina 89-104 para carga manual.

BEGIN;

DELETE FROM gol
WHERE partido_id IN (
    SELECT id
    FROM partido
    WHERE numero_partido_fifa BETWEEN 89 AND 104
);

DELETE FROM auditoria
WHERE entidad = 'Partido'
  AND entidad_id IN (
      SELECT id
      FROM partido
      WHERE numero_partido_fifa BETWEEN 89 AND 104
  );

DELETE FROM partido
WHERE numero_partido_fifa BETWEEN 89 AND 104;

COMMIT;

SELECT COUNT(*) AS partidos_desde_octavos
FROM partido
WHERE numero_partido_fifa BETWEEN 89 AND 104;
