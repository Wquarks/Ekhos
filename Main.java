import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * @author Wquarks
 * @version 4.2
 */

public class Main {

	private static ArrayList<String[]> arrayListMot = new ArrayList<String[]>(); 
	private static String fileName="yourFileName.txt";
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		JFrame fenetre = new JFrame("Ekhos");
		fenetre.setSize(500,400);
		fenetre.setLocation(500,200);  	
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



		JPanel panel = new JPanel();


		JTextField textField = new JTextField();
		textField.setColumns(10);

		panel.add(textField);
		fenetre.add(panel,BorderLayout.CENTER);

		JButton bouton = new JButton("Chercher");
		panel.add(bouton);

		JLabel label = new JLabel("Sonorité :");

		panel.add(label);

		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		panel.add(table);

		model.addColumn("Mot");
		model.addColumn("Sonorité");
		model.addColumn("Nb de similitude"); 

		//tableau.setRowHeight(20);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(400, 300));
		panel.add(scroll);




		init();

		// on compare a tous les mot de la liste 

		bouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if(!textField.getText().isEmpty()) {
					String tf = textField.getText();

					//flush
					while( model.getRowCount() > 0) {
						model.removeRow(0);
					}
					// mise en place du texte

					search(tf);	
					for(int y =0;y<arrayListMot.size();y++) {
						model.addRow(new Object[]{arrayListMot.get(y)[0],arrayListMot.get(y)[1],arrayListMot.get(y)[2]});
					}

					label.setText("sonorité : "+string2API(tf));
				}
			}
		});	


		fenetre.setVisible(true);
	}

	public static void search(String s){
		for(int i=0; i<arrayListMot.size();i++) {
			int sim = nbsimilarite(string2API(s),arrayListMot.get(i)[1]);
			arrayListMot.get(i)[2]=sim+"";
		}

		//on met la liste dans l'ordre des similarité croisante
		tri();

		for(int i=0; i<arrayListMot.size();i++) {
			String motlist = arrayListMot.get(i)[1];
			arrayListMot.get(i)[2]=nbsimilarite(string2API(s),motlist)+"";
			System.out.println(s+" vs "+arrayListMot.get(i)[0]+"		"+arrayListMot.get(i)[2]);
		}

		System.out.println("plus petit rang garder : "+arrayListMot.get(0)[2]+"\nmot choisie:"+string2API(s)+"\nnombre de sonorité dans le mot choisie :"+string2API(s).length());
	}


	/** Methode de tri de l'arrayliste du plus petit au plus grand en fonction du nb de similarité
	 * @param r le rang a supprimé
	 */
	public static void tri(){
		int u=0;
		//recherche du plus grand rang de similarité
		for(int i=0; i<arrayListMot.size();i++) {			
			if (Integer.parseInt(arrayListMot.get(i)[2])>u) {
				u++;
			}
		}
		if(u<2) { // compansation pour ne pas descendre en dessous de 0
			u+=2;
		}
		//suppression de toute les similarité à un certain rang  => gain de temp dans le tri.
		for(int i=0; i<arrayListMot.size();i++) {
			if (Integer.parseInt(arrayListMot.get(i)[2])<u-2) {
				arrayListMot.remove(i);
				i--;
			}
		}

		boolean correction = true;
		while (correction==true)  {
			correction=false;
			for(int n=1; n<arrayListMot.size();n++) {
				if ( Integer.parseInt(arrayListMot.get(n-1)[2]) < Integer.parseInt(arrayListMot.get(n)[2])) {
					arrayListMot.add(0,arrayListMot.get(n));
					arrayListMot.remove(n+1);
					correction = true;
				}	
			}
		}

	}


	@SuppressWarnings("resource")
	public static void init() throws IOException {
		arrayListMot.clear();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String s;
		while( (s = reader.readLine()) != null) {
			String[] mot = new String[3]; // mot[0] => mot de la liste; mot[1]=> mot transformé; mot[2]=> similarité
			String str = new String(s.getBytes(),"UTF-8");

			mot[0]=str;
			mot[1]=string2API(str);
			mot[2]="0";	
			arrayListMot.add(mot);
		}
	}

	public static int nbsimilarite(String a,String b) { //compare les API des 2 mot
		int similarite=0;
		ArrayList<Character> prisEnCompte = new ArrayList<Character>();
		prisEnCompte.add(' ');

		for(int aa=0; aa<a.length();aa++) {
			for(int bb=0; bb<b.length();bb++) {
				if (a.charAt(aa)==b.charAt(bb)) {
					//System.out.println(b.charAt(bb)); // sonorité semblable
					boolean aajouter=true;
					for(char pec: prisEnCompte) {
						if (a.charAt(aa)== pec){ 
							aajouter=false;
						}
					}
					if (aajouter==true){ 
						prisEnCompte.add(a.charAt(aa));
						similarite++;
					}
				}
			}
		}		
		return similarite;
	}


	// string 2 API 
	public static String string2API(String a) {
		String aa = a.toLowerCase();
		char[] charArray = aa.toCharArray();
		String word = "";

		for (int i=0;i<charArray.length;i++) {
			String phon=".";

			switch (charArray[i]) {

			case 'a':case 'à':
				phon="a";
				if (i+2<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]+""+charArray[i+2]) {
					case "aim":
						if ((i-1>=0)&&(charArray[i-1]=='f')) {
							phon="ɛ̃";
							i+=2;
						}
						break;
					case "ain":
						phon="ɛ̃";
						i+=2;
						break;
					}
				}
				if (i+1<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]) {
					case "an":
						if ((i+2<charArray.length) && type(charArray[i+2]).equals("voyelle")) {
							phon="a";
						}else {
							phon="ɑ̃";
							i++;
						}
						break;
					case "am":
						if ((i+2<charArray.length) &&(charArray[i+2]=='b' || charArray[i+2]=='p' )) {
							phon="ɑ̃";
							i++;
						}else {
							phon="a";
						}
						break;
					case "ai":

						if (i+3<charArray.length && (charArray[i]+""+charArray[i+1]+""+charArray[i+2]+""+charArray[i+3]).equals("aill")) {
							phon="aj";
							i+=3;
						}else {
							phon="ɛ";
							i++;
						}
						break;
					case "au":
						phon="o";
						i++;
						break;
					}

				}
				break;

			case 'i': case 'y': case'ï': case'î' :
				phon="i";

				if (i+1<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]) {
					case "in":
						if ((i+2<charArray.length) && type(charArray[i+2]).equals("voyelle")) {
							phon="i";
						}else {
							phon="ɛ̃";
							i++;
						}
						break;
					case "im":
						if ((i+2<charArray.length) &&(charArray[i+2]=='b' || charArray[i+2]=='p' )) {
							phon="ɛ̃";
							i++;
						}else {
							phon="i";
						}
						break;
					case "ie":
						phon="i";
						i++;
						break;
					case "iè":
						phon="jɛ";
						i++;
						break;
					case "io":
						phon="jɔ";
						i++;
						break;
					}
				}
				break;

			case 'o':case 'ô':
				phon="o";
				if (i+1<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]) {
					case "ou":case "où":
						phon="u";
						i++;
						break;
					case "oi":
						phon="wa";
						i++;
						break;
					case "on":
						phon="ɔ̃";
						i++;
						break;
					case "om":
						if ((i+2<charArray.length) &&(charArray[i+2]=='b' || charArray[i+2]=='p' )) {
							phon="ɔ̃";
							i++;
						}else {
							phon="ɔ";
						}
						break;
					case "or":
						phon="ɔr";
						i++;
						break;
					case "ot":
						phon="ɔt";
						i++;
						break;
					}
				}
				if (i+1<charArray.length && i-1>=0) {
					switch (charArray[i-1]+""+charArray[i]+""+charArray[i+1]) {
					case "oue":
						if(i==1) {
							phon="w";
						}else {
							phon="u";
						}
						break;
					case "oui":
						phon="wi";

						break;
					case "oin":
						phon="wɛ̃";
						break;
					case "ong":
						if ((i+3<charArray.length) && type(charArray[i+3]).equals("voyelle")) {
							phon="ɔ̃";
							i++;
						}else {
							phon="ɔ̃";
						}
						break;
					case "omm":
						phon="ɔm";
						i++;
						break;
					case "onn":
						phon="ɔn";
						i++;
						break;
					case "one":
						if (i+2 ==charArray.length) {
							phon="ɔn";
							i++;
							break;
						}else if (charArray[charArray.length-1]=='s') {
							phon="ɔn";
							i++;
							break;
						}
					}


				}
				if (i+2<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]+""+charArray[i+2]) {
					case "oqu":
						phon="ɔk";
						i+=2;
						break;
					}
				}
				break;


			case 'u':case 'û':case 'ü':
				phon="y";
				if (i+1<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]) {
					case "un":
						if ((i+2<charArray.length) && type(charArray[i+2]).equals("voyelle")) {
							phon="y";
						}else {
							phon="ɛ̃";
							i++;
						}
						break;
					case "um":

						if ((i+2<charArray.length) &&(charArray[i+2]=='b' || charArray[i+2]=='p' )) {
							phon="ɛ̃";
							i++;
						}else {
							phon="y";
						}
						break;
					case "ui":
						phon="ɥ";
						break;
					}
				}
				break;

			case 'e':
				phon="ə";
				if (i==charArray.length-1) {
					phon="";
				}
				if ((i+5==charArray.length-1)) {
					if ((charArray[i]+""+charArray[i+1]+""+charArray[i+2]+""+charArray[i+3]+""+charArray[i+4]+""+charArray[i+5]).equals("emment")) {
						phon="amɑ̃";	
						i+=5;
						break;
					}
				}
				if (i+2<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]+""+charArray[i+2]) {
					case "ein":
						phon="ɛ̃";
						i+=2;
						break;
					case "euf":
						phon="œf";
						i+=2;
						break;
					case "ett":
						phon="ɛt";
						i+=2;
						break;
					case "est":
						phon="ɛst";
						i+=2;
						break;
					case "err":
						phon="ɛʁ";
						i+=2;
						break;
					case "eau":
						phon="o";
						i+=2;
						break;
					case "emp" :
						if ((i==charArray.length-4) ||(i==charArray.length-2 && charArray.length-1=='s')) {
							phon="ɑ̃";
							i+=2;
						}else {
							phon="ɑ̃";
							i++;
						}
						break;			
					}
				}
				if (i+1<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]) {
					case "en":						
						if ((i+2<charArray.length) && type(charArray[i+2]).equals("voyelle")) {
							phon="ə";
						}else {
							phon="ɑ̃";
							i++;
						}
						break;
					case "et":			
						if ((i+1<charArray.length)) {
							if ((i+2<charArray.length) && type(charArray[i+2]).equals("voyelle")) {
								phon="ə";
							}else {
								phon="ɛ";
								i++;
							}
						}
						break;
					case "ei":			
						phon="e";
						i++;
						break;
					case "es":			
						if ((i+1==charArray.length-1)) {
							phon="";
							i++;
						}
						break;
					case "er":			
						if ((i+1==charArray.length-1)) {
							phon="e";
							i++;
						}
						break;
					case "eu":
						phon="ø";
						i++;
						break;
					case "ex":
						phon="ɛ";
						break;
					}
				}
				break;

			case 's':
				phon="s";
				if (i==charArray.length-1) {
					phon="";
				}
				if (i+1<charArray.length ){
					switch (charArray[i]+""+charArray[i+1]) {
					case "ss":	
						phon="s";
						i++;
						break;
					case "sh":	
						phon="ʃ";
						i++;
						break;
					}
					if (i-1>=0 && type(charArray[i-1]).equals("voyelle") && type(charArray[i+1]).equals("voyelle")) {
						phon="z";	
						break;
					}
				}
				break;

			case 't':
				phon="t";
				if (i==charArray.length-1) {
					phon="";
				}
				if (i+3<charArray.length){
					if ((charArray[i]+""+charArray[i+1]+""+charArray[i+2]).equals("tio")) {
						phon="sj";	
						i++;
						break;
					}
				}
				if ((i+1<charArray.length)&&(i+1==charArray.length-1)&&(charArray[i+1] =='s')){
					phon="";
					i++;
				}
				break;

			case 'p':
				phon="p";
				if (i+1<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]) {
					case "ph":
						phon="f";
						i++;
						break;
					}
				}
				break;

			case 'c':
				phon="k";
				if (i+1<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]) {
					case "ce":case "ci":
						phon="s";
						break;
					case "ch":
						phon="ʃ";
						i++;
						break;
					case "ck":
						phon="k";
						i++;
						break;
					}
				}
				break;

			case 'g':
				phon="ʒ";
				if (i+1<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]) {
					case "ga":case "go":case "gu":
						phon="g";
						break;
					case"gn":
						phon="ɲ";
						break;
					}
				}
				break;

			case 'm':
				phon="m";
				break;

			case 'q':
				phon="q.";
				if (i+1<charArray.length) {
					switch (charArray[i]+""+charArray[i+1]) {
					case "qu":
						phon="k";
						i++;
						break;
					}
				}
				break;

			case 'n':
				phon="n";
				break;

			case 'r':
				phon="ʁ";
				break;

			case 'j':
				phon="ʒ";
				break;

			case 'b':
				phon="b";
				break;

			case 'k':
				phon="k";
				break;

			case 'œ':
				phon="œ";
				break;

			case 'f':
				phon="f";
				if ((i+4<=charArray.length-1)) {
					if ((charArray[i]+""+charArray[i+1]+""+charArray[i+2]+""+charArray[i+3]+""+charArray[i+4]).equals("femme")) {
						phon="fam";	
						i+=5;
						break;
					}
				}
				break;

			case 'x':
				phon="ɡz";
				if (i==charArray.length-1) {
					phon="";
				}
				if ((i+1<charArray.length)&&(i-1>=0)&&(charArray[i+1] =='i')) {
					phon="zj";
					i++;
				}
				break;

			case 'h':
				phon="h.";
				if (charArray[0]=='h') {
					phon="";
				}
				break;

			case 'z':
				phon="z";
				break;

			case 'l':
				phon="l";
				if (i+1<charArray.length) {
					if (i-1>=0) {
						if ((charArray[i]+""+charArray[i+1]).equals("ll")) {
							phon="l";
							switch (charArray[i-1]+""+charArray[i]+""+charArray[i+1]) {
							case "ill":
								phon="j";
								if (i-2>=0) {
									if((charArray[i-2]=='m') || (charArray[i-2]=='v')){
										phon="l";
										break;
									}
								}
								if (i-7>=0) {
									if((charArray[i-7]+""+charArray[i-6]+""+charArray[i-5]+""+charArray[i-4]+""+charArray[i-3]+""+charArray[i-2]).equals("tranqu")){
										phon="l";
										i++;
										break;
									}
								}
								i++;
								break;
							}
							i++;
						}
					}
				}
				break;

			case 'v':
				phon="v";
				break;

			case 'w':
				phon="w";
				break;

			case 'é':
				phon="e";
				break;

			case 'è':case 'ê':case'ë':
				phon="ɛ";
				break;

			case 'd':
				phon="d";
				break;

			case 'ç':
				phon="s";
				break;

			case 'â':
				phon="a";
				break;	
			case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
				phon="_";
				break;	

			case ' ':case '-':case '\'':case '.':case ';':case '!':
				phon=" ";
				break;

			default:
				phon="*"+charArray[i]+"*";
			}
			word += phon;

		}
		return word;
	}


	public static String type(char c) {
		String res="consonne";
		char[] vliste= {'a','à','e','é','è','y','u','i','î','ï','o'};
		for(char i : vliste) {
			if(c==i) {
				res="voyelle";
			}else if(c==' '){
				res="espace";
			}
		}
		return res;
	}

}
