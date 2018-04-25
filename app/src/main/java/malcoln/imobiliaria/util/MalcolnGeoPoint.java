package malcoln.imobiliaria.util;

/**
 * Created by Malcoln on 05/12/2017.
 */

public class MalcolnGeoPoint {

    static double EARTH_MEAN_RADIUS_KM = 6371.0;
    static double EARTH_MEAN_RADIUS_MILE = 3958.8;

    private double latitude = 0.0;
    private double longitude = 0.0;

    /**
     * Creates a new default point with latitude and longitude set to 0.0.
     */
    public MalcolnGeoPoint() {
    }

    public double getLatitude() {
        return latitude;
    }

    public MalcolnGeoPoint(double latitude, double longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public MalcolnGeoPoint(MalcolnGeoPoint point){
        this(point.getLatitude(),point.getLongitude());
    }

    public void setLatitude(double latitude) {
        if (latitude > 90.0 || latitude < -90.0){
            throw new IllegalArgumentException("Latitude deve estar entre o intervalo de (-90.0, 90.0).");
        }
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        if (longitude > 180.0 || longitude < -180.0){
            throw new IllegalArgumentException("Longitude deve estar entre o intervalo de (-180.0, 180.0)");
        }
        this.longitude = longitude;
    }

    public double distanceInRadiansTo(MalcolnGeoPoint point) {
        double d2r = Math.PI / 180.0; // radian conversion factor
        double lat1rad = latitude * d2r;
        double long1rad = longitude * d2r;
        double lat2rad = point.getLatitude() * d2r;
        double long2rad = point.getLongitude() * d2r;
        double deltaLat = lat1rad - lat2rad;
        double deltaLong = long1rad - long2rad;
        double sinDeltaLatDiv2 = Math.sin(deltaLat / 2.);
        double sinDeltaLongDiv2 = Math.sin(deltaLong / 2.);
        // Square of half the straight line chord distance between both points.
        // [0.0, 1.0]
        double a =
                sinDeltaLatDiv2 * sinDeltaLatDiv2 + Math.cos(lat1rad) * Math.cos(lat2rad)
                        * sinDeltaLongDiv2 * sinDeltaLongDiv2;
        a = Math.min(1.0, a);
        return 2. * Math.asin(Math.sqrt(a));
    }

    public double distanceInKilometersTo(MalcolnGeoPoint point) {
        return distanceInRadiansTo(point) * EARTH_MEAN_RADIUS_KM;
    }

    /**
     * Get distance between this point and another {@code GeoPoint} in kilometers.
     *
     * @param point
     *          {@code GeoPoint} describing the other point being measured against.
     */
    public double distanceInMilesTo(MalcolnGeoPoint point) {
        return distanceInRadiansTo(point) * EARTH_MEAN_RADIUS_MILE;
    }

}
