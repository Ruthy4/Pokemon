package com.decagon.android.sq007

import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.retrofit.PokemonListInterface
import com.decagon.android.sq007.retrofit.RetrofitClient
import com.decagon.android.sq007.ui.Adapter.PokemonRecyclerAdapter
import com.decagon.android.sq007.ui.activities.SecondImplementationActivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_pokemon_list, container, false)

        // get the recycler view, set fixed size and the layout manager
        recyclerView = itemView.findViewById(R.id.pokemon_recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // onclick listener to the floating action button to open the next activity
        val floatingActionButton = itemView.findViewById<FloatingActionButton>(R.id.load_second_implementation)
        floatingActionButton.setOnClickListener { loadSecondImplementation() }

        // check for internet connectivity
        disposable = ReactiveNetwork
            .observeNetworkConnectivity(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.state() == NetworkInfo.State.CONNECTED) {
                    fetchData(100)
                } else if (it.state() == NetworkInfo.State.DISCONNECTED) {
                    Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
                }
            }
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // add onclick listener to the query edit text to toggle items to be displayed
        getLimit.setOnClickListener {

            // validate edit text input
            if (TextUtils.isEmpty(query_ET.text)) {
                query_ET.error = "Enter a query"
                return@setOnClickListener
            } else {
                val limit = query_ET.text.toString().toInt()
                fetchData(limit)
                query_ET.text.clear()
                it.hideKeyboard()
            }
        }
    }

    // function to hide keyboard
    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    // function to get the data from the API and set it to a recycler adapter to populate the recycler view
    private fun fetchData(limit: Int) {

        compositeDisposable.add(
            pokemonListInterface.getLimit(limit, 0)
                // schedule the data gotten to display on the main thread
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { pokemonDex ->

                    val adapter = PokemonRecyclerAdapter(requireContext(), pokemonDex.results)

                    recyclerView.adapter = adapter
                }
        )
    }

    // function to open the secondImplementationActivity
    private fun loadSecondImplementation() {
        val intent = Intent(activity, SecondImplementationActivity::class.java)
        startActivity(intent)
    }
}
