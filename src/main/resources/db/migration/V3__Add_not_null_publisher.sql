UPDATE book
SET publisher = 'Unknown'
WHERE publisher IS NULL;

ALTER TABLE book
    ALTER COLUMN publisher SET NOT NULL;