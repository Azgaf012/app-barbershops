package com.dapm.barbershop.infrastructure.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.Navigation
import com.dapm.barbershop.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminMenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listBarbers = view.findViewById<LinearLayout>(R.id.admin_barber_list)
        val listBarbershops = view.findViewById<LinearLayout>(R.id.admin_barbershop_list)

        listBarbershops.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_adminMenuFragment_to_adminBarbershopListFragment)
        }

        listBarbers.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_adminMenuFragment_to_adminBarberListFragment)
        }

    }


}