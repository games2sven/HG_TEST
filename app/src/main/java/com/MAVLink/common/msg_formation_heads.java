/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE FORMATION_HEADS PACKING
package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

import java.util.Arrays;

/**
* The formation mission information of heads.
*/
public class msg_formation_heads extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_FORMATION_HEADS = 176;
    public static final int MAVLINK_MSG_LENGTH = 63;
    private static final long serialVersionUID = MAVLINK_MSG_ID_FORMATION_HEADS;


      
    /**
    * The formation info.
    */
    public float info[] = new float[4];
      
    /**
    * Thr boundary point. E.g: -114.695086 53.390628 -23.742391.
    */
    public float array[] = new float[6];
      
    /**
    * The serilal number of Aircraft. E.g: 13.
    */
    public int serial_num;
      
    /**
    * The total number of dance id. E.g: 10000.
    */
    public int total_dance_id;
      
    /**
    * The formation version. E.g: V1.1.0.x.
    */
    public short version[] = new short[3];
      
    /**
    * The file md5.
    */
    public short md5[] = new short[16];
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_FORMATION_HEADS;
              
        
        for (int i = 0; i < info.length; i++) {
            packet.payload.putFloat(info[i]);
        }
                    
              
        
        for (int i = 0; i < array.length; i++) {
            packet.payload.putFloat(array[i]);
        }
                    
              
        packet.payload.putUnsignedShort(serial_num);
              
        packet.payload.putUnsignedShort(total_dance_id);
              
        
        for (int i = 0; i < version.length; i++) {
            packet.payload.putUnsignedByte(version[i]);
        }
                    
              
        
        for (int i = 0; i < md5.length; i++) {
            packet.payload.putUnsignedByte(md5[i]);
        }
                    
        
        return packet;
    }

    /**
    * Decode a formation_heads message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
         
        for (int i = 0; i < this.info.length; i++) {
            this.info[i] = payload.getFloat();
        }
                
              
         
        for (int i = 0; i < this.array.length; i++) {
            this.array[i] = payload.getFloat();
        }
                
              
        this.serial_num = payload.getUnsignedShort();
              
        this.total_dance_id = payload.getUnsignedShort();
              
         
        for (int i = 0; i < this.version.length; i++) {
            this.version[i] = payload.getUnsignedByte();
        }
                
              
         
        for (int i = 0; i < this.md5.length; i++) {
            this.md5[i] = payload.getUnsignedByte();
        }
                
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_formation_heads(){
        msgid = MAVLINK_MSG_ID_FORMATION_HEADS;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_formation_heads(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_FORMATION_HEADS;
        unpack(mavLinkPacket.payload);        
    }

                
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_FORMATION_HEADS - sysid:"+sysid+" compid:"+compid+" info:"+ Arrays.toString(info)+" array:"+Arrays.toString(array)+" serial_num:"+serial_num+" total_dance_id:"+total_dance_id+" version:"+Arrays.toString(version)+" md5:"+Arrays.toString(md5)+"";
    }
}
        