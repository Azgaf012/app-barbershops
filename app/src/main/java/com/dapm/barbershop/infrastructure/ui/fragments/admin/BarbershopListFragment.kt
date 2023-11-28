package com.dapm.barbershop.infrastructure.ui.fragments.admin

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.dapm.barbershop.R
import com.dapm.barbershop.databinding.FragmentAdminBarbershopListBinding
import com.dapm.barbershop.domain.entities.Barber
import com.dapm.barbershop.domain.entities.Barbershop
import com.dapm.barbershop.infrastructure.ui.viewmodels.SharedViewModel
import com.dapm.barbershop.infrastructure.ui.viewmodels.admin.BarbershopListViewModel
import com.dapm.barbershop.infrastructure.utils.EnumBarberStatus
import com.dapm.barbershop.infrastructure.utils.showErrorSnackbar
import com.dapm.barbershop.infrastructure.utils.showSuccessSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BarbershopListFragment : Fragment(), BarbershopAdapter.OnBarbershopLister {

    private var _binding: FragmentAdminBarbershopListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BarbershopListViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminBarbershopListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpdateButtonClickListener()
        viewModel.fetchBarbershops()
    }

    private fun observeViewModel() {
        viewModel.barbershopList.observe(viewLifecycleOwner, Observer {
            barbershops -> barbershops?.let { updateListView(it as MutableList<Barbershop>) }
        })

        viewModel.updatedBarbershop.observe(viewLifecycleOwner, Observer { updatedBarbershop ->
            if (updatedBarbershop.isSuccess) {
                requireView().showErrorSnackbar("Barbero habilitado con exito")

            } else {
                requireView().showSuccessSnackbar("Error al habilitar al barbero")
            }
        })
    }

    private fun setUpdateButtonClickListener() {
        binding.backButton.setOnClickListener {
            navigateToMenuAdminFragment()
        }

        binding.addBarbershop.setOnClickListener {
            navigateToBarberFragment()
        }

        binding.searchBarbershop.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (binding.listviewBarbershops.adapter as BarberAdapter).filter.filter(s)
            }
        })
    }

    private fun updateListView(barbershops: MutableList<Barbershop>) {
        binding.listviewBarbershops.adapter = BarbershopAdapter(requireContext(), barbershops, this)
    }

    override fun onEdit(barbershops: Barbershop) {
        sharedViewModel.barbershopSelected.value = barbershops
        navigateToBarberFragment()
    }

    override fun onDisabled(barbershop: Barbershop) {
        barbershop.state = EnumBarberStatus.DISABLED.name
        showConfirmationDialog(barbershop, "¿Desea deshabilitar la barberia")
    }

    override fun onEnabled(barbershop: Barbershop) {
        barbershop.state = EnumBarberStatus.ENABLED.name
        showConfirmationDialog(barbershop, "¿Desea habilitar la barberia")
    }

    private fun navigateToMenuAdminFragment() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_adminBarberListFragment_to_adminMenuFragment)
        }
    }

    private fun navigateToBarberFragment() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_adminMenuFragment_to_adminBarberFragment)
        }
    }



    private fun showConfirmationDialog(barbershop: Barbershop, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage("${message} ${barbershop.name}?")
            setPositiveButton("Si") { dialog, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.updateBarbershop(barbershop)
                }
                (binding.listviewBarbershops.adapter as BarbershopAdapter).updateBarbershop(barbershop)
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

}