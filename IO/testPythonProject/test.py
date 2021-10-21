def checkifORR(i, j):  # controllo orizzontale
    global counter # conta il numero di simboli di fila
    tmp = j # variabile temporale per scorrere la tabella di gioco, utilizzato anche per salvare la posizione iniziale
    counter = 1
    for z in range(4): #scorrimento orizzontale, il 4 è perchè a 5 simboli si vince
        if j < len(b) - 1 and b[i][j]["text"] == b[i][j + 1]["text"]: #controllo destra
            counter += 1
            j += 1
        elif counter < 5 and tmp > 0 and b[i][tmp]["text"] == b[i][tmp - 1]["text"]: #controllo sinistra
            counter += 1
            tmp -= 1
    if counter == 3: #assegno i punti in base a counter
        punti[cplayer] = punti[cplayer] + 2
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])
    elif counter == 4:
        punti[cplayer] = punti[cplayer] + 10
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])
    elif counter == 5:
        punti[cplayer] = punti[cplayer] + 50
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])