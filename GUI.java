import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
import javax.swing.table.DefaultTableModel;

public class GUI  {

	private static JFrame fenetre = new JFrame("Ekhos");
	private static JPanel panel = new JPanel();
	private static JTextField textField = new JTextField();
	private static JButton bouton = new JButton("Chercher");
	private static JLabel label = new JLabel("Sonorit� :");
	private static DefaultTableModel model = new DefaultTableModel();
	private static JTable table = new JTable(model);
	private static JMenuBar menuBar = new JMenuBar();
	private static JMenu menu1 = new JMenu("Fichier");
	private static JMenu menu2 = new JMenu("Edition");
	private static JMenu menu3 = new JMenu("?");
	private static JMenuItem save = new JMenuItem("save");
	private static JMenuItem del = new JMenuItem("supprimer");
	private static JMenuItem copier = new JMenuItem("copier");
	private static JMenuItem coller = new JMenuItem("coller");
	private static JMenuItem doc = new JMenuItem("doc");

	public GUI() {
		
		fenetre.setJMenuBar(menuBar);
		fenetre.setSize(500,400);
		fenetre.setLocation(500,200);  	
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.add(panel,BorderLayout.CENTER);

		textField.setColumns(10);

		panel.add(textField);
		panel.add(bouton);
		panel.add(label);
		panel.add(table);

		model.addColumn("Mot");
		model.addColumn("Sonorit�");
		model.addColumn("Points de similitude"); 

		menu1.add(save);
		menu2.add(del);
		menu2.add(copier);
		menu2.add(coller);
		menu3.add(doc);
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu3);

		//addinlist("");
		
		
		//tableau.setRowHeight(20);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(400, 280));
		panel.add(scroll);

		bouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
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
					label.setText("sonorit� : "+Main.string2API(tf));
				}
			}
		});	


		del.addActionListener(new ActionListener() {		      
			public void actionPerformed(ActionEvent arg0) {
				while (table.getSelectedRow() != -1) {
						model.removeRow(table.getSelectedRow());
				}
				espacevide();
			}
		});


		espacevide();
		color();
		fenetre.setVisible(true);
		
	}
	
	public static void color(){
		Color bc = new Color(52,152,219);
		panel.setBackground(new Color(139,195,254));
		textField.setBackground(Color.white);
		bouton.setBackground(bc);
		table.setBackground(Color.white);
		table.getTableHeader().setBackground(bc);
		menuBar.setBackground(bc);
		table.getTableHeader().setForeground(Color.WHITE);
		bouton.setForeground(Color.white);
	}

	

	public static void espacevide() {
		for(int u=model.getRowCount();u<=15;u++){
			//String[] mot = {"","","0"};
			//arrayListMot.add(arrayListMot.size(),mot);
			model.addRow(new Object[]{"","","0"});
		}
	}
}
