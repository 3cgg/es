
SELECT top 100 PERCENT
	id,
	title,
	firstPicId,
	saasCode,
	status,
	approve,
	isReliable,
	introduction,
	label,
	technicalFieldName,
	cast(modifyDate as datetime) as modifyDate,
	isAvaliable,
	codeType,
	firstPicPath,
	publish_name,
	dataSource
FROM
	v_twelvelibrary
WHERE
	isAvaliable = '1'
 AND approve = 'APPROVED'
 AND status = 'ON'
 and cast(modifyDate as datetime) > :sql_last_value
 order by modifyDate asc