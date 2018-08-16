package robotwebapi

import geometry_msgs.Quaternion
import geometry_msgs.Vector3
import grails.converters.JSON
import grails.transaction.Transactional
import javafx.geometry.Pos
import org.h2.mvstore.ConcurrentArrayList
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.buffer.SlicedChannelBuffer
import org.ros.message.Time

@Transactional
class DataService {

//    private static _position = [x: 0D, y: 0D]
//    private static Time _position_time
//    private static _position_frame = 'odom'
//    private static ConcurrentArrayList<Position> positions = new ConcurrentArrayList<>()
    private static Position lastPosition = null

    void setPosition(double x, double y, Time time, frame) {
//        synchronized (_map) {
        lastPosition = new Position(x: x, y: y, time: time, frame: frame)
//        positions.add(lastPosition)
//        }
    }

    def getPosition() {
        lastPosition
    }

//    private static _twist = 0D
//    private static Time _twist_time
//    private static _twist_frame = 'odom'
//    private static ConcurrentArrayList<Twist> twists = new ConcurrentArrayList<>()
    private static Twist lastTwist = null

    void setTwist(double z, Time time, frame) {
//        synchronized (_map) {
        lastTwist = new Twist(value: z, time: time, frame: frame)
//        twists.add(lastTwist)
//        }

//        drawMap()
    }

    def getTwist() {
        lastTwist
    }

//    private static _velocity = [move: 0D, turn: 0D]
//    private static ConcurrentArrayList<Velocity> velocities = new ConcurrentArrayList<>()
    private static lastVelocity = null

    void setVelocity(double move, double turn) {
//        synchronized (_map) {
        lastVelocity = new Velocity(move: move, turn: turn)
//        velocities.add(lastVelocity)
//        }
    }

    def getVelocity() {
        lastVelocity
    }

//    private static _environment = [scanTime: 0f, timeIncrement: 0f, angleMin: 0f, angleMax: 0f, angleIncrement: 0f, ranges: [], rangeMin: 0f, rangeMax: 0f, intensities: []]
//    private static Time _environment_time
//    private static _environment_frame = 'odom'
//    private static ConcurrentArrayList<Environment> environments = new ConcurrentArrayList<>()
    private static lastEnvironment = null

    void setEnvironment(float scanTime, float timeIncrement, float angleMin, float angleMax, float angleIncrement, float[] ranges, float rangeMin, float rangeMax, float[] intensities, Time time, frame) {
//        synchronized (_map) {
        lastEnvironment = new Environment(
                scanTime: scanTime,
                timeIncrement: timeIncrement,
                angleMin: angleMin,
                angleMax: angleMax,
                angleIncrement: angleIncrement,
                ranges: ranges,
                rangeMin: rangeMin,
                rangeMax: rangeMax,
                intensities: intensities,
                time: time,
                frame: frame

        )
//        environments.add(lastEnvironment)
//        }

//        drawMap()
    }

    def getEnvironment() {
        lastEnvironment
    }

    private static Map lastMap = new Map()

    void setMap(Double originX, Double originY, Integer width, Integer height, ChannelBuffer data) {
        def bytes = new byte[width * height]
        data.readBytes(bytes)
        lastMap = new Map(
                originX: originX,
                originY: originY,
                width: width,
                height: height,
                data: bytes
        )
    }

