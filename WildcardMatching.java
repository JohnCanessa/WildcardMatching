import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;


/**
 * 
 */
public class WildcardMatching{


    /**
     * Given an input string (s) and a pattern (p), 
     * implement wildcard pattern matching with support for '?' and '*'.
     * 
     * first arg == p
     * second arg == s
     */
    static boolean isMatch0(String s, String p) {

        // **** base condition ****
        if (p.length() == 0 && s.length() == 0) {
            return true;
        }

        // // ???? ????
        // System.out.println("<<< p ==>" + p + "<==");

        // **** replace multiple contiguous '*'s with a single '*' ****
        p = p.replaceAll("\\*{2,}", "*");

        // // ???? ????
        // System.out.println("<<< p ==>" + p + "<== p.length: " + p.length());
        // System.out.println("<<< s ==>" + s + "<== s.length: " + s.length());

        // **** '?' in pattern and empty string ****
        if (p.length() > 0 && p.charAt(0) == '?' && s.length() == 0) {
            return false;
        }

        // **** '*' in pattern and empty string ****
        if (p.length() > 0 && p.charAt(0) == '*' && s.length() == 0) {
            return isMatch0(s, p.substring(1));
        }

        // **** '?' in pattern and character in string ****
        if (p.length() > 0 && p.charAt(0) == '?' && s.length() > 0)
            return isMatch0(s.substring(1), p.substring(1));

        // **** make sure the characters after '*' are present in second string ****
        if (p.length() > 1 && p.charAt(0) == '*' && s.length() == 0)
            return false;

        // **** '?' in pattern ****
        if ((p.length() > 1 && p.charAt(0) == '?') || 
            (p.length() != 0 && s.length() != 0 && p.charAt(0) == s.charAt(0)))
            return isMatch0(s.substring(1), p.substring(1));

        // **** a) consider current character of second string
        //      b) ignore current character of second string ****
        if (p.length() > 0 && p.charAt(0) == '*')
            return isMatch0(s, p.substring(1)) || isMatch0(s.substring(1), p);
        
        // **** ****
        return false;
    }


    /**
     * Given an input string (s) and a pattern (p), 
     * implement wildcard pattern matching with support for '?' and '*'. 
     * 
     * Runtime: 26 ms, faster than 43.80% of Java online submissions.
     * Memory Usage: 39.4 MB, less than 47.89% of Java online submissions.
     */
    static boolean isMatch1(String s, String p) {

        // **** initialization ****
        boolean[][] match = new boolean[s.length() + 1][p.length() + 1];
        match[s.length()][p.length()] = true;

        // // ???? ????
        // System.out.println("<<< match: ");
        // for (int i = 0; i < match.length; i++)
        //     System.out.println(Arrays.toString(match[i]));

        // **** initialize match ****
        for (int i = p.length() - 1; i >= 0; i--) {
            if (p.charAt(i) != '*')
                break;
            else 
                match[s.length()][i] = true;
        }

        // // ???? ????
        // System.out.println("<<< match: ");
        // for (int i = 0; i < match.length; i++)
        //     System.out.println(Arrays.toString(match[i]));

        // **** process string and pattern backwards ****
        for (int i = s.length() - 1; i >= 0; i--) {
            for (int j = p.length() - 1; j >= 0; j--) {
                if (s.charAt(i) == p.charAt(j) || p.charAt(j) == '?')
                    match[i][j] = match[i + 1][j + 1];
                else if (p.charAt(j) == '*') 
                    match[i][j] = match[i + 1][j] || match[i][j + 1];
                else
                    match[i][j] = false;
            }
        }

        // // ???? ????
        // System.out.println("<<< match: ");
        // for (int i = 0; i < match.length; i++)
        //     System.out.println(Arrays.toString(match[i]));

        // **** return result ****
        return match[0][0];
    }


    // // ???? to keep track of repeated calls ????
    // static HashMap<String, Integer> hm = null;
    // static int callCounter = 0;


    // **** to cache repeated values (memoization) ****
    static HashMap<String, Boolean> cache = null;


    /**
     * Given an input string (s) and a pattern (p), 
     * implement wildcard pattern matching with support for '?' and '*'.
     * 
     * Entry point for recursion.
     * Bottom down approach.
     */
    static boolean isMatch2(String s, String p) {

        // // ???? to keep track of repeated calls ????
        // hm = new HashMap<String, Integer>();

        // **** initialize cache ****
        cache = new HashMap<String, Boolean>();

        // **** replace multiple contiguous '*'s with a single '*' ****
        p = p.replaceAll("\\*{2,}", "*");

        // **** recursive call ****
        return isMatch2(s, p, 0, 0);
    }


