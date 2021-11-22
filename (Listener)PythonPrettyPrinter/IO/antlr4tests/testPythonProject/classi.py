from tkinter import *
from tkinter import messagebox

root = Tk()
root.title('Filetto')

def distruggi(): #elimina la vecchia tabella di gioco
    if controlloDistruzione :
        frameGiocatore.destroy()
    frameGioco.destroy()
    reset()


def reset(): #metodo per iniziare il gioco
    global inputPlayer, inputMatrice, clicked, frameOpzioni, frameGioco, controlloDistruzione

    controlloDistruzione = False

    frameOpzioni = LabelFrame(root, text="Opzioni di Gioco") #frame opzioni di gioco
    frameOpzioni.pack(side=TOP)

    frameGioco = LabelFrame(root, text="Tabella di gioco")  #frame tabella di gioco
    frameGioco.pack()

    label_Giocatori = Label(frameOpzioni, text="Numero giocatori")  #grafica per la scelta del numero dei giocatori e grandezza della matrice
    label_Giocatori.grid(row=0, column=0, padx=(0, 10), pady=(0, 10))

    inputPlayer = Entry(frameOpzioni)
    inputPlayer.grid(row=1, column=0, padx=(0, 10), pady=(0, 10))

    label_Matrice = Label(frameOpzioni, text="Grandezza matrice")
    label_Matrice.grid(row=0, column=1, padx=(0, 10), pady=(0, 10))

    inputMatrice = Entry(frameOpzioni)
    inputMatrice.grid(row=1, column=1, padx=(0, 10), pady=(0, 10))

    Bottone = Button(frameOpzioni, text="Inserisci dati", command=getDati)  #bottone per salvare i dati di frameOpzioni
    Bottone.grid(row=2, column=1, padx=(0, 10), pady=(0, 10))


def getDati():  #metodo che prende il numero dei giocatori e la grandezza della matrice tramite input
    global clicked, nplayers, gMatrice
    nplayers = int(inputPlayer.get())   #numero dei giocatori
    gMatrice = int(inputMatrice.get())  #grandezza lato della matrice
    frameOpzioni.destroy()  #eliminazione del frame delle opzioni
    scelta()    #richiamo del metodo per la scelta della modalita di gioco


def scelta(): #metodo per la scelta dei simboli
    global scelta, frameScelta
    frameScelta = LabelFrame(root, text="Scelta modalità simbolo")  #frame per la scelta della modalita di gioco
    frameScelta.pack(side=TOP)
    Bottone = Button(frameScelta, text="Default", command=start1)   #bottone modalita di default
    Bottone.grid(row=0, column=0, padx=(0, 10), pady=(0, 10))
    Bottone = Button(frameScelta, text="Simboli personali", command=start2) #bottone modalita simboli personalizzati
    Bottone.grid(row=0, column=1, padx=(0, 10), pady=(0, 10))


def start1():  #inizio del gioco in modalita default ( i simboli di default sono numeri )
    global nplayers, mosseMAX, b, cplayer, punti, countmosse, gMatrice, frameScelta, players
    frameScelta.destroy()   #distruzione del frame di scelta dei simboli
    cplayer = 0  # counter dei player, fa da indice per capire il player corrente
    players = [str(i + 1) for i in range(nplayers)]  # array di giocatori
    punti = [0 for i in range(nplayers)]  # array dei punti di ogni giocatore
    countmosse = 1  # contatore per le mosse utilizzate, usato per controllare il pareggio
    mosseMAX = gMatrice * gMatrice  # numero bottoni, utilizzato per controllare il pareggio
    b = [[build_button(i, j) for j in range(gMatrice)] for i in range(gMatrice)]  # array bottoni


