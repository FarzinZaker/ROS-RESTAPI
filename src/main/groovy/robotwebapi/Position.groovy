package robotwebapi

import org.ros.message.Time

class Position {
    Double x = 0
    Double y = 0
    Time time
    String frame = 'odom'
}
