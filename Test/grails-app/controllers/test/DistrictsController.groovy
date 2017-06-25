package test

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class DistrictsController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Districts.list(params), model:[districtsCount: Districts.count()]
    }

    def show(Districts districts) {
        respond districts
    }

    def create() {
        respond new Districts(params)
    }

    @Transactional
    def save(Districts districts) {
        if (districts == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (districts.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond districts.errors, view:'create'
            return
        }

        districts.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'districts.label', default: 'Districts'), districts.id])
                redirect districts
            }
            '*' { respond districts, [status: CREATED] }
        }
    }

    def edit(Districts districts) {
        respond districts
    }

    @Transactional
    def update(Districts districts) {
        if (districts == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (districts.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond districts.errors, view:'edit'
            return
        }

        districts.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'districts.label', default: 'Districts'), districts.id])
                redirect districts
            }
            '*'{ respond districts, [status: OK] }
        }
    }

    @Transactional
    def delete(Districts districts) {

        if (districts == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        districts.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'districts.label', default: 'Districts'), districts.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'districts.label', default: 'Districts'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
