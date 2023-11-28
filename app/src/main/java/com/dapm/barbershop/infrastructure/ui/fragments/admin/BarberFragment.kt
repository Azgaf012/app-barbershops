package com.dapm.barbershop.infrastructure.ui.fragments.admin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dapm.barbershop.R
import com.dapm.barbershop.databinding.FragmentAdminBarberBinding
import com.dapm.barbershop.domain.entities.Barber
import com.dapm.barbershop.infrastructure.ui.viewmodels.SharedViewModel
import com.dapm.barbershop.infrastructure.ui.viewmodels.admin.BarberViewModel
import com.dapm.barbershop.infrastructure.utils.EnumDayOfWeek
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class BarberFragment : Fragment() {

    private val viewModel: BarberViewModel by viewModels()

    private var _binding: FragmentAdminBarberBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var barberWorkingDays: List<String>
    private val selectedDays = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminBarberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.days.setOnClickListener { showMultiChoiceDialog() }
        binding.startTime.setOnClickListener { showTimePicker(it, binding.startTime.text.toString()) }
        binding.endTime.setOnClickListener { showTimePicker(it, binding.endTime.text.toString()) }
        binding.btnRegisterBarber.setOnClickListener { registerOrUpdateBarber() }
        //binding.btnCancel.setOnClickListener { navigateBack() }
        observeBarberRegistration()
        observeBarberUpdate()

        // Cargar los datos si estamos editando un barbero existente
        sharedViewModel.barberSelected.value?.let { barber ->
            fillBarberData(barber)
        } ?: run {
            initializeForNewBarber()
        }
    }

    private fun showMultiChoiceDialog() {
        val daysOfWeek = EnumDayOfWeek.values().map { it.displayName }

        val checkedItems = BooleanArray(daysOfWeek.size) {
            barberWorkingDays.contains(daysOfWeek[it])
        }

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        val checkBoxes = daysOfWeek.mapIndexed { index, day ->
            CheckBox(context).apply {
                text = day
                isChecked = checkedItems[index]
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedDays.add(day)
                    } else {
                        selectedDays.remove(day)
                    }
                    val sortedDays = selectedDays
                        .map { EnumDayOfWeek.fromDisplayName(it) }
                        .sortedBy { it.order }
                        .map { it.displayName }
                    barberWorkingDays = sortedDays
                    binding.days.setText(sortedDays.joinToString(", "))
                }
            }
        }

        checkBoxes.forEach { layout.addView(it) }

        AlertDialog.Builder(requireContext())
            .setTitle("Selecciona los dias de trabajo")
            .setView(layout)
            .setNegativeButton("Cerrar", null)
            .show()
    }

    private fun showTimePicker(view: View, existingTime: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_time_picker_dialog, null)

        val hourPicker = dialogView.findViewById<NumberPicker>(R.id.hour_picker).apply {
            minValue = 6
            maxValue = 22
        }

        val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minute_picker).apply {
            minValue = 0
            maxValue = 1
            displayedValues = arrayOf("00", "30")
        }

        if (existingTime.isNotEmpty()) {
            val timeParts = existingTime.split(":")
            hourPicker.value = timeParts[0].toInt()
            minutePicker.value = if (timeParts[1] == "00") 0 else 1
        }

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val hour = hourPicker.value
                val minute = if (minutePicker.value == 0) "00" else "30"

                when(view.id){
                    R.id.startTime -> binding.startTime.setText(String.format("%02d:%s", hour, minute))
                    R.id.endTime -> binding.endTime.setText(String.format("%02d:%s", hour, minute))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun fillBarberData(barber: Barber) {
        barberWorkingDays = barber.workingDays.map {
            EnumDayOfWeek.valueOf(it.uppercase(Locale.ROOT)).displayName
        }
    }

    private fun initializeForNewBarber() {
        barberWorkingDays = EnumDayOfWeek.values().map { it.displayName }
        selectedDays.clear()
    }

    private fun registerOrUpdateBarber() {
        // Recoger los datos del formulario
        val uid = sharedViewModel.barberSelected.value?.uid ?: ""
        val name = binding.nameBarber.text.toString().trim()
        val lastName = binding.lastNameBarber.text.toString().trim()
        val startTime = binding.startTime.text.toString().trim()
        val endTime = binding.endTime.text.toString().trim()
        val state = "active"

        val workingDays = selectedDays.map { EnumDayOfWeek.fromDisplayName(it).name }

        val barber = Barber(
            uid = uid,
            name = name,
            lastName = lastName,
            workingDays = workingDays,
            startTime = startTime,
            endTime = endTime,
            state = state
        )

        if (uid.isNotEmpty()) {
            viewModel.updateBarber(barber)
        } else {
            viewModel.registerBarber(barber)
        }
    }

    private fun observeBarberRegistration() {
        viewModel.registerBarber.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { barber ->
                    // Manejar el éxito del registro
                },
                onFailure = { exception ->
                    // Manejar el error en el registro
                }
            )
        }
    }

    private fun observeBarberUpdate() {
        viewModel.updatedBarber.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { barber ->
                    // Manejar el éxito de la actualización
                },
                onFailure = { exception ->
                    // Manejar el error en la actualización
                }
            )
        }
    }

}