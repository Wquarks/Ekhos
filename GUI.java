import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class GUI {

	private static JFrame fenetre = new JFrame("Ekhos");
	private static JPanel panel0 = new JPanel();
	private static JPanel panel1 = new JPanel();
	private static JPanel panel2 = new JPanel();
	private static JPanel panel3 = new JPanel();
	private static JPanel panel4 = new JPanel();
	private static JTextField textField = new JTextField();
	private static JButton bouton = new JButton("Chercher");
	private static JLabel label = new JLabel("Sonorité : None");
	private static DefaultTableModel model = new DefaultTableModel() {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int col) {
			return false;
		};
	};
	private static JTable table = new JTable(model);
	private static JMenuBar menuBar = new JMenuBar();
	private static JMenu menu1 = new JMenu("Fichier");
	private static JMenu menu2 = new JMenu("Edition");
	private static JMenu menu3 = new JMenu("?");
	private static JMenuItem add = new JMenuItem("Ajouter un nouveau mot");
	private static JMenuItem del = new JMenuItem("Supprimer un mot");
	private static JMenuItem flush = new JMenuItem("Réinitialiser la liste");
	private static JMenuItem doc = new JMenuItem("Code source");
	private static JMenuItem help = new JMenuItem("Aide");

	private static ButtonGroup bg=new ButtonGroup();  	
	private static Color cf = new Color(52,152,219); //couleur foncer
	private static Color cc = new Color(139,195,254); //couleur claire 

	protected static JRadioButton rb0 = new JRadioButton("Mot semblable");
	protected static JRadioButton rb1 = new JRadioButton("Similarité sonore");


	public GUI() {

		fenetre.setJMenuBar(menuBar);
		fenetre.setSize(500,404);
		fenetre.setMinimumSize(new Dimension(300, 300));
		fenetre.setLocation(500,200);  	
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.add(panel0,BorderLayout.CENTER);

		menuBar.setBorder(null);

		textField.setColumns(10);
		textField.setMaximumSize(new Dimension(100000000,30));
		textField.setFocusable(true);
		textField.addKeyListener(listener);
		textField.setBorder(new LineBorder(cf,2)) ;

		panel1.setBorder(new LineBorder(cc,2));
		panel2.setBorder(new LineBorder(cc,2));
		panel3.setBorder(new LineBorder(cc,2));

		panel0.add(panel1);
		panel0.add(panel2);
		panel0.add(panel3);
		panel0.add(panel4);
		panel0.setBorder(new LineBorder(cc,15)) ;

		panel0.setLayout(new BoxLayout(panel0, BoxLayout.PAGE_AXIS));
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.LINE_AXIS));
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS));
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.LINE_AXIS));
		panel4.setLayout(new BoxLayout(panel4, BoxLayout.LINE_AXIS));


		bg.add(rb0);
		bg.add(rb1);	
		rb0.setSelected(true);

		panel1.add(textField);
		panel1.add(bouton);
		panel2.setLayout(new BorderLayout());
		panel2.add(label, BorderLayout.WEST);
		panel3.add(rb0,BorderLayout.WEST);
		panel3.add(rb1,BorderLayout.EAST);
		panel4.add(table);

		table.addKeyListener(listener);
		table.setBorder(null);

		model.addColumn("Mot");
		model.addColumn("Sonorité");
		model.addColumn("Points de similitude"); 


		menu1.add(flush);
		menu2.add(add);
		menu2.add(del);
		menu3.add(help);
		menu3.add(doc);
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu3);

		//tableau.setRowHeight(20);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBackground(cc);
		scroll.setBorder(new LineBorder(cc,0));

		panel4.add(scroll);

		add.addActionListener(new ActionListener() {		      
			public void actionPerformed(ActionEvent arg0) {
				String motAAdd= JOptionPane.showInputDialog(null, "Veuilliez saisir le mot à ajouter", "Ajouter", JOptionPane.QUESTION_MESSAGE);
				System.out.println("Mot à add : "+ motAAdd);
				try {
					Liste.addinlist(motAAdd);
					actionChercher();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}); 

		flush.addActionListener(new ActionListener() {		      
			public void actionPerformed(ActionEvent arg0) {
				int option = JOptionPane.showConfirmDialog(null,"Etes vous sûr de vouloir réinitialiser la liste ?","Réinitialisation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(option == JOptionPane.OK_OPTION){
					Main.file.delete();
					Liste.start();
				}
			}
		}); 
		
		help.addActionListener(new ActionListener() {		      
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Voici quelque astuces : \n "
						+ "- La touche ENTRER pour lancer la recherche \n"
						+ "- La touche SUPPR pour suprimmer un mot dans la liste (cela le retirera juste de la recherche actuelle) \n"
						+ "\n"
						+ "Pour information : \n "
						+ "- La liste contient actuellement : "+Liste.nbMots()+" mots \n"
						+ "- Auteur : Wquarks \n"
						+ "- Version : "+Main.version, "Aide",JOptionPane.INFORMATION_MESSAGE);
						
			}
		}); 

		bouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionChercher();
			}
		});	

		rb0.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionChercher();
			}
		});	
		rb1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionChercher();
			}
		});	


		del.addActionListener(new ActionListener() {		      
			public void actionPerformed(ActionEvent arg0) {
				String motADel= JOptionPane.showInputDialog(null, "Veuilliez saisir le mot à supprimer", "Supprimer", JOptionPane.QUESTION_MESSAGE);
				System.out.println("Mot à del : "+ motADel);
				try {
					Liste.delinlist(motADel);
					actionChercher();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}); 

		doc.addActionListener(new ActionListener() {		      
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URL("https://github.com/Wquarks/Ekhos").toURI());
				} catch (Exception e) {}
			}
		});

		espacevide();
		color();
		fenetre.setVisible(true);




	}



	public void actionDel() {
		while (table.getSelectedRow() != -1) {
			model.removeRow(table.getSelectedRow());
		}
		espacevide();
	}
	public static void actionChercher() {
		try {
			Liste.init();
		}catch (IOException e1) {
			e1.printStackTrace();
		}

		if(!textField.getText().isEmpty()) {
			String tf = textField.getText();

			//flush
			while( model.getRowCount() > 0) {
				model.removeRow(0);
			}

			// mise en place du texte

			Liste.search(tf);	
			for(int y =0;y<Liste.arrayListMot.size();y++) {
				model.addRow(new Object[]{Liste.arrayListMot.get(y)[0],Main.patchAPI(Liste.arrayListMot.get(y)[1]),Liste.arrayListMot.get(y)[2]});
			}
			espacevide();

			String word = Main.string2API(tf);
			if(word.equals("")) {
				word="None";
			}
			label.setText("Sonorité : ["+Main.patchAPI(word)+"]");
		}
	}


	public static void color(){

		fenetre.setBackground(cc);
		panel0.setBackground(cc);
		panel1.setBackground(cc);
		panel2.setBackground(cc);
		panel3.setBackground(cc);
		panel4.setBackground(cc);
		rb0.setBackground(cc);
		rb1.setBackground(cc);


		menu1.setForeground(Color.white);
		menu2.setForeground(Color.white);
		menu3.setForeground(Color.white);

		textField.setBackground(Color.white);
		bouton.setBackground(cf);
		table.setBackground(Color.white);
		table.getTableHeader().setBackground(cf);
		menuBar.setBackground(cf);
		table.getTableHeader().setForeground(Color.WHITE);
		bouton.setForeground(Color.white);
	}

	public static void espacevide() {
		for(int u=model.getRowCount();u<14;u++){
			model.addRow(new Object[]{"","","0"});
		}
	}

	public KeyListener listener = new KeyListener() {
		private int Codetouche=0;

		//touche enfoncée
		public void keyPressed(KeyEvent e) {
			Codetouche=e.getKeyCode();
			//System.out.println(e.getKeyCode());
		}

		//touche relevée
		public void keyReleased(KeyEvent e) {}

		//touche enfoncée puis relevée
		public void keyTyped(KeyEvent e) { 

			if(Codetouche==10) { 	//10 == entrer
				actionChercher();
			}else if(Codetouche==27) {		//27 ==  echap
				textField.requestFocus();
			}else if(Codetouche==127) {		//127 == suppr
				if (table.isFocusOwner()==true) {
					actionDel();
				}
			}
		}
	};	
}
