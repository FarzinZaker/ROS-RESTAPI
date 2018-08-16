package robotwebapi

import grails.converters.JSON

class PositionController {

    def dataService

    static defaultAction = "get"

    def get() {
        render dataService.getPosition() as JSON
    }
}
