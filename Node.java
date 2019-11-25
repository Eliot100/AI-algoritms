import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Node {

	String[] ParentsNames;
	Node[] Parents;
	String[] VarValues;
	ArrayList<String> SonsNames;
	String name;
	double[][] CPT;
	int numOfParents; 
	
	public Node(String name) {
		this.name = name;
		this.SonsNames = new ArrayList<String>();
	}
	
	public String toSrting() {
		return this.name;
	}
	
	public void getCPT(BufferedReader br, BayesianNetwork BN) {
		try {
			int x = 1;
			for (int i = 0; i < this.numOfParents; i++) {
				x *= BN.nodesHash.get(this.ParentsNames[i]).VarValues.length;
			}
			this.CPT = new double[x][this.VarValues.length-1];
			String st = br.readLine();
			int[] PrentValuePlace = new int[this.numOfParents];
			int multiplicationOfPrentsValues = 1;
			for (int i = 0; i < this.numOfParents; i++) {
				multiplicationOfPrentsValues *= this.Parents[i].VarValues.length;
			}
			int SwithValueIndex = 1;
			int recacl = 1;
			int[] PrentSwithValueIndex = new int[this.numOfParents];
			int[] PrentRecaclValueIndex = new int[this.numOfParents];
			for (int i = this.numOfParents-1; i >= 0; i--) {
				recacl *=this.Parents[i].VarValues.length;
				PrentRecaclValueIndex[i] = recacl;
				PrentSwithValueIndex[i] = SwithValueIndex;
				SwithValueIndex *=this.Parents[i].VarValues.length;
			}
			
			for (int RowNum = 0; RowNum < multiplicationOfPrentsValues; RowNum++) {
				for (int i = 0; i < this.numOfParents; i++) {
					PrentValuePlace[i] = ((int) RowNum / PrentSwithValueIndex[i]) % PrentRecaclValueIndex[i];
				}
				this.nextCPT_line(multiplicationOfPrentsValues, st, PrentValuePlace, BN, RowNum);
				if(RowNum !=multiplicationOfPrentsValues-1)
					st = br.readLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void nextCPT_line(int multiplicationOfPrentsValues, String st, int[] PrentValuePlace, BayesianNetwork BN, int RowNum) {
		String[] tempWordArray = st.split(",");
		if (tempWordArray.length != this.numOfParents+2*(this.VarValues.length-1)) {
			throw new RuntimeException("This row dosn't present a part of "+this.name+" CPT.\ngot: "+st );
		}
		for (int i = 0; i < this.numOfParents; i++) {
			if(!this.Parents[i].VarValues[PrentValuePlace[i]].equals(tempWordArray[i])) {
				System.out.print(tempWordArray[i]+" == "+this.Parents[i].VarValues[PrentValuePlace[i]]+PrentValuePlace[i]+"\n");
				throw new RuntimeException("In this row 1 value of parent isn't true.\n"+st+"\nPrent: "+this.Parents[i].name+
						", parent value: "+this.Parents[i].VarValues[PrentValuePlace[i]] );
			}
		}
			for (int i = 0; i < this.VarValues.length-1; i++) {
				if(!(tempWordArray[this.numOfParents+2*i].length() > 1 &&
						tempWordArray[this.numOfParents+2*i].charAt(0) == '=' &&
						tempWordArray[this.numOfParents+2*i].subSequence(1, tempWordArray[this.numOfParents+2*i].length()).equals(this.VarValues[i]))) {
					throw new RuntimeException( st+", "+(tempWordArray[this.numOfParents+2*i].length() > 2)+", "+
							(tempWordArray[this.numOfParents+2*i].charAt(0) == '=')+", "+
							tempWordArray[this.numOfParents+2*i].subSequence(1, tempWordArray[this.numOfParents+2*i].length())+" "+(this.VarValues[i]));
				}
				this.CPT[RowNum][i] = Double.parseDouble(tempWordArray[this.numOfParents+2*i+1]);
			}
	}
}
