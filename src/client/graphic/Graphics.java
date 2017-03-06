package client.graphic;

import exceptions.Checkmate;
import exceptions.Draw;
import exceptions.Promotion;
import game.Engine;
import game.GameConstants;
import geometry.Point;





import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import client.graphic.linguistics.EnglishLanguage;
import client.graphic.linguistics.SerbianLanguage;
import client.messages.AddOpponentAsFriend;
import client.messages.AnswerRematch;
import client.messages.ChatMessage;
import client.messages.CloseThreadAndSocket;
import client.messages.GameAccept;
import client.messages.GameDeclined;
import client.messages.GameEnd;
import client.messages.GameRequest;
import client.messages.GameSessionEnded;
import client.messages.InformationSearchGames;
import client.messages.LoginRequest;
import client.messages.OnlineFriendsMessage;
import client.messages.PlayerMove;
import client.messages.SearchForGame;
import client.messages.SearchForGameCancel;
import client.messages.SignOutNotification;
import client.messages.SignUpRequest;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.Account;
import server.messages.FriendInformation;
import server.messages.FriendLogout;
import server.messages.GameAccepted;
import server.messages.GameSearchList;
import server.messages.LoginRequestAccepted;
import server.messages.LoginRequestDenied;
import server.messages.OnlineFriends;
import server.messages.OnlinePlayersNumber;
import server.messages.PlayerRatingUpdate;
import server.messages.PlayerSearchEnded;
import server.messages.PlayerSearchInformation;
import server.messages.ShowRematchDialog;
import server.messages.SignUpRequestAccepted;
import server.messages.SignUpRequestDenied;
import server.messages.StartANewGame;
import specialComponents.ChessButton;
import specialComponents.FigureChoseButton;
import specialComponents.OnlineFriendsLabel;
import specialComponents.PlayerSearchingLabel;
import specialComponents.PlayerTimer;


public class Graphics extends Application implements GameConstants {
	private Socket socket;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private String host = "localhost";
	private int serverPort = 8000;
	private Account account = null;
	private GraphicLogic graphicLogic;
	
	String backgroundColorValue = "#E7FFEF";
	
	private TextField usernameLogin;
	private PasswordField passwordLogin;
	private Button loginButton;
	private Button signUpButton;
	private TextField usernameSignUp;
	private PasswordField passwordSignUp;
	private TextField emailSignUp;
	private TextField repeatEmailSignUp;
	private Button registerButton;
	private Button cancelSignUpButton;
	private Label textRegisterPage;
	private Button okRegisterButton;
	private ImageView avatarImage;
	private Label userNameMain;
	private Label userRatingMain;
	private Label onlinePlayersMain;
	private Label settingsMain;
	private Label signOutMain;
	private Button buttonFor1min;
	private Button buttonFor5min;
	private Button buttonFor10min;
	private Button buttonFor20min;
	private Pane onlinePlayerReadyForGame;
	private Pane onlineFriends;
	private ChessButton[][] buttonsChess;

	private TextArea chatRecive;
	private TextField chatSend;
	private Button sendChat;
	private static String alpha = "ABCDEFGH";
	private static String numbers = "12345678";
	private ImageView avatarImageStatus;
	private ImageView opponentAvatarStatus;
	private Label userNameGame;
	private Label userRatingGame;
	private Label opponentUsernameGame;
	private Label opponentRating;
	private Button searchForGame;
	private Engine game = null;
	private boolean isPromotionPlayed = true;
	private Button homeButton;
	
	private int onlinePlayersNo = 0;
	private boolean playingOnlineGame = false;
	private boolean readMessagesFromServer = true;
	private PlayerTimer playerTimer;
	private PlayerTimer opponentTimer;
	private Label playerTimerLabel;
	private Label playerOpponentLabel;
	private Timeline timeline;
	public Graphics() {
		graphicLogic = new GraphicLogic();
		try {
			socket = new Socket(host, serverPort);
			toServer = new ObjectOutputStream(socket.getOutputStream());
			fromServer = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void startATimer(Stage prStage) {
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(1000),
		        ae -> {
		        	if (game.getOnMove() == TEAM_WHITE) {
		        		try {
							playerTimer.reduce();
							playerTimerLabel.setText(playerTimer.toString());
							playerOpponentLabel.setText(opponentTimer.toString());
						} catch (Exception e) {
							playerTimerLabel.setText(playerTimer.toString());
							timeline.stop();
							if (game.getTypeOfGame() == GAME_MODE_BOTS) {
								Stage showNemG = new Stage();
								GridPane grPane = new GridPane();
								Label labelNewGame = new Label(graphicLogic.getLanguage().getTimesUpNewGameText());
								Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
								bt1.setOnAction(e1 -> {
									game.newGame();
									playerTimer = new PlayerTimer(playerTimer.getTimerBeginingTime(), TEAM_WHITE);
									opponentTimer = new PlayerTimer(playerTimer.getTimerBeginingTime(), TEAM_BLACK);
									timeline.play();
									Platform.runLater(() -> {
										refreshGUIBoard(prStage);
									});
									showNemG.close();
								});
								Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
								bt2.setOnAction(e1 -> {
									Scene scene = new Scene(getMainPane(prStage), 900, 650);
									scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
									prStage.setScene(scene);
									showNemG.close();
								});
								grPane.add(labelNewGame, 0, 0, 2, 1);
								grPane.add(bt1, 0, 1);
								grPane.add(bt2, 1, 1);
								
								Scene sceneNew = new Scene(grPane);
								showNemG.setScene(sceneNew);
								showNemG.show();
							}
							else if (game.getTypeOfGame() == GAME_MODE_ONLINE) {
								playerTimerLabel.setText(playerTimer.toString());
								timeline.stop();
								if (game.getTeam() == playerTimer.getPlayer()) {
									try {
										toServer.writeObject(new GameEnd(account.getId(), GAME_LOST));
									} catch (IOException e3) {
										e3.printStackTrace();
									}
									GridPane gridPane = new GridPane();
									Stage endGameStage = new Stage();
									Label label = new Label(graphicLogic.getLanguage().getTimesUpNewGameText());
									gridPane.add(label, 0, 0, 2, 1);
									Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
									bt1.setOnAction(e1 -> {
										try {
											toServer.writeObject(new AnswerRematch(account.getId(), true));
										} catch (Exception e2) {
											e2.printStackTrace();
										}
										
										endGameStage.close();
									});
									Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
									bt2.setOnAction(e1 -> {
										try {
											toServer.writeObject(new GameSessionEnded(account.getId()));
											playingOnlineGame = false;
										} catch (Exception e2) {
											e2.printStackTrace();
										}
										Scene scene = new Scene(getMainPane(prStage), 900, 650);
										scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
										prStage.setScene(scene);
										endGameStage.close();
									});
									gridPane.add(bt1, 0, 1);
									gridPane.add(bt2, 1, 1);
									
									
									
									Scene sceneE = new Scene(gridPane);
									endGameStage.setScene(sceneE);
									endGameStage.show();
								}	
								
								
							}
						}
		        	}
		        	else {
		        		
		        		try {
							opponentTimer.reduce();
							playerOpponentLabel.setText(opponentTimer.toString());
							
						} catch (Exception e) {
							playerOpponentLabel.setText(playerTimer.toString());
							timeline.stop();
							if (game.getTypeOfGame() == GAME_MODE_BOTS) {
								Stage showNemG = new Stage();
								GridPane grPane = new GridPane();
								Label labelNewGame = new Label(graphicLogic.getLanguage().getTimesUpNewGameText());
								Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
								bt1.setOnAction(e1 -> {
									game.newGame();
									playerTimer = new PlayerTimer(playerTimer.getTimerBeginingTime(), TEAM_WHITE);
									opponentTimer = new PlayerTimer(playerTimer.getTimerBeginingTime(), TEAM_BLACK);
									timeline.play();
									Platform.runLater(() -> {
										refreshGUIBoard(prStage);
									});
									showNemG.close();
								});
								Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
								bt2.setOnAction(e1 -> {
									Scene scene = new Scene(getMainPane(prStage), 900, 650);
									scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
									prStage.setScene(scene);
									showNemG.close();
								});
								grPane.add(labelNewGame, 0, 0, 2, 1);
								grPane.add(bt1, 0, 1);
								grPane.add(bt2, 1, 1);
								
								Scene sceneNew = new Scene(grPane);
								showNemG.setScene(sceneNew);
								showNemG.show();
							}
							else if (game.getTypeOfGame() == GAME_MODE_ONLINE) {
								
								if (game.getTeam() == opponentTimer.getPlayer()) {
									try {
										toServer.writeObject(new GameEnd(account.getId(), GAME_LOST));
									} catch (IOException e3) {
										e3.printStackTrace();
									}
									GridPane gridPane = new GridPane();
									Stage endGameStage = new Stage();
									Label label = new Label(graphicLogic.getLanguage().getCheckmateAnotherGameText());
									gridPane.add(label, 0, 0, 2, 1);
									Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
									bt1.setOnAction(e1 -> {
										try {
											toServer.writeObject(new AnswerRematch(account.getId(), true));
										} catch (Exception e2) {
											e2.printStackTrace();
										}
										
										endGameStage.close();
									});
									Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
									bt2.setOnAction(e1 -> {
										try {
											toServer.writeObject(new GameSessionEnded(account.getId()));
											playingOnlineGame = false;
										} catch (Exception e2) {
											e2.printStackTrace();
										}
										Scene scene = new Scene(getMainPane(prStage), 900, 650);
										scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
										prStage.setScene(scene);
										endGameStage.close();
									});
									gridPane.add(bt1, 0, 1);
									gridPane.add(bt2, 1, 1);
									
									
									
									Scene sceneE = new Scene(gridPane);
									endGameStage.setScene(sceneE);
									endGameStage.show();
								}
								
								
							}
						}
		        	}
		        }));
			timeline.setCycleCount(Timeline.INDEFINITE);
	}
	
