from tkinter import * 
from tkinter import messagebox 
root = Tk ( ) 
root . title ( 'Filetto' ) 
def distruggi ( ) : 
    if controlloDistruzione : 
        frameGiocatore . destroy ( ) 
    frameGioco . destroy ( ) 
    reset ( ) 
def reset ( ) : 
    global inputPlayer , inputMatrice , clicked , frameOpzioni , frameGioco , controlloDistruzione 
    controlloDistruzione = False 
    frameOpzioni = LabelFrame ( root , text = "Opzioni di Gioco" ) 
    frameOpzioni . pack ( side = TOP ) 
    frameGioco = LabelFrame ( root , text = "Tabella di gioco" ) 
    frameGioco . pack ( ) 
    label_Giocatori = Label ( frameOpzioni , text = "Numero giocatori" ) 
    label_Giocatori . grid ( row = 0 , column = 0 , padx = ( 0 , 10 ) , pady = ( 0 , 10 ) ) 
    inputPlayer = Entry ( frameOpzioni ) 
    inputPlayer . grid ( row = 1 , column = 0 , padx = ( 0 , 10 ) , pady = ( 0 , 10 ) ) 
    label_Matrice = Label ( frameOpzioni , text = "Grandezza matrice" ) 
    label_Matrice . grid ( row = 0 , column = 1 , padx = ( 0 , 10 ) , pady = ( 0 , 10 ) ) 
    inputMatrice = Entry ( frameOpzioni ) 
    inputMatrice . grid ( row = 1 , column = 1 , padx = ( 0 , 10 ) , pady = ( 0 , 10 ) ) 
    Bottone = Button ( frameOpzioni , text = "Inserisci dati" , command = getDati ) 
    Bottone . grid ( row = 2 , column = 1 , padx = ( 0 , 10 ) , pady = ( 0 , 10 ) ) 
def getDati ( ) : 
    global clicked , nplayers , gMatrice 
    nplayers = int ( inputPlayer . get ( ) ) 
    gMatrice = int ( inputMatrice . get ( ) ) 
    frameOpzioni . destroy ( ) 
    scelta ( ) 
def scelta ( ) : 
    global scelta , frameScelta 
    frameScelta = LabelFrame ( root , text = "Scelta modalità simbolo" ) 
    frameScelta . pack ( side = TOP ) 
    Bottone = Button ( frameScelta , text = "Default" , command = start1 ) 
    Bottone . grid ( row = 0 , column = 0 , padx = ( 0 , 10 ) , pady = ( 0 , 10 ) ) 
    Bottone = Button ( frameScelta , text = "Simboli personali" , command = start2 ) 
    Bottone . grid ( row = 0 , column = 1 , padx = ( 0 , 10 ) , pady = ( 0 , 10 ) ) 
def start1 ( ) : 
    global nplayers , mosseMAX , b , cplayer , punti , countmosse , gMatrice , frameScelta , players 
    frameScelta . destroy ( ) 
    cplayer = 0 
    players = [ str ( i + 1 ) for i in range ( nplayers ) ] 
    punti = [ 0 for i in range ( nplayers ) ] 
    countmosse = 1 
    mosseMAX = gMatrice * gMatrice 
    b = [ [ build_button ( i , j ) for j in range ( gMatrice ) ] for i in range ( gMatrice ) ] 
def start2 ( ) : 
    global nplayers , mosseMAX , b , cplayer , players , punti , countmosse , gMatrice , frameScelta , frameGiocatore 
    frameScelta . destroy ( ) 
    cplayer = 0 
    players = [ "" for i in range ( nplayers ) ] 
    punti = [ 0 for i in range ( nplayers ) ] 
    countmosse = 1 
    mosseMAX = gMatrice * gMatrice 
    b = [ [ build_button ( i , j ) for j in range ( gMatrice ) ] for i in range ( gMatrice ) ] 
    create_simbolo ( 0 ) 
def disable_all_buttons ( ) : 
    global b 
    for i in range ( gMatrice ) : 
        for j in range ( gMatrice ) : 
            b [ i ] [ j ] [ 'state' ] = DISABLED 
def enable_all_buttons ( ) : 
    global b 
    for i in range ( gMatrice ) : 
        for j in range ( gMatrice ) : 
            b [ i ] [ j ] [ 'state' ] = NORMAL 
