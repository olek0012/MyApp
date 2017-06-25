package test

class Cities {
    String name
    
    static hasMany = [district:Districts]
    static constraints = {
        name (maxLength:100, blank:false)
    }
    @Override
    String toString() {
        return name
    }
}
