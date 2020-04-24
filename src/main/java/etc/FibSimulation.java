package etc;

import java.text.DecimalFormat;

public class FibSimulation {

  public static void main(String[] args) {
    float aspd = 4.8f; // 480%
    int battleDur = 15;
    int empHp = 5000;
    int hit2xOG = 416;
    int hitFib = 390;
    int hitFibAct = 480;

//    simulateFixedTime(aspd, hit2xOG, hitFib, hitFibAct, battleDur);
    simulateFixedHP(aspd, hit2xOG, hitFib, hitFibAct, empHp);
  }

  private static void simulateFixedHP(float aspd, int hitNormal, int hitFib, int hitFibAct, int hp) {
    int N = 10000;
    float finalFibHits = 0f;
    float finalHits = 0f;
    int fibDur = 3;
    float fibChance = 0.05f;

    for (int n = 0; n < N; n++) {
      int buffedHits = 0;
      float dmg = 0f;
      float fibDmg = 0f;
      int hits = 0;
      int fibHits = 0;
      while (dmg < hp || fibDmg < hp) {
        // Damage with FiB
        if (fibDmg < hp) {
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
          fibHits++;
        }

        if (dmg < hp) {
          // Damage with no Fib
          dmg += hitNormal;
          hits++;
        }
      }
      finalHits += hits;
      finalFibHits += fibHits;
    }
    finalFibHits /= N;
    finalHits /= N;
    System.out.println("HP analysis");
    System.out.println(String.format("Fib\t%s hits", new DecimalFormat("###,###,###").format(finalFibHits)));
    System.out.println(String.format("\t\t%s s", new DecimalFormat("###,###,###").format(finalFibHits / aspd)));
    System.out.println(String.format("Dmg\t%s hits", new DecimalFormat("###,###,###").format(finalHits)));
    System.out.println(String.format("\t\t%s s", new DecimalFormat("###,###,###").format(finalHits / aspd)));
  }

  private static void simulateFixedTime(float aspd, int hitNormal, int hitFib, int hitFibAct, int battleDur) {
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
    System.out.println("Time analysis");
    System.out.println("Fib\t" + new DecimalFormat("###,###,###").format(finalFibDmg));
    System.out.println("Dmg\t" + new DecimalFormat("###,###,###").format(finalDmg));
    System.out.println("Diff\t" + new DecimalFormat("###.##").format(100*(finalDmg-finalFibDmg)/finalDmg));
  }

  public static double rand(int min, int max) {
    return min + Math.random() * (max - min);
  }

}
