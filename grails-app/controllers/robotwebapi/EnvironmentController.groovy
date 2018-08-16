package robotwebapi

import grails.converters.JSON

class EnvironmentController {

    def dataService

    static defaultAction = "get"

    def get() {
        render dataService.environment as JSON
    }

    def getRanges() {
        render(dataService.environment?.ranges ?: []) as JSON
    }

    def getIntensities() {
        render dataService.environment.intensities as JSON
    }

    def getMap() {
        render dataService.map as JSON
    }
}
