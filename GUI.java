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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
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
	private static JTextField textField = new JTextField();
	private static JButton bouton = new JButton("Chercher");
	private static JLabel label = new JLabel("Sonorité :");
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
	private static JMenuItem save = new JMenuItem("save");
	private static JMenuItem del = new JMenuItem("supprimer");
	private static JMenuItem copier = new JMenuItem("copier");
	private static JMenuItem coller = new JMenuItem("coller");
	private static JMenuItem doc = new JMenuItem("Code source");

	private static Color cf = new Color(52,152,219); //couleur foncer
	private static Color cc = new Color(139,195,254); //couleur claire 

	public GUI() {

		fenetre.setJMenuBar(menuBar);
		fenetre.setSize(500,402);
		fenetre.setLocation(500,200);  	
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.add(panel0,BorderLayout.CENTER);

		menuBar.setBorder(null);
		

		textField.setColumns(10);
		textField.setMaximumSize(new Dimension(100000000,30));
		textField.setFocusable(true);
		textField.addKeyListener(listener);
		textField.setBorder(new LineBorder(cf,2)) ;
		
		label.setBorder(new LineBorder(cc,5));

		panel0.add(panel1);
		panel0.add(panel2);
		panel0.add(panel3);
		panel0.setBorder(new LineBorder(cc,15)) ;
		
		panel0.setLayout(new BoxLayout(panel0, BoxLayout.PAGE_AXIS));
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.LINE_AXIS));
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS));
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.LINE_AXIS));
		
		panel1.add(textField);
		panel1.add(bouton);
		panel2.setLayout(new BorderLayout());
		panel2.add(label, BorderLayout.WEST);
		panel3.add(table);

		table.addKeyListener(listener);
		table.setBorder(null);
		
		model.addColumn("Mot");
		model.addColumn("Sonorité");
		model.addColumn("Points de similitude"); 
		
		menu1.add(save);
		menu2.add(del);
		menu2.add(copier);
		menu2.add(coller);
		menu3.add(doc);
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu3);


		//tableau.setRowHeight(20);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(400, 280));
		scroll.setBackground(cc);
		scroll.setBorder(new LineBorder(cc,0));
		
		panel3.add(scroll);

		bouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){actionChercher();}
		});	


		del.addActionListener(new ActionListener() {		      
			public void actionPerformed(ActionEvent arg0) {
				actionDel();
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
	public void actionChercher() {
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
				model.addRow(new Object[]{Liste.arrayListMot.get(y)[0],Liste.arrayListMot.get(y)[1],Liste.arrayListMot.get(y)[2]});
			}
			espacevide();
			label.setText("Sonorité : "+Main.string2API(tf));
		}
	}


	public static void color(){
		
		fenetre.setBackground(cc);
		panel0.setBackground(cc);
		panel1.setBackground(cc);
		panel2.setBackground(cc);
		panel3.setBackground(cc);
		
		textField.setBackground(Color.white);
		bouton.setBackground(cf);
		table.setBackground(Color.white);
		table.getTableHeader().setBackground(cf);
		menuBar.setBackground(cf);
		table.getTableHeader().setForeground(Color.WHITE);
		bouton.setForeground(Color.white);
	}

	public static void espacevide() {
		for(int u=model.getRowCount();u<15;u++){
			model.addRow(new Object[]{"","","0"});
		}
	}

	public KeyListener listener = new KeyListener() {
		private int Codetouche=0;

		//touche enfoncée
		public void keyPressed(KeyEvent e) {Codetouche=e.getKeyCode();}

		//touche relevée
		public void keyReleased(KeyEvent e) {}

		//touche enfoncée puis relevée
		public void keyTyped(KeyEvent e) { 

			if(Codetouche==10) { 	//10 == entrer
				actionChercher();
			}else if(Codetouche==8) {		//8 ==  backspace
				System.out.println(8);
			}else if(Codetouche==127) {		//127 == suppr
				if (table.isFocusOwner()==true) {
					actionDel();
				}
			}
		}
	};	
}
