package com.example.znanykonultant.user.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.consultant.profile.ConsultantServicesAdapter
import com.example.znanykonultant.entity.ConsultantService
import com.example.znanykonultant.entity.Favourite

class UserFavouritesFragment : Fragment() {

    lateinit var favouritesRecycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_favourites, container, false)

        /*var fav: Favourite = Favourite()
        var fav2: ConsultantService = ConsultantService(150.0, "kons", "rozmowa")
        var servicesList = arrayListOf(fav)
        servicesList.add(fav2)

        favouritesRecycler = view.findViewById<RecyclerView>(R.id.favoritesRecycler)
        favouritesRecycler.layoutManager = LinearLayoutManager(view.context)
        favouritesRecycler.adapter = FavouritesConsultantAdapter(view.context, favouritesList)
        favouritesRecycler.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))*/

        return view
    }
}