public class Test {

    public static void main(String[] args) {

        // Ein (leeres) Dummy-Objekt 'c' wird für die Konstruktoren benötigt.
        Ordered c_null = null;

        try {
            ISet<Num> isetNum = new ISet<>(c_null);
            OSet<Num> osetNum = new OSet<>(c_null);
            MSet<Num, Num> msetNumNum = new MSet<>(c_null);
            ISet<Bee> isetBee = new ISet<>(c_null);
            OSet<Bee> osetBee = new OSet<>(c_null);
            ISet<WildBee> isetWildBee = new ISet<>(c_null);
            OSet<WildBee> osetWildBee = new OSet<>(c_null);
            //MSet<WildBee, Integer> msetWildBeeInt = new MSet<>(c_null);
            ISet<HoneyBee> isetHoneyBee = new ISet<>(c_null);
            //OSet<HoneyBee> osetHoneyBee = new OSet<>(c_null);
            //MSet<HoneyBee, String> msetHoneyBeeString = new MSet<>(c_null);


        } catch (Exception e) {
            System.out.println("...FEHLER!");
            System.out.println("Ein Fehler ist aufgetreten: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
