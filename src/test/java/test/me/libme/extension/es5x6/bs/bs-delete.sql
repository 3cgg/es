
SELECT top 100 PERCENT
	id,
	cast(modifyDate as datetime) as modifyDate
FROM
	v_twelvelibrary
WHERE
	isAvaliable = '0'
 AND approve = 'APPROVED'
 AND status = 'ON'
 and cast(modifyDate as datetime) > :sql_last_value
 order by modifyDate asc