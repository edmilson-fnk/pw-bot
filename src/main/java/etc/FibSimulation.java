package etc;

import java.text.DecimalFormat;

public class FibSimulation {

  public static void main(String[] args) {
    float aspd = 4.8f; // 480%
    int battleDur = 10;
    int hitNormal = 360;
    int hitFibAct = 460;
    int hitFib = 350;

    simulate(aspd, hitNormal, hitFib, hitFibAct, battleDur);
  }

  private static void simulate(float aspd, int hitNormal, int hitFib, int hitFibAct, int battleDur) {
    int N = 100000;
    float finalFibDmg = 0f;
    float finalDmg = 0f;
    int fibDur = 3;
    float fibChance = 0.05f;
    float totalHits = aspd * battleDur;

    for (int n = 0; n < N; n++) {
      int buffedHits = 0;
      float dmg = 0f;
      float fibDmg = 0f;
      for (int i = 0; i < totalHits; i++) {
        // Damage with FiB
        double prob = rand(0, 1);
        if (prob <= fibChance) {
          buffedHits = (int) aspd * fibDur;
        }
        if (buffedHits > 0) {
          fibDmg += hitFibAct;
          buffedHits--;
        } else {
          fibDmg += hitFib;
        }

        // Damage with no Fib
        dmg += hitNormal;
      }
      finalFibDmg += fibDmg;
      finalDmg += dmg;
    }
    finalDmg /= N;
    finalFibDmg /= N;
    System.out.println("Fib\t" + new DecimalFormat("###,###,###").format(finalFibDmg));
    System.out.println("Dmg\t" + new DecimalFormat("###,###,###").format(finalDmg));
    System.out.println("Diff\t" + new DecimalFormat("###.##").format(100*(finalDmg-finalFibDmg)/finalDmg));
  }

  public static double rand(int min, int max) {
    return min + Math.random() * (max - min);
  }

}
