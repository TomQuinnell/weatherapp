package tq215.weatherapp.Back;

import main.tq215.weatherapp.Back.LocationCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import main.tq215.weatherapp.utils.Location;

@RunWith(JUnit4.class)
public class cacheTest {

    private static final int CACHECAP = LocationCache.getCAPACITY();

    @Test
    public void addToEmpty_single() {
        LocationCache cache = new LocationCache();

        cache.add( "0.0_0.0", "Test");

        System.out.println(cache);
        assert cache.getCache().size() == 1;
        assert cache.getRecents().size() == 1;
    }

    @Test
    public void addToEmpty_fiveDistinct() {
        LocationCache cache = new LocationCache();

        cache.add( "0.0_0.0", "Test0");
        cache.add( "1.0_1.0", "Test1");
        cache.add( "2.0_2.0", "Test2");
        cache.add( "3.0_3.0", "Test3");
        cache.add( "4.0_4.0", "Test4");

        System.out.println(cache);
        assert cache.getCache().size() == CACHECAP;
        assert cache.getRecents().size() == CACHECAP;
    }

    @Test
    public void addToEmpty_fiveSame() {
        LocationCache cache = new LocationCache();

        cache.add( "0.0_0.0", "Test0");
        cache.add( "0.0_0.0", "Test1");
        cache.add( "0.0_0.0", "Test2");
        cache.add( "0.0_0.0", "Test3");
        cache.add( "0.0_0.0", "Test4");


        System.out.println(cache);
        assert cache.getCache().size() == 1;
        assert cache.getRecents().size() == 1;
    }

    @Test
    public void doesNotOverfill_20Distinct() {
        LocationCache cache = new LocationCache();

        for (int i = 0; i < 20; i++) {
            cache.add(i + ".0" + "_" + i + ".0", "Test" + i);
        }

        System.out.println(cache);
        assert cache.getCache().size() == CACHECAP;
        assert cache.getRecents().size() == CACHECAP;
    }

    @Test
    public void doesNotOverfill_2x10Distinct() {
        LocationCache cache = new LocationCache();
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 10; i++) {
                cache.add(i + ".0" + "_" + i + ".0", "Test" + i);
            }
        }

        System.out.println(cache);
        assert cache.getCache().size() == CACHECAP;
        assert cache.getRecents().size() == CACHECAP;
    }




    @Test
    public void findLoc_inCache_toHead() {
        LocationCache cache = new LocationCache();
        String latlon = "0.0_0.0";
        String locName = "Test_0";
        cache.add(latlon, locName);
        cache.add("100.0_100.0", "dummy");

        System.out.println(cache);
        System.out.println();

        Location found = cache.findLocation(latlon, locName, true);

        System.out.println(cache);

        assert found.getLatlon().equals(latlon);
        assert found.getName().equals(locName);

        assert cache.getRecents().get(0) == found;
    }

    @Test
    public void findLoc_inCache_notoHead() {
        LocationCache cache = new LocationCache();
        String latlon = "0.0_0.0";
        String locName = "Test_0";
        cache.add(latlon, locName);
        cache.add("100.0_100.0", "dummy");

        System.out.println(cache);
        System.out.println();

        Location found = cache.findLocation(latlon, locName, false);

        System.out.println(cache);

        assert found.getLatlon().equals(latlon);
        assert found.getName().equals(locName);

        assert cache.getRecents().get(0) != found;
        assert cache.getRecents().lastElement() == found;
    }

    @Test
    public void findLoc_notInCache_toHead() {
        LocationCache cache = new LocationCache();
        String latlon = "0.0_0.0";
        String locName = "Test_0";
        //cache.add(latlon, locName);
        cache.add("100.0_100.0", "dummy");

        System.out.println(cache);
        System.out.println();

        Location found = cache.findLocation(latlon, locName, true);

        System.out.println(cache);

        assert found.getLatlon().equals(latlon);
        assert found.getName().equals(locName);

        assert cache.getRecents().get(0) == found;
    }

    @Test
    public void findLoc_notInCache_notoHead() {
        LocationCache cache = new LocationCache();
        String latlon = "0.0_0.0";
        String locName = "Test_0";
        //cache.add(latlon, locName);
        cache.add("100.0_100.0", "dummy");

        System.out.println(cache);
        System.out.println();

        Location found = cache.findLocation(latlon, locName, false);

        System.out.println(cache);

        assert found.getLatlon().equals(latlon);
        assert found.getName().equals(locName);

        assert cache.getRecents().get(0) != found;
        assert cache.getRecents().lastElement() == found;
    }

    @Test
    public void findLoc_wasInCache_butNotForLong_toHead() {
        LocationCache cache = new LocationCache();
        String latlon = "0.0_0.0";
        String locName = "Test_0";
        cache.add(latlon, locName);

        for (int i = 0; i < CACHECAP; i++) {
            cache.add(i + ".0_" + i + ".0", "dummy");
        }

        System.out.println(cache);
        System.out.println();

        Location found = cache.findLocation(latlon, locName, true);

        System.out.println(cache);

        assert found.getLatlon().equals(latlon);
        assert found.getName().equals(locName);

        assert cache.getRecents().get(0) == found;
    }

    @Test
    public void findLoc_wasInCache_butNotForLong_notoHead() {
        LocationCache cache = new LocationCache();
        String latlon = "0.0_0.0";
        String locName = "Test_0";
        cache.add(latlon, locName);

        for (int i = 0; i < CACHECAP; i++) {
            cache.add(i + ".0_" + i + ".0", "dummy");
        }

        System.out.println(cache);
        System.out.println();

        Location found = cache.findLocation(latlon, locName, false);

        System.out.println(cache);

        assert found.getLatlon().equals(latlon);
        assert found.getName().equals(locName);

        assert cache.getRecents().get(0) != found;
        assert cache.getRecents().lastElement() == found;
    }
}