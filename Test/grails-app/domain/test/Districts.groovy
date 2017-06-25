package test

class Districts {
    Cities cities
    
    String name
    String numberOfResidents
    String foundationDate
    
    static belongsTo = Cities
    
    @Override
    String toString() {
        return name
    }
}
