package projet.graciannethevret.SIG.utils;


public class Options {
    public static final String IPGEO_DEFAULT = "192.168.1.14";
    public static final String PREF = "pref";
    public static final String KEYGEO = "ipGeoserver";
    public static final String[] TYPES ={"hopital","cabinetMedical","salleSport","parcoursSante"};
    public static String[] CRITERE;

    public static String IPGEO = IPGEO_DEFAULT;

    private Options(){}

    public static boolean contains(String tag, String[] critere) {
        for(String s : critere)
            if(s.equals(tag))
                return true;
        return false;
    }
}
