package com.example.znanykonultant.user.consultant.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.databinding.OpinionRowItemBinding
import com.example.znanykonultant.entity.Opinion
import com.example.znanykonultant.entity.User
import com.google.firebase.database.*
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem

class UserConsultantOpinionFragment(consultantUid: String) : Fragment() {

    private lateinit var recyclerView : RecyclerView
    val adapter = GroupieAdapter()
    val opinions: HashMap<String, Opinion> = HashMap<String, Opinion>()
    val consultantUid = consultantUid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_user_consultant_opinion, container, false)
        recyclerView = view.findViewById(R.id.opinionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        setDatabaseListener()
        return view
    }

    private fun setDatabaseListener() {
        val ref = FirebaseDatabase.getInstance().getReference("opinions/$consultantUid")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val opinion = snapshot.getValue(Opinion::class.java)?: return
                opinions[snapshot.key!!] = opinion
                refreshRecyclerView()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val opinion = snapshot.getValue(Opinion::class.java)?: return
                opinions[snapshot.key!!] = opinion
                refreshRecyclerView()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                opinions.remove(snapshot.key)
                refreshRecyclerView()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun refreshRecyclerView() {
        adapter.clear()
        opinions.values.forEach {
            adapter.add(OpinionItem(it))
        }
    }

}

class OpinionItem(val opinion: Opinion): BindableItem<OpinionRowItemBinding>(){
    var opinionUser: User? = null
    override fun getLayout(): Int {
        return R.layout.opinion_row_item
    }

    override fun bind(viewBinding: OpinionRowItemBinding, position: Int) {
        FirebaseDatabase.getInstance().getReference(
            "users/${opinion.userId}"
        ).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                opinionUser = snapshot.getValue(User::class.java)?: return
                viewBinding.opinionName.text = opinionUser!!.name + " " + opinionUser!!.surname
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        viewBinding.opinionTitle.text = opinion.title
        viewBinding.opinionRating.text = opinion.rating.toString()
        viewBinding.opinionDescription.text = opinion.description
    }

    override fun initializeViewBinding(view: View): OpinionRowItemBinding {
        return OpinionRowItemBinding.bind(view)
    }

}