def start2():  #inizio del gioco in modalita simboli personalizzati, analogo a start1
    global nplayers, mosseMAX, b, cplayer, players, punti, countmosse, gMatrice, frameScelta, frameGiocatore
    frameScelta.destroy()
    cplayer = 0
    players = ["" for i in range(nplayers)]
    punti = [0 for i in range(nplayers)]
    countmosse = 1
    mosseMAX = gMatrice * gMatrice
    b = [[build_button(i, j) for j in range(gMatrice)] for i in range(gMatrice)]
    create_simbolo(0)   #richiamo del metodo che si occupa dell'assegnazione dei simboli


def disable_all_buttons():  # rende i bottoni inutilizzabili
    global b
    for i in range(gMatrice):
        for j in range(gMatrice):
            b[i][j]['state'] = DISABLED


def enable_all_buttons():  # rende i bottoni utilizzabili
    global b
    for i in range(gMatrice):
        for j in range(gMatrice):
            b[i][j]['state'] = NORMAL


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


def checkifVER(i, j):  # controllo verticale
    global counter
    tmp = i
    counter = 1
    for z in range(4):
        if i < len(b) - 1 and b[i][j]["text"] == b[i + 1][j]["text"]:
            counter += 1
            i += 1
        elif counter < 5 and tmp > 0 and b[tmp][j]["text"] == b[tmp - 1][j]["text"]:
            counter += 1
            tmp -= 1
    if counter == 3:
        punti[cplayer] = punti[cplayer] + 2
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])
    elif counter == 4:
        punti[cplayer] = punti[cplayer] + 10
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])
    elif counter == 5:
        punti[cplayer] = punti[cplayer] + 50
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])


def checkifDIAGDX(i, j):  # controllo diagonale destra
    global counter
    tmpi = i
    tmpj = j
    counter = 1
    for z in range(4):
        if i > 0 and j < len(b) - 1 and b[i][j]["text"] == b[i - 1][j + 1]["text"]:
            counter += 1
            i -= 1
            j += 1
        elif counter < 5 and tmpj > 0 and tmpi < len(b) - 1 and b[tmpi][tmpj]["text"] == b[tmpi + 1][tmpj - 1]["text"]:
            counter += 1
            tmpi += 1
            tmpj -= 1
    if counter == 3:
        punti[cplayer] = punti[cplayer] + 2
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])
    elif counter == 4:
        punti[cplayer] = punti[cplayer] + 10
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])
    elif counter == 5:
        punti[cplayer] = punti[cplayer] + 50
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])


def checkifDIAGSX(i, j):  # controllo diagonale sinistra
    global counter
    tmpi = i
    tmpj = j
    counter = 1
    for z in range(4):
        if i > 0 and j > 0 and b[i][j]["text"] == b[i - 1][j - 1]["text"]:
            counter += 1
            i -= 1
            j -= 1
        elif counter < 5 and tmpi < len(b) - 1 and tmpj < len(b) - 1 and b[tmpi][tmpj]["text"] == b[tmpi + 1][tmpj + 1][
            "text"]:
            counter += 1
            tmpi += 1
            tmpj += 1
    if counter == 3:
        punti[cplayer] = punti[cplayer] + 2
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])
    elif counter == 4:
        punti[cplayer] = punti[cplayer] + 10
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])
    elif counter == 5:
        punti[cplayer] = punti[cplayer] + 50
        print("punti attuali giocatore", cplayer + 1, "punti", punti[cplayer])


def b_click(i, j):  # onclick di qualsiasi bottone della tabella
    global cplayer, countmosse, players
    if b[i][j]["text"] == " ":  # controllo che la casella non sia già occupata da un altro giocatore
        b[i][j]["text"] = players[cplayer]  #scrivo sulla casella vuota il simbolo del giocatore
        checkifORR(i, j)    #richiami ai vari controlli
        checkifVER(i, j)
        checkifDIAGDX(i, j)
        checkifDIAGSX(i, j)
        label_punti = Label(frameGioco, text="Punti attuali giocatore: " + str(cplayer+1) + ": " + str(punti[cplayer])) #stampa dei punti su schermo
        label_punti.grid(row=0, column=gMatrice+1)
        if punti[cplayer] >= 50:    # controllo per la vittoria
            c = "Vittoria del giocatore " + str(cplayer + 1) + " Punti:" + str(punti[cplayer])
            messagebox.showinfo("Vittoria", c)
            disable_all_buttons()
        cplayer += 1
        if countmosse == mosseMAX:  #controllo per il pareggio
            checkifPAR()
        countmosse += 1
        if cplayer == nplayers: #cambio del turno
            cplayer = 0
    else:
        messagebox.showinfo("Attenzione", "Casella gia occupata")   #messaggio di errore se la casella è occupata
    if players[cplayer] == "" : #assegnazione dei vari simboli
        create_simbolo(cplayer)


