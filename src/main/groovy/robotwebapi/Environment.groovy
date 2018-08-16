package robotwebapi

import org.ros.message.Time

class Environment {
    Float scanTime = 0f
    Float timeIncrement = 0f
    Float angleMin = 0f
    Float angleMax = 0f
    Float angleIncrement = 0f
    def ranges = []
    Float rangeMin = 0f
    Float rangeMax = 0f
    def intensities = []
    Time time
    String frame = 'odom'
}
