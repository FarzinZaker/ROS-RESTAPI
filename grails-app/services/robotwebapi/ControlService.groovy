package robotwebapi

import geometry_msgs.Transform
import geometry_msgs.Twist
import grails.converters.JSON
import grails.transaction.Transactional
import nav_msgs.GetMap
import nav_msgs.MapMetaData
import nav_msgs.OccupancyGrid
import nav_msgs.Odometry
import org.ros.concurrent.DefaultScheduledExecutorService
import org.ros.internal.node.DefaultNode
import org.ros.message.MessageListener
import org.ros.node.DefaultNodeFactory
import org.ros.node.NodeConfiguration
import sensor_msgs.Imu
import sensor_msgs.JointState
import sensor_msgs.LaserScan
import tf.tfMessage

@Transactional
class ControlService {

    def masterAddress = 'http://10.7.200.123:11311'
    def connected = false
    def currentVelocityCommand

    def dataService

    def connect() {
        def masterUri = new URI(masterAddress)
        def socket = new Socket(masterUri.host, masterUri.port)
        def localNetworkAddress = socket.localAddress
        socket.close()
        def nodeConfiguration = NodeConfiguration.newPublic(localNetworkAddress.hostAddress, masterUri)
        nodeConfiguration.setNodeName("master-${UUID.randomUUID()}"?.replace('-', ''))
        def factory = new DefaultNodeFactory(new DefaultScheduledExecutorService())
        def node = factory.newNode(nodeConfiguration) as DefaultNode
        def publisher = node.newPublisher('/cmd_vel', Twist._TYPE)
        currentVelocityCommand = publisher.newMessage()
        currentVelocityCommand.linear.x = 0
        currentVelocityCommand.linear.y = 0
        currentVelocityCommand.linear.z = 0
        currentVelocityCommand.angular.x = 0
        currentVelocityCommand.angular.y = 0
        currentVelocityCommand.angular.z = 0

//        def imuSubscriber = node.newSubscriber("imu", Imu._TYPE)
//        imuSubscriber.addMessageListener(new MessageListener() {
//            @Override
//            void onNewMessage(Object message) {
////                println message
//            }
//        })
        def positionSubscriber = node.newSubscriber("odom", Odometry._TYPE)
        positionSubscriber.addMessageListener(new MessageListener<Odometry>() {
            @Override
            void onNewMessage(Odometry message) {
                try {
                    dataService.setPosition(message.pose.pose.position.x, message.pose.pose.position.y, message.header.stamp, message.header.frameId)
                    dataService.setTwist(message.pose.pose.orientation.z, message.header.stamp, message.header.frameId)
                } catch (ignored) {
                    println ignored.message
                }
            }
        })
//        def mapSubscriber1 = node.newSubscriber("map_metadata", MapMetaData._TYPE)
//        mapSubscriber1.addMessageListener(new MessageListener<MapMetaData>() {
//            @Override
//            void onNewMessage(MapMetaData message) {
//                println message
//            }
//        })
        def mapSubscriber = node.newSubscriber("map", OccupancyGrid._TYPE)
        mapSubscriber.addMessageListener(new MessageListener<OccupancyGrid>() {
            @Override
            void onNewMessage(OccupancyGrid message) {
                try {
                    dataService.setMap(message.info.origin.position.x, message.info.origin.position.y, message.info.width, message.info.height, message.data)
                } catch (ignored) {
                    println ignored.message
                }
            }
        })
        def sensorSubscriber = node.newSubscriber("joint_states", JointState._TYPE)
        sensorSubscriber.addMessageListener(new MessageListener<JointState>() {
            @Override
            void onNewMessage(JointState message) {
                try {
                    dataService.setVelocity(message.velocity[0], message.velocity[1])
                } catch (ignored) {
                    println ignored.message
                }
            }
        })
        def laserSubscribe = node.newSubscriber("scan", LaserScan._TYPE)
        laserSubscribe.addMessageListener(new MessageListener<LaserScan>() {
            @Override
            void onNewMessage(LaserScan message) {
                try {
                    dataService.setEnvironment(message.scanTime, message.timeIncrement, message.angleMin, message.angleMax, message.angleIncrement, message.ranges, message.rangeMin, message.rangeMax, message.intensities, message.header.stamp, message.header.frameId)
                } catch (ignored) {
                    println ignored.message
                }
            }
        })
//        def transformSubscriber = node.newSubscriber("/tf", tfMessage._TYPE)
//        transformSubscriber.addMessageListener(new MessageListener() {
//            @Override
//            void onNewMessage(Object o) {
//                try {
//                    def message = o as tfMessage
//                    message.transforms?.each {
//                        dataService.setTransform(it.header.stamp, it.header.frameId, it.childFrameId, it.transform?.translation, it.transform?.rotation)
//                    }
//                } catch (ignored) {
//                    println ignored.message
//                }
//            }
//        })

        def publisherTimer = new Timer()
        publisherTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    publisher.publish(currentVelocityCommand)
                } catch (ignored) {
                    println ignored.message
                }
            }
        }, 0, 80)
        connected = true
    }

    def setVelocity(java.util.Map linear, java.util.Map angular) {
        if (!connected)
            connect()
        currentVelocityCommand.linear.x = linear.x
        currentVelocityCommand.linear.y = linear.y
        currentVelocityCommand.linear.z = linear.z
        currentVelocityCommand.angular.x = angular.x
        currentVelocityCommand.angular.y = angular.y
        currentVelocityCommand.angular.z = angular.z
    }
}
