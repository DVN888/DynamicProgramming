import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.LinkedList;

/**
 * This class implements a game of Row of Bowls.
 * For the games rules see Blatt05. The goal is to find an optimal strategy.
 */
public class RowOfBowls {

    private int[][] matrix;
    private int[] logvalues;

    public RowOfBowls() {
    }
    
    /**
     * Implements an optimal game using dynamic programming
     * @param values array of the number of marbles in each bowl
     * @return number of game points that the first player gets, provided both parties play optimally
     */
    public int maxGain(int[] values) throws InputMismatchException
    {
        if(values.length<1 || values==null) throw new InputMismatchException();
        this.logvalues = Arrays.copyOf(values,values.length);
        matrix = new int[values.length][values.length];
        for (int n = 0 ; n<values.length ; n++) {
            for (int i = 0 ; i<values.length-n ; i++) {
                matrix[n+i][i] = OPT_Function(values,n+i,i,matrix);
            }
        }
        return matrix[values.length-1][0];
    }

    private int OPT_Function(int[] values, int indexR, int indexL, int[][] matrix) {
        if(indexR==indexL) return values[indexL];
        return Math.max(values[indexL]-matrix[indexR][indexL+1], values[indexR]-matrix[indexR-1][indexL]);
    }

    /**
     * Implements an optimal game recursively.
     *
     * @param values array of the number of marbles in each bowl
     * @return number of game points that the first player gets, provided both parties play optimally
     */
    public int maxGainRecursive(int[] values) throws InputMismatchException {
        if(values.length<1 || values==null) throw new InputMismatchException();
        this.logvalues = Arrays.copyOf(values,values.length);
        return maxGainRecursive(values,0, values.length-1);
    }

    private int maxGainRecursive(int[] values,int first, int last) {
        if(first==last) return values[first];
        int chooseLeft = values[first]-maxGainRecursive(values,first+1,last);
        int chooseRight = values[last]-maxGainRecursive(values,first,last-1);
        return Math.max(chooseLeft,chooseRight);
    }


    /**
     * Calculates an optimal sequence of bowls using the partial solutions found in maxGain(int values)
     * @return optimal sequence of chosen bowls (represented by the index in the values array)
     */
    public Iterable<Integer> optimalSequence()
    {
        LinkedList<Integer> moves = new LinkedList<Integer>();
        int left = 0;
        int right = logvalues.length-1;
        for (int i=0; i < logvalues.length-1; i++) {
            if(logvalues[left]-matrix[right][left+1] >= logvalues[right]-matrix[right-1][left]) {
                moves.add(left);
                left += 1;
            } else {
                moves.add(right);
                right -= 1;
            }
        }
        moves.add(left);
        return moves;
    }


    public static void main(String[] args)
    {
        // For Testing
        int[] values = {6,4,2,1,3,5,7};
        RowOfBowls rofbols = new RowOfBowls();
        System.out.println(rofbols.maxGainRecursive(values));
        System.out.println(rofbols.maxGain(values));
        System.out.println(rofbols.optimalSequence());
    }
}

