package com.dapm.barbershop.infrastructure.ui.fragments.admin

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import com.dapm.barbershop.R
import com.dapm.barbershop.domain.entities.Barbershop
import java.util.Locale

class BarbershopAdapter(
    context: Context,
    private val barbershops: MutableList<Barbershop>,
    private val listener: OnBarbershopLister
) : ArrayAdapter<Barbershop>(context, R.layout.admin_barbershop_item_list, barbershops), Filterable {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val allBarbershops = ArrayList(barbershops)

    interface OnBarbershopLister{
        fun onEdit(barbershop: Barbershop)
        fun onDisabled(barbershop: Barbershop)
        fun onEnabled(barbershop: Barbershop)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.admin_barbershop_item_list, parent, false)
        val barbershop = getItem(position)

        val nameView = view.findViewById<TextView>(R.id.barbershop_name)
        val barberStatusView = view.findViewById<TextView>(R.id.barbershop_status)
        val disabledBarberButton = view.findViewById<ImageButton>(R.id.disabled_barbershop)
        val enabledBarberButton = view.findViewById<ImageButton>(R.id.enabled_barbershop)

        nameView.text = barbershop?.name
        barberStatusView.text = "HABILITADO"
        barberStatusView.setTextColor(Color.BLUE)

        if(barbershop?.state == "RETIRED"){
            barberStatusView.text = "DESHABILITADO"
            barberStatusView.setTextColor(Color.RED)
        }

        if(barbershop?.state == "ACTIVED"){
            disabledBarberButton.visibility = View.GONE
            enabledBarberButton.visibility = View.VISIBLE
        } else {
            disabledBarberButton.visibility = View.VISIBLE
            enabledBarberButton.visibility = View.GONE
        }

        setButtonsListeners(view, barbershop)

        return view

    }

    override fun getItem(position: Int): Barbershop {
        return barbershops[position]
    }

    override fun getCount(): Int {
        return barbershops.size
    }

    private fun setButtonsListeners(view: View, barbershop:Barbershop?){
        val editButton = view.findViewById<ImageButton>(R.id.update_barber)
        val disabledButton = view.findViewById<ImageButton>(R.id.disabled_barber)
        val enabledButton = view.findViewById<ImageButton>(R.id.enabled_barber)

        editButton.setOnClickListener {
            barbershop?.let { it -> listener.onEdit(it) }
        }

        disabledButton.setOnClickListener {
            barbershop?.let { it -> listener.onDisabled(it) }
        }

        enabledButton.setOnClickListener {
            barbershop?.let { it -> listener.onEnabled(it) }
        }
    }

    fun updateBarbershop(barbershop: Barbershop){
        val index = barbershops.indexOfFirst { it.uid == barbershop.uid }
        if (index != -1) {
            barbershops[index] = barbershop
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<Barbershop>()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(allBarbershops)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                    for (barber in allBarbershops) {
                        if (barber.name.lowercase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(barber)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                barbershops.clear()
                barbershops.addAll(results?.values as Collection<Barbershop>)
                notifyDataSetChanged()
            }
        }
    }
}