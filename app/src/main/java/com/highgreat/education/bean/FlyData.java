package com.highgreat.education.bean;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class FlyData {

    double speed;
    double height;
    double distance;
    int gpsNum;
    int battery;
    double vs;
    double hs;
    float yaw;
    long time;
    int voltage;
    int channel;//遥控器信号
    int rssi;//图传信号
    float ground_distance;
    float roll;
    float pitch;
    int eph;
    int epv;
    int satellites_visible;
    float vx;
    float vy;
    long flying_total_time;
    int quality;
    float velx;
    float vely;
    long flying_info;
    int flight_mode;
    int system_status;
    float zacc;
    float angle;//俯仰角

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public int getEph() {
        return eph;
    }

    public void setEph(int eph) {
        this.eph = eph;
    }

    public int getEpv() {
        return epv;
    }

    public void setEpv(int epv) {
        this.epv = epv;
    }

    public int getSatellites_visible() {
        return satellites_visible;
    }

    public void setSatellites_visible(int satellites_visible) {
        this.satellites_visible = satellites_visible;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public long getFlying_total_time() {
        return flying_total_time;
    }

    public void setFlying_total_time(long flying_total_time) {
        this.flying_total_time = flying_total_time;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public float getVelx() {
        return velx;
    }

    public void setVelx(float velx) {
        this.velx = velx;
    }

    public float getVely() {
        return vely;
    }

    public void setVely(float vely) {
        this.vely = vely;
    }

    public long getFlying_info() {
        return flying_info;
    }

    public void setFlying_info(long flying_info) {
        this.flying_info = flying_info;
    }

    public int getFlight_mode() {
        return flight_mode;
    }

    public void setFlight_mode(int flight_mode) {
        this.flight_mode = flight_mode;
    }

    public int getSystem_status() {
        return system_status;
    }

    public void setSystem_status(int system_status) {
        this.system_status = system_status;
    }

    public float getZacc() {
        return zacc;
    }

    public void setZacc(float zacc) {
        this.zacc = zacc;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getGpsNum() {
        return gpsNum;
    }

    public void setGpsNum(int gpsNum) {
        this.gpsNum = gpsNum;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public double getVs() {
        return vs;
    }

    public void setVs(double vs) {
        this.vs = vs;
    }

    public double getHs() {
        return hs;
    }

    public void setHs(double hs) {
        this.hs = hs;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public float getGround_distance() {
        return ground_distance;
    }

    public void setGround_distance(float ground_distance) {
        this.ground_distance = ground_distance;
    }

    @Override
    public String toString() {
        return "FlyData{" +
                "speed=" + speed +
                ", height=" + height +
                ", distance=" + distance +
                ", gpsNum=" + gpsNum +
                ", battery=" + battery +
                '}';
    }
}
