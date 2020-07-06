package main.tq215.weatherapp.Back;

import main.tq215.weatherapp.utils.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class LocationCache {
    private HashMap<String, Location> cache; // latlon string to Location object
    private Vector<Location> recents; // vector of recents, with most recent at left, oldest at right
    private static final int CAPACITY = 5;

    public LocationCache() {
        this.cache = new HashMap<>();
        this.recents = new Vector<>();
    }

    private void cons(Location l, boolean inRecents, boolean toHead) {
        // add to head or end
        if (toHead) {
            recents.insertElementAt(l, 0);
        } else {
            if (recents.size() < CAPACITY) {
                recents.add(l);
            }
        }

        // remove duplicate location if already there
        if (inRecents) {
            recents.remove(recents.lastIndexOf(l, recents.size() - 1));
        }

        // chop down to size if needed
        if (recents.size() > CAPACITY) {
            for (int i = CAPACITY; i < recents.size(); i++) {
                String newLatLon = recents.get(i).getLatlon();
                cache.remove(recents.get(i).getLatlon());
            }
            recents.setSize(CAPACITY);
        }
    }

    public void add(String latlon, String locName) {
        // first check cache
        if (cache.containsKey(latlon)) {
            // must also be in recents
            // get location and move to front of recents
            Location newLocation = cache.get(latlon);
            cons(newLocation, true, true);
        } else {
            // not in cache or recents
            // create new Location from latlon String
            String[] split = latlon.split("_");
            double lat = Double.parseDouble(split[0]);
            double lon = Double.parseDouble(split[1]);
            Location newLocation = new Location(locName, lat, lon);

            // add to cache
            cache.put(latlon, newLocation);

            // add to recents
            cons(newLocation, false, true);
        }
    }

    public Location findLocation(String latlon, String locName, boolean addToHead) {
        // first check cache
        if (cache.containsKey(latlon)) {
            // get Location from cache and reshuffle recents if asked to
            Location newLocation = cache.get(latlon);
            if (addToHead) {
                cons(newLocation, true, true);
            }

            return newLocation;
        } else {
            // not in cache, so not in recents, so add new Location to recents and return it
            String[] split = latlon.split("_");
            double lat = Double.parseDouble(split[0]);
            double lon = Double.parseDouble(split[1]);
            Location newLocation = new Location(locName, lat, lon);

            cons(newLocation, false, addToHead);
            cache.put(latlon, newLocation);

            return newLocation;
        }
    }

    public HashMap<String, Location> getCache() {
        // TODO deep copy??
        return cache;
    }

    public void setCache(HashMap<String, Location> cache) {
        this.cache = cache;
    }

    public Vector<Location> getRecents() {
        return recents;
    }

    public void setRecents(Vector<Location> recents) {
        this.recents = recents;
    }

    public static int getCAPACITY() {
        return CAPACITY;
    }

    @Override
    public String toString() {
        return "LocationCache{" +
                "cache=" + cache +
                ",\n recents=" + recents +
                '}';
    }
}
