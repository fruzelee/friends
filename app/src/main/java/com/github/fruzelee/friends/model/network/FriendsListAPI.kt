package com.github.fruzelee.friends.model.network

import com.github.fruzelee.friends.model.entities.FriendsListResponse
import com.github.fruzelee.friends.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Fazle Rabbi
 * github.com/fruzelee
 * web: fr.crevado.com
 */
interface FriendsListAPI {
    /**
     * To Make a GET request.
     *
     * Pass the endpoint of the URL that is defined in the Constants.
     *
     *
     */
    @GET(Constants.API_ENDPOINT)
    fun getRandomFriends(
        // Query parameter appended to the URL. This is the best practice instead of appending it.
        @Query(Constants.KEY_RESULTS) results: Int,
    ): Single<FriendsListResponse.Friend> // The Single class implements the Reactive Pattern for a single value response. Click on the class using the Ctrl + Left Mouse Click to know more.

    // For more details have a look at http://reactivex.io/documentation/single.html or http://reactivex.io/RxJava/javadoc/io/reactivex/Single.html
}