package com.decagon.android.sq007

import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.retrofit.PokemonListInterface
import com.decagon.android.sq007.retrofit.RetrofitClient
import com.decagon.android.sq007.ui.Adapter.PokemonRecyclerAdapter
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import retrofit2.Retrofit

class PokemonList : Fragment() {

    private var compositeDisposable = CompositeDisposable()
    private var pokemonListInterface: PokemonListInterface
    private lateinit var recyclerView: RecyclerView
    lateinit var disposable: Disposable

    init {
        val retrofit: Retrofit = RetrofitClient.instance
        pokemonListInterface = retrofit.create(PokemonListInterface::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_pokemon_list, container, false)

        recyclerView = itemView.findViewById(R.id.pokemon_recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        disposable = ReactiveNetwork
            .observeNetworkConnectivity(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.state() == NetworkInfo.State.CONNECTED) {
                    fetchData()
                } else if (it.state() == NetworkInfo.State.DISCONNECTED) {
                    Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
                }
            }
        // fetchData()

        return itemView
    }

    private fun fetchData() {
        // val limit = pokemon_limit_ET.text.toString().toInt()
        compositeDisposable.add(
            pokemonListInterface.getLimit(100, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { pokemonDex ->

                    Log.d("FetchData", "fetchData: $pokemonDex")
//                Common.pokemonList =pokemonDex.results
                    val adapter = PokemonRecyclerAdapter(requireContext(), pokemonDex.results)

                    recyclerView.adapter = adapter
                }
        )
    }
}
