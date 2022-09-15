final a = [1, 2, 3, 4, 5, 6, 7, 8, 9]

//Take five elements starting on the second position
assert [2, 3, 4, 5, 6] == a[1..5]

//Reverse the list
assert [9, 8, 7, 6, 5, 4, 3, 2, 1] == a[-1..0]

println a.join("-")

println 'ok'