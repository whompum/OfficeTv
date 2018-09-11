package com.californiadreamshostel.officetv.WEATHER.model;

import android.support.annotation.Nullable;

import java.util.List;

public class DarkSkyWeather {

    private int id;

    private double latitude;
    private double longitude;
    private String timezone;

    private Currently currently;

    private Daily daily;

    private Hourly hourly;

    @Nullable
    public Daily getDaily(){
        return daily;
    }

    @Nullable
    public Currently getCurrently(){
        return currently;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public String getTimezone(){
        return timezone;
    }

    public int getId() {
        return id;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setCurrently(Currently currently) {
        this.currently = currently;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    public static class Currently{
        private Long time;
        private String summary;
        private String icon;
        private Double precipIntensity;
        private Double precipProbability;
        private Double temperature;
        private Double apparentTemperature;
        private Double windSpeed;

        public void setTime(Long time) {
            this.time = time;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setPrecipIntensity(Double precipIntensity) {
            this.precipIntensity = precipIntensity;
        }

        public void setPrecipProbability(Double precipProbability) {
            this.precipProbability = precipProbability;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public void setApparentTemperature(Double apparentTemperature) {
            this.apparentTemperature = apparentTemperature;
        }

        public void setWindSpeed(Double windSpeed) {
            this.windSpeed = windSpeed;
        }

        public Long getTime() {
            return time;
        }

        public String getSummary() {
            return summary;
        }

        public String getIcon() {
            return icon;
        }

        public Double getPrecipIntensity() {
            return precipIntensity;
        }

        public Double getPrecipProbability() {
            return precipProbability;
        }

        public Double getTemperature() {
            return temperature;
        }

        public Double getApparentTemperature() {
            return apparentTemperature;
        }

        public Double getWindSpeed() {
            return windSpeed;
        }
    }

    public static class Daily{
        private String summary;
        private String icon;

        private List<DailyDataPoints> data;

        public String getSummary() {
            return summary;
        }

        public String getIcon() {
            return icon;
        }

        public List<DailyDataPoints> getData() {
            return data;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setData(List<DailyDataPoints> data) {
            this.data = data;
        }

        public static class DailyDataPoints{
            private long time;
            private String summary;
            private String icon;
            private long sunriseTime;
            private long sunsetTime;
            private String precipType;
            private double temperatureHigh;
            private double temperatureLow;
            private double windSpeed;

            public long getTime() {
                return time;
            }

            public String getSummary() {
                return summary;
            }

            public String getIcon() {
                return icon;
            }

            public long getSunriseTime() {
                return sunriseTime;
            }

            public long getSunsetTime() {
                return sunsetTime;
            }

            public String getPrecipType() {
                return precipType;
            }

            public double getTemperatureHigh() {
                return temperatureHigh;
            }

            public double getTemperatureLow() {
                return temperatureLow;
            }

            public double getWindSpeed() {
                return windSpeed;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public void setSunriseTime(long sunriseTime) {
                this.sunriseTime = sunriseTime;
            }

            public void setSunsetTime(long sunsetTime) {
                this.sunsetTime = sunsetTime;
            }

            public void setPrecipType(String precipType) {
                this.precipType = precipType;
            }

            public void setTemperatureHigh(double temperatureHigh) {
                this.temperatureHigh = temperatureHigh;
            }

            public void setTemperatureLow(double temperatureLow) {
                this.temperatureLow = temperatureLow;
            }

            public void setWindSpeed(double windSpeed) {
                this.windSpeed = windSpeed;
            }
        }
    }

    public static class Hourly{
        private String summary;
        private String icon;
        private List<HourlyDataPoints> data;

        public String getSummary() {
            return summary;
        }

        public String getIcon() {
            return icon;
        }

        public List<HourlyDataPoints> getData() {
            return data;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setData(List<HourlyDataPoints> data) {
            this.data = data;
        }

        public static class HourlyDataPoints{

            private long time;
            private String summary;
            private String icon;
            private double precipIntensity;
            private double precipProbability;
            private double temperature;
            private double apparentTemperature;
            private double windSpeed;

            public long getTime() {
                return time;
            }

            public String getSummary() {
                return summary;
            }

            public String getIcon() {
                return icon;
            }

            public double getPrecipIntensity() {
                return precipIntensity;
            }

            public double getPrecipProbability() {
                return precipProbability;
            }

            public double getTemperature() {
                return temperature;
            }

            public double getApparentTemperature() {
                return apparentTemperature;
            }

            public double getWindSpeed() {
                return windSpeed;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public void setPrecipIntensity(double precipIntensity) {
                this.precipIntensity = precipIntensity;
            }

            public void setPrecipProbability(double precipProbability) {
                this.precipProbability = precipProbability;
            }

            public void setTemperature(double temperature) {
                this.temperature = temperature;
            }

            public void setApparentTemperature(double apparentTemperature) {
                this.apparentTemperature = apparentTemperature;
            }

            public void setWindSpeed(double windSpeed) {
                this.windSpeed = windSpeed;
            }
        }
    }

}
