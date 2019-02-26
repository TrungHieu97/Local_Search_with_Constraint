package baitap;
import java.io.PrintWriter;
import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.functions.*;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.*;
import localsearch.selectors.*;

public class NQueens2 {
	LocalSearchManager mgr;
	VarIntLS[] x;
	ConstraintSystem S;
	int n;
	
	public void stateModel() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[n];
		for (int i =0; i < n; i++) {
			x[i] = new VarIntLS(mgr, 0, n-1);
		}
		
		S = new ConstraintSystem(mgr);
		S.post(new AllDifferent(x));
		IFunction[] f1 = new IFunction[n];
		for(int i = 0; i < n; i++) {
			f1[i] = new FuncPlus(x[i],i);
		}
		
		S.post(new AllDifferent(f1));
		IFunction[] f2 = new IFunction[n];
		for(int i = 0; i < n; i++) {
			f2[i] = new FuncPlus(x[i], -i);
		}
		
		S.post(new AllDifferent(f2));
		
		mgr.close();
	}
	
	public void printSolution() {
		for(int i = 0 ;i < n ; i++) {
			System.out.print(x[i].getValue() + " ");
		}
		System.out.println();
		
	}
	
	public void search() {
		printSolution();
		System.out.println("Init violations = " + S.violations());
		int it = 0;
		MinMaxSelector mns = new MinMaxSelector(S);
		
		while(it < 10000 && S.violations() > 0) {
			VarIntLS sel_x = mns.selectMostViolatingVariable();
			int sel_v = mns.selectMostPromissingValue(sel_x);
			sel_x.setValuePropagate(sel_v);
			System.out.println("Step " + it+ " violations " + S.violations());
			it++;
		}
		
	}
	
	public NQueens2(int n) {
		this.n= n;
	}
	
	public void solve() {
		stateModel();
		search();
		
	}
	
	public void printHTML(String fn) {
		
	}
	
	public static void main(String[] args) {
		System.out.println("START");
		NQueens2 q = new NQueens2(9);
		q.solve();
		q.printHTML("text.html");
		q.printSolution();
	}
}
