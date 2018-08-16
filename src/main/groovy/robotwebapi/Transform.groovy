package robotwebapi

import geometry_msgs.Quaternion
import geometry_msgs.Vector3
import org.ros.message.Time

class Transform {
    Time time
    String frame
    Double x
    Double y
}