def checkifORR ( i , j ) : 
    global counter 
    tmp = j 
    counter = 1 
    for z in range ( 4 ) : 
        if j < len ( b ) - 1 and b [ i ] [ j ] [ "text" ] == b [ i ] [ j + 1 ] [ "text" ] : 
            counter += 1 
            j += 1 
        elif counter < 5 and tmp > 0 and b [ i ] [ tmp ] [ "text" ] == b [ i ] [ tmp - 1 ] [ "text" ] : 
            counter += 1 
            tmp -= 1 
    if counter == 3 : 
        punti [ cplayer ] = punti [ cplayer ] + 2 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
    elif counter == 4 : 
        punti [ cplayer ] = punti [ cplayer ] + 10 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
    elif counter == 5 : 
        punti [ cplayer ] = punti [ cplayer ] + 50 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
def checkifVER ( i , j ) : 
    global counter 
    tmp = i 
    counter = 1 
    for z in range ( 4 ) : 
        if i < len ( b ) - 1 and b [ i ] [ j ] [ "text" ] == b [ i + 1 ] [ j ] [ "text" ] : 
            counter += 1 
            i += 1 
        elif counter < 5 and tmp > 0 and b [ tmp ] [ j ] [ "text" ] == b [ tmp - 1 ] [ j ] [ "text" ] : 
            counter += 1 
            tmp -= 1 
    if counter == 3 : 
        punti [ cplayer ] = punti [ cplayer ] + 2 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
    elif counter == 4 : 
        punti [ cplayer ] = punti [ cplayer ] + 10 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
    elif counter == 5 : 
        punti [ cplayer ] = punti [ cplayer ] + 50 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
def checkifDIAGDX ( i , j ) : 
    global counter 
    tmpi = i 
    tmpj = j 
    counter = 1 
    for z in range ( 4 ) : 
        if i > 0 and j < len ( b ) - 1 and b [ i ] [ j ] [ "text" ] == b [ i - 1 ] [ j + 1 ] [ "text" ] : 
            counter += 1 
            i -= 1 
            j += 1 
        elif counter < 5 and tmpj > 0 and tmpi < len ( b ) - 1 and b [ tmpi ] [ tmpj ] [ "text" ] == b [ tmpi + 1 ] [ tmpj - 1 ] [ "text" ] : 
            counter += 1 
            tmpi += 1 
            tmpj -= 1 
    if counter == 3 : 
        punti [ cplayer ] = punti [ cplayer ] + 2 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
    elif counter == 4 : 
        punti [ cplayer ] = punti [ cplayer ] + 10 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
    elif counter == 5 : 
        punti [ cplayer ] = punti [ cplayer ] + 50 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
def checkifDIAGSX ( i , j ) : 
    global counter 
    tmpi = i 
    tmpj = j 
    counter = 1 
    for z in range ( 4 ) : 
        if i > 0 and j > 0 and b [ i ] [ j ] [ "text" ] == b [ i - 1 ] [ j - 1 ] [ "text" ] : 
            counter += 1 
            i -= 1 
            j -= 1 
        elif counter < 5 and tmpi < len ( b ) - 1 and tmpj < len ( b ) - 1 and b [ tmpi ] [ tmpj ] [ "text" ] == b [ tmpi + 1 ] [ tmpj + 1 ] [ "text" ] : 
            counter += 1 
            tmpi += 1 
            tmpj += 1 
    if counter == 3 : 
        punti [ cplayer ] = punti [ cplayer ] + 2 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
    elif counter == 4 : 
        punti [ cplayer ] = punti [ cplayer ] + 10 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
    elif counter == 5 : 
        punti [ cplayer ] = punti [ cplayer ] + 50 
        print ( "punti attuali giocatore" , cplayer + 1 , "punti" , punti [ cplayer ] ) 
