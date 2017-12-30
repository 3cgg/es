
SELECT top 100 PERCENT
	id as id ,
	title as title,
	summary as summary,
	link as link,
	saas_code as saasCode,
	CONVERT (
		VARCHAR (100),
		storage_time,
		23
	) as storageTime,
	CONVERT (
		VARCHAR (100),
		create_date,
		23
	) as createDate,
	category_code as categoryCode,
	modify_date as modifyDate
FROM
	t_smart_push
WHERE
	is_available = '1'
 AND title NOT LIKE '%<%'
 AND title NOT LIKE '%>%'
 AND summary NOT LIKE '%<%'
 AND summary NOT LIKE '%>%'
 and modify_date > :sql_last_value
 order by modify_date asc