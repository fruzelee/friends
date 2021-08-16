package com.github.fruzelee.friends.model.network

import com.github.fruzelee.friends.model.entities.FriendsListResponse
import com.github.fruzelee.friends.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Fazle Rabbi
 * github.com/fruzelee
 * web: fr.crevado.com
 */
class FriendsListApiService {
    /**
     * Retrofit adapts a Java interface to HTTP calls by using annotations on the declared methods to
     * define how requests are made. Create instances using {@linkplain Builder the builder} and pass
     * your interface to {create} to generate an implementation.
     */
    private val api = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL) // Set the API base URL.
        // Add converter factory for serialization and deserialization of objects.
        /**
         * A Converter.Factory converter which uses Gson for JSON.
         *
         * Because Gson is so flexible in the types it supports, this converter assumes that it can handle
         * all types.
         */
        .addConverterFactory(GsonConverterFactory.create())
        /**
         * **
         * Add a call adapter factory for supporting service method return types other than.
         *
         * A CallAdapter.Factory call adapter which uses RxJava 3 for creating observables.
         *
         * Adding this class to Retrofit allows you to return an Observable, Flowable, Single, Completable
         * or Maybe from service methods.
         */
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build() // Create the Retrofit instance using the configured values.
        // Create an implementation of the API endpoints defined by the service interface in our case it is RandomFriendsAPI.
        .create(FriendsListAPI::class.java)

    fun getRandomFriends(): Single<FriendsListResponse.Friend> {
        return api.getRandomFriends(
            Constants.RANDOM_FRIENDS_NUMBER
        )
    }
}