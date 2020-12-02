package provider.ulti;

/**
 * Created by Biao on 14/12/2017.
 */
public class Nationality {
    private static String nationality = "India";

    public static String getNationality() {
        return nationality;
    }

    public static void setNationality(String nationality) {
        Nationality.nationality = nationality;
    }
}
