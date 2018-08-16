package robotwebapi

import grails.converters.JSON

class DataController {

    def dataService

    static defaultAction = "get"

    def get() {
        render([
                environment: dataService.environment,
                position   : dataService.position,
                twist      : dataService.twist,
                velocity   : dataService.velocity
        ] as JSON)
    }
}
