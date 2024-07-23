import java.util.Arrays;

public class SimpleArrays {
    private String[] array;

    public SimpleArrays(String s) {
        array = new String[4];
        Arrays.fill(array, s);
    }

    public SimpleArrays() {
        this("Hello, ENSF 380");
    }


    public String arrayConcat(int index) {
        if (index < 0 || index >= array.length) {
            index = 0; 
        }
        StringBuilder result = new StringBuilder();
        for (int i = index; i < array.length; i++) {
            result.append(array[i]);
            if (i < array.length - 1) {
                result.append("#");
            }
        }
        return result.toString();
    }


    public String arrayConcat() {
        return arrayConcat(0);
    }


    public String arrayCrop(int start, int end) {
        if (start < 0 || end < 0 || start >= array.length || end >= array.length) {
            return "Fail";
        }
        if (start == end) {
            return "Match";
        }
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        StringBuilder result = new StringBuilder();
        for (int i = start; i <= end; i++) {
            result.append(array[i]);
            if (i < end) {
                result.append("#");
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        SimpleArrays myArray1 = new SimpleArrays();
        String foundResult1 = myArray1.arrayConcat();
        System.out.println(foundResult1);

        SimpleArrays myArray2 = new SimpleArrays();
        String foundResult2 = myArray2.arrayConcat(2);
        System.out.println(foundResult2);

        SimpleArrays myArray3 = new SimpleArrays("Hi you");
        String foundResult3 = myArray3.arrayConcat();
        System.out.println(foundResult3);

        SimpleArrays myArray4 = new SimpleArrays("Hi you");
        String foundResult4 = myArray4.arrayConcat(2);
        System.out.println(foundResult4);

        SimpleArrays myArray5 = new SimpleArrays("Hi you");
        String foundResult5 = myArray5.arrayCrop(0, 2);
        System.out.println(foundResult5);

        SimpleArrays myArray6 = new SimpleArrays("Hi you");
        String foundResult6 = myArray6.arrayCrop(3, 2);
        System.out.println(foundResult6);

        SimpleArrays myArray7 = new SimpleArrays("Hi you");
        String foundResult7 = myArray7.arrayCrop(0, 6);
        System.out.println(foundResult7);

        SimpleArrays myArray8 = new SimpleArrays("Hi you");
        String foundResult8 = myArray8.arrayCrop(3, 3);
        System.out.println(foundResult8);
    }
}

