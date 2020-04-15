package calc;

import java.text.DecimalFormat;

public class FibSimulation {

  public static void main(String[] args) {
    float aspd = 4.8f; // 480%
    int hitNormal = 370;
    int hitFib = 350;
    int hitFibAct = 440;
    float battleDur = 20f;

    simulate(aspd, hitNormal, hitFib, hitFibAct, battleDur);
  }

  private static void simulate(float aspd, int hitNormal, int hitFib, int hitFibAct, float battleDur) {
    int N = 5000;
    float finalFibDmg = 0f;
    float finalDmg = 0f;
    for (int n = 0; n < N; n++) {
      int fibDur = 3;
      float fibChance = 0.05f;

      int buffedHits = 0;
      float totalHits = aspd * battleDur;
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
  }

  public static double rand(int min, int max) {
    return min + Math.random() * (max - min);
  }

}
