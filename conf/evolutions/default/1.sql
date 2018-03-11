# --- !Ups

CREATE OR REPLACE FUNCTION entity_update_func()
  RETURNS TRIGGER AS
$$
DECLARE
BEGIN
  NEW.updated_at := CURRENT_TIMESTAMP;;
  IF NEW.version = OLD.version
  THEN
    NEW.version := OLD.version + 1;;
  END IF;;
  RETURN NEW;;
END;;
$$
LANGUAGE plpgsql;

# --- !Downs

DROP FUNCTION entity_update_func();
