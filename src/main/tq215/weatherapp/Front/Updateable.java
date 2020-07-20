package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.Location;

public interface Updateable {
    // an interface for objects which can be updated from a location, or from an internal current location
    void update(Location newLocation);
    void update();
}
