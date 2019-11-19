/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE REPORT_STATS PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/**
* 
*/
public class msg_report_stats extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_REPORT_STATS = 207;
    public static final int MAVLINK_MSG_LENGTH = 84;
    private static final long serialVersionUID = MAVLINK_MSG_ID_REPORT_STATS;


      
    /**
    * Drone System Time, ms
    */
    public long utc;
      
    /**
    * Position X，unit:cm
    */
    public int lat;
      
    /**
    * Position Y，unit:cm
    */
    public int lon;
      
    /**
    * Position Z，unit:cm
    */
    public short alt;
      
    /**
    * Formation Position X，unit:cm
    */
    public short x;
      
    /**
    * Formation Position Y，unit:cm
    */
    public short y;
      
    /**
    * Formation Position Z，unit:cm
    */
    public short z;
      
    /**
    * Yaw，unit:0.01°
    */
    public int yaw;
      
    /**
    * Sensor Present
    */
    public int sensors_present;
      
    /**
    * Sensor Healthy
    */
    public int sensors_health;
      
    /**
    * Drone ID
    */
    public int drone_id;
      
    /**
    * Version of firmware，1234 means 1.2.3.4
    */
    public int firmware_version;
      
    /**
    * Version of dance，123 means 1.2.3
    */
    public int dance_version;
      
    /**
    * Dance Name
    */
    public short dance_name[] = new short[8];
      
    /**
    * Dance Md5
    */
    public short dance_md5[] = new short[16];
      
    /**
    * Product ID
    */
    public short product_id[] = new short[16];
      
    /**
    * Aux dance step token
    */
    public short aux_token;
      
    /**
    * Time Synchronization token
    */
    public short time_token;
      
    /**
    * Battery volumn
    */
    public short battery_volumn;
      
    /**
    * Formation status
    */
    public short formation_status;
      
    /**
    * RGB status
    */
    public short rgb_status;
      
    /**
    * Acc Clibration status
    */
    public short acc_clibration_status;
      
    /**
    * Mag Clibration status
    */
    public short mag_clibration_status;
      
    /**
    * Temperature，unit:℃
    */
    public byte temperature;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_REPORT_STATS;
              
        packet.payload.putUnsignedLong(utc);
              
        packet.payload.putInt(lat);
              
        packet.payload.putInt(lon);
              
        packet.payload.putShort(alt);
              
        packet.payload.putShort(x);
              
        packet.payload.putShort(y);
              
        packet.payload.putShort(z);
              
        packet.payload.putUnsignedShort(yaw);
              
        packet.payload.putUnsignedShort(sensors_present);
              
        packet.payload.putUnsignedShort(sensors_health);
              
        packet.payload.putUnsignedShort(drone_id);
              
        packet.payload.putUnsignedShort(firmware_version);
              
        packet.payload.putUnsignedShort(dance_version);
              
        
        for (int i = 0; i < dance_name.length; i++) {
            packet.payload.putUnsignedByte(dance_name[i]);
        }
                    
              
        
        for (int i = 0; i < dance_md5.length; i++) {
            packet.payload.putUnsignedByte(dance_md5[i]);
        }
                    
              
        
        for (int i = 0; i < product_id.length; i++) {
            packet.payload.putUnsignedByte(product_id[i]);
        }
                    
              
        packet.payload.putUnsignedByte(aux_token);
              
        packet.payload.putUnsignedByte(time_token);
              
        packet.payload.putUnsignedByte(battery_volumn);
              
        packet.payload.putUnsignedByte(formation_status);
              
        packet.payload.putUnsignedByte(rgb_status);
              
        packet.payload.putUnsignedByte(acc_clibration_status);
              
        packet.payload.putUnsignedByte(mag_clibration_status);
              
        packet.payload.putByte(temperature);
        
        return packet;
    }

    /**
    * Decode a report_stats message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.utc = payload.getUnsignedLong();
              
        this.lat = payload.getInt();
              
        this.lon = payload.getInt();
              
        this.alt = payload.getShort();
              
        this.x = payload.getShort();
              
        this.y = payload.getShort();
              
        this.z = payload.getShort();
              
        this.yaw = payload.getUnsignedShort();
              
        this.sensors_present = payload.getUnsignedShort();
              
        this.sensors_health = payload.getUnsignedShort();
              
        this.drone_id = payload.getUnsignedShort();
              
        this.firmware_version = payload.getUnsignedShort();
              
        this.dance_version = payload.getUnsignedShort();
              
         
        for (int i = 0; i < this.dance_name.length; i++) {
            this.dance_name[i] = payload.getUnsignedByte();
        }
                
              
         
        for (int i = 0; i < this.dance_md5.length; i++) {
            this.dance_md5[i] = payload.getUnsignedByte();
        }
                
              
         
        for (int i = 0; i < this.product_id.length; i++) {
            this.product_id[i] = payload.getUnsignedByte();
        }
                
              
        this.aux_token = payload.getUnsignedByte();
              
        this.time_token = payload.getUnsignedByte();
              
        this.battery_volumn = payload.getUnsignedByte();
              
        this.formation_status = payload.getUnsignedByte();
              
        this.rgb_status = payload.getUnsignedByte();
              
        this.acc_clibration_status = payload.getUnsignedByte();
              
        this.mag_clibration_status = payload.getUnsignedByte();
              
        this.temperature = payload.getByte();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_report_stats(){
        msgid = MAVLINK_MSG_ID_REPORT_STATS;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_report_stats(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_REPORT_STATS;
        unpack(mavLinkPacket.payload);        
    }

                                                    
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_REPORT_STATS - sysid:"+sysid+" compid:"+compid+" utc:"+utc+" lat:"+lat+" lon:"+lon+" alt:"+alt+" x:"+x+" y:"+y+" z:"+z+" yaw:"+yaw+" sensors_present:"+sensors_present+" sensors_health:"+sensors_health+" drone_id:"+drone_id+" firmware_version:"+firmware_version+" dance_version:"+dance_version+" dance_name:"+dance_name+" dance_md5:"+dance_md5+" product_id:"+product_id+" aux_token:"+aux_token+" time_token:"+time_token+" battery_volumn:"+battery_volumn+" formation_status:"+formation_status+" rgb_status:"+rgb_status+" acc_clibration_status:"+acc_clibration_status+" mag_clibration_status:"+mag_clibration_status+" temperature:"+temperature+"";
    }
}
        