    Map getMap() {
        lastMap
    }

//    private static _transforms = []
//    private static ConcurrentArrayList<Transform> transforms = new ConcurrentArrayList<>()
//
//    void setTransform(Time time, String frame, String childFrame, Vector3 translation, Quaternion rotation) {
////        synchronized (_map) {
//        transforms.add(new Transform(
//                time: time,
//                frame: frame,
//                x: translation.x,
//                y: translation.y
//        ))
////        }
//    }

//    public getTransforms() {
//        transforms.peekLast()
//    }

//    private static _map = []
//
//    List<Map> getMap() {
//        _map
//    }

//    private static _normalizedMap
//
//    List<Map> getNormalizedMap() {
//        _normalizedMap
//    }

//    private static initialPosition
//    private static initialTwist
//
//    void drawMap() {
//        Thread.startDaemon {
//            synchronized (_map) {
//
//                Environment environment = null
//                Transform environmentTransform = null
//                Position position = null
//                Transform positionTransform = null
//                Twist twist = null
//
//                environments.each { env ->
//                    if (!environment || environment.time < env.time) {
//                        def envT = transforms.find { it.frame == env.frame && it.time.secs == env.time.secs } as Transform
//                        if (envT) {
//                            def pos = positions.find { it.time.secs == env.time.secs } as Position
//                            if (pos) {
//                                def posT = transforms.find { it.frame == pos.frame && it.time.secs == pos.time.secs } as Transform
//                                if (posT) {
//                                    def tw = twists.find { it.time.secs == env.time.secs } as Twist
//                                    if (tw) {
//                                        environment = env
//                                        environmentTransform = envT
//                                        position = pos
//                                        positionTransform = posT
//                                        twist = tw
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//                if (!environment || !environmentTransform || !position || !positionTransform || !twist)
//                    return
//                position.x += positionTransform.x
//                position.y += positionTransform.y
//
////            if (!initialPosition)
////                initialPosition = [x: _position.x, y: _position.y]
////            if (initialTwist == null)
////                initialTwist = _twist
//
////            if (_position_time?.secs != _environment_time?.secs)
////                return
//
////            def previousAlpha = Math.asin(initialTwist) * 2
//                def alpha = Math.asin(twist.value) * 2
////            def alpha = previousAlpha - newAlpha
////            if (Math.abs(alpha) > 0.05)
////                println(Math.toDegrees(alpha))
//
////            rotatePoint(initialPosition, alpha)
////            _map?.each {
////                it.x += (_position.x - initialPosition.x) * 100
////                it.y += (_position.y - initialPosition.y) * 100
////                rotatePoint(it, alpha)
////            }
//                def ranges = environment.ranges
//                if (ranges) {
//                    def average = ranges.sum() / ranges.size()
//                    ranges.eachWithIndex { item, index ->
//                        if (item <= average) {
//                            def point = [
//                                    x: item * Math.cos(Math.toRadians(index)),// + environmentTransform.x,
//                                    y: item * Math.sin(Math.toRadians(index)),// + environmentTransform.y,
//                                    z: item
//                            ]
//
//                            rotatePoint(point, alpha)
//                            point.x -= position.x
//                            point.y -= position.y
//                            try {
//                                if ((point.x != 0 || point.y != 0) && !_map.any { it.x == point.x && it.y == point.y })
//                                    _map << point
//
//                            }
//                            catch (ignored) {
//                            }
//                        }
//                    }
//                }
//
////            initialPosition = [x: _position.x, y: _position.y]
////            initialTwist = _twist
//
//                normalizeMap()
//
//                def env = environments.find { it.time <= environment.time } as Environment
//                while (env) {
//                    environments.removeFirst(env)
//                    env = environments.find { it.time <= environment.time } as Environment
//                }
//
//                def pos = positions.find { it.time <= position.time } as Position
//                while (pos) {
//                    positions.removeFirst(pos)
//                    pos = positions.find { it.time <= position.time } as Position
//                }
//
//                def tw = twists.find { it.time <= twist.time } as Twist
//                while (tw) {
//                    twists.removeFirst(tw)
//                    tw = twists.find { it.time <= twist.time } as Twist
//                }
//            }
//        }
//    }
//
//    private void normalizeMap() {
//        println "map created"
//        _normalizedMap = map.findAll { point ->
//            !_map.any {
//                Math.pow(it.x - point.x, 2) + Math.pow(it.y - point.y, 2) < 0.0000001 && it.z < point.z
//            }
//        }
//    }
//
//    private static void roundPoint(Map point) {
//        point.x = (Math.round(point.x / 4) * 4).toFloat()
//        point.y = (Math.round(point.y / 4) * 4).toFloat()
//    }
//
//    private static void rotatePoint(Map point, Double alpha) {
//        Float x = point.x.toString().toFloat()
//        Float y = point.y.toString().toFloat()
//        point.x = x * Math.cos(alpha) - y * Math.sin(alpha)
//        point.y = x * Math.sin(alpha) + y * Math.cos(alpha)
//    }
//
//    void resetMap() {
//        synchronized (_map) {
//            _map.clear()
//            initialPosition = null
//            initialTwist = null
//        }
//    }
}
