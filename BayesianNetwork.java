
import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Iterator;
//import java.util.Scanner;
//import javax.management.loading.PrivateClassLoader;
import java.util.HashMap;
import java.util.Iterator;

;
public class BayesianNetwork {
	protected HashMap <String, Node> nodesHash;
	
	private BayesianNetwork(){
		nodesHash = new HashMap<String, Node>();
	}
	
	private String toSrting() {
		String st = "{ ";
		Iterator<Node> itr = this.iteretor(); 
		while (itr.hasNext()) {
			Node mapNode = itr.next(); 
			st +=mapNode.toSrting()+", ";
		}
		st = st.substring(0, st.length()-2)+" }";
		return st;
	}
	
	private static void bagining(BufferedReader br, File file, BayesianNetwork BN) throws IOException {
		String st;
		boolean Dbag = false; // false  true
		if((st = br.readLine()) == null || !st.equals("Network")) { 
			throw new RuntimeException("The file input: isn't a representation of Bayesian Network" );
		}
		if( (st = br.readLine()) != null && !(st.length() <= 12) && (String) st.subSequence(0, 11) != "Variables: ") {   //
			if(Dbag){System.out.println(st.subSequence(0, 11));}
			try {
				String[] verticesNames = ((String) st.subSequence(11, st.length())).split(",");
				for (int i = 0; i < verticesNames.length; i++) {
					BN.nodesHash.put(verticesNames[i], new Node(verticesNames[i]));
				}
				if(Dbag){System.out.println(BN.toSrting());}
			} catch (RuntimeException e) {
				throw new RuntimeException("The string: "+ st +" isn't represent Bayesian Network vertices.");
			}
		} else {
			if(Dbag){System.out.println(st.subSequence(0, 11));}
			throw new RuntimeException("The file input: isn't a represent of Bayesian Network" );
		}
		if((st = br.readLine()) == null || !st.isEmpty() ) {
			throw new RuntimeException("The file input isn't a represent of Bayesian Network" );
		}
	}
	
	public Iterator<Node> iteretor() {
		Iterator<Node> itr = this.nodesHash.values().iterator();
		return itr;
	}
	
	private static void verticesBuild(BufferedReader br, File Readingfile, BayesianNetwork BN) throws IOException {
		String st ;
		boolean Dbag = true; // true || false
		st = br.readLine();
		Iterator<Node> itr = BN.iteretor(); 
		while (itr.hasNext()) {
			Node mapNode = itr.next();	
			if(st.length() != 5 || !st.substring(0,4).equals("Var ")) {//
				if(Dbag){System.out.println(st);}
				throw new RuntimeException("This isn't a Var like suppose to be.");
			}
			if(st.substring(0,4) == "Var " && st.substring(5,st.length()) != mapNode.name ) {
				if(Dbag){System.out.println();}
				throw new RuntimeException("The Var "+mapNode+" suppose to be here insted we got: "+st.substring(5,st.length()) );
			}
			st = br.readLine();
			
			if (st.length() < 9 || !st.substring(0,8).equals( "Values: ")) {
				if(Dbag){System.out.println(st.substring(0,8)+!st.substring(0,8).equals( "Values: "));}
				throw new RuntimeException("This isn't a Values like suppose to be.");
			}
			String[] VarValues = st.substring(8,st.length()).split(",");
			mapNode.VarValues = VarValues;
			st = br.readLine();
			
			if (st.length() < 10 || !st.substring(0,9).equals("Parents: ")) {
				if(Dbag){System.out.println();}
				throw new RuntimeException("This isn't a Parents like suppose to be.");
			}
			String[] ParentsNames = st.substring(9,st.length()).split(",");
			mapNode.ParentsNames = ParentsNames;
			
			mapNode.numOfParents = ParentsNames.length;
			if(mapNode.numOfParents == 1 && ParentsNames[0].equals("none")) {
				mapNode.numOfParents = 0;
			}
			mapNode.Parents = new Node[mapNode.numOfParents];
			for (int i = 0; i < mapNode.numOfParents; i++) {
				mapNode.Parents[i] = BN.nodesHash.get(ParentsNames[i]);
			}
			
			if(Dbag){System.out.println("Number of parents = "+mapNode.numOfParents);}
			st = br.readLine();
			if(mapNode.numOfParents > 0) {
				for (int i = 0; i < ParentsNames.length; i++) {
					BN.nodesHash.get(ParentsNames[i]).SonsNames.add(mapNode.name);
				}
			}
			if (!st.equals("CPT:")) {
				if(Dbag){System.out.println();}
				throw new RuntimeException("This isn't a CPT like suppose to be.");
			}
			mapNode.getCPT(br, BN);   ///////////////////**********************************
			st = br.readLine();
			System.out.println(st); ///"the next line is: "+
			if(st == null || !st.isEmpty() ) {
				if(Dbag){System.out.println(st);}
				throw new RuntimeException("");
			}else if(itr.hasNext())
				st = br.readLine();
		}
	}
	
	private static File writeOutpotFile(BufferedReader br, File Readingfile, BayesianNetwork BN) throws IOException {
		String st = br.readLine();// = "regqerge\ndhoghlds";
		if(!st.equals("Queries")) {
			System.out.println(st);
			throw new RuntimeException("This isn't the Queries part.");
		}
		
		File output = new File(st);
		System.out.println(output);
		return output;
	}
	
	public static void main(String[] args) throws IOException  {
		File file = new File("C:\\Users\\user\\Desktop\\files to run\\java\\AI algoritems\\input.txt");
		BayesianNetwork BN = new BayesianNetwork();
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		bagining(br, file, BN);
		verticesBuild(br, file, BN);
		File OutpotFile = writeOutpotFile(br, file, BN);
		br.close();
		for (int i = 0; i < BN.nodesHash.get("A").CPT.length; i++) {
			System.out.print("[");
			for (int j = 0; j < BN.nodesHash.get("A").CPT[0].length; j++) {
				System.out.print(BN.nodesHash.get("A").CPT[i][j]);
				if(j != BN.nodesHash.get("A").CPT[0].length-1)
					System.out.print(", ");
			}
			System.out.println("]");
		}
		
	}
	
}
