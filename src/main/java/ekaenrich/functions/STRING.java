package ekaenrich.functions;

public class STRING {
    private static final String EMPTY = "";

    private STRING() {

    }

    public static STRING getInstance() {
        return new STRING();
    }

    public boolean isNotNull(Object obj) {
        if(obj == null)
            return false;
        else
            return !String.valueOf(obj).isEmpty();
    }

    public boolean isNull(Object obj) {

        if (obj == null)
            return true;
        else
            return String.valueOf(obj).isEmpty();
    }

    public int strictLength(Object obj) {

        strictNullCheck(obj);
        return length(obj);
    }

    public int length(Object obj)  {

        if (!isNull(obj))
            return String.valueOf(obj).length();
        else
            return 0;
    }

    public int strictIndexOf(Object obj, Object regEx)  {

        strictNullCheck(obj);
        return indexOf(obj, regEx);
    }

    public int indexOf(Object obj, Object regEx)  {

        if (!isNull(obj) && !isNull(regEx))
            return String.valueOf(obj).indexOf(String.valueOf(regEx)) + 1;
        else
            return -1;
    }

    public int strictLastIndexOf(Object obj, Object regEx)  {

        strictNullCheck(obj);
        return lastIndexOf(obj, regEx);
    }

    public int lastIndexOf(Object obj, Object regEx)  {

        if (!isNull(obj) && !isNull(regEx))
            return String.valueOf(obj).lastIndexOf(String.valueOf(regEx)) + 1;
        else
            return -1;

    }

    public boolean strictEquals(Object obj1, Object obj2)  {

        strictNullCheck(obj1, obj2);
        return equals(obj1, obj2);
    }

    public boolean equals(Object obj1, Object obj2)  {

        if (isNull(obj1) || isNull(obj2))
            return false;
        else
            return obj1.equals(obj2);
    }

    public boolean strictEqualsIgnoreCase(Object obj1, Object obj2)  {

        strictNullCheck(obj1, obj2);
        return equalsIgnoreCase(obj1, obj2);
    }

    public boolean equalsIgnoreCase(Object obj1, Object obj2)  {

        if (!isNull(obj1) && !isNull(obj2))
            return String.valueOf(obj1).equalsIgnoreCase(String.valueOf(obj2));
        return false;
    }

    public boolean strictContains(Object obj, Object regEx)  {

        strictNullCheck(obj);
        return contains(obj, regEx);

    }

    public boolean contains(Object obj, Object regEx)  {

        if (!isNull(obj) && !isNull(regEx))
            return String.valueOf(obj).contains(String.valueOf(regEx));
        else
            return false;
    }

    public String strictSubstring(Object obj, int startIdx, int endIdx)  {

        strictNullCheck(obj);
        return substring(obj, startIdx, endIdx);
    }

    public String substring(Object obj, int startIdx, int endIdx)  {

        int length = length(obj);
        if (!isNull(obj) && startIdx > -1 && endIdx > -1 && startIdx < length) {
            String val = String.valueOf(obj);
            if (endIdx <= length)
                return val.substring(startIdx - 1, endIdx);
            else
                return val.substring(startIdx - 1, length);
        } else
            return EMPTY;
    }

    public String strictSubstring(Object obj, int startIdx)  {

        strictNullCheck(obj);
        return substring(obj, startIdx);
    }

    public String substring(Object obj, int startIdx)  {

        int length = length(obj);
        if (!isNull(obj) && startIdx > -1 && startIdx < length)
            return String.valueOf(obj).substring(startIdx - 1);
        else
            return EMPTY;
    }

    public String strictTrim(Object obj)  {

        strictNullCheck(obj);
        return trim(obj);
    }

    public String trim(Object obj)  {

        if (!isNull(obj))
            return String.valueOf(obj).trim();
        else
            return EMPTY;
    }

    public String strictToLowerCase(Object obj)  {

        strictNullCheck(obj);
        return toLowerCase(obj);
    }

    public String toLowerCase(Object obj)  {

        if (!isNull(obj))
            return String.valueOf(obj).toLowerCase();
        else
            return EMPTY;
    }

