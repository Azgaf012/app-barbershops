package com.dapm.barbershop.infrastructure.ui.fragments.admin

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.dapm.barbershop.R
import com.dapm.barbershop.databinding.FragmentAdminBarberListBinding
import com.dapm.barbershop.domain.entities.Barber
import com.dapm.barbershop.infrastructure.ui.viewmodels.SharedViewModel
import com.dapm.barbershop.infrastructure.ui.viewmodels.admin.BarberListViewModel
import com.dapm.barbershop.infrastructure.utils.EnumBarberStatus
import com.dapm.barbershop.infrastructure.utils.showErrorSnackbar
import com.dapm.barbershop.infrastructure.utils.showSuccessSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BarberListFragment : Fragment(), BarberAdapter.OnBarberLister {

    private var _binding: FragmentAdminBarberListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BarberListViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminBarberListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpdateButtonClickListener()
        viewModel.fetchBarbers()
    }

    private fun observeViewModel() {
        viewModel.barberList.observe(viewLifecycleOwner, Observer {
            barbers -> barbers?.let { updateListView(it as MutableList<Barber>) }
        })

        viewModel.updatedBarber.observe(viewLifecycleOwner, Observer { updatedBarber ->
            if (updatedBarber.isSuccess) {
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

        binding.addBarber.setOnClickListener {
            navigateToBarberFragment()
        }

        binding.searchBarber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (binding.listviewBarbers.adapter as BarberAdapter).filter.filter(s)
            }
        })
    }

    private fun updateListView(barbers: MutableList<Barber>) {
        binding.listviewBarbers.adapter = BarberAdapter(requireContext(), barbers, this)
    }

    override fun onEdit(barber: Barber) {
        sharedViewModel.barberSelected.value = barber
        navigateToBarberFragment()
    }

    override fun onDisabled(barber: Barber) {
        barber.state = EnumBarberStatus.DISABLED.name
        showConfirmationDialog(barber, "¿Desea deshabilitar al barbero")
    }

    override fun onEnabled(barber: Barber) {
        barber.state = EnumBarberStatus.ENABLED.name
        showConfirmationDialog(barber, "¿Desea habilitar al barbero")
    }

    private fun navigateToMenuAdminFragment() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_adminBarbershopListFragment_to_adminMenuFragment)
        }
    }

    private fun navigateToBarberFragment() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_adminMenuFragment_to_adminBarberFragment)
        }
    }



    private fun showConfirmationDialog(barber: Barber, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage("${message} ${barber.name}?")
            setPositiveButton("Si") { dialog, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.updateBarber(barber)
                }
                (binding.listviewBarbers.adapter as BarberAdapter).updateBarber(barber)
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

}