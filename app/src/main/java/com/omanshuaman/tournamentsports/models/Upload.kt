package com.omanshuaman.tournamentsports.models

class Upload {
    var imageUrl: String? = null
    var matchDate: String? = null
    var registerDate: String? = null
    var requirement: String? = null
    var rules: String? = null
    var address: String? = null
    var tournamentName: String? = null
    var longitude: String? = null
    var latitude: String? = null
    var Id: String? = null
    var SportsType: String? = null
    var entryFee: String? = null
    var prizeMoney: String? = null
    var uid: String? = null


    constructor(
        Id: String?,
        tournamentName: String?,
        matchDate: String?,
        registerDate: String?,
        requirement: String?,
        rules: String?,
        address: String?,
        imageUrl: String?,
        longitude: String?,
        latitude: String?,
        SportsType: String?,
        entryFee: String?,
        prizeMoney: String?,
        uid: String?
    ) {
        this.Id = Id
        this.imageUrl = imageUrl
        this.tournamentName = tournamentName
        this.matchDate = matchDate
        this.registerDate = registerDate
        this.requirement = requirement
        this.rules = rules
        this.address = address
        this.longitude = longitude
        this.latitude = latitude
        this.SportsType = SportsType
        this.entryFee = entryFee
        this.prizeMoney = prizeMoney
        this.uid = uid


    }

    constructor() {}
}
