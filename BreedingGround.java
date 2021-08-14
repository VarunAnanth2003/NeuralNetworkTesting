import java.util.Random;

public class BreedingGround {
    public static int[][] breedA(int[][] breeders) {
        int[][] ret_val = new int[OldMain.populationSize][];
        for (int i = 0; i < ret_val.length; i++) {
            int[] sub = new int[OldMain.len];
            for (int j = 0; j < sub.length; j++) {
                boolean randSel = new Random().nextInt(2) == 0 ? false : true;
                if (randSel) {
                    sub[j] = breeders[1][j];
                } else {
                    sub[j] = breeders[0][j];
                }
                randSel = new Random().nextInt(2) == 0 ? false : true;
                if (randSel) {
                    sub[j] += 1;
                } else {
                    sub[j] -= 1;
                }
            }
            ret_val[i] = sub;
        }
        return ret_val;
    }
    public static int[][] breedB(int[][] breeders) {
        int[][] ret_val = new int[OldMain.populationSize][];
        for (int i = 0; i < ret_val.length; i++) {
            int[] sub = new int[OldMain.len];
            for (int j = 0; j < sub.length; j++) {
                sub[j] = (breeders[0][j] + breeders[1][j]) / 2;
                boolean randSel = new Random().nextInt(2) == 0 ? false : true;
                if (randSel) {
                    sub[j] += 5;
                } else {
                    sub[j] -= 5;
                }
            }
            ret_val[i] = sub;
        }
        return ret_val;
    }
    public static int[][] breedC(int[][] breeders) {
        int[][] ret_val = new int[OldMain.populationSize][];
        for (int i = 0; i < ret_val.length; i++) {
            int[] sub = new int[OldMain.len];
            for (int j = 0; j < sub.length; j++) {
                int avg = (breeders[0][j] + breeders[1][j]) / 2;
                boolean randSel = new Random().nextInt(2) == 0 ? false : true;
                if (randSel) {
                    sub[j] = avg + new Random().nextInt(avg);
                } else {
                    sub[j] = avg - new Random().nextInt(avg);
                }
            }
            ret_val[i] = sub;
        }
        return ret_val;
    }
}
