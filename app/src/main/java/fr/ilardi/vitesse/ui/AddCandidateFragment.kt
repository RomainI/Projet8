package fr.ilardi.vitesse.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import fr.ilardi.vitesse.databinding.AddCandidateFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import fr.ilardi.vitesse.R

@AndroidEntryPoint
class AddCandidateFragment : Fragment() {

    private var _binding: AddCandidateFragmentBinding? = null
    private val binding get() = _binding!!

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
        addTextWatcher(firstNameLayout,firstNameEditText)

        val lastNameEditText = binding.lastName
        val phoneNumberEditText = binding.phoneNumber
        val emailAddressEditText = binding.email
        val dateOfBirth = binding.dateOfBirth
        val salaryEditText = binding.salaryExpectations
        val notesEditText = binding.notes
        val saveButton = binding.saveButton
        val imageCandidateImageView = binding.picture

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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}