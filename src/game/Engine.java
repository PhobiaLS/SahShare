package game;

import geometry.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.*;
import exceptions.Checkmate;
import exceptions.Draw;
import exceptions.Promotion;
import game.chessPieces.*;
import bot.Bot;


// TODO Implementirati novi isAttacked
// TODO => Pravi novu granu i sredi engine skroz
public class Engine implements GameConstants{

	private ChessPiece[][] board;
	private int onMove;
	private List<Move> moves = new ArrayList<>();
	private Point selFigure;
	private int typeOfGame;
	private int team;
	private Bot bot = null;

	private List<Point> movFigures = new ArrayList<Point>();
	private List<Point> attackers = new ArrayList<Point>();
	
	public Engine(int typeOfGame,int team){
		newGame();
		this.typeOfGame = typeOfGame;
		if(team == TEAM_WHITE)
			this.team = 1;
		else 
			this.team = -1;
		if(typeOfGame == GAME_MODE_BOTS)
			bot = new Bot(this);
	}
	
	//Kreira novu tablu\\
	private void initializeBoard() {
		board = new ChessPiece[8][8];
		
		for (int i = 2; i < 6; i++)
			for (int j = 0; j < GameConstants.BOARD_SIZE; j++)
				board[i][j] = null;
		
		for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
			board[1][i] = new Pawn(-1);
			board[6][i] = new Pawn(1);		
		}
		
		board[0][0] = new Rook(-1);
		board[0][7] = new Rook(-1);
		board[7][0] = new Rook(1);
		board[7][7] = new Rook(1);
		
		
		board[0][1] = new Knight(-1);
		board[0][6] = new Knight(-1);
		board[7][1] = new Knight(1);
		board[7][6] = new Knight(1);
		
		board[0][2] = new Bishop(-1);
		board[0][5] = new Bishop(-1);
		board[7][2] = new Bishop(1);
		board[7][5] = new Bishop(1);
		
		board[0][3] = new Queen(-1);
		board[7][3] = new Queen(1);
		board[0][4] = new King(-1);
		board[7][4] = new King(1);
		
