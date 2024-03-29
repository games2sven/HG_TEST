/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE AUX_SETUP PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/**
* 
*/
public class msg_aux_setup extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_AUX_SETUP = 210;
    public static final int MAVLINK_MSG_LENGTH = 20;
    private static final long serialVersionUID = MAVLINK_MSG_ID_AUX_SETUP;


      
    /**
    * Latitude * 1E7
    */
    public int lat;
      
    /**
    * Longitude * 1E7
    */
    public int lon;
      
    /**
    * Altitude * 1E3
    */
    public int alt;
      
    /**
    * Yaw
    */
    public float yaw;
      
    /**
    * Aux dance step token
    */
    public long token;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_AUX_SETUP;
              
        packet.payload.putInt(lat);
              
        packet.payload.putInt(lon);
              
        packet.payload.putInt(alt);
              
        packet.payload.putFloat(yaw);
              
        packet.payload.putUnsignedInt(token);
        
        return packet;
    }

    /**
    * Decode a aux_setup message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.lat = payload.getInt();
              
        this.lon = payload.getInt();
              
        this.alt = payload.getInt();
              
        this.yaw = payload.getFloat();
              
        this.token = payload.getUnsignedInt();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_aux_setup(){
        msgid = MAVLINK_MSG_ID_AUX_SETUP;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_aux_setup(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_AUX_SETUP;
        unpack(mavLinkPacket.payload);        
    }

              
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_AUX_SETUP - sysid:"+sysid+" compid:"+compid+" lat:"+lat+" lon:"+lon+" alt:"+alt+" yaw:"+yaw+" token:"+token+"";
    }
}
        