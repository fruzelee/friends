package com.github.fruzelee.friends.model.entities

import java.io.Serializable

/**
 * @author Fazle Rabbi
 * github.com/fruzelee
 * web: fr.crevado.com
 */
object FriendsListResponse {
    data class Friend(
        val info: Info,
        val results: List<Result>
    ) : Serializable

    data class Info(
        val page: Int,
        val results: Int,
        val seed: String,
        val version: String
    ) : Serializable

    data class Result(
        val cell: String,
        val dob: Dob,
        val email: String,
        val gender: String,
        val id: Id,
        val location: Location,
        val login: Login,
        val name: Name,
        val nat: String,
        val phone: String,
        val picture: Picture,
        val registered: Registered
    ) : Serializable

    data class Dob(
        val age: Int,
        val date: String
    ) : Serializable

    data class Id(
        val name: String,
        val value: String
    ) : Serializable

    data class Location(
        val city: String,
        val coordinates: Coordinates,
        val country: String,
        val postcode: Any,
        val state: String,
        val street: Street,
        val timezone: Timezone
    ) : Serializable

    data class Login(
        val md5: String,
        val password: String,
        val salt: String,
        val sha1: String,
        val sha256: String,
        val username: String,
        val uuid: String
    ) : Serializable

    data class Name(
        val first: String,
        val last: String,
        val title: String
    ) : Serializable

    data class Picture(
        val large: String,
        val medium: String,
        val thumbnail: String
    ) : Serializable

    data class Registered(
        val age: Int,
        val date: String
    ) : Serializable

    data class Coordinates(
        val latitude: String,
        val longitude: String
    ) : Serializable

    data class Street(
        val name: String,
        val number: Int
    ) : Serializable

    data class Timezone(
        val description: String,
        val offset: String
    ) : Serializable
}