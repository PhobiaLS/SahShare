package server;

import game.GameConstants;

















import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;







import serverMessages.FriendInformation;
import serverMessages.FriendLogout;
import serverMessages.GameAccepted;
import serverMessages.LoginRequestAccepted;
import serverMessages.LoginRequestDenied;
import serverMessages.OnlineFriends;
import serverMessages.OnlinePlayersNumber;
import serverMessages.PlayerRatingUpdate;
import serverMessages.PlayerSearchEnded;
import serverMessages.PlayerSearchInformation;
import serverMessages.ShowRematchDialog;
import serverMessages.SignUpRequestAccepted;
import serverMessages.SignUpRequestDenied;
import serverMessages.StartANewGame;
import clientMessages.AddOpponentAsFriend;
import clientMessages.AnswerRematch;
import clientMessages.ChatMessage;
import clientMessages.CloseThreadAndSocket;
import clientMessages.GameAccept;
import clientMessages.GameDeclined;
import clientMessages.GameEnd;
import clientMessages.GameRequest;
import clientMessages.GameSessionEnded;
import clientMessages.InformationSearchGames;
import clientMessages.LoginRequest;
import clientMessages.OnlineFriendsMessage;
import clientMessages.PlayerMove;
import clientMessages.SearchForGame;
import clientMessages.SearchForGameCancel;
import clientMessages.SignOutNotification;
import clientMessages.SignUpRequest;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application implements GameConstants {
	private static ArrayList<UserConnection> connections = new ArrayList<>();
	private static ArrayList<Game> games = new ArrayList<>();
	private static ArrayList<RematchConformation> rematchConformation = new ArrayList<>();
	private static ServerSocket serverSocket;
	private Connection connection;
	private static ArrayList<GameSearch> gameSearch = new ArrayList<>();
	public static int onlinePlayers;
	public TextArea taLog;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		taLog = new TextArea();
		
		Scene scene = new Scene(new ScrollPane(taLog), 450, 200);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		new Thread(() -> {
			try {
				serverSocket = new ServerSocket(8000);
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost/chessbase","chessUsername","chessPassword");
				while (true) {
					Socket connectedPlayer = serverSocket.accept();
					InetAddress address = connectedPlayer.getInetAddress();
					Platform.runLater(() -> {
						taLog.appendText(address.getHostAddress() + " connected to server\n");
					});
					GuestConnection guestConnection = new GuestConnection(connectedPlayer, new ObjectOutputStream(connectedPlayer.getOutputStream()), 
							new ObjectInputStream(connectedPlayer.getInputStream()));
					new Thread(new HandleAConnection(guestConnection)).start();
				}		
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		
	}
	
	public static void main(String[] args) { 
		Application.launch(args);
	}
		
	class HandleAConnection implements Runnable {
		private GuestConnection guestConnection;
		private PreparedStatement preparedStatement;
		private UserConnection userConnection;
		private String queryLogin = "select * from user where ? = user.userName and ? = user.userPassword";
		private String querrySignUp = "insert into user(userName, userPassword, userEmail) values (?, ?, ?)";
		private String updateUserRating = "update user set userRating = userRating + ? where user.userName = ?";
		private String getPlayerRatingQuery = "select userRating from user where user.userName = ?";
		private String insertFriendsQuery = "insert into friends(idFriend1, idFriend2) values(?, ?)";
		private String queryFriends = "select * from friends where (idFriend1 = ? and idFriend2 = ?) or (idFriend1 = ? and idFriend2 = ?)";
		private String showPlayerInformation = "select userName, userRating from user where idUser = ?";
		private String showFriendsQuery = "select * from friends where idFriend1 = ? or idFriend2 = ?";
		private String getUsernameQuery = "select userName from user where user.idUser = ?";
		private boolean runAThread = true;
		
		public HandleAConnection(GuestConnection guestConnection) {
			this.guestConnection = guestConnection;
		}
		@Override
		public void run() {
			while (runAThread) {
				try {
					Object object = guestConnection.getFromGuest().readObject();
					if (object instanceof LoginRequest) {
						LoginRequest loginRequest = (LoginRequest) object;
						try {
							preparedStatement = connection.prepareStatement(queryLogin);
							preparedStatement.setString(1, loginRequest.getUsername());
							preparedStatement.setString(2, loginRequest.getPassword());
							
							ResultSet resSet = preparedStatement.executeQuery();
							if (resSet.next()) {
								int id = Integer.parseInt(resSet.getString(1));
								String username = resSet.getString(2);
								int rating = Integer.parseInt(resSet.getString(4));
								String avatar = resSet.getString(6);
								
								Account account = new Account(id, username, rating, avatar);
								
								Platform.runLater(() -> {
									taLog.appendText(username + " with id " + id + " logged in\n");
								});
								
								guestConnection.getToGuest().writeObject(new LoginRequestAccepted(account));
								
								UserConnection userConn = new UserConnection(id, username, guestConnection.getSocket(), guestConnection.getToGuest(), guestConnection.getFromGuest());
								userConnection = new UserConnection(id, username, guestConnection.getSocket(), guestConnection.getToGuest(), guestConnection.getFromGuest());
								connections.add(userConn);
								
								onlinePlayers++;
								
								for (int i = 0; i < connections.size(); i++) {
									connections.get(i).getToClient().writeObject(new OnlinePlayersNumber(onlinePlayers));
								}
								
								
								preparedStatement = connection.prepareStatement(showFriendsQuery);
								preparedStatement.setInt(1, account.getId());
								preparedStatement.setInt(2, account.getId());
								
								ResultSet resSetKome = preparedStatement.executeQuery();
								while (resSetKome.next()) {
									int id1 = resSetKome.getInt(1);
									int id2 = resSetKome.getInt(2);
									int friendId;
									if (id1 == account.getId())
										friendId = id2;
									else
										friendId = id1;
									
									
									for (int g = 0; g < connections.size(); g++)
										if (connections.get(g).getId() == friendId) {
											connections.get(g).getToClient().writeObject(new FriendInformation(account.getId(), account.getUsername(), account.getRating()));
										}
									
								}
								
								
								break;
							}
							else {
								guestConnection.getToGuest().writeObject(new LoginRequestDenied());
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					if (object instanceof SignUpRequest) {
						SignUpRequest signUpRequest = (SignUpRequest) object;
						try {
							preparedStatement = connection.prepareStatement(queryLogin);
							preparedStatement.setString(1, signUpRequest.getUsername());
							preparedStatement.setString(2, signUpRequest.getPassword());
							
							ResultSet rSet = preparedStatement.executeQuery();
							
							if (rSet.next()) {
								guestConnection.getToGuest().writeObject(new SignUpRequestDenied("I will do this later"));
							}
							else {
								preparedStatement = connection.prepareStatement(querrySignUp);
								preparedStatement.setString(1, signUpRequest.getUsername());
								preparedStatement.setString(2, signUpRequest.getPassword());
								preparedStatement.setString(3, signUpRequest.getEmail());
								
								preparedStatement.executeUpdate();
								
								Platform.runLater(() -> {
									taLog.appendText("Account with " + signUpRequest.getUsername() + " was just created\n");
								});
								
								guestConnection.getToGuest().writeObject(new SignUpRequestAccepted());
							}
							
							
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				// OVde nastavljamo
				
			while (runAThread) {
					try {
						Object object = userConnection.getFromClient().readObject();
					
						if (object instanceof PlayerMove) {
							PlayerMove pMove = (PlayerMove) object;
							for (int i = 0; i < games.size(); i++) {
								if (games.get(i).getUser1().getId() == pMove.getClientID() || games.get(i).getUser2().getId() == pMove.getClientID()) {
									if (games.get(i).getUser1().getId() == pMove.getClientID()) {
										games.get(i).getUser2().getToClient().writeObject(pMove);
									}
									else {
										games.get(i).getUser1().getToClient().writeObject(pMove);
									}
								}
							}
						}
						else if (object instanceof ChatMessage) {
							ChatMessage chatMessage = (ChatMessage) object;
							if (chatMessage.getType() == IN_GAME_CHAT) {
								for (int i = 0; i < games.size(); i++) {
									if (games.get(i).getUser1().getId() == chatMessage.getClientID()) {
										games.get(i).getUser2().getToClient().writeObject(chatMessage);
									}
									else if (games.get(i).getUser2().getId() == chatMessage.getClientID()) {
										games.get(i).getUser1().getToClient().writeObject(chatMessage);
									}
								}
							}
							 
						}
						else if (object instanceof SearchForGame) {
							SearchForGame search = (SearchForGame) object;
					
							
							// Automatski spoji sa nekim
							
							
							GameSearch gSearch = new GameSearch(search.getClientID(), search.getUsername(), search.getRating(), search.getGameTime());
							gameSearch.add(gSearch);
							PlayerSearchInformation pSInformation = new PlayerSearchInformation(search.getClientID(), search.getUsername(), search.getRating(), search.getGameTime());
							
							for (int i = 0; i < connections.size(); i++) {
								connections.get(i).getToClient().writeObject(pSInformation);
							}
							
						}
						else if (object instanceof SignOutNotification) {
							SignOutNotification signOut = (SignOutNotification) object;
							for (int i = 0; i < connections.size(); i++) {
								if (signOut.getClientID() == connections.get(i).getId()) {
									connections.remove(i);
									onlinePlayers--;
								
								}
							}
							
							String userN = null;
							
							try {
								preparedStatement = connection.prepareStatement(getUsernameQuery);
								preparedStatement.setInt(1, signOut.getClientID());
								
								ResultSet resSet = preparedStatement.executeQuery();
								
								if (resSet.next()) {
									userN = resSet.getString(1);
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
							
							for (int i = 0; i < connections.size(); i++) {
								connections.get(i).getToClient().writeObject(new OnlinePlayersNumber(onlinePlayers));
								connections.get(i).getToClient().writeObject(new FriendLogout(new Friend(signOut.getClientID(), userN)));
							}
							
						}
						else if (object instanceof GameRequest) {
							GameRequest gRequest = (GameRequest) object;
							Game game = new Game(null, null);
							
							for (int i = 0; i < connections.size(); i++) {
								if (gRequest.getOponentId() == connections.get(i).getId()) {
									connections.get(i).getToClient().writeObject(gRequest);
								}
								if (gRequest.getClientID() == connections.get(i).getId()) {
									game.setUser1(connections.get(i));
								}
							}
							games.add(game);
						}
						else if (object instanceof GameAccept) {
							GameAccept gameAcc = (GameAccept) object;
							for (int i = 0; i < games.size(); i++) {
								if (games.get(i).getUser1().getId() == gameAcc.getOponentId()) {
									for (int j = 0; j < connections.size(); j++) {
										if (connections.get(j).getId() == gameAcc.getClientID()) {
											games.get(i).setUser2(connections.get(j));
											int rating = 0;
											try {
												preparedStatement = connection.prepareStatement(getPlayerRatingQuery);
												preparedStatement.setString(1, games.get(i).getUser2().getUsername());
												
												ResultSet resSet = preparedStatement.executeQuery();
												if (resSet.next()) {
													rating = resSet.getInt(1);
												}
											} catch (SQLException e) {
												e.printStackTrace();
											}
											
											
											
											games.get(i).getUser1().getToClient().writeObject(new GameAccepted(games.get(i).getUser2().getUsername(), rating));
											
											// Posalji da ovaj igrac ne trazi vise igru
											
											for (int g = 0; g < connections.size(); g++) {
												connections.get(g).getToClient().writeObject(new PlayerSearchEnded(games.get(i).getUser1().getId()));
												connections.get(g).getToClient().writeObject(new PlayerSearchEnded(games.get(i).getUser2().getId()));
											}
																		
											
											for (int g = 0; g < gameSearch.size(); g++) {
												if (gameSearch.get(g).getPlayerId() == games.get(i).getUser1().getId())
													gameSearch.remove(g);
											}
											for (int g = 0; g < gameSearch.size(); g++) {
												if (gameSearch.get(g).getPlayerId() == games.get(i).getUser2().getId())
													gameSearch.remove(g);
											}
										}
									}
								}
							}
							
							
							
						}
						else if (object instanceof GameDeclined) {
							GameDeclined gameDec = (GameDeclined) object;
							for (int i = 0; i < games.size(); i++) {
								if (games.get(i).getUser1().getId() == gameDec.getOponentId()) {
									games.remove(i);
									break;
								}
							}
						}
						else if (object instanceof SearchForGameCancel) {
							SearchForGameCancel sCancel = (SearchForGameCancel) object;
							for (int g = 0; g < connections.size(); g++) {
								connections.get(g).getToClient().writeObject(new PlayerSearchEnded(sCancel.getClientID()));
							}
							for (int g = 0; g <gameSearch.size(); g++) {
								if (gameSearch.get(g).getPlayerId() == sCancel.getClientID())
									gameSearch.remove(g);
							}
						}
						else if (object instanceof GameSessionEnded) {
							GameSessionEnded gameSE = (GameSessionEnded) object;
							
							//IZRACUNAJ 
							//AZURIRAJ BAZU
							int user1Id = 0;
							int user2Id = 0;
							for (int i = 0; i < games.size(); i++) {
								if (games.get(i).getUser1().getId() == gameSE.getClientID()) {
									user1Id = games.get(i).getUser1().getId();
									user2Id = games.get(i).getUser2().getId();
								}
								if (games.get(i).getUser2().getId() == gameSE.getClientID()) {
									user1Id = games.get(i).getUser2().getId();
									user2Id = games.get(i).getUser1().getId();
								}
							}
							
							
							
							// POSALJI SUPROTNOM DA SE ZAVRSILA IGRA
							for (int i = 0; i < games.size(); i++) {
								if (games.get(i).getUser1().getId() == gameSE.getClientID() || games.get(i).getUser2().getId() == gameSE.getClientID()) {
									if (games.get(i).getUser1().getId() == gameSE.getClientID()) {
										games.get(i).getUser2().getToClient().writeObject(gameSE);
									}
									else {
										games.get(i).getUser1().getToClient().writeObject(gameSE);
									}
								}
							}
							
							//IZBACI IGRU
							for (int i = 0; i < games.size(); i++) {
								if ((games.get(i).getUser1().getId() == user1Id && games.get(i).getUser2().getId() == user2Id)
									|| (games.get(i).getUser1().getId() == user2Id && games.get(i).getUser2().getId() == user1Id)) {
									games.remove(i);
								}
							}
						}
						else if (object instanceof InformationSearchGames) {
							InformationSearchGames info = (InformationSearchGames) object;
							for (int i = 0; i < gameSearch.size(); i++) {
								PlayerSearchInformation psi = new PlayerSearchInformation(gameSearch.get(i).getPlayerId(),
									gameSearch.get(i).getPlayerUsername(), gameSearch.get(i).getPlayerRating(), gameSearch.get(i).getGameTime());
								for (int g = 0; g < connections.size(); g++) {
									if (connections.get(g).getId() == info.getClientID()) {
										connections.get(g).getToClient().writeObject(psi);
									}
								}
							}
							
						}
						else if (object instanceof GameEnd) {
							GameEnd gameEnd = (GameEnd) object;

							//IZRACUNAJ 
							//AZURIRAJ BAZU
							String userName1 = null; // username1 umanjujemo poene
							int user1Id = 0;
							String userName2 = null; // ovom uvecavamo
							int user2Id = 0;
							for (int i = 0; i < games.size(); i++) {
								if (games.get(i).getUser1().getId() == gameEnd.getClientID()) {
									userName1 = games.get(i).getUser1().getUsername();
									user1Id = games.get(i).getUser1().getId();
									userName2 = games.get(i).getUser2().getUsername();
									user2Id = games.get(i).getUser2().getId();
									if (gameEnd.getCauseOfEnd() == GAME_LOST)
										games.get(i).getUser2().getToClient().writeObject(new ShowRematchDialog(GAME_LOST));
									else
										games.get(i).getUser2().getToClient().writeObject(new ShowRematchDialog(GAME_DRAW));
								}
								if (games.get(i).getUser2().getId() == gameEnd.getClientID()) {
									userName1 = games.get(i).getUser2().getUsername();
									user1Id = games.get(i).getUser2().getId();
									userName2 = games.get(i).getUser1().getUsername();
									user2Id = games.get(i).getUser1().getId();
									if (gameEnd.getCauseOfEnd() == GAME_LOST)
										games.get(i).getUser1().getToClient().writeObject(new ShowRematchDialog(GAME_LOST));
									else
										games.get(i).getUser1().getToClient().writeObject(new ShowRematchDialog(GAME_DRAW));
								}
							}
							
							//KREIRAJ STURKTURU REMATCH CONFORMATION
							
							rematchConformation.add(new RematchConformation(user1Id, user2Id));
							
							if (gameEnd.getCauseOfEnd() == GAME_LOST) {	
								try {
									preparedStatement = connection.prepareStatement(updateUserRating);
									preparedStatement.setInt(1, -25);
									preparedStatement.setString(2, userName1);
									preparedStatement.executeUpdate();
									
									preparedStatement = connection.prepareStatement(updateUserRating);
									preparedStatement.setInt(1, 25);
									preparedStatement.setString(2, userName2);
									preparedStatement.executeUpdate();
									
									
									
								} catch (SQLException e) {
									e.printStackTrace();
								}
								
								// POSALJI IM NOVE RATINGE
								try {
									preparedStatement = connection.prepareStatement(getPlayerRatingQuery);
									preparedStatement.setString(1, userName1);
									
									ResultSet resSet = preparedStatement.executeQuery();
									if (resSet.next()) {
										int rating = resSet.getInt(1);
										for (int i = 0; i < connections.size(); i++)
											if (connections.get(i).getId() == user1Id) {
												connections.get(i).getToClient().writeObject(new PlayerRatingUpdate(user1Id, rating));
											}
									}
									
									preparedStatement = connection.prepareStatement(getPlayerRatingQuery);
									preparedStatement.setString(1, userName2);
									
									resSet = preparedStatement.executeQuery();
									if (resSet.next()) {
										int rating = resSet.getInt(1);
										for (int i = 0; i < connections.size(); i++)
											if (connections.get(i).getId() == user2Id) {
												connections.get(i).getToClient().writeObject(new PlayerRatingUpdate(user2Id, rating));
											}
									}
									
								} catch (SQLException e) {
									e.printStackTrace();
								}
								
							}
						}
						else if (object instanceof AnswerRematch) {
							AnswerRematch ans = (AnswerRematch) object;
							for (int i = 0; i < rematchConformation.size(); i++) {
								if (ans.getClientID() == rematchConformation.get(i).getUserId1() || 
										ans.getClientID() == rematchConformation.get(i).getUserId2()) {
									if (ans.getClientID() == rematchConformation.get(i).getUserId1()) {
										rematchConformation.get(i).setAnswer1(ans);
										if (ans.isAnswer()) {
											if (rematchConformation.get(i).getAnswer2() != null && rematchConformation.get(i).getAnswer2().isAnswer()) { //
												// E DRUGI JE ODGOVORIO DA ZELI IGRU
												for (int l = 0; l < games.size(); l++) {
													if (games.get(l).getUser1().getId() == ans.getClientID() || games.get(l).getUser2().getId() == ans.getClientID()) {
														// ZAPOCNITE NOVU IGRU
														games.get(l).getUser1().getToClient().writeObject(new StartANewGame());
														games.get(l).getUser2().getToClient().writeObject(new StartANewGame());
													}
												}
											}
										}
										else { // POSALJI DRUGOM DA JE KRAJ PARTIJE KLIKNO SAM NO
											rematchConformation.remove(i);
											for (int g = 0; g < connections.size(); g++) {
												if (connections.get(g).getId() == rematchConformation.get(i).getUserId2()) { 
													// POSALJI DRUGOM DA SAM JA REKAO NE ZELIM NOVU IGRU
													connections.get(g).getToClient().writeObject(new GameSessionEnded(ans.getClientID()));
												}
											}
										}
									}
									else {
										rematchConformation.get(i).setAnswer2(ans);
										if (ans.isAnswer()) {
											if (rematchConformation.get(i).getAnswer1() != null && rematchConformation.get(i).getAnswer1().isAnswer()) {
												// E DRUGI JE ODGOVORIO DA ZELI IGRU
												for (int l = 0; l < games.size(); l++) {
													if (games.get(l).getUser1().getId() == ans.getClientID() || games.get(l).getUser2().getId() == ans.getClientID()) {
														// ZAPOCNITE NOVU IGRU
														games.get(l).getUser1().getToClient().writeObject(new StartANewGame());
														games.get(l).getUser2().getToClient().writeObject(new StartANewGame());
													}
												}
											}
										}
										else { // POSALJI DRUGOM DA JE KRAJ PARTIJE KLIKNUO SAM NO
											rematchConformation.remove(i);
											for (int g = 0; g < connections.size(); g++) {
												if (connections.get(g).getId() == rematchConformation.get(i).getUserId1()) { 
													// POSALJI DRUGOM DA SAM JA REKAO NE ZELIM NOVU IGRU
													connections.get(g).getToClient().writeObject(new GameSessionEnded(ans.getClientID()));
												}
											}
										}
									}
								}
							}
						}
						else if (object instanceof CloseThreadAndSocket) {
							CloseThreadAndSocket clo = (CloseThreadAndSocket) object;
							
							runAThread = false;
							
							String userName1 = null; // username1 umanjujemo poene
							int userForUpdate = 0;
							String userName2 = null; // ovom uvecavamo
							
							// POSALJI ONOM DRUGOM DA JE KORISNIK NAPUSTIO IGRU I UGASI GAME
							for (int i = 0; i < games.size(); i++) {
								if (clo.getClientID() == games.get(i).getUser1().getId() ||
									clo.getClientID() == games.get(i).getUser2().getId()) {
									if (games.get(i).getUser1().getId() == clo.getClientID()) {
										userName1 = games.get(i).getUser1().getUsername();
										userName2 = games.get(i).getUser2().getUsername();
										userForUpdate = games.get(i).getUser2().getId();
										games.get(i).getUser2().getToClient().writeObject(new GameSessionEnded(clo.getClientID()));
									}
									if (games.get(i).getUser2().getId() == clo.getClientID()) {
										userName1 = games.get(i).getUser2().getUsername();
										userForUpdate = games.get(i).getUser1().getId();
										userName2 = games.get(i).getUser1().getUsername();
										games.get(i).getUser1().getToClient().writeObject(new GameSessionEnded(clo.getClientID()));
									}
									
									
									// IZRACUNAJ IM NOVE REJTINGE
									
									try {
										preparedStatement = connection.prepareStatement(updateUserRating);
										preparedStatement.setInt(1, -25);
										preparedStatement.setString(2, userName1);
										preparedStatement.executeUpdate();
										
										preparedStatement = connection.prepareStatement(updateUserRating);
										preparedStatement.setInt(1, 25);
										preparedStatement.setString(2, userName2);
										preparedStatement.executeUpdate();
									} catch (SQLException e) {
										e.printStackTrace();
									}
									
									try {
										preparedStatement = connection.prepareStatement(getPlayerRatingQuery);
										preparedStatement.setString(1, userName2);
										
										
										// POSALJI ONOM KOJI SE NIJE ISKLJUCIO NJEGOV RATING
										ResultSet resSet = preparedStatement.executeQuery();
										if (resSet.next()) {
											int rating = resSet.getInt(1);
											for (int g = 0; g < connections.size(); g++)
												if (connections.get(g).getId() == userForUpdate) {
													connections.get(g).getToClient().writeObject(new PlayerRatingUpdate(userForUpdate, rating));
												}
										}
									} catch (SQLException e) {
										e.printStackTrace();
									}
									
									
									
									// OBRISI OVU IGRU
									games.remove(i);
								}
							}
							
							
							// IZBACI GA IZ CONNECTIONS ISTO
							
							for (int i = 0; i < connections.size(); i++) {
								if (clo.getClientID() == connections.get(i).getId()) {
									// IZBACI GA IZ KONEKCIJA
									int broj = i;
									Platform.runLater(() -> {
										taLog.appendText(connections.get(broj).getUsername() + " with id " + connections.get(broj).getId() + " log out\n");
										connections.remove(broj);
									});
									
								}
							}
							
							// IZBACI GA IZ SEARCHING GAMES-a
							
							for (int i = 0; i < gameSearch.size(); i++) {
								if (clo.getClientID() == gameSearch.get(i).getPlayerId()) {
									// IZBACI AKO JE TRAZIO NEKI SEARCH TAJ LIK
									gameSearch.remove(i);
								}
							}
							
							// ZATVORI SOCKET KA NJEMU
							
							guestConnection.getSocket().close();
						}
						else if (object instanceof LoginRequest) {		
							
							LoginRequest loginRequest = (LoginRequest) object;
							try {
								preparedStatement = connection.prepareStatement(queryLogin);
								preparedStatement.setString(1, loginRequest.getUsername());
								preparedStatement.setString(2, loginRequest.getPassword());
								
								ResultSet resSet = preparedStatement.executeQuery();
								if (resSet.next()) {
									int id = Integer.parseInt(resSet.getString(1));
									String username = resSet.getString(2);
									int rating = Integer.parseInt(resSet.getString(4));
									String avatar = resSet.getString(6);
									
									Account account = new Account(id, username, rating, avatar);
									
									Platform.runLater(() -> {
										taLog.appendText(username + " with id " + id + " logged in\n");
									});
									
									guestConnection.getToGuest().writeObject(new LoginRequestAccepted(account));
									
									//UserConnection userConn = new UserConnection(id, username, guestConnection.getSocket(), guestConnection.getToGuest(), guestConnection.getFromGuest());
									//userConnection = new UserConnection(id, username, guestConnection.getSocket(), guestConnection.getToGuest(), guestConnection.getFromGuest());
									connections.add(userConnection);
									
									onlinePlayers++;
									
									for (int i = 0; i < connections.size(); i++) {
										connections.get(i).getToClient().writeObject(new OnlinePlayersNumber(onlinePlayers));
									}
									
								}
								else {
									guestConnection.getToGuest().writeObject(new LoginRequestDenied());
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
							
						}
						else if (object instanceof SignUpRequest) {
							SignUpRequest signUpRequest = (SignUpRequest) object;
							try {
								preparedStatement = connection.prepareStatement(queryLogin);
								preparedStatement.setString(1, signUpRequest.getUsername());
								preparedStatement.setString(2, signUpRequest.getPassword());
								
								ResultSet rSet = preparedStatement.executeQuery();
								
								if (rSet.next()) {
									guestConnection.getToGuest().writeObject(new SignUpRequestDenied("I will do this later"));
								}
								else {
									preparedStatement = connection.prepareStatement(querrySignUp);
									preparedStatement.setString(1, signUpRequest.getUsername());
									preparedStatement.setString(2, signUpRequest.getPassword());
									preparedStatement.setString(3, signUpRequest.getEmail());
									
									preparedStatement.executeUpdate();
									
									Platform.runLater(() -> {
										taLog.appendText("Account with " + signUpRequest.getUsername() + " was just created\n");
									});
									
									guestConnection.getToGuest().writeObject(new SignUpRequestAccepted());
								}
								
								
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
						}
						else if (object instanceof AddOpponentAsFriend) {
							AddOpponentAsFriend addObj = (AddOpponentAsFriend) object;
							for (int i = 0; i < games.size(); i++) {
								if (games.get(i).getUser1().getId() == addObj.getClientID() ||
									games.get(i).getUser2().getId() == addObj.getClientID()) {
									// PROVERI DA LI SU ONI VEC PRIJATELJI
									try {
										preparedStatement = connection.prepareStatement(queryFriends);
										preparedStatement.setInt(1, games.get(i).getUser1().getId());
										preparedStatement.setInt(2, games.get(i).getUser2().getId());
										preparedStatement.setInt(3, games.get(i).getUser2().getId());
										preparedStatement.setInt(4, games.get(i).getUser1().getId());
										
										ResultSet resSet = preparedStatement.executeQuery();
										
										if (resSet.next() == false) {
											try {
												preparedStatement = connection.prepareStatement(insertFriendsQuery);
												preparedStatement.setInt(1, games.get(i).getUser1().getId());
												preparedStatement.setInt(2, games.get(i).getUser2().getId());
												
												preparedStatement.executeUpdate();
											} catch (SQLException e) {
												e.printStackTrace();
											}
										}
									} catch (SQLException e1) {
										e1.printStackTrace();
									}
									
								}
							}
						}
						else if (object instanceof OnlineFriendsMessage) {
							OnlineFriendsMessage onlineFriend = (OnlineFriendsMessage) object;
							// IZVUCI IZ BAZE PRIJATELJE
							
							try {
								preparedStatement = connection.prepareStatement(showFriendsQuery);
								preparedStatement.setInt(1, onlineFriend.getClientID());
								preparedStatement.setInt(2, onlineFriend.getClientID());
								
								ResultSet resSet = preparedStatement.executeQuery();
								ArrayList<FriendInformation> friendList = new ArrayList<>();

								while (resSet.next()) {
									int id1 = resSet.getInt(1);
									int id2 = resSet.getInt(2);
									int friendId;
									if (id1 == onlineFriend.getClientID())
										friendId = id2;
									else
										friendId = id1;
									// izvuci podatke o prijatelju
									
									preparedStatement = connection.prepareStatement(showPlayerInformation);
									preparedStatement.setInt(1, friendId);
									//PROVERI JEL FRIEND ONLINE
									ResultSet resSetFriend = preparedStatement.executeQuery();
									if (resSetFriend.next()) {
										FriendInformation fInf = new FriendInformation(friendId, resSetFriend.getString(1), resSetFriend.getInt(2));
										for (int l = 0; l < connections.size(); l++)
											if (connections.get(l).getId() == fInf.getId())
												friendList.add(fInf);
									}
											
									
								}
								guestConnection.getToGuest().writeObject(new OnlineFriends(friendList));
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				
		}
			
	}
		
}

