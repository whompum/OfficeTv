package com.californiadreamshostel.officetv.Weather.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.californiadreamshostel.officetv.Weather.Persistence.DailyConverter;
import com.californiadreamshostel.officetv.Weather.Persistence.HourlyConverter;

import java.util.List;

@Entity
public class WeatherData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Embedded(prefix = "currently_")
    private Currently currently;

    @Embedded(prefix = "hourly_")
    private Hourly hourly;

    @Embedded(prefix = "daily_")
    private Daily daily;

    public int getId() {
        return id;
    }

    public Currently getCurrently() {
        return currently;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public Daily getDaily() {
        return daily;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCurrently(Currently currently) {
        this.currently = currently;
    }

    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    public static class Currently{

        private long timestamp;
        private double temperature;
        private String condition;
        private double windSpeed;

        public long getTimestamp() {
            return timestamp;
        }

        public double getTemperature() {
            return temperature;
        }

        public String getCondition() {
            return condition;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public void setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
        }
    }
    public static class Hourly{

        @TypeConverters(HourlyConverter.class)
        private List<DataPoints> data;

        public static class DataPoints{

            private long timestamp;
            private double temperature;
            private String condition;
            private double windSpeed;

            public long getTimestamp() {
                return timestamp;
            }

            public double getTemperature() {
                return temperature;
            }

            public String getCondition() {
                return condition;
            }

            public double getWindSpeed() {
                return windSpeed;
            }

            public void setTimestamp(long timestamp) {
                this.timestamp = timestamp;
            }

            public void setTemperature(double temperature) {
                this.temperature = temperature;
            }

            public void setCondition(String condition) {
                this.condition = condition;
            }

            public void setWindSpeed(double windSpeed) {
                this.windSpeed = windSpeed;
            }
        }

        public List<DataPoints> getData() {
            return data;
        }

        public void setData(List<DataPoints> data) {
            this.data = data;
        }
    }
    public static class Daily{

        @TypeConverters(DailyConverter.class)
        private List<DataPoints> data;

        public List<DataPoints> getData() {
            return data;
        }

        public void setData(List<DataPoints> data) {
            this.data = data;
        }

        public static class DataPoints{

            private long timestamp;
            private double temperature;
            private String condition;
            private double windSpeed;

            public long getTimestamp() {
                return timestamp;
            }

            public double getTemperature() {
                return temperature;
            }

            public String getCondition() {
                return condition;
            }

            public double getWindSpeed() {
                return windSpeed;
            }

            public void setTimestamp(long timestamp) {
                this.timestamp = timestamp;
            }

            public void setTemperature(double temperature) {
                this.temperature = temperature;
            }

            public void setCondition(String condition) {
                this.condition = condition;
            }

            public void setWindSpeed(double windSpeed) {
                this.windSpeed = windSpeed;
            }
        }

    }

}
