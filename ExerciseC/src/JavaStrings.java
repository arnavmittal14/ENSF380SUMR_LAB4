public class JavaStrings {

    
    public int addTogether(String str1, String str2) {
        str1 = str1.trim();
        str2 = str2.trim();
        return (str1 + str2).length();
    }

    
    public String idProcessing(String firstName, String lastName, String petName, int year) {
        return firstName.charAt(0) + lastName.substring(0, 1) + petName.charAt(0) + year;
    }

    
    public String secretCode(String str) {
        str = str.toLowerCase().replaceAll("[aeiou]", "z");
        return str.substring(0, 3);
    }

    
    public int addTogetherRegEx(String str1, String str2) {
        str1 = str1.replaceAll("^\\s+|\\s+$", "");
        str2 = str2.replaceAll("^\\s+|\\s+$", "");
        return (str1 + str2).length();
    }

   
    public String idProcessingRegEx(String firstName, String lastName, String petName, int year) {
        String firstInitial = firstName.replaceAll("^(\\S).*$", "$1");
        String lastInitial = lastName.replaceAll("^(\\S).*$", "$1");
        String petInitial = petName.replaceAll("^(\\S).*$", "$1");
        return firstInitial + lastInitial + petInitial + year;
    }

   
    public String secretCodeRegEx(String str) {
        str = str.toLowerCase().replaceAll("[aeiou]", "z");
        return str.substring(0, 3);
    }

    public static void main(String[] args) {
        JavaStrings myObject = new JavaStrings();

       
        String oneExample = " 12 4 6789 ";
        String twoExample = " abcdef gh ";
        int theLength = myObject.addTogether(oneExample, twoExample);
        System.out.println(theLength);

 
        oneExample = " " + oneExample + " \n ";
        twoExample = " \t " + twoExample;
        theLength = myObject.addTogether(oneExample, twoExample);
        System.out.println(theLength);

    
        String personFirst = " Dorothy ";
        String personLast = " Gale ";
        String petName = " Toto ";
        int petBirth = 1899;
        String theID = myObject.idProcessing(personFirst.trim(), personLast.trim(), petName.trim(), petBirth);
        System.out.println(theID);

    
        String ingredientOne = " tomato ";
        String ingredientTwo = " WATER ";
        String theCode = myObject.secretCode(ingredientOne.trim());
        System.out.println(theCode);
        theCode = myObject.secretCode(ingredientTwo.trim());
        System.out.println(theCode);

    
        theLength = myObject.addTogetherRegEx(oneExample, twoExample);
        System.out.println(theLength);
        
        oneExample = " " + oneExample + " \n ";
        twoExample = " \t " + twoExample;
        theLength = myObject.addTogetherRegEx(oneExample, twoExample);
        System.out.println(theLength);

        theID = myObject.idProcessingRegEx(personFirst.trim(), personLast.trim(), petName.trim(), petBirth);
        System.out.println(theID);

        theCode = myObject.secretCodeRegEx(ingredientOne.trim());
        System.out.println(theCode);
        theCode = myObject.secretCodeRegEx(ingredientTwo.trim());
        System.out.println(theCode);
    }
}
