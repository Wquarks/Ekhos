import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Liste {

	protected static String fileName="yourFileName";
	
	protected static ArrayList<String[]> arrayListMot = new ArrayList<String[]>(); 
	
	public static void addinlist(String txt) throws IOException {
		FileWriter fw=new FileWriter(fileName, true);    
        fw.write("\n"+txt);    
        fw.close();
	}
	
	public static void search(String s){
		for(int i=0; i<arrayListMot.size();i++) {
			int sim = nbsimilarite(Main.string2API(s),arrayListMot.get(i)[1]);
			arrayListMot.get(i)[2]=sim+"";
		}

		//on met la liste dans l'ordre des similarit� d�croisante
		tri();

		for(int i=0; i<arrayListMot.size();i++) {
			String motlist = arrayListMot.get(i)[1];
			arrayListMot.get(i)[2]=nbsimilarite(Main.string2API(s),motlist)+"";
			//System.out.println(s+" vs "+arrayListMot.get(i)[0]+"		"+arrayListMot.get(i)[2]);
		}

	}
	
	/** Methode de tri de l'arrayliste du plus petit au plus grand en fonction du nb de similarit�
	 * @param r le rang a supprim�
	 */
	public static void tri(){

		//suppression de toute les similarit� � un certain rang  => gain de temp dans le tri.
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
	 * @param a le mot � comparer
	 * @param b les mots de la liste
	 * @return le point de similarit�
	 */
	public static int nbsimilarite(String a,String b) { //compare les API des 2 mot
		int similarite=0;
		ArrayList<Character> prisEnCompte = new ArrayList<Character>();
		prisEnCompte.add(' ');

		int enchainement=0;
		for(int aa=1; aa<a.length();aa++) {	
			int soustour=0;
			for(int bb=1; bb<b.length();bb++) {

				//suite de 2 son
				if (a.charAt(aa-1)==b.charAt(bb-1) && a.charAt(aa)==b.charAt(bb) ){
					soustour++;
					//System.out.println(a.charAt(aa-1)+""+a.charAt(aa)+"=="+b.charAt(bb-1)+""+b.charAt(bb)+"#"+enchainement+soustour+similarite); // sonorit� semblable
					similarite+=2;

				}else{
					//System.out.println(a.charAt(aa-1)+""+a.charAt(aa)+"!="+b.charAt(bb-1)+""+b.charAt(bb)+"_"+enchainement+soustour+similarite);
				}

				// son present dans le mot
				if (a.charAt(aa)==b.charAt(bb)) {
					soustour++;
					//System.out.println(a.charAt(aa)+"=="+b.charAt(bb)+"#"+enchainement+soustour+similarite); // sonorit� semblable
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
	
	
	@SuppressWarnings("resource")
	public static void init() throws IOException {
		arrayListMot.clear();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String s;
		while( (s = reader.readLine()) != null) {
			String[] mot = new String[3]; // mot[0] => mot de la liste; mot[1]=> mot transform�; mot[2]=> similarit�
			String str = new String(s.getBytes(),"UTF-8");

			mot[0]=str;
			mot[1]=Main.string2API(str);
			mot[2]="0";	
			arrayListMot.add(mot);
		}
	}
}