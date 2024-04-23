CREATE OR REPLACE FUNCTION update_alerts()
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT danger from emotions where NEW.emotion_id = id) > 8
    THEN
        insert into alerts (human, danger, emotion, time) values
            ( NEW.owner_id,
              (SELECT danger from emotions where NEW.emotion_id = id),
              NEW.emotion_id,
              NOW()
            );
    END IF;
    RAISE NOTICE '%', to_json((SELECT danger from emotions where NEW.emotion_id = id));
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_alerts
AFTER INSERT OR UPDATE ON feelings
FOR EACH ROW
EXECUTE FUNCTION update_alerts();

