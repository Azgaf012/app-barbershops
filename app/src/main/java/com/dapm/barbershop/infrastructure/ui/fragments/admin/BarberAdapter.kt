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
import com.dapm.barbershop.domain.entities.Barber
import java.util.Locale

class BarberAdapter(
    context: Context,
    private val barbers: MutableList<Barber>,
    private val listener: OnBarberLister
) : ArrayAdapter<Barber>(context, R.layout.admin_barber_item_list, barbers), Filterable {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val allBarbers = ArrayList(barbers)

    interface OnBarberLister{
        fun onEdit(barber: Barber)
        fun onDisabled(barber: Barber)
        fun onEnabled(barber: Barber)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.admin_barber_item_list, parent, false)
        val barber = getItem(position)

        val nameView = view.findViewById<TextView>(R.id.barber_name)
        val barberStatusView = view.findViewById<TextView>(R.id.barber_status)
        val disabledBarberButton = view.findViewById<ImageButton>(R.id.disabled_barber)
        val enabledBarberButton = view.findViewById<ImageButton>(R.id.enabled_barber)

        nameView.text = barber?.name
        barberStatusView.text = "HABILITADO"
        barberStatusView.setTextColor(Color.BLUE)

        if(barber?.state == "RETIRED"){
            barberStatusView.text = "DESHABILITADO"
            barberStatusView.setTextColor(Color.RED)
        }

        if(barber?.state == "ACTIVED"){
            disabledBarberButton.visibility = View.GONE
            enabledBarberButton.visibility = View.VISIBLE
        } else {
            disabledBarberButton.visibility = View.VISIBLE
            enabledBarberButton.visibility = View.GONE
        }

        setButtonsListeners(view, barber)

        return view

    }

    override fun getItem(position: Int): Barber {
        return barbers[position]
    }

    override fun getCount(): Int {
        return barbers.size
    }

    private fun setButtonsListeners(view: View, barber:Barber?){
        val editButton = view.findViewById<ImageButton>(R.id.update_barber)
        val disabledButton = view.findViewById<ImageButton>(R.id.disabled_barber)
        val enabledButton = view.findViewById<ImageButton>(R.id.enabled_barber)

        editButton.setOnClickListener {
            barber?.let { it -> listener.onEdit(it) }
        }

        disabledButton.setOnClickListener {
            barber?.let { it -> listener.onDisabled(it) }
        }

        enabledButton.setOnClickListener {
            barber?.let { it -> listener.onEnabled(it) }
        }
    }

    fun updateBarber(barber: Barber){
        val index = barbers.indexOfFirst { it.uid == barber.uid }
        if (index != -1) {
            barbers[index] = barber
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<Barber>()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(allBarbers)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                    for (barber in allBarbers) {
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
                barbers.clear()
                barbers.addAll(results?.values as Collection<Barber>)
                notifyDataSetChanged()
            }
        }
    }
}