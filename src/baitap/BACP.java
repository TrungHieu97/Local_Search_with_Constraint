package baitap;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.LessThan;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.functions.max_min.Max;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class BACP {
	
	int N; // course
	int M; // semester
	int[] Crd;
	int MinCrd;
	int MaxCrd;
	int MinCourse;
	int MaxCourse;
	int[] I;
	int[] J; // Dk tieen quyet
	
	
	
	
	LocalSearchManager mgr;
	VarIntLS[] x; /////////// mang cac mon hoc
	int[] crd;    //////////// mang tin chi tung mon
	ConstraintSystem S;
	

	public void test() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[5];
		crd = new int[5];
		
		for(int i = 0 ; i < 5; i++) {
			x[i] = new VarIntLS(mgr, 0,2);
		}
		crd[0] = 3; 
		crd[1] = 6; 
		crd[2] = 4; 
		crd[3] = 7; 
		crd[4] = 9;
		
		IFunction  C = new ConditionalSum(x, crd, 1);
		mgr.close();
		x[3].setValuePropagate(1);
		x[1].setValuePropagate(1);
		int delta = C.getAssignDelta(x[1], 1);
		System.out.println(C.getValue());

	}
	
	public void stateModel() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[N];
		IFunction[] credits;
		IFunction[] courses;
		for(int i = 0; i < N; i++) {
			x[i] = new VarIntLS(mgr, 0, M-1);
		}
		
		S = new ConstraintSystem(mgr);
		
		for(int k = 0; k < I.length; k++) {
			S.post(new LessThan(x[I[k]], x[J[k]]));
		}
		
		credits = new IFunction[M];
		for(int i = 0; i < M; i++) {
			credits[i] = new ConditionalSum(x, crd, i);
			S.post(new LessOrEqual(credits[i], MaxCrd));
			S.post(new LessOrEqual(MinCrd, credits[i]));
		}
		
		courses = new IFunction[M];
		for(int i = 0 ; i < M; i++) {
			courses[i] = new ConditionalSum(x, i);
			S.post(new LessOrEqual(courses[i], MaxCourse));
			S.post(new LessOrEqual(MinCourse, courses[i]));
		}
		
		mgr.close();
		
	}
	
	public void ReadData(String fn) {
		try {
			Scanner in = new Scanner(new File(fn));
			N = in.nextInt();
			M = in.nextInt();
			MinCrd = in.nextInt();
			MaxCrd = in.nextInt();
			MinCourse = in.nextInt();
			MaxCourse = in.nextInt();
			crd = new int[N];
			for (int i = 0 ; i < N; i++) {
				crd[i] = in.nextInt();
			}
			
			int K = in.nextInt();
			I = new int[K];
			J = new int[K];
			
			for(int i = 0 ; i < K; i++) {
				I[i] = in.nextInt()-1;
				J[i] = in.nextInt()-1;
			}
			in.close();
			
		} catch (Exception e) {
			System.out.println();
		}
	}
	
	public void HillClimbing(IConstraint c, int maxIter) {
		VarIntLS[] y = c.getVariables();
		ArrayList<AssignMove> cand = new ArrayList<AssignMove>();
		
		int it = 0;
		Random R = new Random();
		while(it < maxIter && c.violations() > 0) {
			cand.clear();
			int minDelta = Integer.MAX_VALUE;
			for(int i = 0 ; i < y.length ; i++) {
				for( int v = y[i].getMinValue(); v <= y[i].getMaxValue(); v++) {
					int d = c.getAssignDelta(y[i], v);
					if(d < minDelta ) {
						cand.clear();
						cand.add(new AssignMove(i, v));
						minDelta = d;
					}
					else if(d == minDelta) {
						cand.add(new AssignMove(i, v));
					}
				}
			}
			int idx= R.nextInt(cand.size());
			AssignMove m  = cand.get(idx);
			y[m.i].setValuePropagate(m.v);;
			System.out.println("Step " +it + " violations = " + c.violations());
			it++;
		}
	}
	
	public void search() {
		HillClimbing(S, 10000);
	}
	
	public void solve() {
		stateModel();
		search();
		for(int i = 0; i < N; i++ ) {
			System.out.print(" " +x[i].getValue());
		}
		
	}
	
	public static void main(String[] args) {
		BACP B = new BACP();
		B.ReadData("data/BACP/bacp.in01");
		B.solve();

		
	}

}
