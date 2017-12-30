
SELECT top 100 PERCENT
	id as id ,
	modify_date as modifyDate
FROM
	t_smart_push
WHERE
	is_available = '0'
 and modify_date > :sql_last_value
 order by modify_date asc