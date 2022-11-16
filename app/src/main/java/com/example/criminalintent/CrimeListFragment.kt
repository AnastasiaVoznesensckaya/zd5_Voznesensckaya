package com.example.criminalintent


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG="CrimeListFragment"

class CrimeListFragment : Fragment() {
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())
    private val crimeListViewModel:CrimeListViewModel by lazy{
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)

    }
    override fun onAttach(context: Context) {

        super.onAttach(context)
        callbacks = context as Callbacks?
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view=inflater.inflate(R.layout.fragment_crime_list,container,false)
        crimeRecyclerView=view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager= LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let { Log.i(TAG,"Got crimes ${crimes.size}")
                    updateUI(crimes)}
            }
        )
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
    private fun updateUI(crimes: List<Crime>) {
        adapter=CrimeAdapter(crimes)
        crimeRecyclerView.adapter=adapter
    }
    private inner class CrimeAdapter(var crimes: List<Crime>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun getItemViewType(position: Int): Int {
            if (crimes[position].requiresPolice==true){
                return 1
            }
            return 0
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
            if (viewType==0){
                return CrimeHolder(LayoutInflater.from(context).inflate(R.layout.list_item_crime,parent,false))
            }
            return CrimeHolderPolice(LayoutInflater.from(context).inflate(R.layout.list_item_crime_police,parent,false))
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (crimes[position].requiresPolice==false){
                val crime=crimes[position]
                (holder as CrimeHolder).bind(crime)
            } else{
            val crime=crimes[position]
            (holder as CrimeHolderPolice).bindPolice(crime)}
        }

        override fun getItemCount(): Int {
            return crimes.size
        }
    }
    private inner class CrimeHolder(view: View):RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var crime: Crime
        val titleTextView: TextView =itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        init {
            itemView.setOnClickListener(this)
        }
        fun bind(crime:Crime){
            this.crime=crime
            titleTextView.text=this.crime.title
            dateTextView.text=this.crime.date.toString()
        }
        override fun onClick(v: View){
            callbacks?.onCrimeSelected(crime.id)
        }
    }
    private inner class CrimeHolderPolice(view:View):RecyclerView.ViewHolder(view),View.OnClickListener{
        private lateinit var crime: Crime
        val title: TextView=itemView.findViewById(R.id.crime_title_police)
        val datePolice: TextView=itemView.findViewById(R.id.crime_date_police)
        init{
            itemView.setOnClickListener(this)
        }
        fun bindPolice(crime: Crime){
            this.crime=crime
            title.text=this.crime.title
            datePolice.text=this.crime.date.toString()
        }
        override fun onClick(v:View){
            Toast.makeText(context,"${crime.title} pressed! 911", Toast.LENGTH_SHORT).show()
        }
    }
    companion object{
        fun newInstance() : CrimeListFragment{
            return CrimeListFragment()
        }
    }
}