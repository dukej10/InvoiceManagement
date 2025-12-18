--- revisar procedimiento
SELECT LINE, POSITION, TEXT 
FROM USER_ERRORS 
WHERE NAME = 'INSERT_INVOICE'
  AND TYPE = 'PROCEDURE'
ORDER BY SEQUENCE;

-- revisar tipos
SELECT name, type, line, position, text
FROM user_errors
WHERE name IN ('VARCHAR2_LIST', 'NUMBER_LIST', 'FLOAT_LIST')
ORDER BY name, sequence;