# if_stmt: IF cond=test COLON suite elif_clause* else_clause?

# IF test COLON suite
if x == 5: pass

# IF test COLON suite elif_clause
if x == 4:
    pass
elif x == 3:
    if x == 3:
        print("ciao")
    elif x ==2:
        if x == 3:
            print("ciao")
        elif x ==2:
            print("ciao")

# IF test COLON suite else_clause
if x == 7:
    pass
else:
    pass

# IF test COLON suite elif_clause elif_clause else_clause
if x == 4:
    pass
elif x == 3:
    pass
else:
    pass
