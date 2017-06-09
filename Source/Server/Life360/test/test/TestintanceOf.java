package test;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TestintanceOf {

    public static void main(String[] args) throws Exception {
        long start = 0L;
        Object who_cares = "10132132s"; // Used to prevent compiler optimization

        for (int i = 0; i < 10; ++i) {
            System.out.println("Testing instanceof");
            start = System.nanoTime();
            int testInstanceOf = testInstanceOf(who_cares);
            System.out.println("instanceof completed in " + (System.nanoTime() - start) + " ms " + testInstanceOf);

            System.out.println("Testing try/catch");
            start = System.nanoTime();
            int testTryCatch = testTryCatch(who_cares);
            System.out.println("try/catch completed in " + (System.nanoTime() - start) + " ms" + testTryCatch);
            
            System.out.println("----------");
        }
    }

    private static int testInstanceOf(Object who_cares) {
        if (who_cares instanceof Integer) {
            return (Integer) who_cares;
        }
        return -1;

    }

    private static int testTryCatch(Object who_cares) {
        try {
            return Integer.parseInt(who_cares.toString());
        } catch (Exception ex) {
            return -1;
        }
    }

}
