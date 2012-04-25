select * from InsertionResult where storyid = 12 and optionPresented1 = 1 and optionPresented2 = 2
union
select * from InsertionResult where storyid = 12 and optionPresented1 = 2 and optionPresented2 = 1


-- count valid users

SELECT count( DISTINCT (user) ) FROM InsertionResult
WHERE NOT (
user
IN (
'bianca', 'bianca_1', 'jld', 'a', 'J. B. Berthelin', 'J. B. Berthelin_1'
)
OR user LIKE 'anonymous%'
) 