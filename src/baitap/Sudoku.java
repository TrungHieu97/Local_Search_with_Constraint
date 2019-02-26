package baitap;

import java.util.ArrayList;
import java.util.Random;

import core.VarInt;
import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class Sudoku {

	class SwapMove{
		int i;
		int j1;
		int j2;
		public SwapMove(int i, int j1, int j2) {
			this.i = i;
			this.j1 = j1;
			this.j2 = j2;
		}
	}
	
	LocalSearchManager mgr;
	VarIntLS[][] x;
	ConstraintSystem S;
	
	public void stateModel() {
		
		mgr = new LocalSearchManager();
		x = new VarIntLS[9][9];
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				x[i][j] = new VarIntLS(mgr, 1, 9);
				x[i][j].setValue(j+1);
			}
		}
		
		S = new ConstraintSystem(mgr);
		for(int i = 0; i < 9; i++) {
			S.post(new AllDifferent(x[i]));
		}
		
		for(int j = 0; j < 9; j++) {
			IFunction[] f1 = new IFunction[9];
			for(int i = 0; i < 9; i++) {
				f1[i] = new FuncPlus(x[i][j], 0);
			}
			S.post(new AllDifferent(f1));
		}
		
		for(int I = 0; I < 3; I++) {
			for(int J = 0; J < 3; J++) {
				VarIntLS[] y = new VarIntLS[9];
				int idx = -1;
				for(int i = 0; i <= 2; i++) {
					for(int j = 0; j <= 2; j++) {
						idx++;
						y[idx] = x[3*I+i][3*J+j];
					}
				}
				S.post(new AllDifferent(y));
			}
		}
		
		mgr.close();
	}
	
	public void hillClimbing(IConstraint c, int maxIter) {
		VarIntLS[] y = c.getVariables();//cho bien dinh nghia rang buoc c
		ArrayList<AssignMove> cand = new ArrayList<AssignMove>();
		Random R = new Random();
		int it = 0;
		while(it < maxIter && c.violations() > 0) {
			cand.clear();
			int minDelta = Integer.MAX_VALUE;
			for(int i = 0; i < y.length; i++) {
				for(int j = 0; j < y.length; j++) {
					if(i != j) {
						int d = c.getSwapDelta(y[i], y[j]);
					}
				}
			}
		}
	}
	
	
	
	public void search() {
//		hillClimbing(S, 10000);
		ArrayList<SwapMove> cand = new ArrayList<Sudoku.SwapMove>();
		int it = 0;
		Random R = new Random();
		while(it < 100000 && S.violations() > 0) {
			cand.clear();
			int minDelta = Integer.MAX_VALUE;
			for(int i = 0; i <= 8; i++) {
				for(int j1 = 0; j1 < 8; j1++) {
					for(int j2 = j1 + 1; j2 <= 8; j2++) {
						int delta = S.getSwapDelta(x[i][j1], x[i][j2]);
						if(delta < minDelta) {
							cand.clear();
							cand.add(new SwapMove(i, j1, j2));
							minDelta = delta;
						}else if(delta == minDelta){
							cand.add(new SwapMove(i, j1, j2));
						}
					}
				}
			}
			SwapMove m = cand.get(R.nextInt(cand.size()));
			x[m.i][m.j1].swapValuePropagate(x[m.i][m.j2]);
			System.out.println("Step " + it + ", S = " + S.violations() );
			it++;
		}
	}
	
	public void solve() {
		stateModel();
		search();
		for(int i = 0; i < x.length; i++) {
			for(int j = 0; j < x[i].length; j++) {
				System.out.print(x[i][j].getValue() + " ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Sudoku su = new Sudoku();
		su.solve();
	}

}
