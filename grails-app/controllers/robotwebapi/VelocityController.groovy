package robotwebapi

import grails.converters.JSON

class VelocityController {

    def controlService
    def dataService

    static defaultAction = "get"

    def set() {
        controlService.setVelocity(
                [
                        x: params.move as Float,
                        y: 0,
                        z: 0
                ],
                [
                        x: 0,
                        y: 0,
                        z: params.turn as Float
                ]
        )
        render "DONE!"
    }

    def get() {
        render dataService.getVelocity() as JSON
    }
}