		board[0][0].setEngine(this);
		selFigure = null;
		moves = new ArrayList<Move>();
	}

	//Kreira novu igru(Inicijalizuje novu tablu i resetuje ko je na potezu)\\
	public void newGame(){
		initializeBoard();
		onMove = 1;
	}

	public void invertTeam(){
		team *= -1;
	}
	
	//Klonira figuru za potez\\
	private ChessPiece cloneFigure(ChessPiece figure){
		ChessPiece clonedFigure = null;
		
		if(figure instanceof Bishop)
			clonedFigure = new Bishop(figure.getTeam());
		else if(figure instanceof King)
			clonedFigure = new King(figure.getTeam());
		else if(figure instanceof Knight)
			clonedFigure = new Knight(figure.getTeam());
		else if(figure instanceof Pawn)
			clonedFigure = new Pawn(figure.getTeam());
		else if(figure instanceof Queen)
			clonedFigure = new Queen(figure.getTeam());
		else if(figure instanceof Rook)
			clonedFigure = new Rook(figure.getTeam());
		else 
			clonedFigure = null;
		
		return clonedFigure;
	}
	
	//Da li je figura pomerana od pocetka partije\\
	private boolean isMoved(Point figPosition){
		for (int i = 0; i < moves.size(); i++) {
			if(moves.get(i).getFrom().getX() == figPosition.getY() && moves.get(i).getFrom().getY() == figPosition.getY())
				return true;
		}
		return false;
	}

	//Proverava da li moze da se odigra mala rokada, ako ne moze vraca NULL\\
	private Point mozeMalaRokada(){
		Point a = null;
		if(onMove == 1){//Beli igrac
			if(isAttacked(new Point(7, 4)))
				return null;
			if(!isMoved(new Point(7, 4)) && !isMoved(new Point(7, 7))){
				for(int j=5;j<7;j++)
					if(board[7][j]!=null || isAttacked(new Point(7, j)))
						return null;
					
				a = new Point(7, 6);
			}
		} else {//Crni igrac
			if(isAttacked(new Point(0, 4)))
				return null;
			if(!isMoved(new Point(0, 4)) && !isMoved(new Point(0, 7))){
				for(int j=5;j<7;j++)
					if(board[0][j] !=null || isAttacked(new Point(0, j)))
						return null;
					
				a = new Point(0, 6);
			}
		}
		
		return a;
	}

	//Proverava da li moze da se odigra velika rokada, ako ne moze vraca NULL\\
	private Point mozeVelikaRokada(){
		Point a = null;
		if(onMove == 1){
			if(isAttacked(new Point(7, 4)))
				return null;
			if(!isMoved(new Point(7, 4)) && !isMoved(new Point(7, 0))){
				for(int j=1;j<4;j++)
					if(board[7][j]!=null || isAttacked(new Point(7, j)))
						return null;
					
				a = new Point(7, 2);
			}
		} else {
			if(isAttacked(new Point(0, 4)))
				return null;
			if(!isMoved(new Point(0, 4)) && !isMoved(new Point(0, 0))){
				for(int j=1;j<4;j++)
					if(board[0][j] !=null || isAttacked(new Point(0, j)))
						return null;
					
				a = new Point(0, 2);
			}
		}
		
		return a;
	}

	//Proverava da li moze da se odigra potez EnPasant, ako ne moze vraca NULL\\
	private Point mozeEnPassant(int i, int j){
		if ((board[i][j].getTeam() == 1 && i == 3) || (board[i][j].getTeam()==-1 && i == 4)){
			if((j-1 > -1 && board[i][j-1] instanceof Pawn) || (j+1<GameConstants.BOARD_SIZE && board[i][j+1] instanceof Pawn)){
				Point from = getLastMove().getFrom();
				Point to = getLastMove().getTo();
				if(board[to.getX()][to.getY()] instanceof Pawn && Math.abs(from.getX() - to.getX()) == 2)
						if(Math.abs(to.getY()-j)==1)//ako je skocio dva polja i to je pored naseg pijuna
							return	new Point(to.getX()-board[i][j].getTeam(), to.getY());
				
			}
		} 
		return null;
	}
	
	//Fu-ja koja u zavisnosti od polozaja koji su joj prosledjeni izvodi rokadu\\
	private void playRokada(int toi,int toj){
		if(toj==2) {//Velika rokada
			//Top
			board[toi][3] = board[toi][0];
			board[toi][0] = null;
			//kralj
			board[toi][2] = board[toi][4];
			board[toi][4] = null;
			moves.add(new Move(new Point(toi, 0), new Point(toi, 3), new Rook(onMove), new Rook(onMove)));
			moves.add(new Move(new Point(toi, 4), new Point(toi, 2), new King(onMove), new King(onMove)));
		} else if(toj==6){//Mala rokada
			board[toi][5] = board[toi][7];
			board[toi][7] = null;
			board[toi][6] = board[toi][4];
			board[toi][4] = null;
			moves.add(new Move(new Point(toi, 7), new Point(toi, 5), new Rook(onMove), new Rook(onMove)));
			moves.add(new Move(new Point(toi, 4), new Point(toi, 6), new King(onMove), new King(onMove)));
		}
		//Ovde uneti za move kao specijalni potez//
	}

	private void makeMove(Point from,Point to) throws Promotion {
		int fromi = from.getX(),fromj = from.getY();
		int toi = to.getX(), toj = to.getY();
		if (board[fromi][fromj] instanceof King && Math.abs(fromj-toj)==2) {
			playRokada(toi,toj);
		} else {
			//Provera za zamenu, da li je pijun stigao do kraja ;)
			if(board[fromi][fromj] instanceof Pawn){
				if(onMove == 1 && (toi == 0)){
						throw new Promotion();				
				} else if(onMove == -1 && (toi == 7)){
						throw new Promotion();
				}
				else {//Za EnPasant
					Point p = mozeEnPassant(fromi, fromj);
					if(p!=null){
						board[toi+onMove][toj] = null;
					}
				}
			}
			//kraj provere za zamenu
				board[toi][toj] = board[fromi][fromj];
				board[fromi][fromj] = null;
				moves.add(new Move(new Point(fromi, fromj), new Point(toi, toj),cloneFigure(board[fromi][fromj]),cloneFigure(board[toi][toj])));
			
		}
		attackers.clear();
		onMove *= -1;
	}
	
	//Menja figuru kada pijun stigne do kraja table\\
	public void zamena(int i, int j,int figura){
		ChessPiece fig = null;
		switch(figura){
		case GameConstants.FIGURE_BISHOP://Bishop
			fig = new Bishop(onMove);
			break;
		case GameConstants.FIGURE_KNIGHT://Knight
			fig = new Knight(onMove);
			break;
		case GameConstants.FIGURE_QUEEN://queen
			fig = new Queen(onMove);
			break;
		case GameConstants.FIGURE_ROOK://Rook
			fig = new Rook(onMove);
			break;
		}

		board[i][j] = fig;
		board[selFigure.getX()][selFigure.getY()] = null;

		moves.add(new Move(new Point(selFigure.getX(), selFigure.getY()), new Point(i, j), new Pawn(onMove), cloneFigure(board[i][j])));
		attackers.clear();
		onMove *= -1;
	}
	
	public void playMove(Point position) throws Promotion{
		if(selFigure != null)
			makeMove(new Point(selFigure.getX(), selFigure.getY()), position);
	}
	
	public void interpretMove(Move move){
		Point from = move.getFrom();
		Point to = move.getTo();
		ChessPiece toFig = move.getToFig();
		try {
			makeMove(new Point(from.getX(), from.getY()), new Point(to.getX(), to.getY()));
		} catch (Promotion e) {
			if(toFig instanceof Bishop)
				zamena(to.getX(), to.getY(), 1);
			else if(toFig instanceof Knight)
				zamena(to.getX(), to.getY(), 2);
			else if(toFig instanceof Queen)
				zamena(to.getX(), to.getY(), 3);
			else if(toFig instanceof Rook)
				zamena(to.getX(), to.getY(), 4);
			board[from.getX()][from.getY()] = null;
		}
	}
	
	//-----------Geteri---------------\\
	
	//Vraca celu tablu\\
	public ChessPiece[][] getBoard(){
		return board;
	}
	
	//Daje sliku figure cije su koordinate prosledjene
	public Image getFigureImage(int i,int j){
		if(board[i][j] != null)
			return (new Image(board[i][j].getImage()));
		else 
			return null;
	}
	
	//Daje ko je na potezu ( 1 - beli igrac | -1 crni igrac )\\
	public int getOnMove(){
		if(onMove == 1)
			return GameConstants.TEAM_WHITE;
		else 
			return GameConstants.TEAM_BLACK;
	}
	
	//Daje zadnje odigrani potez (zbog slanja serveru)\\
	public Move getLastMove(){
		return moves.get(moves.size()-1);
	}
	
	//Vraca tip igre (botovi | online)\\
	public int getTypeOfGame() {
		return typeOfGame;
	}

	//Vraca da li je igrac beli | crni \\
	public int getTeam() {
		if(team == 1)
			return GameConstants.TEAM_WHITE;
		else 
			return GameConstants.TEAM_BLACK;
	}
	
	public Bot getBot() {
		return bot;
	}
	
	//Vraca putanjom kojom figura napada drugu figuru\\
	private List<Point> getAttackingPath(Point attackerPosition,Point figurePosition){
		//Put kojim figura napada kralja
		List<Point> attackPath = new ArrayList<>();
		//Dodajemo poziciju napadaca
		attackPath.add(attackerPosition);
		//Ako nije konj oonda ima jos neko polje na koje moze da se stane, da se blokira sah
		if(!(board[attackerPosition.getX()][attackerPosition.getY()] instanceof Knight)){
			if(attackerPosition.getX()==figurePosition.getX()){//Napada Horizontalno
				if(attackerPosition.getY()<figurePosition.getY()){//napada sa leva na desno 
					for (int i = attackerPosition.getY(); i < figurePosition.getY(); i++) {
						attackPath.add(new Point(attackerPosition.getX(), i));
					}
				} else {//napada sa desna na levo
					for (int i = figurePosition.getY(); i < attackerPosition.getY(); i++) {
						attackPath.add(new Point(attackerPosition.getX(), i));
					}
				}
			} else if(attackerPosition.getY()==figurePosition.getY()){//Napada Vertikalno
				if(attackerPosition.getX()<figurePosition.getX()){//Napada na dole
					for (int i = attackerPosition.getX(); i < figurePosition.getX(); i++) {
						attackPath.add(new Point(i, attackerPosition.getY()));
					}
				} else {//Napada na gore
					for (int i = figurePosition.getX()+1; i < attackerPosition.getX(); i++) {
						attackPath.add(new Point(i, attackerPosition.getY()));
					}
				}
			} else {//Napada Dijagnoalno
				if(attackerPosition.getX()<figurePosition.getX()){//Napada na dole				
					if(attackerPosition.getY()<figurePosition.getY()){
						for (int i = 0; i < (figurePosition.getY()-attackerPosition.getY()); i++) {//Napada Desno 
							attackPath.add(new Point(attackerPosition.getX()+i, attackerPosition.getY()+i));
						}
					} else {
						for (int i = 0; i < (attackerPosition.getY() - figurePosition.getY()); i++) {//Napada Levo
							attackPath.add(new Point(attackerPosition.getX()+i, attackerPosition.getY()-i));
						}
					}
				} else {//Napada na gore
					if(attackerPosition.getY()<figurePosition.getY()){
						for (int i = 0; i < (figurePosition.getY()-attackerPosition.getY()); i++) {//Napada Desno
							attackPath.add(new Point(attackerPosition.getX()-i, attackerPosition.getY()+i));
						}
					} else {
						for (int i = 0; i < (attackerPosition.getY()-figurePosition.getY()); i++) {//Napada Levo
							attackPath.add(new Point(attackerPosition.getX()-i, attackerPosition.getY()-i));
						}
					}
				}
			}
		}
		return attackPath;
	}
		
	//Proverava da li bi se figurinim pomeranjem stvorio sah\\
	private boolean proveri(Point kingPos,Point sPos, Point nPos,List<Point> protivFigure){
		boolean ind = false;
		//Privremeno pomera figuru
		ChessPiece fig = board[nPos.getX()][nPos.getY()];
		board[nPos.getX()][nPos.getY()] = board[sPos.getX()][sPos.getY()];
		board[sPos.getX()][sPos.getY()] = null;
		//Ako kralj nema napadaca posle pomeranja figure, figura moze da stane na to mesto
		if(getAttackers(kingPos,protivFigure).size() == 0) 
			ind = true;
		//Vracamo figuru na staru poziciju
		board[sPos.getX()][sPos.getY()] = board[nPos.getX()][nPos.getY()];
		board[nPos.getX()][nPos.getY()] = fig;	
		return ind;
	}
	
	//Uzima sve figure koje napadaju prosledjenu figuru\\
	private List<Point> getAttackers(Point figurePostition,List<Point> protivFigure){
		//Lista figura koje napadaju
		List<Point> attackers = new ArrayList<Point>();	
		//provera da li je na udaru neke figure i ako jeste uzmi njenu poziciju
		//Na udaru je ako se nalazi u listi pozicija na koje figura moze da skoci (figre.possibleMoves(i,j);)
		for (int i = 0; i < protivFigure.size(); i++) {
			if(Point.exists(figurePostition,board[protivFigure.get(i).getX()][protivFigure.get(i).getY()].possibleMoves(protivFigure.get(i).getX(),protivFigure.get(i).getY())))
				attackers.add(new Point(protivFigure.get(i).getX(),protivFigure.get(i).getY()));
		}
		return attackers;
	}
	
	//Daje sve figure koje igrac moze da pomera, u zavisnosti da li je sah ili nije\\
	public List<Point> getMovableFigures() throws Draw, Checkmate{
		if((typeOfGame == GAME_MODE_ONLINE && onMove == team) || typeOfGame != GAME_MODE_ONLINE){
			//Pozicija kralja trenutnog igraca
			Point king = new Point(0, 0);
			//Sve figure trenutnog igraca (sem kralja)
			List<Point> mojeFigure = new ArrayList<>();
			//Protivnicke figure
			List<Point> protivFigure = new ArrayList<>();
			//Raspored Figura
			for (int i = 0; i < GameConstants.BOARD_SIZE; i++) 
				for (int j = 0; j < GameConstants.BOARD_SIZE; j++) 
					if(board[i][j] != null){
						if(board[i][j].getTeam()==onMove){
							if(board[i][j] instanceof King )
								king = new Point(i,j);
							else 
								mojeFigure.add(new Point(i, j));
						} else 
							protivFigure.add(new Point(i, j));
					}
			
			attackers = new ArrayList<Point>();
			//Uzimamo napadace
			attackers = getAttackers(king,protivFigure);
			//Kreira list za figure koje igrac moze da pomera
			movFigures = new ArrayList<Point>();
			//Ako oba igraca imaju samo kraljeve, onda je nereseno
			if(mojeFigure.size()==0 && protivFigure.size() == 1){
				throw new Draw();
			}
			//Da li kralj moze da se pomera
			if(board[king.getX()][king.getY()].possibleMoves(king.getX(), king.getY()).size()>0){
				//Dodajemo kralja jer ima gde da se pomeri
				movFigures.add(new Point(king.getX(), king.getY()));
			} else {
				//Ako se desi da kralj ne moze da se pomeri i ima vise napadaca, onda je automatski sahmat
				if(attackers.size()>1)
					throw new Checkmate();
			}
			//Ako kralja napada figura
			//proveri da li moze neka da se ispreci ili pojede tu figuru
			if(attackers.size()==1){
				//Uzima putanju kojom figura napada kralja
				List<Point> attackingPath = getAttackingPath(attackers.get(0), king);
				//pomocna promenljiva za cuvanje mogucih poteza figure u for petlji
				List<Point> posMov;
				for (int i = 0; i < mojeFigure.size(); i++) {
					//Uzimam sve moguce pozicije na koje igraceva figura moze da stane
					posMov = board[mojeFigure.get(i).getX()][mojeFigure.get(i).getY()].possibleMoves(mojeFigure.get(i).getX(), mojeFigure.get(i).getY());
					//Dodajem samo figure koje mogu da se isprece, ili pojedu figuru koja napada kralja
					for (int j = 0; j < attackingPath.size(); j++) {
						//Ako se neka od pozicija putanje napada nalazi u nizu mogucih poteza figure
						//dodaj figuru u listu mogucih figura
						if(Point.exists(attackingPath.get(j), posMov)){
							movFigures.add(new Point(mojeFigure.get(i).getX(), mojeFigure.get(i).getY()));
							break;
						}	
					}
				}
				//Ako kralj nema gde da se pomeri, i ni jedna druga figura ne moze da otkloni sah
				//baca se izuzetak Checkmate koji oznacava sahmat
				if(board[king.getX()][king.getY()].possibleMoves(king.getX(), king.getY()).size()==0 && movFigures.size()==0)
						throw new Checkmate();
			
			} else if(attackers.size()==0){//Ako kralj nije napadnut
				//Dodaj samo figure koje mogu da se pomeraju, a njihovim pomeranjem ne moze da se ugrozi kralj
				
				for (int i = 0; i < mojeFigure.size(); i++) {
					List<Point> pos = board[mojeFigure.get(i).getX()][mojeFigure.get(i).getY()].possibleMoves(mojeFigure.get(i).getX(), mojeFigure.get(i).getY());
					if(pos.size()>0){
						//Ako bar jedno pomeranje ne izaziva sah, dodaj figuru u listu pomerajucih figura
						for (int j = 0; j < pos.size(); j++) {
							if(proveri(king, new Point(mojeFigure.get(i).getX(),mojeFigure.get(i).getY()), pos.get(j),protivFigure)){
									movFigures.add(new Point(mojeFigure.get(i).getX(),mojeFigure.get(i).getY()));
									break;
							}
						}
					}
				}
				
				//Ako kralj nema gde da se pomeri i ni jedna figura ne moze da se pomeri, onda je nereseno
				if(board[king.getX()][king.getY()].possibleMoves(king.getX(), king.getY()).size()==0 && movFigures.size()==0)
						throw new Draw();
				
			} else {//ako ga napada vise od jedne figure
				if(movFigures.size()==0)//Ako kralj ne moze da se pomeri shamat je
					throw new Checkmate();
			}
			return movFigures;
		} else //vraca praznu listu ukoliko se pozove ova funkcija, a nije red na igraca koji je pozvao funkciju(klikom na svoju figuru)
			return new ArrayList<Point>();
	}
		
	//Daje gde moze da skoci figura cije su koordinate prosledjene\\
	public List<Point> getPossibleMoves(int i, int j){
		if((typeOfGame == GAME_MODE_ONLINE && onMove == team) || typeOfGame != GAME_MODE_ONLINE){
			if(board[i][j] != null && board[i][j].getTeam() == onMove){
				//Izabiramo figuru igraca koji je na potezu i vracamo njene moguce poteze
				selFigure = new Point(i, j);
				//Lista polja na koje figura moze da skoci
				List<Point> lista = board[i][j].possibleMoves(i, j);
				//Kreiramo listu koja ce sadrzati validne poteze figure
				List<Point> possibleMovies = new ArrayList<Point>(); 
				
				if(board[i][j] instanceof King){
					//Ako smo kliknuli na kralja, samo uzimamo njegove moguce poteze i dodajemo rokade ako su moguce
					Point malaRokada = mozeMalaRokada();
					if(malaRokada!=null)
						lista.add(malaRokada);
					
					Point velikaRokada = mozeVelikaRokada();
					if(velikaRokada!=null)
						lista.add(velikaRokada);
					
					return lista;
				} else {//Ako je odabrana druga figura uzeti njene poteze
					
					if(board[i][j] instanceof Pawn){//Ako je figura pijun proveravamo da li moze da odigra potez enpasant
						Point p = mozeEnPassant(i, j);
						//Aako metod vrati neku poziciju dodaj je na listu mogucih poteza 
						if(p!=null)
							lista.add(p);
					}
					
					//Uzimamo mesto kralja i protivnicke figure
					Point king = null;
					List<Point> protivFigure = new ArrayList<>();
					for (int k = 0; k < GameConstants.BOARD_SIZE; k++) {
						for (int k2 = 0; k2 < GameConstants.BOARD_SIZE; k2++) {
							if(board[k][k2] != null && board[k][k2].getTeam()==onMove && board[k][k2] instanceof King){
								king = new Point(k, k2);
							} else if(board[k][k2] != null && board[k][k2].getTeam()!=onMove)
								protivFigure.add(new Point(k, k2));
						}
					}
					
					if(attackers != null && attackers.size()==1) {//Ako je kralj napadnut, daj moguce poteze koji ce ga spasiti
						//Uzimamo put kojim je kralj napadnut
						List<Point> path = getAttackingPath(attackers.get(0), king);
						//proci kroz sve moguce poteze figure i odabrati samo one kojei se nalaze na putanji napada
						//a da pritom pomeranjem figure na to mesto ne bi ugrozilo kralja sa druge strane
						for (int k = 0; k < path.size(); k++) {
							if(Point.exists(path.get(k),lista) && proveri(king,new Point(i, j),path.get(k),protivFigure))
								possibleMovies.add(path.get(k));
						}
					} else { //Ako kralj nije napadnut samo dodaj poteze koji ga nece ugroziti
						//Proci kroz sve poteze figure i odabrati one koje ne ugrozavaju kralja
						for (int k = 0; k < lista.size(); k++) {
							if(proveri(king, new Point(i, j),lista.get(k) , protivFigure))
								possibleMovies.add(lista.get(k));
						}
					}
					
					return possibleMovies;											
				}
			} else //igrac nije odabrao figuru, ili je odabrao protivnicku figuru, koristi se za pomeranje figura
				return null;
		} else//Nije red na igraca koji je pozvao metodu 
			return new ArrayList<Point>();
	}
	
	/**
	 * Provetrava da li je prosledjena pozicija u okviru table
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean inTable(Point point){
		if((point.getX() > -1 && point.getX() < GameConstants.BOARD_SIZE) && (point.getY() > -1 && point.getY() < GameConstants.BOARD_SIZE))
			return true;
		return false;
	}
	
	public boolean inTable(int x,int y){
		if((x > -1 && x < GameConstants.BOARD_SIZE) && (y > -1 && y < GameConstants.BOARD_SIZE))
			return true;
		return false;
	}
	
	public boolean validPosition(Point point){
		int x = point.getX(), y = point.getY();
		if(inTable(point) && (board[x][y] == null || (board[x][y] != null && board[x][y].getTeam() != onMove)))
			return true;
		return false;
	}
	
	public boolean validPosition(int x, int y){
		if(inTable(x,y) && (board[x][y] == null || (x < GameConstants.BOARD_SIZE && board[x][y] != null && board[x][y].getTeam() != onMove)))
			return true;
		return false;
	}
	
	public Point getKingsPos(int team){
		for(int i=0;i<GameConstants.BOARD_SIZE;i++)
			for(int j=0; j<GameConstants.BOARD_SIZE;j++)
				if(board[i][j] instanceof King && board[i][j].getTeam() == team)
					return new Point(i,j);
		return new Point(-1,-1);
	}
	
	private boolean isAttacked(Point poistion){
		for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
			for (int j = 0; j < GameConstants.BOARD_SIZE; j++) {
				if(board[i][j] != null && board[i][j].getTeam() != onMove && Point.exists(poistion,board[i][j].possibleMoves(i, j)))
					return true;
			}
		}		
		return false;
	}
	
	//Da li polju preti neka figura\\
	public boolean isAttackedKing(Point position){
		int x = position.getX(), y = position.getY();
		boolean ind = false;
		
		//Proverava za sve pravce
		for (Directions direction : Directions.getDirections()) {
			ind = ind || checkDirection(x, y, direction);
		}
		
		// TODO => Ovaj deo mozda uradimmalo lepse
		if(onMove == TEAM_WHITE){
			if((validPosition(x-1, y-1) && board[x-1][y-1] instanceof Pawn)
				|| (validPosition(x-1,y+1) && board[x-1][y+1] instanceof Pawn)){
				return true;	
			}
			
		} else {
			if((validPosition(x+1, y-1) && board[x+1][y-1] instanceof Pawn)
				|| (validPosition(x+1, y+1) && board[x+1][y+1] instanceof Pawn))
			return true;
		}
		
		// Provera za konja
		List<Point> list = Knight.generateCheckPoints(x, y);
		for (Point point : list) {
			if(inTable(point) && board[point.getX()][point.getY()] instanceof Knight && board[point.getX()][point.getY()].getTeam()!=onMove)
				return true;
		}
		
		//Provera za kralja
		list = King.generateCheckPoints(x, y);
		for (Point point : list) {
			if(inTable(point) && board[point.getX()][point.getY()] instanceof King && board[point.getX()][point.getY()].getTeam()!=onMove)
				return true;
		}
		return ind;
	}
	
	private boolean checkDirection(int x,int y, Directions direction){
		ChessPiece piece;
		int inc_x = direction.getX(), inc_y = direction.getY(); 
		x += inc_x; y += inc_y; // uzima prvu poziciju setnje
		while(inTable(new Point(x,y))) {
			piece = board[x][y];
			if(piece != null) { // Ako je naisao na figuru
				if(piece.getTeam()!=onMove){
					switch (direction) {
					// Prvo provere za figure koje idu dijagonalno
					case UP_LEFT:
					case UP_RIGHT:
					case DOWN_LEFT:
					case DOWN_RIGHT:
						if(piece instanceof Queen || piece instanceof Bishop)
							return true;
						break;
					//Figure koje idu pravo
					case UP:
					case DOWN:
					case LEFT:
					case RIGHT:
						if(piece instanceof Queen || piece instanceof Rook)
							return true;
						break;
					}
					
				} 
				// Provera za kralja
				break;
			}
			x += inc_x; y += inc_y; // pomera na sledecu poziciju setnje
		}
		return false;
	}
}