package robotwebapi

import ex3.PRM
import ex3.navigation.Navigator
import ex3.search.Dijkstra
import ex4.MainNode
import org.ros.node.DefaultNodeMainExecutor
import org.ros.node.NodeConfiguration
import org.ros.node.NodeMainExecutor

class TestController {

    def masterAddress = 'http://10.7.200.123:11311'

    def index() {

        def masterUri = new URI(masterAddress)
        def socket = new Socket(masterUri.host, masterUri.port)
        def localNetworkAddress = socket.localAddress
        socket.close()
        def nodeConfiguration = NodeConfiguration.newPublic(localNetworkAddress.hostAddress, masterUri)
        nodeConfiguration.setNodeName("master-${UUID.randomUUID()}"?.replace('-', ''))
        NodeMainExecutor exec = DefaultNodeMainExecutor.newDefault()
        PRM prm = new PRM(new Dijkstra(), false);
        exec.execute(prm, nodeConfiguration)
        nodeConfiguration.setNodeName("master-${UUID.randomUUID()}"?.replace('-', ''))
        exec.execute(new Navigator(prm), nodeConfiguration)
        nodeConfiguration.setNodeName("master-${UUID.randomUUID()}"?.replace('-', ''))
        exec.execute(new MainNode(), nodeConfiguration)

        render "started"
    }
}
