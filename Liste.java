import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public abstract class Liste {



	protected static ArrayList<String[]> arrayListMot = new ArrayList<String[]>(); 


	public static void addinlist(String txt) throws IOException {

		ArrayList<String> AL = new ArrayList<String>(); 

		BufferedReader reader = new BufferedReader(new FileReader(Main.file.getAbsoluteFile()));
		String s;


		while( (s = reader.readLine()) != null) {
			AL.add(s);
		}
		reader.close();

		boolean present = false;
		for(String word : AL) {
			if(word.equalsIgnoreCase(txt)) {
				present = true;
				break;
			}
		}
		if(present==false) {
			AL.add(txt);
		}

		reWriteListeMot(AL);

	}

	public static void delinlist(String txt) throws IOException {

		ArrayList<String> AL = new ArrayList<String>(); 

		BufferedReader reader = new BufferedReader(new FileReader(Main.file.getAbsoluteFile()));
		String s;


		while( (s = reader.readLine()) != null) {
			AL.add(s);
		}
		reader.close();

		int i =0;
		for(String word : AL) {
			if(word.equalsIgnoreCase(txt)) {
				AL.remove(i);
				break;
			}
			i++;
		}


		reWriteListeMot(AL);

	}
	public static int nbMots() {

		int nb=0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(Main.file.getAbsoluteFile()));
			while ( in.readLine() != null){
				nb++;
			}
			in.close();
		} catch (IOException e) {e.printStackTrace();}
		return nb;
	}

	public static void start() {
		if (!Main.file.exists()) {
			try {
				Main.file.createNewFile();

				BufferedReader in = new BufferedReader(new InputStreamReader(Liste.class.getResourceAsStream("liste_mots.txt")));
				String line;

				FileWriter fw = new FileWriter(Main.file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);

				boolean firstLine=true;
				while ((line = in.readLine()) != null){

					if(firstLine==false) {
						bw.write("\n");
					}else{
						firstLine=false;
					}
					System.out.println (line);
					bw.write(line);

				}
				in.close();
				bw.close();

			} catch (IOException e) {}
		}
		GUI.actionChercher();
	}


	public static void reWriteListeMot(ArrayList<String> ALS) throws IOException{

		FileWriter fw = new FileWriter(Main.file);
		BufferedWriter bw = new BufferedWriter(fw);
		for(int u = 0;u< ALS.size();u++) {
			String s = new String(ALS.get(u).getBytes(),"UTF-8");

			if(u!=0) {
				bw.write("\n");    
			}
			bw.write(s);   
			bw.flush();

			System.out.println(s);
		}

		bw.close();

	}

	public static void search(String s){
		for(int i=0; i<arrayListMot.size();i++) {
			int sim = nbsimilarite(Main.string2API(s),arrayListMot.get(i)[1]);
			arrayListMot.get(i)[2]=sim+"";
		}

		//on met la liste dans l'ordre des similarité décroisante
		tri();

		for(int i=0; i<arrayListMot.size();i++) {
			String motlist = arrayListMot.get(i)[1];
			arrayListMot.get(i)[2]=nbsimilarite(Main.string2API(s),motlist)+"";
		}

	}

	/** Methode de tri de l'arrayliste du plus petit au plus grand en fonction du nb de similarité
	 * @param r le rang a supprimé
	 */
	public static void tri(){

		//suppression de toute les similarité à un certain rang  => gain de temp dans le tri.
		for(int i=0; i<arrayListMot.size();i++) {
			if (Integer.parseInt(arrayListMot.get(i)[2])<1) {
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

	/**
	 * @param a le mot à comparer
	 * @param b les mots de la liste
	 * @return le point de similarité
	 */
	public static int nbsimilarite(String a,String b) { //compare les API des 2 mot
		if(GUI.rb0.isSelected()) {
			return algo1(a,b);
		}else {

			return algo2(a,b);
		}
	}

	public static int algo1(String a,String b) {
		int similarite=0;
		ArrayList<Character> prisEnCompte = new ArrayList<Character>();
		prisEnCompte.add(' ');

		int enchainement=0;

		for(int aa=1; aa<a.length();aa++) {	
			int soustour=0;
			//suite de 2 son
			for(int bb=1; bb<b.length();bb++) {				
				if (a.charAt(aa-1)==b.charAt(bb-1) && a.charAt(aa)==b.charAt(bb) ){
					soustour++;
					similarite+=2;
				}
			}
			if(soustour>0) {
				similarite+=enchainement++;
			}else {
				enchainement=0;
				similarite--;
			}
		}	

		// sons present dans le mot
		for(int aa=0; aa<a.length();aa++) {	
			int soustour=0;
			for(int bb=0; bb<b.length();bb++) {

				// sons present dans le mot
				if (a.charAt(aa)==b.charAt(bb)) {
					soustour++;
					//System.out.println(a.charAt(aa)+"=="+b.charAt(bb)+"#"+enchainement+soustour+similarite); // sonorité semblable
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
			if(soustour>0) {
				similarite+=enchainement++;
			}else {
				enchainement=0;
				similarite--;
			}

		}		

		similarite-=b.length()-a.length();
		return similarite;
	}

	public static int algo2(String a,String b) {
		int similarite=0;
		int chance=0;
		int longueur=0;

		int cal = a.length()-b.length();
		if (cal!=0){
			return 0;
		}

		if(a.length()<b.length()) {
			longueur=a.length();
		}else {
			longueur=b.length();
		}
		for(int l=0; l<longueur;l++) {
			if (a.charAt(l)==b.charAt(l)) {
				similarite++;
			}else {
				if(chance>0) {
					chance--;
					similarite++;
				}
			}
		}

		return similarite;
	}


	public static void init() throws IOException {
		arrayListMot.clear();

		BufferedReader reader = new BufferedReader(new FileReader(Main.file.getAbsoluteFile()));
		String s;
		while( (s = reader.readLine()) != null) {
			String[] mot = new String[3]; // mot[0] => mot de la liste; mot[1]=> mot transformé; mot[2]=> similarité
			String str = new String(s.getBytes(),"UTF-8");

			mot[0]=str;
			mot[1]=Main.string2API(str);
			mot[2]="0";	
			arrayListMot.add(mot);
		}
		reader.close();
	}
}
