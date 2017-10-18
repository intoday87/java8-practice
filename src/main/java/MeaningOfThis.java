import java.util.Arrays;

public class MeaningOfThis {
   public final int value = 4;
   public void doIt() {
     int value = 6;
     Runnable r = new Runnable() {
         public final int value = 5;
         @Override
         public void run() {
             int value = 10;
             System.out.println(this.value);
         }
     };
     r.run();
   }

   public static void main(String... args) {
       System.out.println(args.length);
       Arrays.stream(args).forEach(v -> System.out.println(v));
       new MeaningOfThis().doIt();
   }
}
