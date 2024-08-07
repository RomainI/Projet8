package fr.ilardi.vitesse.ui

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import fr.ilardi.vitesse.databinding.AddCandidateFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import fr.ilardi.vitesse.R
import fr.ilardi.vitesse.model.Candidate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddCandidateFragment : Fragment() {

    private var _binding: AddCandidateFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCandidateFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddCandidateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstNameEditText = binding.firstName
        val firstNameLayout = binding.firstNameLayout
        addTextWatcher(firstNameLayout, firstNameEditText)

        val lastNameEditText = binding.lastName
        val lastNameLayout = binding.lastNameLayout
        addTextWatcher(lastNameLayout, lastNameEditText)

        val phoneNumberEditText = binding.phoneNumber
        val phoneNumberLayout = binding.phoneNumberLayout
        addTextWatcher(phoneNumberLayout, phoneNumberEditText)

        val emailAddressEditText = binding.email
        val emailAddressLayout = binding.emailLayout
        addTextWatcher(emailAddressLayout, emailAddressEditText)

        val dateOfBirth = binding.dateOfBirth
        val dateOfBirthLayout = binding.dateOfBirthLayout
        addTextWatcher(dateOfBirthLayout, dateOfBirth)

        val salaryEditText = binding.salaryExpectations
        val salaryLayout = binding.salaryExpectationsLayout
        addTextWatcher(salaryLayout, salaryEditText)

        val notesEditText = binding.notes
        val notesLayout = binding.notesLayout
        addTextWatcher(notesLayout, notesEditText)

        val saveButton = binding.saveButton
        val imageCandidateImageView = binding.picture
        saveButton.setOnClickListener {
            if (validateFields()) {

                viewModel.insertCandidate(
                    candidate = Candidate(
                        firstName = binding.firstName.text.toString(),
                        lastName = binding.lastName.text.toString(),
                        phoneNumber = binding.phoneNumber.text.toString(),
                        email = binding.email.text.toString(),
                        dateOfBirth = dateOfBirth.text.toString(),
                        salary = salaryEditText.text.toString(),
                        pictureURI = "",
                        notes = notesEditText.text.toString(),
                        isFavorite = false
                    )
                )
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.fieds_must_be_completed),
                    Snackbar.LENGTH_SHORT
                )
            }
        }
        dateOfBirth.setOnClickListener {
            showDatePickerDialog(dateOfBirth)
        }

    }

    private fun addTextWatcher(layout: TextInputLayout, editText: TextInputEditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateInput(layout, editText)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun validateInput(layout: TextInputLayout, editText: TextInputEditText) {
        val text = editText.text.toString()
        if (text.isEmpty()) {
            layout.error = getString(R.string.empty_field)
        } else {
            layout.error = ""
        }
    }

    private fun validateFields(): Boolean {
        val fields = listOf(
            binding.firstName,
            binding.lastName,
            binding.phoneNumber,
            binding.email,
            binding.dateOfBirth,
            binding.salaryExpectations,
            binding.notes
        )

        return fields.all { it.text?.isNotEmpty() == true }
    }

    private fun showDatePickerDialog(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editText.setText(selectedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}