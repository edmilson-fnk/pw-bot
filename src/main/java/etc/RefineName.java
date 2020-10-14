package etc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RefineName {

  public static void main(String[] args) {
    String itemName = "Survival Ring";
    String strPattern = "\\+?(\\d).*";
    Pattern p = Pattern.compile(strPattern);
    Matcher matcher = p.matcher(itemName);
    if (matcher.matches()) {
      String refineStr = matcher.group(1);
      int refine = Integer.parseInt(refineStr);
      System.out.println(refine);
    }
  }

}