    /**
     * Given an input string (s) and a pattern (p), 
     * implement wildcard pattern matching with support for '?' and '*'.
     * 
     * Recursive call.
     * Bottom-down approach.
     */
    static private boolean isMatch2(String s, String p, int startS, int startP) {

        // **** initialization ****
        boolean result = false;

        // **** build pair string ****
        String pair = "" + startS + "," + startP;


        // // ???? update pair count ????
        // if (!hm.containsKey(pair))
        //     hm.put(pair, 1);
        // else {
        //     int value = hm.get(pair);
        //     hm.put(pair, ++value);
        // }

        // // ???? count this call ????
        // callCounter++;


        // **** reached the end of both s and p ****
        if (startS == s.length() && startP == p.length())
            result = true;

        // **** there are still characters in S => there is no match ****
        else if (startP == p.length())
            result = false;

        // **** hopefully the remaining characters in P are all stars ****
        else if (startS == s.length())
            result = p.charAt(startP) == '*' && isMatch2(s, p, startS, startP + 1);

        // **** '*' either matches 0 or >= 1 character ****
        else if (p.charAt(startP) == '*')
            result = isMatch2(s, p, startS + 1, startP) || isMatch2(s, p, startS, startP + 1);

        // **** move both pointers ****
        else if (p.charAt(startP) == '?' || p.charAt(startP) == s.charAt(startS))
            result = isMatch2(s, p, startS + 1, startP + 1);

        // **** # the current char from P is a lowercase char different from s[startS] ****
        else 
            result =  false;
            
        // **** save result in cache (if needed) ****
        if (!cache.containsKey(pair))
            cache.put(pair, result);

        // **** return result ****
        return result;
    }


    /**
     * Given an input string (s) and a pattern (p), 
     * implement wildcard pattern matching with support for '?' and '*'.
     * 
     * Bottom-up approach.
     * 
     * Runtime: 29 ms, faster than 27.41% of Java online submissions.
     * Memory Usage: 39.3 MB, less than 57.77% of Java online submissions.
     */
    static private boolean isMatch3(String s, String p) {

        // // **** replace multiple contiguous '*'s with a single '*' ****
        // p = p.replaceAll("\\*{2,}", "*");

        // **** initialization ****
        boolean dp[][] = new boolean[s.length() + 1][p.length() + 1];

        // **** ****
        for (int i = 0; i < s.length() + 1; i++) {
            for (int j = 0; j < p.length() + 1; j++) {

                // **** ****
                int startS = i - 1;
                int startP = j - 1;

                // **** conditions ****
                if (i == 0 && j == 0)
                    dp[i][j] = true;

                else if (i == 0) 
                    dp[i][j] = p.charAt(startP) == '*' && dp[i][j - 1];

                else if (j == 0) 
                    dp[i][j] = false;

                else if (p.charAt(startP) == '*') 
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];

                else if (s.charAt(startS) == p.charAt(startP) || p.charAt(startP) == '?')
                    dp[i][j] = dp[i - 1][j - 1]; 
            }
        }

        // **** return result ****
        return dp[s.length()][p.length()];
    }


    /**
     * Given an input string (s) and a pattern (p), 
     * implement wildcard pattern matching with support for '?' and '*'.
     * 
     * Runtime: 2 ms, faster than 100.00% of Java online submissions.
     * Memory Usage: 38.6 MB, less than 99.58% of Java online submissions.
     */
    static boolean isMatch(String s, String p) {

        // **** initialization ****
        int sLen = s.length(); 
        int pLen = p.length();

        int sIdx = 0;
        int pIdx = 0;

        int starIdx = -1;
        int sTmpIdx = -1;
        
        // **** ****
        while (sIdx < sLen) {
            if (pIdx < pLen && (s.charAt(sIdx) == p.charAt(pIdx) || p.charAt(pIdx) == '?')){
                sIdx++;
                pIdx++;
            } else if (pIdx < pLen && p.charAt(pIdx) == '*'){
                starIdx = pIdx;
                sTmpIdx = sIdx;
                pIdx++;
            } else if (starIdx == -1){
                return false;
            } else {
                sIdx    = sTmpIdx + 1;
                pIdx    = starIdx + 1;
                sTmpIdx = sIdx;
            }
            
        }

        // **** return false result ****
        for (int i = pIdx; i < pLen; i++){
            if (p.charAt(i) != '*')
                return false;
        }

        // **** return true result ****
        return true;
    }


    /**
     * Test scaffold.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // **** initialization ****
        long start  = 0;
        long end    = 0;

        // **** open buffered reader ****
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // **** read string ****
        String s = br.readLine().trim();

        // **** read pattern ****
        String p = br.readLine().trim();

        // **** close buffered reader ****
        br.close();

        // ???? ????
        System.out.println("main <<< s ==>" + s + "<==");
        System.out.println("main <<< p ==>" + p + "<==");

        // **** call method of interest and display result ****
        start = System.currentTimeMillis();
        System.out.println("main <<<   output: " + isMatch0(s, p));
        end = System.currentTimeMillis();
        System.out.println("main <<< duration: " + (end - start) + " ms");

        // **** call method of interest and display result ****
        start = System.currentTimeMillis();
        System.out.println("main <<<   output: " + isMatch1(s, p));
        end = System.currentTimeMillis();
        System.out.println("main <<< duration: " + (end - start) + " ms");

        // **** call method of interest and display result ****
        start = System.currentTimeMillis();
        System.out.println("main <<<   output: " + isMatch2(s, p));
        end = System.currentTimeMillis();
        System.out.println("main <<< duration: " + (end - start) + " ms");

        // ???? ????
        // System.out.println("main <<<      hm: " + hm.toString());
        // System.out.println("main <<< hm.size: " + hm.size() + " callCounter: " + callCounter);

        // **** call method of interest and display result ****
        start = System.currentTimeMillis();
        System.out.println("main <<<   output: " + isMatch3(s, p));
        end = System.currentTimeMillis();
        System.out.println("main <<< duration: " + (end - start) + " ms");

        // **** call method of interest and display result ****
        start = System.currentTimeMillis();
        System.out.println("main <<<   output: " + isMatch(s, p));
        end = System.currentTimeMillis();
        System.out.println("main <<< duration: " + (end - start) + " ms");
    }

}