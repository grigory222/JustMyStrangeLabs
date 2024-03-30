SELECT * FROM people;

SELECT people.first_name, people.last_name
FROM people
JOIN creators ON people.id = creators.creator
JOIN feelings ON people.id = feelings.owner_id
WHERE feelings.emotion_id = 1;

SELECT miracles.name
FROM miracles
JOIN creators ON miracles.id = creators.miracle
JOIN people ON creators.creator = people.id
JOIN feelings ON people.id = feelings.owner_id
WHERE feelings.emotion_id = 1;
