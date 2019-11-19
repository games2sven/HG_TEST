/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE AUX_SETUP_ACK PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/**
* 
*/
public class msg_aux_setup_ack extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_AUX_SETUP_ACK = 211;
    public static final int MAVLINK_MSG_LENGTH = 7;
    private static final long serialVersionUID = MAVLINK_MSG_ID_AUX_SETUP_ACK;


      
    /**
    * Aux dance step token
    */
    public long token;
      
    /**
    * Drone ID
    */
    public int id;
      
    /**
    * Result: 1, 
    */
    public short result;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_AUX_SETUP_ACK;
              
        packet.payload.putUnsignedInt(token);
              
        packet.payload.putUnsignedShort(id);
              
        packet.payload.putUnsignedByte(result);
        
        return packet;
    }

    /**
    * Decode a aux_setup_ack message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.token = payload.getUnsignedInt();
              
        this.id = payload.getUnsignedShort();
              
        this.result = payload.getUnsignedByte();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_aux_setup_ack(){
        msgid = MAVLINK_MSG_ID_AUX_SETUP_ACK;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_aux_setup_ack(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_AUX_SETUP_ACK;
        unpack(mavLinkPacket.payload);        
    }

          
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_AUX_SETUP_ACK - sysid:"+sysid+" compid:"+compid+" token:"+token+" id:"+id+" result:"+result+"";
    }
}
        