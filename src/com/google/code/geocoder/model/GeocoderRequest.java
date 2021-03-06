package com.google.code.geocoder.model;


import java.io.Serializable;
import java.util.EnumMap;

/**
 * @author <a href="mailto:panchmp@gmail.com">Michael Panchenko</a>
 */
public class GeocoderRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String address;         //Address. Optional.
    private String language;        //Preferred language for results. Optional.
    private String region;          //Country code top-level domain within which to search. Optional.
    private LatLngBounds bounds;    //LatLngBounds within which to search. Optional.
    private LatLng location;        //LatLng about which to search. Optional.
    private final EnumMap<GeocoderComponent, String> components; //Components. Optional

    public GeocoderRequest() {
        this.components = new EnumMap<GeocoderComponent, String>(GeocoderComponent.class);
    }

    public GeocoderRequest(String address, String language) {
        this();
        this.address = address;
        this.language = language;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void addComponent(GeocoderComponent component, String value) {
        components.put(component, value);
    }

    public EnumMap<GeocoderComponent, String> getComponents() {
        return components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeocoderRequest that = (GeocoderRequest) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (bounds != null ? !bounds.equals(that.bounds) : that.bounds != null) return false;
        if (components != null ? !components.equals(that.components) : that.components != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (bounds != null ? bounds.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (components != null ? components.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GeocoderRequest{" +
                "address='" + address + '\'' +
                ", language='" + language + '\'' +
                ", region='" + region + '\'' +
                ", bounds=" + bounds +
                ", location=" + location +
                ", components=" + components +
                '}';
    }
}