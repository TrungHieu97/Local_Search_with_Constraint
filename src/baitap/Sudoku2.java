package baitap;

import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class Sudoku2 {
	
	public class SwapMove {
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
		S = new ConstraintSystem(mgr);
		for(int i = 0; i < 9; i++) {
			for(int j =0 ; j < 9; j++) {
				x[i][j] = new VarIntLS(mgr, 1,9);
				x[i][j].setValue(j+1);
  			}
		}
		
		for(int i =0 ; i < 9; i++) {
			S.post(new AllDifferent(x[i]));
		}
		
		for(int i = 0; i < 9; i++) {
			VarIntLS[] y = new VarIntLS[9];
			for(int j = 0; j < 9; j++) {
				y[j] = x[j][i];
			}
			S.post(new AllDifferent(y));
		}
		
		for(int I = 0; I < 3; I++) {
			for(int J =0 ; J < 3; J++) {
				VarIntLS[] z = new VarIntLS[9];
				int idx = -1;
				for (int i = 0; i < 3; i++) {
					for(int j = 0; j <3; j++) {
						idx++;
						z[idx] = x[I*3+i][J*3 +j];
					}
				}
				S.post(new AllDifferent(z));
				
				
			}
		}
		
		mgr.close();
		
	}
	
	
	void HillClimbing(IConstraint c, int maxiter) {
		
	}
	
	void search() {
		ArrayList<SwapMove> cand = new ArrayList<SwapMove>();
		Random R = new Random();
		int it = 0;
		
		while(it < 100 && S.violations() >0) {
			int minDelta = Integer.MAX_VALUE;
			cand.clear();
			for(int i = 0 ; i <=8 ; i++) {
				for(int j1 = 0 ; j1 < 8 ; j1++) {
					for(int j2 = j1 +1; j2 <= 8; j2++) {
						int d = S.getSwapDelta(x[i][j1], x[i][j2]);
						if(d < minDelta) {
							cand.clear();
							cand.add(new SwapMove(i, j1,j2));
							minDelta = d;
						}
						else if (d == minDelta) {
							cand.add(new SwapMove(i, j1, j2));
						}
					}
				}
			}
			int idx = R.nextInt(cand.size());
			SwapMove m = cand.get(idx);
			x[m.i][m.j1].swapValuePropagate(x[m.i][m.j2]);
			System.out.println("Step " + it + " violations : " + S.violations());
			it++;
		}
		
		
		
	}
	
	void solve() {
		stateModel();
		for(int i = 0; i < 9 ; i++) {
			System.out.println();
			for( int j = 0; j <9; j++) {
				System.out.print( "  "+ x[i][j].getValue());
			}
		}
		System.out.println();
		
		search();
		for(int i = 0; i < 9 ; i++) {
			System.out.println();
			for( int j = 0; j <9; j++) {
				System.out.print( "  "+ x[i][j].getValue());
			}
		}
	}
	
	public static void main(String[] args) {
		Sudoku2 s = new Sudoku2();
		s.solve();
	}
	
}