	private void connectClient(Stage prStage) {
		new Thread(() -> {
			while (readMessagesFromServer) {
				try {
					Object obj = fromServer.readObject();
					if (obj instanceof PlayerMove) {
						PlayerMove pMove = (PlayerMove) obj;//Ovde prima potez
						game.interpretMove(pMove.getPlayerMove());//Odigrava potez
						Platform.runLater(() -> {
							refreshGUIBoard(prStage);
						});
						
					}
					else if (obj instanceof PlayerSearchInformation) {
						PlayerSearchInformation pSInformation = (PlayerSearchInformation) obj;
						PlayerSearchingLabel lab = new PlayerSearchingLabel(pSInformation);
						Platform.runLater(() -> {
							onlinePlayerReadyForGame.getChildren().add(lab);
						});
						lab.setOnMouseClicked(e -> {
							if (account.getId() != pSInformation.getId()) {
								GameRequest gameRequest = new GameRequest(account.getId(), account.getUsername(), pSInformation.getId(), pSInformation.getGameTime(), account.getRating());
								playerTimer = new PlayerTimer(gameRequest.getGameTime(), TEAM_WHITE);
								opponentTimer = new PlayerTimer(gameRequest.getGameTime(), TEAM_BLACK);
								try {
									toServer.writeObject(gameRequest);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						});
						
					}
					else if (obj instanceof OnlinePlayersNumber) {
						OnlinePlayersNumber num = (OnlinePlayersNumber) obj;
						onlinePlayersNo = num.getNumber();
						Platform.runLater(() -> {
							onlinePlayersMain.setText(Integer.toString(onlinePlayersNo));
						});
						
					}
					else if (obj instanceof GameRequest) {
						GameRequest gameRequestOp = (GameRequest) obj;
						Platform.runLater(() -> {
							Stage gameRequestStage = new Stage();
							GridPane paneRequest = new GridPane();
							Label labRequest = new Label(graphicLogic.getLanguage().getGameRequestFromText() + " " + gameRequestOp.getClientUsername());
							
							paneRequest.add(labRequest, 0, 0, 2, 1);
							
							Button button1 = new Button(graphicLogic.getLanguage().getOkText());
							button1.setOnAction(e -> {
								try {
									toServer.writeObject(new GameAccept(account.getId(), gameRequestOp.getClientID()));
									playingOnlineGame = true;
									Platform.runLater(() -> {
										game = new Engine(GAME_MODE_ONLINE, TEAM_BLACK);
										playerTimer = new PlayerTimer(gameRequestOp.getGameTime(), TEAM_WHITE);
										opponentTimer = new PlayerTimer(gameRequestOp.getGameTime(), TEAM_BLACK);
										Pane gamePane = getGamePane(prStage, gameRequestOp.getClientUsername(), gameRequestOp.getRating());
										Scene gameScene = new Scene(gamePane, 900, 650);
										gameScene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
										prStage.setScene(gameScene);
										gameRequestStage.close();
									});
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							});
							
							Button button2 = new Button(graphicLogic.getLanguage().getCancelText());
							button2.setOnAction(e -> {
								try {
									toServer.writeObject(new GameDeclined(account.getId(), gameRequestOp.getClientID()));
									gameRequestStage.close();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							});
							
							paneRequest.add(button1, 0, 1);
							paneRequest.add(button2, 1, 1);
							
							Scene scene = new Scene(paneRequest);
							gameRequestStage.setTitle(graphicLogic.getLanguage().getGameRequestText());
							gameRequestStage.setScene(scene);
							gameRequestStage.show();
						});
					}
					else if (obj instanceof GameAccepted) {
						game = new Engine(GAME_MODE_ONLINE, TEAM_WHITE);
						
						
						GameAccepted gAccepted = (GameAccepted) obj;
						playingOnlineGame = true;
						Platform.runLater(() -> {
							Pane gamePane = getGamePane(prStage, gAccepted.getOpponentUsername(), gAccepted.getPlayerRating());
							Scene gameScene = new Scene(gamePane, 900, 650);
							gameScene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
							prStage.setScene(gameScene);
						});
						
					}
					else if (obj instanceof PlayerSearchEnded) {
						PlayerSearchEnded pSEnded = (PlayerSearchEnded) obj;
						for (int i = 0; i < onlinePlayerReadyForGame.getChildren().size(); i++) {
							if (onlinePlayerReadyForGame.getChildren() != null) {
								PlayerSearchingLabel labelOfPlayer = (PlayerSearchingLabel) onlinePlayerReadyForGame.getChildren().get(i);
								PlayerSearchInformation pSearchInformation = labelOfPlayer.getPlayer();
								int numberToRemove = i;
								if (pSearchInformation.getId() == pSEnded.getPlayerId()) {
									Platform.runLater(() -> {
										onlinePlayerReadyForGame.getChildren().remove(numberToRemove);
									});
									
								}
							}
						}
						
					}
					else if (obj instanceof ChatMessage) {
						ChatMessage chatMessage = (ChatMessage) obj;
						Platform.runLater(() -> {
							chatRecive.appendText(chatMessage.getUsername() + ": " + chatMessage.getMessage() + "\n");
						});
					}
					else if (obj instanceof GameSessionEnded) {
						Platform.runLater(() -> {
							Stage stageNew = new Stage();
							GridPane gridPane = new GridPane();
							playingOnlineGame = false;
							Label label = new Label(graphicLogic.getLanguage().getOpponentLeftAGameText());
							Button btOk = new Button(graphicLogic.getLanguage().getOkText());
							btOk.setOnAction(e -> {
								Scene scene = new Scene(getMainPane(prStage), 900, 650);
								scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
								prStage.setScene(scene);
								stageNew.close();
							});
							gridPane.add(label, 0, 0);
							gridPane.add(btOk, 0, 1);
							Scene scene = new Scene(gridPane);
							stageNew.setScene(scene);
							stageNew.show();
						});
					}
					else if (obj instanceof PlayerRatingUpdate) {
						PlayerRatingUpdate pRatingUpd = (PlayerRatingUpdate) obj;
						account.updateRating(pRatingUpd.getNewRating());
					}
					else if (obj instanceof GameSearchList) {
						GameSearchList gameSearchList = (GameSearchList) obj;
						ArrayList<PlayerSearchInformation> listOfInf = gameSearchList.getListOfSearchGames();
						if (listOfInf != null) {
							for (int i = 0; i < listOfInf.size(); i++) {
								PlayerSearchingLabel lab = new PlayerSearchingLabel(listOfInf.get(i));
								Platform.runLater(() -> {
									onlinePlayerReadyForGame.getChildren().add(lab);
								});
								int broj = i;
								lab.setOnMouseClicked(e -> {
									if (account.getId() != listOfInf.get(broj).getId()) {
										GameRequest gameRequest = new GameRequest(account.getId(), account.getUsername(), listOfInf.get(broj).getId(), listOfInf.get(broj).getGameTime(), account.getRating());
										playerTimer = new PlayerTimer(gameRequest.getGameTime(), TEAM_WHITE);
										opponentTimer = new PlayerTimer(gameRequest.getGameTime(), TEAM_BLACK);
										try {
											toServer.writeObject(gameRequest);
										} catch (Exception e1) {
											e1.printStackTrace();
										}
									}
								});
							}
						}
					}
					else if (obj instanceof ShowRematchDialog) {
						Platform.runLater(() -> {
							ShowRematchDialog showR = (ShowRematchDialog) obj;
							GridPane gridPane = new GridPane();
							Stage endGameStage = new Stage();
							Label label = new Label();
							if (showR.getCauseOfEnd() == GAME_LOST)
								label.setText(graphicLogic.getLanguage().getCheckmateAnotherGameText());
							else
								label.setText(graphicLogic.getLanguage().getDrawAnotherGameText());
							gridPane.add(label, 0, 0, 2, 1);
							Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
							bt1.setOnAction(e1 -> {
								
								try {
									toServer.writeObject(new AnswerRematch(account.getId(), true));
								} catch (Exception e) {
									e.printStackTrace();
								}
								endGameStage.close();
							});
							Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
							bt2.setOnAction(e1 -> {
								try {
									toServer.writeObject(new GameSessionEnded(account.getId())); 
									playingOnlineGame = false;
								} catch (Exception e2) {
									e2.printStackTrace();
								}
								Scene scene = new Scene(getMainPane(prStage), 900, 650);
								scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
								prStage.setScene(scene);
								endGameStage.close();
							});
							gridPane.add(bt1, 0, 1);
							gridPane.add(bt2, 1, 1);
							
							
							
							Scene sceneE = new Scene(gridPane);
							endGameStage.setScene(sceneE);
							endGameStage.show();
						});
						
					}
					else if (obj instanceof StartANewGame) {
						// INVERTUJ TEAMOVE
						
						String userName1 = userNameGame.getText();
						String userName2 = opponentUsernameGame.getText();
						String id1rat = userRatingGame.getText();
						String id2rat = opponentRating.getText();
						Platform.runLater(() -> {
							opponentUsernameGame.setText(userName1);
							opponentRating.setText(id1rat);
							userNameGame.setText(userName2);
							userRatingGame.setText(id2rat);
							
							if (userNameGame.getText() == account.getUsername()) {
								opponentUsernameGame.setOnMouseClicked(e -> {
									Stage opponentStage = new Stage();
									
									GridPane gridPane = new GridPane();
									Label addOpponentLabel = new Label(graphicLogic.getLanguage().getAddOpponentAsFrindText());
									gridPane.add(addOpponentLabel, 0, 0, 2, 1);
									
									Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
									bt1.setOnAction(e1 -> {
										try {
											toServer.writeObject(new AddOpponentAsFriend(account.getId()));
										} catch (Exception e3) {
											e3.printStackTrace();
										}
										opponentStage.close();
									});
									Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
									bt2.setOnAction(e2 -> {
										
										opponentStage.close();
									});
									gridPane.add(bt1, 0, 1);
									gridPane.add(bt2, 1, 1);
									
									
									Scene scene = new Scene(gridPane);
									opponentStage.setScene(scene);
									opponentStage.show();
									
								});
							}
							else {
								userNameGame.setOnMouseClicked(e -> {
									Stage opponentStage = new Stage();
									
									GridPane gridPane = new GridPane();
									Label addOpponentLabel = new Label(graphicLogic.getLanguage().getAddOpponentAsFrindText());
									gridPane.add(addOpponentLabel, 0, 0, 2, 1);
									
									Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
									bt1.setOnAction(e1 -> {
										try {
											toServer.writeObject(new AddOpponentAsFriend(account.getId()));
										} catch (Exception e3) {
											e3.printStackTrace();
										}
										opponentStage.close();
									});
									Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
									bt2.setOnAction(e2 -> {
										
										opponentStage.close();
									});
									gridPane.add(bt1, 0, 1);
									gridPane.add(bt2, 1, 1);
									
									
									Scene scene = new Scene(gridPane);
									opponentStage.setScene(scene);
									opponentStage.show();
								});
							}
						});
						
						
						game.newGame();
						game.invertTeam();
						playerTimer = new PlayerTimer(playerTimer.getTimerBeginingTime(), TEAM_WHITE);
						opponentTimer = new PlayerTimer(opponentTimer.getTimerBeginingTime(), TEAM_WHITE);
						timeline.play();
						Platform.runLater(() -> {
							refreshGUIBoard(prStage);
						});
						
						
					}
					else if (obj instanceof LoginRequestAccepted) {
						LoginRequestAccepted loginRequestAccepted = (LoginRequestAccepted) obj;
						account = loginRequestAccepted.getAccount();
						Platform.runLater(() -> {
							Scene scene = new Scene(getMainPane(prStage), 900, 650);
							scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
							prStage.setScene(scene);
						});
					}
					else if (obj instanceof LoginRequestDenied) {
						Platform.runLater(() -> {
							Stage wrongPasswordStage = new Stage();
							GridPane passPane = new GridPane();
							passPane.setAlignment(Pos.CENTER);
							Label wrongPassLabel = new Label(graphicLogic.getLanguage().getWrongPassOrEmailText());
							Button okButtonPass = new Button(graphicLogic.getLanguage().getOkText());
							okButtonPass.setPrefWidth(80);
							okButtonPass.setOnAction(e1 -> {
								wrongPasswordStage.close();
							});
							passPane.add(wrongPassLabel, 0, 0);
							passPane.add(okButtonPass, 0, 1);
							
							Scene scenePass = new Scene(passPane, 200, 100);
							wrongPasswordStage.setScene(scenePass);
							wrongPasswordStage.setTitle(graphicLogic.getLanguage().getAlertText());
							wrongPasswordStage.show();
						});
						
					}
					else if (obj instanceof SignUpRequestAccepted) {
						Platform.runLater(() -> {
							Scene scene = new Scene(getRegisterPage(prStage), 900, 650);
							scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
							prStage.setScene(scene);
						});
						
					}
					else if (obj instanceof SignUpRequestDenied) {	
						SignUpRequestDenied signUpRequestDenied = (SignUpRequestDenied) obj;
						Platform.runLater(() -> {
							Stage signUpDeniedStage = new Stage();
							GridPane passPane = new GridPane();
							passPane.setAlignment(Pos.CENTER);
							Label wrongPassLabel = new Label(signUpRequestDenied.getMessage());
							Button okButtonPass = new Button(graphicLogic.getLanguage().getOkText());
							okButtonPass.setPrefWidth(80);
							okButtonPass.setOnAction(e1 -> {
								signUpDeniedStage.close();
							});
							passPane.add(wrongPassLabel, 0, 0);
							passPane.add(okButtonPass, 0, 1);
							
							Scene scenePass = new Scene(passPane, 200, 100);
							signUpDeniedStage.setScene(scenePass);
							signUpDeniedStage.setTitle(graphicLogic.getLanguage().getAlertText());
							signUpDeniedStage.show();
						});
						
					}
					else if (obj instanceof OnlineFriends) {
						OnlineFriends onlineFriendsSer = (OnlineFriends) obj;
						
						if (onlineFriendsSer.getFriends().size() > 0) {
							Platform.runLater(() -> {
								onlineFriends.getChildren().remove(0);
							});
							
						}
						for (int i = 0; i < onlineFriendsSer.getFriends().size(); i++) {
							OnlineFriendsLabel labFriend = new OnlineFriendsLabel(onlineFriendsSer.getFriends().get(i));
							Platform.runLater(() -> {
								onlineFriends.getChildren().add(labFriend);
							});
							labFriend.setOnMouseClicked(e -> {
								Platform.runLater(() -> {
									Stage showOp = new Stage();
									GridPane gridPane = new GridPane();
									Label whatTime = new Label(graphicLogic.getLanguage().getChoseTimeText());
									gridPane.add(whatTime, 0, 0, 4, 1);
									Button bt1 = new Button("1");
									bt1.setOnAction(e1 -> {
										GameRequest gReq = new GameRequest(account.getId(), account.getUsername(), labFriend.getFriend().getId(), 1, account.getRating());
										playerTimer = new PlayerTimer(gReq.getGameTime(), TEAM_WHITE);
										opponentTimer = new PlayerTimer(gReq.getGameTime(), TEAM_BLACK);
										try {
											toServer.writeObject(gReq);
											showOp.close();
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									});
									Button bt5 = new Button("5");
									bt5.setOnAction(e1 -> {
										GameRequest gReq = new GameRequest(account.getId(), account.getUsername(), labFriend.getFriend().getId(), 5, account.getRating());
										playerTimer = new PlayerTimer(gReq.getGameTime(), TEAM_WHITE);
										opponentTimer = new PlayerTimer(gReq.getGameTime(), TEAM_BLACK);
										try {
											toServer.writeObject(gReq);
											showOp.close();
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									});
									Button bt10 = new Button("10");
									bt10.setOnAction(e1 -> {
										GameRequest gReq = new GameRequest(account.getId(), account.getUsername(), labFriend.getFriend().getId(), 10, account.getRating());
										playerTimer = new PlayerTimer(gReq.getGameTime(), TEAM_WHITE);
										opponentTimer = new PlayerTimer(gReq.getGameTime(), TEAM_BLACK);
										try {
											toServer.writeObject(gReq);
											showOp.close();
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									});
									Button bt20 = new Button("20");
									bt20.setOnAction(e1 -> {
										GameRequest gReq = new GameRequest(account.getId(), account.getUsername(), labFriend.getFriend().getId(), 20, account.getRating());
										playerTimer = new PlayerTimer(gReq.getGameTime(), TEAM_WHITE);
										opponentTimer = new PlayerTimer(gReq.getGameTime(), TEAM_BLACK);
										try {
											toServer.writeObject(gReq);
											showOp.close();
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									});
									gridPane.add(bt1, 0, 1);
									gridPane.add(bt5, 1, 1);
									gridPane.add(bt10, 2, 1);
									gridPane.add(bt20, 3, 1);
									
									Scene scene = new Scene(gridPane);
									showOp.setScene(scene);
									showOp.show();
								});
							});
						}
					}
					else if (obj instanceof FriendLogout) {
						FriendLogout friendLog = (FriendLogout) obj;
						for (int i = 0; i < onlineFriends.getChildren().size(); i++) {
							if (onlineFriends.getChildren() != null) {
								if (onlineFriends.getChildren().get(i) instanceof OnlineFriendsLabel) {
									OnlineFriendsLabel labelOfFriend = (OnlineFriendsLabel) onlineFriends.getChildren().get(i);
									FriendInformation friendInf = labelOfFriend.getFriend();
									int numberToRemove = i;
									if (friendLog.getFriend().getId() == friendInf.getId()) {
										Platform.runLater(() -> {
											onlineFriends.getChildren().remove(numberToRemove);
										});
									}
								}
								
							}
						}
						
					}
					else if (obj instanceof FriendInformation) {
						FriendInformation fInfo = (FriendInformation) obj;
						if (onlineFriends.getChildren().size() > 0) {
							if (onlineFriends.getChildren().get(0) instanceof Label) {
								if (onlineFriends.getChildren().get(0) instanceof OnlineFriendsLabel == false) {
									Platform.runLater(() -> {
										onlineFriends.getChildren().remove(0);
									});
									
								}
							}
						}
						
						OnlineFriendsLabel labFriend = new OnlineFriendsLabel(fInfo);
						Platform.runLater(() -> {
							onlineFriends.getChildren().add(labFriend);
						});
						labFriend.setOnMouseClicked(e -> {
							Platform.runLater(() -> {
								Stage showOp = new Stage();
								GridPane gridPane = new GridPane();
								Label whatTime = new Label(graphicLogic.getLanguage().getChoseTimeText());
								gridPane.add(whatTime, 0, 0, 4, 1);
								Button bt1 = new Button("1");
								bt1.setOnAction(e1 -> {
									GameRequest gReq = new GameRequest(account.getId(), account.getUsername(), labFriend.getFriend().getId(), 1, account.getRating());
									playerTimer = new PlayerTimer(gReq.getGameTime(), TEAM_WHITE);
									opponentTimer = new PlayerTimer(gReq.getGameTime(), TEAM_BLACK);
									try {
										toServer.writeObject(gReq);
										showOp.close();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								});
								Button bt5 = new Button("5");
								bt5.setOnAction(e1 -> {
									GameRequest gReq = new GameRequest(account.getId(), account.getUsername(), labFriend.getFriend().getId(), 5, account.getRating());
									playerTimer = new PlayerTimer(gReq.getGameTime(), TEAM_WHITE);
									opponentTimer = new PlayerTimer(gReq.getGameTime(), TEAM_BLACK);
									try {
										toServer.writeObject(gReq);
										showOp.close();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								});
								Button bt10 = new Button("10");
								bt10.setOnAction(e1 -> {
									GameRequest gReq = new GameRequest(account.getId(), account.getUsername(), labFriend.getFriend().getId(), 10, account.getRating());
									playerTimer = new PlayerTimer(gReq.getGameTime(), TEAM_WHITE);
									opponentTimer = new PlayerTimer(gReq.getGameTime(), TEAM_BLACK);
									try {
										toServer.writeObject(gReq);
										showOp.close();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								});
								Button bt20 = new Button("20");
								bt20.setOnAction(e1 -> {
									GameRequest gReq = new GameRequest(account.getId(), account.getUsername(), labFriend.getFriend().getId(), 20, account.getRating());
									playerTimer = new PlayerTimer(gReq.getGameTime(), TEAM_WHITE);
									opponentTimer = new PlayerTimer(gReq.getGameTime(), TEAM_BLACK);
									try {
										toServer.writeObject(gReq);
										showOp.close();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								});
								gridPane.add(bt1, 0, 1);
								gridPane.add(bt5, 1, 1);
								gridPane.add(bt10, 2, 1);
								gridPane.add(bt20, 3, 1);
								
								Scene scene = new Scene(gridPane);
								showOp.setScene(scene);
								showOp.show();
								
							});
							
						});
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	@Override
	public void start(Stage prStage) throws Exception {
		Scene scene = new Scene(getLoginPage(prStage), 900, 650);
		scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
		prStage.setTitle("Chess project Theban");
		prStage.setScene(scene);
		prStage.setResizable(false);
		connectClient(prStage);
		prStage.setOnCloseRequest(e -> {
			Platform.runLater(() -> {
				try {
					// POSALJI SERVERU DA GA UGASI, NJEGOV SOCKET I THREAD
					toServer.writeObject(new CloseThreadAndSocket(account.getId()));
					// UGASI SVOJ THREAD ZA CITANJE
					readMessagesFromServer = false;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			});
		});
		prStage.show();
	}
	
	private Pane getLoginPage(Stage prStage) {
		GridPane loginPagePane = new GridPane();
		loginPagePane.setAlignment(Pos.CENTER);
		loginPagePane.setVgap(15);
		loginPagePane.setStyle("-fx-background-color: " + backgroundColorValue);
		
		Label gameNameLogin = new Label(graphicLogic.getLanguage().getGameTitleText());
		gameNameLogin.getStyleClass().add("game-title");
		loginPagePane.add(gameNameLogin, 0, 0);
		
		Label usernameText = new Label(graphicLogic.getLanguage().getUsernameText());
		usernameText.setPrefWidth(55);
		usernameText.getStyleClass().add("text-label");
		usernameLogin = new TextField();
		usernameLogin.getStyleClass().add("text-field");
		usernameLogin.setPrefColumnCount(17);
		HBox usernameHBox = new HBox(20);
		usernameHBox.getChildren().addAll(usernameText, usernameLogin);
		
		loginPagePane.add(usernameHBox, 0, 1);
		
		Label passwordText = new Label(graphicLogic.getLanguage().getPasswordText());
		passwordText.setPrefWidth(55);
		passwordText.getStyleClass().add("text-label");
		passwordLogin = new PasswordField();
		passwordLogin.setPrefColumnCount(17);
		HBox passwordHBox = new HBox(20);
		passwordHBox.getChildren().addAll(passwordText, passwordLogin);
		
		loginPagePane.add(passwordHBox, 0, 2);
		
		HBox loginButtonsHBox = new HBox(30);
		loginButton = new Button(graphicLogic.getLanguage().getLoginText());
		loginButton.setPrefWidth(80);
		loginButton.getStyleClass().add("custom-button");
		loginButton.setOnAction(e -> { 
			LoginRequest loginRequest = new LoginRequest(usernameLogin.getText(), passwordLogin.getText());
			try {
				toServer.writeObject(loginRequest);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		});
		signUpButton = new Button(graphicLogic.getLanguage().getSignUpText());
		signUpButton.setPrefWidth(80);
		signUpButton.getStyleClass().add("custom-button");
		signUpButton.setOnAction(e -> {
			Scene scene = new Scene(getSignUpPage(prStage), 900, 650);
			scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
			prStage.setScene(scene);
		});
		loginButtonsHBox.getChildren().addAll(loginButton, signUpButton);
		loginButtonsHBox.setAlignment(Pos.CENTER_RIGHT);
		
		loginPagePane.add(loginButtonsHBox, 0, 3);
	
		return loginPagePane;
		
	}
	
	private Pane getSignUpPage(Stage prStage) {
		GridPane signUpPagePane = new GridPane();
		signUpPagePane.setAlignment(Pos.CENTER);
		signUpPagePane.setVgap(15);
		signUpPagePane.setStyle("-fx-background-color: " + backgroundColorValue);
		
		Label gameNameSignUp = new Label(graphicLogic.getLanguage().getGameTitleText());
		gameNameSignUp.setStyle("-fx-font-size: 30; -fx-font-family: Ariel");
		signUpPagePane.add(gameNameSignUp, 0, 0);
		
		Label usernameSignUpText = new Label(graphicLogic.getLanguage().getUsernameText());
		usernameSignUpText.getStyleClass().add("text-label");
		usernameSignUpText.setPrefWidth(70);
		usernameSignUp = new TextField();
		usernameSignUp.setPrefColumnCount(17);
		HBox usernameHBox = new HBox(20);
		usernameHBox.getChildren().addAll(usernameSignUpText, usernameSignUp);
		
		signUpPagePane.add(usernameHBox, 0, 1);
		
		Label passwordSignUpText = new Label(graphicLogic.getLanguage().getPasswordText());
		passwordSignUpText.getStyleClass().add("text-label");
		passwordSignUpText.setPrefWidth(70);
		passwordSignUp = new PasswordField();
		passwordSignUp.setPrefColumnCount(17);
		HBox passwordHBox = new HBox(20);
		passwordHBox.getChildren().addAll(passwordSignUpText, passwordSignUp);
		
		signUpPagePane.add(passwordHBox, 0, 2);
		
		Label emailSignUpText = new Label(graphicLogic.getLanguage().getEmailText());
		emailSignUpText.getStyleClass().add("text-label");
		emailSignUpText.setPrefWidth(70);
		emailSignUp = new TextField();
		emailSignUp.setPrefColumnCount(17);
		HBox emailHBox = new HBox(20);
		emailHBox.getChildren().addAll(emailSignUpText, emailSignUp);
		
		signUpPagePane.add(emailHBox, 0, 3);
		
		Label repeatEmailSignUpText = new Label(graphicLogic.getLanguage().getRepeatEmailText());
		repeatEmailSignUpText.getStyleClass().add("text-label");
		repeatEmailSignUpText.setPrefWidth(70);
		repeatEmailSignUp = new TextField();
		repeatEmailSignUp.setPrefColumnCount(17);
		HBox repeatEmailHBox = new HBox(20);
		repeatEmailHBox.getChildren().addAll(repeatEmailSignUpText, repeatEmailSignUp);
		
		signUpPagePane.add(repeatEmailHBox, 0, 4);
		
		HBox signUpButtonsHBox = new HBox(30);
		registerButton = new Button(graphicLogic.getLanguage().getRegisterText());
		registerButton.setPrefWidth(80);
		registerButton.getStyleClass().add("custom-button");
		registerButton.setOnAction(e -> {
			if (emailSignUp.getText().equals(repeatEmailSignUp.getText())) {
				SignUpRequest signUpRequest = new SignUpRequest(usernameSignUp.getText(), passwordSignUp.getText(), emailSignUp.getText());
				try {
					toServer.writeObject(signUpRequest);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			else {
				Stage samePassStage = new Stage();
				GridPane passPane = new GridPane();
				passPane.setAlignment(Pos.CENTER);
				Label wrongPassLabel = new Label(graphicLogic.getLanguage().getEmailsNotEqualText());
				Button okButtonPass = new Button(graphicLogic.getLanguage().getOkText());
				okButtonPass.setPrefWidth(80);
				okButtonPass.setOnAction(e1 -> {
					samePassStage.close();
				});
				passPane.add(wrongPassLabel, 0, 0);
				passPane.add(okButtonPass, 0, 1);
				
				Scene scenePass = new Scene(passPane, 200, 100);
				samePassStage.setScene(scenePass);
				samePassStage.setTitle(graphicLogic.getLanguage().getAlertText());
				samePassStage.show();
			}
			
		});
		cancelSignUpButton = new Button(graphicLogic.getLanguage().getCancelText());
		cancelSignUpButton.setPrefWidth(80);
		cancelSignUpButton.getStyleClass().add("custom-button");
		cancelSignUpButton.setOnAction(e -> {
			Scene scene = new Scene(getLoginPage(prStage), 900, 650);
			scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
			prStage.setScene(scene);
		});
		signUpButtonsHBox.getChildren().addAll(registerButton, cancelSignUpButton);
		signUpButtonsHBox.setAlignment(Pos.CENTER_RIGHT);
		
		signUpPagePane.add(signUpButtonsHBox, 0, 5);
		
		return signUpPagePane;
	}
	
	private Pane getRegisterPage(Stage prStage) {
		GridPane registerPane = new GridPane();
		registerPane.setAlignment(Pos.CENTER);
		registerPane.setVgap(15);
		registerPane.setStyle("-fx-background-color: " + backgroundColorValue);
		
		textRegisterPage = new Label(graphicLogic.getLanguage().getThankRegisterText());
		
		registerPane.add(textRegisterPage, 0, 0);
		
		okRegisterButton = new Button(graphicLogic.getLanguage().getOkText());
		okRegisterButton.setPrefWidth(80);
		okRegisterButton.getStyleClass().add("custom-button");
		okRegisterButton.setOnAction(e -> {
			Scene scene = new Scene(getLoginPage(prStage), 900, 650);
			scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
			prStage.setScene(scene);
		});
		
		GridPane.setHalignment(okRegisterButton, HPos.CENTER);
		registerPane.add(okRegisterButton, 0, 1);
		
		return registerPane;
	}
	
	private Pane getMainPane(Stage prStage) {
		BorderPane pane = new BorderPane();
		HBox hBox1 = new HBox(10);
		RadioButton chose1min = new RadioButton("1 min");
		RadioButton chose5min = new RadioButton("5 min");
		RadioButton chose10min = new RadioButton("10 min");
		RadioButton chose20min = new RadioButton("20 min");
		ToggleGroup toggleGroup = new ToggleGroup();
		chose1min.setToggleGroup(toggleGroup);
		chose5min.setToggleGroup(toggleGroup);
		chose10min.setToggleGroup(toggleGroup);
		chose20min.setToggleGroup(toggleGroup);
		
		searchForGame = new Button(graphicLogic.getLanguage().getSearchText());
		searchForGame.setOnAction(e -> {
			if (searchForGame.getText() == "Search") {
				Platform.runLater(() -> {
					searchForGame.setText("Cancel");
				});
				int time;
				if (chose1min.isSelected())
					time = 1;
				else if (chose5min.isSelected())
					time = 5;
				else if (chose10min.isSelected())
					time = 10;
				else
					time = 20;
				SearchForGame search = new SearchForGame(account.getId(), time, account.getUsername(), account.getRating());
				try {
					toServer.writeObject(search);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			else {
				Platform.runLater(() -> {
					searchForGame.setText("Search");
				});
				try {
					toServer.writeObject(new SearchForGameCancel(account.getId()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		hBox1.getChildren().addAll(chose1min, chose5min, chose10min, chose20min, searchForGame);
		hBox1.setPadding(new Insets(5));
		
		
		Pane toolbarPane = getTopBarPane(prStage);
		
		pane.setTop(toolbarPane);
		BorderPane.setAlignment(toolbarPane, Pos.CENTER);
		
		Pane leftPlayPane = getBotsGamePane(prStage);
		pane.setLeft(leftPlayPane);
		
		GridPane paneCenter = new GridPane();
		
		Pane searchingPane  = getOnlinePlayersReadyForGamePane(prStage);
		paneCenter.add(searchingPane, 0, 0);
		paneCenter.add(hBox1, 0, 1);
		
		pane.setCenter(paneCenter);
		BorderPane.setMargin(paneCenter, new Insets(15));
		
		Pane rightMainPane = getOnlineFriends(prStage);
		rightMainPane.setPrefWidth(200);
		pane.setRight(rightMainPane);
		BorderPane.setMargin(rightMainPane, new Insets(10));
		
		
		pane.setStyle("-fx-background-color: " + backgroundColorValue);
		pane.getStyleClass().add("topBarBoxMain");
		return pane;
	}
	
	private Pane getTopBarPane(Stage prStage) {
		HBox topHBox = new HBox();
		
		homeButton = new Button(graphicLogic.getLanguage().getHomeText());
		homeButton.setPrefHeight(30);
		homeButton.getStyleClass().add("topBarText");
		homeButton.setPrefWidth(100);
		homeButton.setAlignment(Pos.CENTER);
		homeButton.setOnAction(e -> {
			if (playingOnlineGame) {

				Stage stage2 = new Stage();
				GridPane gridPane = new GridPane();
				Label lab = new Label(graphicLogic.getLanguage().getYouWillLoseGameText());
				Button yesBt = new Button(graphicLogic.getLanguage().getYesText());
				yesBt.setOnAction(e1 -> {
					Scene scene = new Scene(getMainPane(prStage), 900, 650);
					scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
					prStage.setScene(scene);
					playingOnlineGame = false;
					stage2.close();
					
					
					//IZRACUNAJ GUBITKE POENA
					
					try {
						toServer.writeObject(new GameSessionEnded(account.getId()));
						playingOnlineGame = false;
					} catch (Exception e3) {
						e3.printStackTrace();
					}
				});
				Button noBt = new Button(graphicLogic.getLanguage().getNoText());
				noBt.setOnAction(e2 -> {
					stage2.close();
				});
				gridPane.setAlignment(Pos.CENTER);
				gridPane.add(lab, 0, 0, 2, 1);
				gridPane.add(yesBt, 0, 1);
				gridPane.add(noBt, 1, 1);
				Scene scene2 = new Scene(gridPane);
				stage2.setScene(scene2);
				stage2.show();
				
			}
			else {
				Scene scene = new Scene(getMainPane(prStage), 900, 650);
				scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
				prStage.setScene(scene);
			}
		});
		
		topHBox.getChildren().add(homeButton);
		
		Image image = new Image("file:res/images/user.png");
		avatarImage = new ImageView(image);
		avatarImage.setFitWidth(50);
		avatarImage.setFitHeight(50);
		
		topHBox.getChildren().add(avatarImage);
		
		userNameMain = new Label(account.getUsername());
		userNameMain.setPrefHeight(30);
		userNameMain.getStyleClass().add("topBarText");
		userNameMain.setPrefWidth(100);
		userNameMain.setAlignment(Pos.CENTER);
		
		topHBox.getChildren().add(userNameMain);
		
		userRatingMain = new Label(Integer.toString(account.getRating()));
		userRatingMain.setPrefHeight(30);
		userRatingMain.getStyleClass().add("topBarText");
		userRatingMain.setPrefWidth(100);
		userRatingMain.setAlignment(Pos.CENTER);
		
		topHBox.getChildren().add(userRatingMain);

		onlinePlayersMain = new Label(Integer.toString(onlinePlayersNo));
		onlinePlayersMain.setPrefHeight(30);
		onlinePlayersMain.getStyleClass().add("topBarText");
		onlinePlayersMain.setPrefWidth(100);
		onlinePlayersMain.setAlignment(Pos.CENTER);
		
		topHBox.getChildren().add(onlinePlayersMain);
		
		settingsMain = new Label(graphicLogic.getLanguage().getSettingsText());
		settingsMain.setPrefHeight(30);
		settingsMain.getStyleClass().add("topBarText");
		settingsMain.setPrefWidth(100);
		settingsMain.setAlignment(Pos.CENTER);
		settingsMain.setOnMouseClicked(e -> {
			showSettingStage();
		});
		
		topHBox.getChildren().add(settingsMain);
		
		signOutMain = new Label(graphicLogic.getLanguage().getSignOutText());
		signOutMain.setPrefHeight(30);
		signOutMain.getStyleClass().add("topBarText");
		signOutMain.setPrefWidth(100);
		signOutMain.setAlignment(Pos.CENTER);
		signOutMain.setOnMousePressed(e -> {
			try {
				toServer.writeObject(new SignOutNotification(account.getId()));
				timeline.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			account = null;
			Scene scene = new Scene(getLoginPage(prStage), 900, 650);
			scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
			prStage.setScene(scene);
		});
		
		topHBox.getChildren().add(signOutMain);
	
		HBox.setMargin(avatarImage, new Insets(15));
		HBox.setMargin(userNameMain, new Insets(15));
		HBox.setMargin(userRatingMain, new Insets(15));
		HBox.setMargin(onlinePlayersMain, new Insets(15));
		HBox.setMargin(settingsMain, new Insets(15));
		HBox.setMargin(signOutMain, new Insets(15));
		
		topHBox.getStyleClass().add("topBarBoxMain");
		topHBox.setAlignment(Pos.CENTER);
		
		return topHBox;
	}
	
	private Pane getBotsGamePane(Stage prStage) {
		GridPane gridPane = new GridPane();
		Label playWithBotsLabel = new Label(graphicLogic.getLanguage().getPlayAGameWithBotsText());
		playWithBotsLabel.setAlignment(Pos.CENTER);
		playWithBotsLabel.setStyle("-fx-font-size: 18");

		gridPane.add(playWithBotsLabel, 0, 0);
		
		buttonFor1min = new Button("1");
		buttonFor1min.setPrefSize(80, 80);
		buttonFor1min.setStyle("-fx-background-radius: 50");
		buttonFor1min.setOnAction(e -> {
			game = new Engine(GAME_MODE_BOTS, TEAM_WHITE);
			playerTimer = new PlayerTimer(1, TEAM_WHITE);
			opponentTimer = new PlayerTimer(1, TEAM_BLACK);
			Scene scene = new Scene(getGamePane(prStage, "Bot", 0), 900, 650);
			scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
			prStage.setScene(scene);
			
		});
		
		buttonFor5min = new Button("5");
		buttonFor5min.setPrefSize(80, 80);
		buttonFor5min.setStyle("-fx-background-radius: 50");
		buttonFor5min.setOnAction(e -> {
			game = new Engine(GAME_MODE_BOTS, TEAM_WHITE);
			playerTimer = new PlayerTimer(5, TEAM_WHITE);
			opponentTimer = new PlayerTimer(5, TEAM_BLACK);
			//startATimer(prStage);
			Scene scene = new Scene(getGamePane(prStage, "Bot", 0), 900, 650);
			scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
			prStage.setScene(scene);
			
		});
		
		
		HBox hBox1 = new HBox(30);
		hBox1.getChildren().addAll(buttonFor1min, buttonFor5min);
		
		buttonFor10min = new Button("10");
		buttonFor10min.setPrefSize(80, 80);
		buttonFor10min.setStyle("-fx-background-radius: 50");
		buttonFor10min.setOnAction(e -> {
			game = new Engine(GAME_MODE_BOTS, TEAM_WHITE);
			playerTimer = new PlayerTimer(10, TEAM_WHITE);
			opponentTimer = new PlayerTimer(10, TEAM_BLACK);
			//startATimer(prStage);
			Scene scene = new Scene(getGamePane(prStage, "Bot", 0), 900, 650);
			scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
			prStage.setScene(scene);
			
		});
		
		buttonFor20min = new Button("20");
		buttonFor20min.setPrefSize(80, 80);
		buttonFor20min.setStyle("-fx-background-radius: 50");
		buttonFor20min.setOnAction(e -> {
			game = new Engine(GAME_MODE_BOTS, TEAM_WHITE);
			playerTimer = new PlayerTimer(20, TEAM_WHITE);
			opponentTimer = new PlayerTimer(20, TEAM_BLACK);
			//startATimer(prStage);
			Scene scene = new Scene(getGamePane(prStage, "Bot", 0), 900, 650);
			scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
			prStage.setScene(scene);
			
		});
		
		HBox hBox2 = new HBox(30);
		hBox2.getChildren().addAll(buttonFor10min, buttonFor20min);
		
		gridPane.add(hBox1, 0, 1);
		gridPane.add(hBox2, 0, 2);
		
		gridPane.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 10");
		GridPane.setHalignment(playWithBotsLabel, HPos.CENTER);
		GridPane.setMargin(hBox1, new Insets(20));
		GridPane.setMargin(hBox2, new Insets(20));
		GridPane.setMargin(playWithBotsLabel, new Insets(20));
		
		return gridPane;
	}
	
	private Pane getOnlinePlayersReadyForGamePane(Stage prStage) {
		GridPane pane = new GridPane();
		try {
			toServer.writeObject(new InformationSearchGames(account.getId()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		onlinePlayerReadyForGame = new VBox(10);
		onlinePlayerReadyForGame.setPrefWidth(400);
		onlinePlayerReadyForGame.setPrefHeight(400);
		
		Label onlinePlayersSearchingForGameLabel = new Label(graphicLogic.getLanguage().getOnlinePlayersSearchingGameText());
		onlinePlayersSearchingForGameLabel.setAlignment(Pos.CENTER);
		onlinePlayersSearchingForGameLabel.setStyle("-fx-font-size: 18");
		
		pane.add(onlinePlayersSearchingForGameLabel, 0, 0);
		pane.add(new ScrollPane(onlinePlayerReadyForGame), 0, 1);
		
		return pane;
	}
	
	private Pane getOnlineFriends(Stage prStage) {
		try {
			toServer.writeObject(new OnlineFriendsMessage(account.getId()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		GridPane pane = new GridPane();
		Label onlineFriendsLabel = new Label(graphicLogic.getLanguage().getOnlineFriends());
		onlineFriendsLabel.setAlignment(Pos.CENTER);
		onlineFriendsLabel.setStyle("-fx-font-size: 18");
		onlineFriends = new VBox(10);
		onlineFriends.getChildren().add(new Label(graphicLogic.getLanguage().getNoOnlineFriendsText()));
		onlineFriends.setPrefWidth(150);
		onlineFriends.setPrefHeight(300);
		
		
		pane.add(onlineFriendsLabel, 0, 0);
		pane.add(new ScrollPane(onlineFriends), 0, 1);
		GridPane.setMargin(onlineFriendsLabel, new Insets(15));
		GridPane.setMargin(onlineFriends, new Insets(15));
		return pane;
	}
	
	public Pane getSettingsPane(Stage prStage) {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		Label lab = new Label("WILL BE MICROSOFT PROGRAMMER");
		pane.add(lab, 0, 0);
		Button back = new Button(graphicLogic.getLanguage().getBackText());
		back.setOnAction(e -> {
			Scene scene = new Scene(getMainPane(prStage), 900, 650);
			scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
			prStage.setScene(scene);
		});
		pane.add(back, 0, 1);
		return pane;
	}
	
	private Pane getGamePane(Stage prStage, String opponentUsername, int opponentRating) {
		BorderPane pane = new BorderPane();
		pane.setTop(getTopBarPane(prStage));
		startATimer(prStage);
		timeline.play();
		buttonsChess = new ChessButton[8][8];
		Pane boardPane = getBoardPane(prStage);
		pane.setCenter(boardPane);
		BorderPane.setMargin(boardPane, new Insets(30));	
		
		Pane statusPane = getStatusPane(prStage, opponentUsername, opponentRating);
		
		GridPane paneRight = new GridPane();
		chatRecive = new TextArea();
		chatRecive.setPrefWidth(300);
		chatRecive.setPrefHeight(400);
		chatRecive.setEditable(false);
		
		HBox hBox = new HBox(5);
		chatSend = new TextField();
		chatSend.setPrefWidth(250);
		chatSend.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				String text = chatSend.getText();
				chatSend.setText("");
				chatRecive.appendText(userNameMain.getText() + ": " + text + "\n");
				try {
					toServer.writeObject(new ChatMessage(account.getId(), account.getUsername(), -1, IN_GAME_CHAT, text));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		sendChat = new Button(graphicLogic.getLanguage().getSendText());
		sendChat.setPrefWidth(50);
		sendChat.setOnAction(e -> {
			String text = chatSend.getText();
			chatSend.setText("");
			chatRecive.appendText(userNameMain.getText() + ": " + text + "\n");
			try {
				toServer.writeObject(new ChatMessage(account.getId(), account.getUsername(), -1, IN_GAME_CHAT, text));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		hBox.getChildren().addAll(chatSend, sendChat);
		
		paneRight.add(statusPane, 0, 0);
		paneRight.add(chatRecive, 0, 1);
		paneRight.add(hBox, 0, 2);
		GridPane.setMargin(chatRecive, new Insets(10, 10, 5, 10));
		GridPane.setMargin(hBox, new Insets(0, 10, 5, 10));
		
		pane.setRight(paneRight);
		
		return pane;
	}
	
	private void nextMove(Stage prStage){
		if(isPromotionPlayed) {
			if(game.getTypeOfGame() == GAME_MODE_ONLINE){
				try {
					toServer.writeObject(new PlayerMove(account.getId(), game.getLastMove()));
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
				refreshGUIBoard(prStage);
			} else if(game.getTypeOfGame() == GAME_MODE_BOTS){

				try{
					game.getBot().play();
					refreshGUIBoard(prStage);
				} 
				catch (Exception e1) {
					
					Label label = new Label();
					if(e1 instanceof Checkmate)
						label.setText(graphicLogic.getLanguage().getCheckmateAnotherGameText());
					else if(e1 instanceof Draw)
						label.setText(graphicLogic.getLanguage().getDrawAnotherGameText());
					
					GridPane gridPane = new GridPane();
					Stage endGameStage = new Stage();
					
					gridPane.add(label, 0, 0, 2, 1);
					Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
					bt1.setOnAction(e -> {
						game.newGame();
						endGameStage.close();
						refreshGUIBoard(prStage);
					});
					Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
					bt2.setOnAction(e -> {
						Scene scene = new Scene(getMainPane(prStage), 900, 650);
						scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
						prStage.setScene(scene);
						endGameStage.close();
					});
					gridPane.add(bt1, 0, 1);
					gridPane.add(bt2, 1, 1);
					
					
					
					Scene sceneE = new Scene(gridPane);
					endGameStage.setScene(sceneE);
					endGameStage.show();	
					
				}				
				try {
					Thread.sleep(500);
					opponentTimer.reduce();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private Pane getBoardPane(Stage prStage) {
		BorderPane pane = new BorderPane();
		
		HBox hBox1 = new HBox();
		hBox1.setPrefSize(500, 30);
		hBox1.setMaxSize(500, 30);
		hBox1.setAlignment(Pos.CENTER);
		hBox1.setBackground(new Background(new BackgroundFill(graphicLogic.getTheme().getBoardBorderColor(), CornerRadii.EMPTY, Insets.EMPTY)));
		
		HBox hBox2 = new HBox();
		hBox2.setPrefSize(500, 30);
		hBox2.setMaxSize(500, 30);
		hBox2.setAlignment(Pos.CENTER);
		hBox2.setBackground(new Background(new BackgroundFill(graphicLogic.getTheme().getBoardBorderColor(), CornerRadii.EMPTY, Insets.EMPTY)));
		BorderPane.setMargin(hBox2, new Insets(0, 0, 75, 0));
		
		for (int i = 0; i < alpha.length(); i++) {
			Label l1 = new Label(alpha.substring(i, i + 1));
			hBox1.getChildren().add(l1);
			l1.setPrefWidth(55);
			l1.setAlignment(Pos.CENTER);
		
			Label l2 = new Label(alpha.substring(i, i + 1));
			hBox2.getChildren().add(l2);
			l2.setPrefWidth(55);
			l2.setAlignment(Pos.CENTER);
		}
		
		VBox vBox1 = new VBox();
		vBox1.setPrefSize(30, 440);
		vBox1.setMaxSize(30, 440);
		vBox1.setAlignment(Pos.CENTER);
		vBox1.setBackground(new Background(new BackgroundFill(graphicLogic.getTheme().getBoardBorderColor(), CornerRadii.EMPTY, Insets.EMPTY)));
		
		VBox vBox2 = new VBox();
		vBox2.setPrefSize(30, 440);
		vBox2.setMaxSize(30, 440);
		vBox2.setAlignment(Pos.CENTER);
		vBox2.setBackground(new Background(new BackgroundFill(graphicLogic.getTheme().getBoardBorderColor(), CornerRadii.EMPTY, Insets.EMPTY)));
		BorderPane.setMargin(vBox2, new Insets(0, 15, 0, 0));
		
		for (int i = numbers.length() - 1; i >= 0; i--) {
			Label l1 = new Label(numbers.substring(i, i + 1));
			vBox1.getChildren().add(l1);
			l1.setPrefHeight(55);
			l1.setAlignment(Pos.CENTER);
			
			Label l2 = new Label(numbers.substring(i, i + 1));
			vBox2.getChildren().add(l2);
			l2.setPrefHeight(55);
			l2.setAlignment(Pos.CENTER);
		}
		
		pane.setTop(hBox1);
		pane.setBottom(hBox2);
		pane.setLeft(vBox1);
		pane.setRight(vBox2);
		
		GridPane buttonsPane = new GridPane();
		Image img;
		ImageView imgV;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				buttonsChess[i][j] = new ChessButton(i, j, game);
				buttonsChess[i][j].setPrefSize(55, 55);
				buttonsChess[i][j].setMinSize(55, 55);
				buttonsChess[i][j].setMaxSize(55, 55);
				buttonsPane.add(buttonsChess[i][j], j, i);
				img = game.getFigureImage(i, j);
				if (img != null) {
					imgV = new ImageView(img);
					buttonsChess[i][j].setGraphic(imgV);
				}
				buttonsChess[i][j].setOnAction(e -> {
					ArrayList<Point> list = new ArrayList<Point>();

					int ii = ((ChessButton)e.getSource()).getI();
					int jj = ((ChessButton)e.getSource()).getJ();
					 	
					list = game.getPossibleMoves(ii, jj);
					
					if(list==null){
						try {
							game.playMove(new Point(ii, jj));
						} catch (Promotion e1) {
							isPromotionPlayed = false;
							Stage choseStage = new Stage();
							GridPane paneChoser = new GridPane();
							paneChoser.setAlignment(Pos.CENTER);
							FigureChoseButton bishopChose = new FigureChoseButton(game.getOnMove(), "Bishop");
							bishopChose.setOnAction(e2 -> {
								game.zamena(ii, jj, GameConstants.FIGURE_BISHOP);
								isPromotionPlayed = true;
								refreshGUIBoard(prStage);
								nextMove(prStage);
								choseStage.close();
							});
							FigureChoseButton knightChose = new FigureChoseButton(game.getOnMove(), "Knight");
							knightChose.setOnAction(e2 -> {
								game.zamena(ii, jj, GameConstants.FIGURE_KNIGHT);
								isPromotionPlayed = true;
								refreshGUIBoard(prStage);
								nextMove(prStage);
								choseStage.close();
							});
							FigureChoseButton queenChose = new FigureChoseButton(game.getOnMove(), "Queen");
							queenChose.setOnAction(e2 -> {
								game.zamena(ii, jj, GameConstants.FIGURE_QUEEN);
								isPromotionPlayed = true;
								refreshGUIBoard(prStage);
								nextMove(prStage);
								choseStage.close();
							});
							FigureChoseButton rookChose = new FigureChoseButton(game.getOnMove(), "Rook");
							rookChose.setOnAction(e2 -> {
								game.zamena(ii, jj, FIGURE_ROOK);
								isPromotionPlayed = true;
								refreshGUIBoard(prStage);
								nextMove(prStage);
								choseStage.close();
							});
							Label choseFigureLabel = new Label(graphicLogic.getLanguage().getChoseAFigureText());
							paneChoser.add(choseFigureLabel, 0, 0, 4, 1);
							paneChoser.add(bishopChose, 0, 1);
							paneChoser.add(knightChose, 1, 1);
							paneChoser.add(queenChose, 2, 1);
							paneChoser.add(rookChose, 3, 1);
							
							Scene sceneChoser = new Scene(paneChoser, 300, 120);
							
							choseStage.setScene(sceneChoser);
							choseStage.setTitle(graphicLogic.getLanguage().getAlertText());
							choseStage.show();
	
						}
						nextMove(prStage);						
					} else {
						refreshGUIBoard(prStage);
						for (int k = 0; k < list.size(); k++) {
							buttonsChess[list.get(k).getI()][list.get(k).getJ()].setBackground(new Background(new BackgroundFill(graphicLogic.getTheme().getPossibleMovesColor(), CornerRadii.EMPTY, Insets.EMPTY)));
							buttonsChess[list.get(k).getI()][list.get(k).getJ()].setDisable(false);
						}
					}
				});
			}
		
		int ind = 1;
		int colorI = 1;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (colorI % 2 == 0)
					buttonsChess[i][j].setBackground(new Background(new BackgroundFill(graphicLogic.getTheme().getBlackFieldColor(), CornerRadii.EMPTY, Insets.EMPTY)));
				else
					buttonsChess[i][j].setBackground(new Background(new BackgroundFill(graphicLogic.getTheme().getWhiteFieldColor(), CornerRadii.EMPTY, Insets.EMPTY)));
				colorI++;
			}
			if (ind == 1) {
				colorI = 2;
				ind = 0;
			}
			else {
				colorI = 1;
				ind = 1;
			}
		}
		
		pane.setCenter(buttonsPane);
		
		return pane;
	}
	
	private Pane getStatusPane(Stage prStage, String opponentUsername, int opponenentRating) {
		// Imena nisu korrektna username prvi opponent je drugi black(player) 
		GridPane pane = new GridPane();
		Image image = new Image("file:res/images/user.png");
		avatarImageStatus = new ImageView(image);
		opponentAvatarStatus = new ImageView(image);
		avatarImageStatus.setFitHeight(40);
		avatarImageStatus.setFitWidth(40);
		opponentAvatarStatus.setFitHeight(40);
		opponentAvatarStatus.setFitWidth(40);
		
		
		
		pane.add(avatarImageStatus, 0, 0);
		pane.add(opponentAvatarStatus, 1, 0);
		if (game.getTeam() == TEAM_WHITE) {
			opponentUsernameGame = new Label(opponentUsername);
			userNameGame = new Label(account.getUsername());
			userRatingGame = new Label("" + account.getRating());
			opponentRating = new Label("" + opponenentRating);
			if (game.getTypeOfGame() == GAME_MODE_ONLINE) {
				opponentUsernameGame.setOnMouseClicked(e -> {
					Stage opponentStage = new Stage();
					
					GridPane gridPane = new GridPane();
					Label addOpponentLabel = new Label(graphicLogic.getLanguage().getAddOpponentAsFrindText());
					gridPane.add(addOpponentLabel, 0, 0, 2, 1);
					
					Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
					bt1.setOnAction(e1 -> {
						try {
							toServer.writeObject(new AddOpponentAsFriend(account.getId()));
						} catch (Exception e3) {
							e3.printStackTrace();
						}
						opponentStage.close();
					});
					Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
					bt2.setOnAction(e2 -> {
						
						opponentStage.close();
					});
					gridPane.add(bt1, 0, 1);
					gridPane.add(bt2, 1, 1);
					
					
					Scene scene = new Scene(gridPane);
					opponentStage.setScene(scene);
					opponentStage.show();
					
				});
			}
			
		}
		else {
			userNameGame = new Label(opponentUsername);
			opponentUsernameGame = new Label(account.getUsername());
			opponentRating = new Label("" + account.getRating());
			userRatingGame = new Label("" + opponenentRating);
			if (game.getTypeOfGame() == GAME_MODE_ONLINE) {
				userNameGame.setOnMouseClicked(e -> {
					Stage opponentStage = new Stage();
					
					GridPane gridPane = new GridPane();
					Label addOpponentLabel = new Label(graphicLogic.getLanguage().getAddOpponentAsFrindText());
					gridPane.add(addOpponentLabel, 0, 0, 2, 1);
					
					Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
					bt1.setOnAction(e1 -> {
						try {
							toServer.writeObject(new AddOpponentAsFriend(account.getId()));
						} catch (Exception e3) {
							e3.printStackTrace();
						}
						opponentStage.close();
					});
					Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
					bt2.setOnAction(e2 -> {
						
						opponentStage.close();
					});
					gridPane.add(bt1, 0, 1);
					gridPane.add(bt2, 1, 1);
					
					
					Scene scene = new Scene(gridPane);
					opponentStage.setScene(scene);
					opponentStage.show();
					
				});
			}
			
		}
		
		
		playerTimerLabel = new Label(playerTimer.toString());
		playerOpponentLabel = new Label(opponentTimer.toString());
		
		
		pane.add(userNameGame, 0, 1);
		pane.add(opponentUsernameGame, 1, 1);
		
		
		
		pane.add(userRatingGame, 0, 2);
		pane.add(opponentRating, 1, 2);
		
		
		pane.add(playerTimerLabel, 0, 3);
		pane.add(playerOpponentLabel, 1, 3);
		
		pane.setAlignment(Pos.CENTER);
		GridPane.setMargin(avatarImageStatus, new Insets(5, 10, 5, 5));
		GridPane.setMargin(opponentAvatarStatus, new Insets(5, 5, 5, 10));
		GridPane.setMargin(userNameGame, new Insets(0, 10, 0, 5));
		GridPane.setMargin(opponentUsernameGame, new Insets(0, 5, 0, 10));
		GridPane.setMargin(userRatingGame, new Insets(10));
		GridPane.setMargin(opponentRating, new Insets(10));
		
		return pane;
	}
	
	private void refreshGUIBoard(Stage prStage) {
		int i;
		int j;
		for (i = 0; i < 8; i++)
			for (j = 0; j < 8; j++) {
				buttonsChess[i][j].setGraphic(new ImageView(game.getFigureImage(i, j)));
				if ((i * 8 + j + i % 2) % 2 == 0)
					buttonsChess[i][j].setBackground(new Background(new BackgroundFill(graphicLogic.getTheme().getWhiteFieldColor(), CornerRadii.EMPTY, Insets.EMPTY)));
				else
					buttonsChess[i][j].setBackground(new Background(new BackgroundFill(graphicLogic.getTheme().getBlackFieldColor(), CornerRadii.EMPTY, Insets.EMPTY)));
				buttonsChess[i][j].setDisable(true);
				buttonsChess[i][j].setOpacity(1);
 			}
		try {
			int k = 0;
			ArrayList<Point> points = game.getMovableFigures();
			while (k < points.size()) {
				buttonsChess[points.get(k).getI()][points.get(k).getJ()].setDisable(false);
				k++;
			}
		} catch (Draw e) {
			try {
				toServer.writeObject(new GameEnd(account.getId(), GAME_DRAW));
			} catch (IOException e3) {
				e3.printStackTrace();
			}
			GridPane gridPane = new GridPane();
			Stage endGameStage = new Stage();
			Label label = new Label(graphicLogic.getLanguage().getDrawAnotherGameText());
			gridPane.add(label, 0, 0, 2, 1);
			Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
			bt1.setOnAction(e1 -> {
				try {
					toServer.writeObject(new AnswerRematch(account.getId(), true));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				endGameStage.close();
			});
			Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
			bt2.setOnAction(e1 -> {
				try {
					toServer.writeObject(new GameSessionEnded(account.getId()));
					playingOnlineGame = false;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				Scene scene = new Scene(getMainPane(prStage), 900, 650);
				scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
				prStage.setScene(scene);
				endGameStage.close();
			});
			gridPane.add(bt1, 0, 1);
			gridPane.add(bt2, 1, 1);
			
			
			
			Scene sceneE = new Scene(gridPane);
			endGameStage.setScene(sceneE);
			endGameStage.show();
			
		} catch (Checkmate e) {
			try {
				toServer.writeObject(new GameEnd(account.getId(), GAME_LOST));
			} catch (IOException e3) {
				e3.printStackTrace();
			}
			GridPane gridPane = new GridPane();
			Stage endGameStage = new Stage();
			Label label = new Label(graphicLogic.getLanguage().getCheckmateAnotherGameText());
			gridPane.add(label, 0, 0, 2, 1);
			Button bt1 = new Button(graphicLogic.getLanguage().getYesText());
			bt1.setOnAction(e1 -> {
				try {
					toServer.writeObject(new AnswerRematch(account.getId(), true));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
				endGameStage.close();
			});
			Button bt2 = new Button(graphicLogic.getLanguage().getNoText());
			bt2.setOnAction(e1 -> {
				try {
					toServer.writeObject(new GameSessionEnded(account.getId()));
					playingOnlineGame = false;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				Scene scene = new Scene(getMainPane(prStage), 900, 650);
				scene.getStylesheets().add("file:src/client/graphic/appearance/style.css");
				prStage.setScene(scene);
				endGameStage.close();
			});
			gridPane.add(bt1, 0, 1);
			gridPane.add(bt2, 1, 1);
			
			
			
			Scene sceneE = new Scene(gridPane);
			endGameStage.setScene(sceneE);
			endGameStage.show();
			
		}
	}
	
	private void showSettingStage() {
		Stage stageSett = new Stage();
		GridPane gridPane = new GridPane();
		Label labelJ = new Label(graphicLogic.getLanguage().getLanguageText());
		
		gridPane.add(labelJ, 0, 0);
		
		ToggleGroup group = new ToggleGroup();

		RadioButton rb1 = new RadioButton(graphicLogic.getLanguage().getEnglishLanguageText());
		rb1.setToggleGroup(group);
		rb1.setSelected(true);

		RadioButton rb2 = new RadioButton(graphicLogic.getLanguage().getSerbianLanguageText());
		rb2.setToggleGroup(group);
		 
		gridPane.add(rb1, 0, 1);
		gridPane.add(rb2, 0, 2);
		
		Button btOk = new Button(graphicLogic.getLanguage().getOkText());
		btOk.setOnAction(e -> {
			if (rb1.isSelected()) {
				graphicLogic.setLanguage(new EnglishLanguage());
			}
			else {
				graphicLogic.setLanguage(new SerbianLanguage());
			}
			stageSett.close();
		});
		
		gridPane.add(btOk, 0, 3);
		
		Scene sceneSett = new Scene(gridPane);
		stageSett.setScene(sceneSett);
		stageSett.show();
		
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
}
