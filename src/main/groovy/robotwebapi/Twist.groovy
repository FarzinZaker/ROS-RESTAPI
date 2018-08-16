package robotwebapi

import org.ros.message.Time

class Twist {
    Double value = 0D
    Time time
    String frame = 'odom'
}