def b_click ( i , j ) : 
    global cplayer , countmosse , players 
    if b [ i ] [ j ] [ "text" ] == " " : 
        b [ i ] [ j ] [ "text" ] = players [ cplayer ] 
        checkifORR ( i , j ) 
        checkifVER ( i , j ) 
        checkifDIAGDX ( i , j ) 
        checkifDIAGSX ( i , j ) 
        label_punti = Label ( frameGioco , text = "Punti attuali giocatore: " + str ( cplayer + 1 ) + ": " + str ( punti [ cplayer ] ) ) 
        label_punti . grid ( row = 0 , column = gMatrice + 1 ) 
        if punti [ cplayer ] >= 50 : 
            c = "Vittoria del giocatore " + str ( cplayer + 1 ) + " Punti:" + str ( punti [ cplayer ] ) 
            messagebox . showinfo ( "Vittoria" , c ) 
            disable_all_buttons ( ) 
        cplayer += 1 
        if countmosse == mosseMAX : 
            checkifPAR ( ) 
        countmosse += 1 
        if cplayer == nplayers : 
            cplayer = 0 
    else : 
        messagebox . showinfo ( "Attenzione" , "Casella gia occupata" ) 
    if players [ cplayer ] == "" : 
        create_simbolo ( cplayer ) 
def checkifPAR ( ) : 
    puntiMax = punti [ 0 ] 
    pareggio = False 
    winner = 0 
    for x in range ( nplayers - 1 ) : 
        if punti [ x ] < punti [ x + 1 ] : 
            puntiMax = punti [ x + 1 ] 
            pareggio = False 
            winner = x + 1 
        elif punti [ x ] == punti [ x + 1 ] and punti [ x ] == puntiMax : 
            pareggio = True 
    if pareggio == False : 
        wp = "Vittoria del giocatore " + str ( winner + 1 ) + " con " + str ( punti [ winner ] ) + " punti!" 
        messagebox . showinfo ( "Vittoria!" , wp ) 
    else : 
        messagebox . showinfo ( "Pareggio" , "La partita è finita in pareggio" ) 
    disable_all_buttons ( ) 
def create_simbolo ( i ) : 
    global inputSimbolo , players , frameGiocatore , controlloDistruzione 
    disable_all_buttons ( ) 
    frameGiocatore = LabelFrame ( root , text = "Scelta del simbolo" ) 
    frameGiocatore . pack ( side = TOP ) 
    controlloDistruzione = True 
    label_Simbolo = Label ( frameGiocatore , text = "Simbolo desiderato giocatore: " + str ( i + 1 ) ) 
    label_Simbolo . grid ( row = 0 , column = 0 , padx = ( 0 , 10 ) , pady = ( 0 , 10 ) ) 
    inputSimbolo = Entry ( frameGiocatore ) 
    inputSimbolo . grid ( row = 1 , column = 0 , padx = ( 0 , 10 ) , pady = ( 0 , 10 ) ) 
    bottone = Button ( frameGiocatore , text = "Inserisci dati" ) 
    bottone [ 'command' ] = lambda arg = i : prendiSimbolo ( arg ) 
    bottone . grid ( row = 2 , column = 0 ) 
def prendiSimbolo ( i ) : 
    global simbolo , players 
    simbolo = inputSimbolo . get ( ) 
    if simbolo == "" : 
        messagebox . showerror ( "Errore" , "Il simbolo non puo essere nullo!!" ) 
        frameGiocatore . destroy ( ) 
        create_simbolo ( i ) 
    else : 
        controllo = checkSimboliUguali ( ) 
        if controllo : 
            messagebox . showerror ( "Errore" , "Il simbolo è gia stato scelto da un altro giocatore" ) 
            frameGiocatore . destroy ( ) 
            create_simbolo ( i ) 
        else : 
            players [ i ] = simbolo 
            frameGiocatore . destroy ( ) 
            enable_all_buttons ( ) 
def checkSimboliUguali ( ) : 
    controllo = False 
    for i in range ( nplayers ) : 
        if simbolo == players [ i ] : 
            controllo = True 
            return controllo 
    return controllo 
def build_button ( i , j ) : 
    if gMatrice > 7 : 
        button = Button ( frameGioco , text = " " , height = 1 , width = 2 ) 
    else : 
        button = Button ( frameGioco , text = " " , font = ( "Helvetica" , 20 ) , height = 3 , width = 6 , bg = "SystemButtonFace" , ) 
    button [ 'command' ] = lambda arg1 = i , arg2 = j : b_click ( arg1 , arg2 ) 
    button . grid ( row = i , column = j ) 
    return button 
