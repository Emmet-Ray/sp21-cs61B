package flik;

public class HorribleSteve {
    public static void main(String[] args) throws Exception {
        int i = 0;
        boolean result;
        for (int j = 0; i < 500; ++i, ++j) {
            result = Flik.isSameNumber(i, j);
            if (!result) {
                throw new Exception(
                        String.format("i:%d not same as j:%d ??", i, j));
            }
        }
        System.out.println("i is " + i);
    }
}
