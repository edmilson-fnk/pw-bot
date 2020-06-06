package etc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RefinePattern {

  private static final List<Pattern> PATTERN_LIST = new LinkedList<Pattern>(){{
    this.add(Pattern.compile("F{3}S{2}"));
    this.add(Pattern.compile("F{4}S{3}"));
    this.add(Pattern.compile("F{5}S{4}"));
    this.add(Pattern.compile("F{6}S{5}"));
    this.add(Pattern.compile("F{7}S{6}"));
    this.add(Pattern.compile("F{8}S{7}"));
    this.add(Pattern.compile("F{9}S{8}"));
  }};

  public static void main(String[] args) {
    String sequence = "FFFSSSFFFFFSSSFFSFSSFSFSFSFSFFSFSFFFSSSSSFFFFFFSSFFFFFSSFFFSS";

    Map<Pattern, Integer> patternCount = new HashMap<>();
    for (Pattern p : PATTERN_LIST) {
      Matcher matcher = p.matcher(sequence);
      int count = 0;
      while (matcher.find())
        count++;
      patternCount.put(p, count);
    }
    System.out.println(patternCount);
  }

}
