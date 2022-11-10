import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class whack_a_mole {

	static Frame frame = new Frame("final_project");
	static JButton btn[][] = new JButton[3][3];
	static JButton startButton = new JButton("Start!!");
	static Random random = new Random();
	static Label timeLabel = new Label("Time");
	static Label scoreLabel = new Label("Score");
	static JButton closeButton = new JButton("Close");
	static Action1Lis act = new Action1Lis();
	static Action2Lis Menuact = new Action2Lis();
	static boolean isStart = false;
	static boolean isUse[][] = new boolean[3][3];
	static long setTime[][] = new long[3][3];
	static long lastSet = Long.MAX_VALUE;
	static MenuBar mb = new MenuBar();
	static Menu menu1 = new Menu("Fuction");
	static MenuItem menu11 = new MenuItem("Scoreboard");
	static MenuItem menu13 = new MenuItem("Clear Scoreboard");
	static MenuItem menu12 = new MenuItem("Exit");
	final static int gameTime = 20;
	static long now1 = 0, now = 0;
	static int scoreCounter = 0;

	public static void main(String[] args) {
		mb.add(menu1);
		menu1.add(menu11);
		menu1.add(menu13);
		menu1.add(menu12);
		menu11.addActionListener(Menuact);
		menu12.addActionListener(Menuact);
		menu13.addActionListener(Menuact);
		frame.setMenuBar(mb);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setBackground(new Color(98, 204, 63));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				btn[i][j] = new JButton();
				btn[i][j].setBounds(i * 150 + 65, j * 100 + 50, 70, 70);
				frame.add(btn[i][j]);
				btn[i][j].addActionListener(act);
			}
		}
		buttonClose();
		frame.setBounds(300, 200, 520, 500);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		timeLabel.setBackground(Color.PINK);
		timeLabel.setFont(new Font("Arial", Font.BOLD, 18));
		timeLabel.setBounds(50, 340, 100, 50);
		frame.add(timeLabel);
		scoreLabel.setBackground(Color.PINK);
		scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
		scoreLabel.setBounds(200, 340, 100, 50);
		frame.add(scoreLabel);
		startButton.setBackground(Color.PINK);
		startButton.setFont(new Font("Arial", Font.BOLD, 18));
		startButton.setBounds(350, 340, 100, 50);
		frame.add(startButton);
		startButton.addActionListener(act);
		closeButton.setBounds(350, 420, 100, 50);
		closeButton.setBackground(Color.PINK);
		closeButton.setFont(new Font("Arial", Font.BOLD, 18));
		closeButton.addActionListener(act);
		frame.add(closeButton);

	}

	static class Action1Lis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new Thread() {
				public void run() {
					JButton btnSorce = (JButton) e.getSource();

					if (btnSorce == startButton && isStart == false) {
						isStart = true;
						buttonOpen();
						now = System.currentTimeMillis();
						now1 = System.currentTimeMillis();
						while ((now1 - now) < (gameTime * 1000)) {
							now1 = System.currentTimeMillis();
							timeLabel.setText(Long.toString((gameTime - (now1 - now) / 1000)));
							if ((gameTime - (now1 - now) / 100) < lastSet) {
								lastSet = (gameTime - (now1 - now) / 100);
								int x = Math.abs(random.nextInt() % 3);
								int y = Math.abs(random.nextInt() % 3);
								int type = Math.abs(random.nextInt() % 3);
								if (isUse[x][y] == false) {
									isUse[x][y] = true;
									btn[x][y].setIcon(new ImageIcon(String.format("%d.png", type)));
									setTime[x][y] = now1;
									btn[x][y].setText(type + "");
								}
							}
							checkSetTime();
						}
						isStart = false;
						buttonClose();
						int save = JOptionPane.showConfirmDialog(null,
								String.format("You got %d points!\r\nWould you save your score?", scoreCounter),
								"Game Over", JOptionPane.YES_NO_OPTION);
						if (save == 0) {
							// chose Yes
							String challenger = JOptionPane.showInputDialog("Please input your name.");
							updateScoreBoard(challenger, scoreCounter);
							selectScoreBoard();

						}
					} else if (btnSorce == closeButton) {
						System.exit(0);
					} else {
						for (int i = 0; i < 3; i++) {
							for (int j = 0; j < 3; j++) {
								if (btnSorce == btn[i][j]) {
									if (isUse[i][j] == true) {
										if (btn[i][j].getText().equals("0")) {
											scoreCounter += 2;
										} else if (btn[i][j].getText().equals("1")) {
											scoreCounter++;
										} else {
											scoreCounter += 3;
										}
										scoreLabel.setText(scoreCounter + "");
										isUse[i][j] = false;
										btn[i][j].setIcon(null);
										btn[i][j].setText(null);
									}
								}
							}

						}
					}
				}
			}.start();
		}
	}

	static class Action2Lis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			MenuItem item = (MenuItem) e.getSource();
			if (item == menu11) {
				selectScoreBoard();
			} else if (item == menu12) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure to exit?", "exit",
						JOptionPane.YES_NO_OPTION) == 0) {
					System.exit(0);
				}
			} else if (item == menu13) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure to clear scoreboard ?", "exit",
						JOptionPane.YES_NO_OPTION) == 0) {
					try {
						PrintWriter out = new PrintWriter("scoreboard.txt");
						out.print("");
						out.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}

	}

	static void buttonOpen() {
		lastSet = Long.MAX_VALUE;
		scoreCounter = 0;
		scoreLabel.setText(scoreCounter + "");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				btn[i][j].setEnabled(true);
				isUse[i][j] = false;
				setTime[i][j] = 0;
			}
		}

	}

	static void buttonClose() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				btn[i][j].setEnabled(false);
				btn[i][j].setIcon(null);
				btn[i][j].setText(null);
			}
		}
	}

	static void checkSetTime() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if ((now1 - setTime[i][j]) / 1000 > Math.random() * 100) {
					btn[i][j].setIcon(null);
					isUse[i][j] = false;
					btn[i][j].setText(null);
				}
			}
		}
	}

	static void selectScoreBoard() {
		String resultString = String.format("%5s\t%40s\t%40s\r\n", "Rank", "Name", "Score");
		try {
			Scanner sc = new Scanner(new File("scoreboard.txt"));
			int cnt = 1;
			while (sc.hasNext()) {
				resultString += String.format("% 5d\t%40s\t% 40d\r\n", cnt++, sc.next(), sc.nextInt());
			}
			sc.close();
			JOptionPane.showMessageDialog(null, resultString, "Scoreboard", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Loading Scoreboard Error", "Error", JOptionPane.ERROR_MESSAGE);

		}
	}
	
	static void updateScoreBoard(String name, int score) {
		try {
			Scanner sc = new Scanner(new File("scoreboard.txt"));
			ArrayList<String> nameList = new ArrayList<String>();
			ArrayList<Integer> scoreList = new ArrayList<Integer>();
			while (sc.hasNext()) {
				nameList.add(sc.next());
				scoreList.add(sc.nextInt());
			}
			nameList.add(name);
			scoreList.add(score);
			sc.close();
			for (int i = 0; i < scoreList.size(); i++) {
				for (int j = 0; j < scoreList.size() - 1; j++) {
					if (scoreList.get(j) < scoreList.get(j + 1)) {
						int temp = scoreList.get(j);
						scoreList.set(j, scoreList.get(j + 1));
						scoreList.set(j + 1, temp);

						String tmp = nameList.get(j);
						nameList.set(j, nameList.get(j + 1));
						nameList.set(j + 1, tmp);
					}
				}
			}
			PrintWriter out = new PrintWriter("scoreboard.txt");
			for (int i = 0; i < scoreList.size(); i++) {
				out.print(nameList.get(i) + " " + scoreList.get(i) + " ");
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
