package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.Location;

public interface Updateable {
    // TODO move this back to GUIForecastComposite??
    void update(Location newLocation);
    void update();
}
