package fr.ilardi.vitesse.ui

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
    private var candidate: Candidate? = null
    private var imageUri: Uri? = null

    private lateinit var imageCandidateImageView: ImageView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.READ_MEDIA_IMAGES] == true ||
                permissions[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] == true
            ) {
                openGallery()
            } else {
                view?.let {
                    Snackbar.make(it, getString(R.string.settings), Snackbar.LENGTH_LONG).show()
                }
            }
        }

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    try {
                        requireContext().contentResolver.takePersistableUriPermission(
                            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        imageUri = uri
                        imageCandidateImageView.setImageURI(uri)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                        Snackbar.make(
                            binding.root,
                            "Failed to persist URI permissions",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    //getParcelable deprecated, but fixed can only be used afetr TIRAMISU version
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            candidate = arguments?.getParcelable(ARG_CANDIDATE, Candidate::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_CANDIDATE)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddCandidateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageCandidateImageView = binding.picture
        initTextWatchers()
        initImageClickHandler()
        binding.saveButton.isEnabled = false
        binding.saveButton.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.disabled_button_gray
            )
        )
        if (candidate != null) {
            fillFieldsWhenEdit()
        }
        setupSaveButton()
        binding.dateOfBirth.setOnClickListener {
            showDatePickerDialog(binding.dateOfBirth)
        }
        binding.arrowIcon.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(MainFragment())
        }

    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            var stringImageUri: String
            if (imageUri == null) {
                stringImageUri = ""
            } else {
                stringImageUri = imageUri.toString()
            }
            if (candidate != null && validateFields()) {
                val updatedCandidate = candidate?.copy(
                    firstName = binding.firstName.text.toString(),
                    lastName = binding.lastName.text.toString(),
                    phoneNumber = binding.phoneNumber.text.toString(),
                    email = binding.email.text.toString(),
                    dateOfBirth = binding.dateOfBirth.text.toString(),
                    salary = binding.salaryExpectations.text.toString(),
                    notes = binding.notes.text.toString(),
                    pictureURI = stringImageUri
                )
                updatedCandidate?.let {
                    viewModel.updateCandidate(it)
                }
            } else {
                if (validateFields()) {
                    viewModel.insertCandidate(
                        candidate = Candidate(
                            firstName = binding.firstName.text.toString(),
                            lastName = binding.lastName.text.toString(),
                            phoneNumber = binding.phoneNumber.text.toString(),
                            email = binding.email.text.toString(),
                            dateOfBirth = binding.dateOfBirth.text.toString(),
                            salary = binding.salaryExpectations.text.toString(),
                            pictureURI = stringImageUri,
                            notes = binding.notes.text.toString(),
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
            (activity as? MainActivity)?.replaceFragment(MainFragment())
        }
    }

    private fun fillFieldsWhenEdit() {
        candidate?.let { candidate ->
            binding.firstName.setText(candidate.firstName)
            binding.lastName.setText(candidate.lastName)
            binding.phoneNumber.setText(candidate.phoneNumber)
            binding.email.setText(candidate.email)
            binding.dateOfBirth.setText(candidate.dateOfBirth)
            binding.salaryExpectations.setText(candidate.salary)
            binding.notes.setText(candidate.notes)
            if (candidate.pictureURI != "") {
                imageCandidateImageView.setImageURI(Uri.parse(candidate.pictureURI))
            }
        }
    }

    //checking if the SDK is >= 33 to adapt the permission function
    private fun initImageClickHandler(){
        imageCandidateImageView.setOnClickListener {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                        ) == PackageManager.PERMISSION_GRANTED -> {
                    openGallery()
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.READ_MEDIA_IMAGES
                        ) == PackageManager.PERMISSION_GRANTED -> {
                    openGallery()
                }

                Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED -> {
                    openGallery()
                }

                else -> {
                    val permissions = mutableListOf<String>()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        permissions.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                    requestPermissionLauncher.launch(permissions.toTypedArray())
                }
            }
        }
    }

    private fun initTextWatchers() {
        addTextWatcher(binding.firstNameLayout, binding.firstName, false)
        addTextWatcher(binding.lastNameLayout, binding.lastName, false)
        addTextWatcher(binding.phoneNumberLayout, binding.phoneNumber, false)
        addTextWatcher(binding.emailLayout, binding.email, true)
        addTextWatcher(binding.dateOfBirthLayout, binding.dateOfBirth, false)
        addTextWatcher(binding.salaryExpectationsLayout, binding.salaryExpectations, false)
        addTextWatcher(binding.notesLayout, binding.notes, false)
    }

    // open the photo gallery
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        selectImageLauncher.launch(intent)
    }

    private fun addTextWatcher(
        layout: TextInputLayout,
        editText: TextInputEditText,
        isEmail: Boolean
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateInput(layout, editText, isEmail)
                checkAllFields()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    //function used to enable or disable the save button and change its color
    private fun checkAllFields() {
        val isAllFieldsValid =
            validateFields()
        binding.saveButton.isEnabled = isAllFieldsValid
        if (isAllFieldsValid) {
            binding.saveButton.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
    }

    //function used to display a message in the Textinput fields
    private fun validateInput(
        layout: TextInputLayout,
        editText: TextInputEditText,
        isEmail: Boolean
    ) {
        val text = editText.text.toString()
        if (isEmail && !isEmailValid(editText.text.toString())) {
            layout.error = getString(R.string.email_invalid)
        } else if (text.isEmpty()) {
            layout.error = getString(R.string.empty_field)
        } else {
            layout.error = ""
        }
    }

    //function used to validate the completion of all fields
    private fun validateFields(): Boolean {
        val firstNameValid = binding.firstName.text?.isNotEmpty() == true
        val lastNameValid = binding.lastName.text?.isNotEmpty() == true
        val phoneNumberValid = binding.phoneNumber.text?.isNotEmpty() == true
        val emailValid =
            binding.email.text?.isNotEmpty() == true && isEmailValid(binding.email.text.toString())
        val dateOfBirthValid = binding.dateOfBirth.text?.isNotEmpty() == true
        val salaryValid = binding.salaryExpectations.text?.isNotEmpty() == true
        val notesValid = binding.notes.text?.isNotEmpty() == true

        return firstNameValid && lastNameValid && phoneNumberValid && emailValid && dateOfBirthValid && salaryValid && notesValid
    }

    //used to select a date
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

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CANDIDATE = "candidate"

        @JvmStatic
        fun newInstance(candidate: Candidate) =
            AddCandidateFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CANDIDATE, candidate)
                }
            }
    }
}