    public String strictToUpperCase(Object obj)  {

        strictNullCheck(obj);
        return toUpperCase(obj);
    }

    public String toUpperCase(Object obj)  {

        if (!isNull(obj))
            return String.valueOf(obj).toUpperCase();
        else
            return EMPTY;
    }

    public String concat(Object obj1, Object... obj2) {

        StringBuilder sb = new StringBuilder();
        if (!isNull(obj1))
            sb.append(obj1);
        if (!isNull(obj2)) {
            for (int i = 0; i < obj2.length; i++) {
                if (!isNull(obj2[i]))
                    sb.append(obj2[i]);
            }
        }
        return sb.toString();
    }

    public String strictValueOf(Object obj)  {

        strictNullCheck(obj);
        return valueOf(obj);
    }

    public String valueOf(Object obj)  {

        if (!isNull(obj))
            return String.valueOf(obj);
        else
            return EMPTY;
    }

    public boolean strictStartsWith(Object obj, Object str)  {

        strictNullCheck(obj);
        return startsWith(obj, str);
    }

    public boolean startsWith(Object obj, Object str)  {

        if (!isNull(obj) && !isNull(str))
            return String.valueOf(obj).startsWith(String.valueOf(str));
        else
            return false;
    }

    public boolean strictEndsWith(Object obj, Object str)  {

        strictNullCheck(obj);
        return endsWith(obj, str);
    }

    public boolean endsWith(Object obj, Object str)  {

        if (!isNull(obj) && !isNull(str))
            return String.valueOf(obj).endsWith(String.valueOf(str));
        else
            return false;
    }

    public String[] strictSplit(Object obj, Object delimiter)  {

        strictNullCheck(obj);
        return split(obj, delimiter);
    }

    public String[] split(Object obj, Object delimiter)  {

        if (!isNull(obj) && !isNull(delimiter))
            return String.valueOf(obj).split(String.valueOf(delimiter));
        else
            return new String[0];
    }

    public String strictSplitAndGet(Object obj, Object delimiter, int idx)  {

        strictNullCheck(obj);
        return splitAndGet(obj, delimiter, idx);
    }

    public String splitAndGet(Object obj, Object delimiter, int idx)  {

        if (!isNull(obj) && !isNull(delimiter) && idx > 0) {
            try {
                String[] s = String.valueOf(obj).split(String.valueOf(delimiter));
                if (idx <= s.length && !isNull(s[idx - 1]))
                    return s[idx - 1];
                else
                    return EMPTY;
            } catch (Exception e) {
                e.printStackTrace();
                return EMPTY;
            }
        } else
            return EMPTY;
    }

    public Double strictToDouble(Object obj)  {

        strictNullCheck(obj);
        return toDouble(obj);
    }

    public Double toDouble(Object obj) {

        if (isNull(obj)) {
            return 0D;
        } else {
            try {
                return Double.parseDouble(obj.toString());
            } catch (Exception e) {
				e.printStackTrace();
                throw e;
            }
        }
    }

    public Integer strictToInteger(Object obj)  {

        strictNullCheck(obj);
        return toInteger(obj);
    }

    public Integer toInteger(Object obj)  {

        if (isNull(obj)) {
            return 0;
        } else {
            try {
                return (new Double(obj.toString())).intValue();
            } catch (Exception e) {
				e.printStackTrace();
                throw e;
            }
        }
    }

    private void strictNullCheck(Object... args)  {

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null)
                throw new RuntimeException("Argument at pos:" + (i + 1) + " found to be null");
        }
    }

    public static void main(String[] args)  {

        STRING s = new STRING();
        System.out.println(s.strictToDouble("2"));
        System.out.println(s.strictToDouble("2.4"));
        System.out.println(s.strictToDouble(".2"));
        System.out.println(s.strictToDouble("0.2"));
        // System.out.println(s.toDouble("asd"));

        System.out.println(s.strictToInteger("2"));
        System.out.println(s.strictToInteger("2.4"));
        System.out.println(s.strictToInteger(".2"));
        System.out.println(s.strictToInteger("0.2"));
        // System.out.println(s.toInteger("asd"));

    }
}
