
public class CPT {
	
	public static double[][] CPT(Node n, BayesianNetwork BN) {
		double[][] table = null;
		int x = 1;
		for (int i = 0; i < n.numOfParents; i++) {
			x *= BN.nodesHash.get(n.ParentsNames[i]).VarValues.length;
		}
		table = new double[n.VarValues.length][x];
		return table;
	}
}
