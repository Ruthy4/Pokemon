package com.decagon.android.sq007.retrofit

import com.decagon.android.sq007.model.Pokemon
import com.decagon.android.sq007.model.PokemonX
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonListInterface {

    @GET("pokemon?limit=100&offset=0")
    fun getPoke(): Observable<PokemonX>

    @GET("pokemon")
    fun getLimit(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Observable<PokemonX>

    @GET
    fun getPokemon(@Url pokemonUrl: String): Observable<Pokemon>
}
