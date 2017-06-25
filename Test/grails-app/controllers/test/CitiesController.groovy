package test

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CitiesController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Cities.list(params), model:[citiesCount: Cities.count()]
    }

    def show(Cities cities) {
        respond cities
    }

    def create() {
        respond new Cities(params)
    }

    @Transactional
    def save(Cities cities) {
        if (cities == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (cities.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond cities.errors, view:'create'
            return
        }

        cities.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'cities.label', default: 'Cities'), cities.id])
                redirect cities
            }
            '*' { respond cities, [status: CREATED] }
        }
    }

    def edit(Cities cities) {
        respond cities
    }

    @Transactional
    def update(Cities cities) {
        if (cities == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (cities.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond cities.errors, view:'edit'
            return
        }

        cities.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'cities.label', default: 'Cities'), cities.id])
                redirect cities
            }
            '*'{ respond cities, [status: OK] }
        }
    }

    @Transactional
    def delete(Cities cities) {

        if (cities == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        cities.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'cities.label', default: 'Cities'), cities.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'cities.label', default: 'Cities'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