def checkifPAR():  #metodo del pareggio
    puntiMax = punti[0]
    pareggio = False
    winner = 0
    for x in range(nplayers - 1):
        if punti[x] < punti[x + 1]: #controllo i valori dei punti a due a due ed assegno il piu grande alla varriabile puntiMax
            puntiMax = punti[x + 1]
            pareggio = False
            winner = x + 1
        elif punti[x] == punti[x + 1] and punti[x] == puntiMax: #se trovo due punteggi uguali che sono anche identici alla variabile puntiMax assegno True alla variabile pareggio
            pareggio = True
    if pareggio == False:   #controllo chi ha vinto se ho finito le caselle disponibili
        wp = "Vittoria del giocatore " + str(winner + 1) + " con " + str(punti[winner]) + " punti!"
        messagebox.showinfo("Vittoria!", wp)
    else:   #messaggio di pareggio in caso non ci sia nessun vincitore
        messagebox.showinfo("Pareggio", "La partita è finita in pareggio")
    disable_all_buttons()


def create_simbolo(i):  #metodo per la grafica della scelta del simbolo
    global inputSimbolo, players, frameGiocatore, controlloDistruzione
    disable_all_buttons()
    frameGiocatore = LabelFrame(root, text="Scelta del simbolo")
    frameGiocatore.pack(side=TOP)
    controlloDistruzione = True
    label_Simbolo = Label(frameGiocatore, text="Simbolo desiderato giocatore: " + str(i+1))
    label_Simbolo.grid(row=0, column=0, padx=(0, 10), pady=(0, 10))
    inputSimbolo = Entry(frameGiocatore)
    inputSimbolo.grid(row=1, column=0, padx=(0, 10), pady=(0, 10))
    bottone = Button(frameGiocatore, text="Inserisci dati")
    bottone['command'] = lambda arg=i: prendiSimbolo(arg)
    bottone.grid(row=2, column=0)


def prendiSimbolo(i): #metodo che prende il simbolo da input tastiera
    global simbolo, players
    simbolo = inputSimbolo.get()
    if simbolo == "":   #controllo che il simbolo scelto non sia nullo
        messagebox.showerror("Errore", "Il simbolo non puo essere nullo!!")
        frameGiocatore.destroy()
        create_simbolo(i)
    else:
        controllo = checkSimboliUguali()
        if controllo:
            messagebox.showerror("Errore", "Il simbolo è gia stato scelto da un altro giocatore")
            frameGiocatore.destroy()
            create_simbolo(i)
        else:
            players[i] = simbolo
            frameGiocatore.destroy()
            enable_all_buttons()


def checkSimboliUguali():  #controllo che il simbolo non sia gia stato scelto
    controllo = False
    for i in range(nplayers):
        if simbolo == players[i]:
            controllo = True
            return controllo
    return controllo


def build_button(i, j): # crea i bottoni dinamicamente
    if gMatrice > 7: # se la matrice è troppo grande rimpiccioliamo i bottoni
        button = Button(frameGioco, text=" ", height=1, width=2)
    else: # crea bottoni con grafica
        button = Button(frameGioco, text=" ", font=("Helvetica", 20), height=3, width=6, bg="SystemButtonFace", )
    button['command'] = lambda arg1=i, arg2=j: b_click(arg1, arg2)
    button.grid(row=i, column=j)
    return button