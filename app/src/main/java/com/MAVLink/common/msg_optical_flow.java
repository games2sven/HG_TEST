/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE OPTICAL_FLOW PACKING
package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/**
* Optical flow from a flow sensor (e.g. optical mouse sensor)
*/
public class msg_optical_flow extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_OPTICAL_FLOW = 100;
    public static final int MAVLINK_MSG_LENGTH = 52;
    private static final long serialVersionUID = MAVLINK_MSG_ID_OPTICAL_FLOW;

    /**
    * Timestamp (UNIX)
    */
    public long time_usec;

    public long flow_timespan;
    /**
    * Flow in meters in x-sensor direction, angular-speed compensated
    */
    public float flow_comp_m_x;
      
    /**
    * Flow in meters in y-sensor direction, angular-speed compensated
    */
    public float flow_comp_m_y;
      
    /**
    * Ground distance in meters. Positive value: distance known. Negative value: Unknown distance
    */
    public float ground_distance;
      
    /**
    * Flow in pixels * 10 in x-sensor direction (dezi-pixels)
    */
    public float flow_x;
      
    /**
    * Flow in pixels * 10 in y-sensor direction (dezi-pixels)
    */
    public float flow_y;

//    public short flow_rot;
    public float flow_rot;//Sven
    /**
    * Sensor ID
    */
    public byte sensor_id;
      
    /**
    * Optical flow quality / confidence. 0: bad, 255: maximum quality
    */
    public byte quality;

    /**
     * Optical flow rotation quality / confidence. 0: bad, 255: maximum quality
     */
    public byte rot_quality;
    /**
     * hover flag.
     */
    public byte hover_flag;

    public long result_timespan;
    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_OPTICAL_FLOW;
              
        packet.payload.putUnsignedLong(time_usec);

        packet.payload.putUnsignedLong(flow_timespan);

        packet.payload.putFloat(flow_comp_m_x);
              
        packet.payload.putFloat(flow_comp_m_y);
              
        packet.payload.putFloat(ground_distance);
              
        packet.payload.putFloat(flow_x);
              
        packet.payload.putFloat(flow_y);

//        packet.payload.putShort(flow_rot);
        packet.payload.putFloat(flow_rot);//Sven修改

        packet.payload.putByte(sensor_id);
              
        packet.payload.putByte(quality);

        packet.payload.putByte(rot_quality);

        packet.payload.putByte(hover_flag);

        packet.payload.putUnsignedLong(result_timespan);

        return packet;
    }

    /**
    * Decode a optical_flow message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();

        this.time_usec = payload.getUnsignedLong();

        this.flow_timespan = payload.getUnsignedLong();

        this.flow_comp_m_x = payload.getFloat();
              
        this.flow_comp_m_y = payload.getFloat();
              
        this.ground_distance = payload.getFloat();
              
        this.flow_x = payload.getFloat();
              
        this.flow_y = payload.getFloat();

//        this.flow_rot = payload.getShort();
        this.flow_rot = payload.getFloat();//Sven修改

        this.sensor_id = payload.getByte();
              
        this.quality = payload.getByte();

        this.rot_quality = payload.getByte();

        this.hover_flag = payload.getByte();

        this.result_timespan = payload.getUnsignedLong();
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_optical_flow(){
        msgid = MAVLINK_MSG_ID_OPTICAL_FLOW;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_optical_flow(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_OPTICAL_FLOW;
        unpack(mavLinkPacket.payload);        
    }

                    
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_OPTICAL_FLOW -"+" time_usec:"+time_usec+" flow_comp_m_x:"+flow_comp_m_x+" flow_comp_m_y:"+flow_comp_m_y+" ground_distance:"+ground_distance+" flow_x:"+flow_x+" flow_y:"+flow_y+" sensor_id:"+sensor_id+" quality:"+quality+"";
    }
}
        