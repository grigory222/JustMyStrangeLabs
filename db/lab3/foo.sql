CREATE OR REPLACE FUNCTION update_average_age()
RETURNS TRIGGER AS $$
DECLARE
    avg_age NUMERIC;
BEGIN
    -- Вычисляем средний возраст всех людей
    SELECT AVG(EXTRACT(YEAR FROM age(current_date, p.birthday)))
    INTO avg_age
    FROM people p;

    -- Выводим результат на экран
    RAISE NOTICE 'Average age of people: %', avg_age;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- удалить старый триггер
DROP TRIGGER update_average_age_trigger ON people;

-- создать новый
CREATE TRIGGER update_average_age_trigger
AFTER INSERT OR UPDATE OF birthday ON people
FOR EACH ROW
EXECUTE FUNCTION update_average_age();

