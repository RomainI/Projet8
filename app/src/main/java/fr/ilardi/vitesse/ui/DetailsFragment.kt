package fr.ilardi.vitesse.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import fr.ilardi.vitesse.R
import fr.ilardi.vitesse.databinding.DetailsFragmentBinding
import fr.ilardi.vitesse.model.Candidate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsFragmentViewModel by viewModels()

    private var candidate: Candidate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        candidate = arguments?.getParcelable(ARG_CANDIDATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        candidate?.let { candidate ->
            binding.toolbarTitle.text = "${candidate.firstName} ${candidate.lastName}"
            binding.birthdayTextView.text = formatDate(candidate.dateOfBirth)
            CoroutineScope(Dispatchers.Main).launch {
                binding.salaryTextView.text =
                    candidate.salary + "€\n" + "£ " + String.format(
                        "%.2f",
                        viewModel.getGbpFromEur(candidate.salary.toDouble())
                    )
            }
            binding.notesTextView.text = candidate.notes
            if (candidate.isFavorite) {
                Log.d("FAV", "is favorite")
                binding.favImage.setImageResource(R.drawable.ic_star_filled)
            }
            if (candidate.pictureURI != "") {
                binding.photoDetail.setImageURI(Uri.parse(candidate.pictureURI))
            }
            binding.smsIcon.setOnClickListener {
                sendSMS(candidate.phoneNumber, "Hello ${candidate.firstName}")
            }

            binding.emailIcon.setOnClickListener {
                sendEmail(candidate.email, "Subject", "Hello ${candidate.firstName}")
            }

            binding.phoneIcon.setOnClickListener {
                makePhoneCall(candidate.phoneNumber)
            }
        }

        binding.favImage.setOnClickListener {
            candidate?.let {
                val updatedCandidate = it.copy(
                    isFavorite = !it.isFavorite
                )
                updateFavStar(updatedCandidate.isFavorite)
                viewModel.updateCandidate(updatedCandidate)
                candidate = updatedCandidate
            }
        }

        binding.deleteImage.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle(getString(R.string.delete))
            builder.setMessage(getString(R.string.delete_message))

            builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                candidate?.let { it ->
                    viewModel.deleteCandidate(it)
                }
                (activity as? MainActivity)?.replaceFragment(MainFragment())
            }

            // Ajouter un bouton "Annuler" avec une action (optionnel)
            builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss() // Close the popup
            }

            // Créer et afficher l'AlertDialog
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

        binding.editImage.setOnClickListener {
            Log.d("editImage.setOnClickListener", "editImage.setOnClickListener")
            candidate?.let { it ->
                Log.d(
                    "DetailsFragment",
                    "Navigating to AddCandidateFragment with candidate: ${it.firstName} ${it.lastName}"
                )
                val fragment = AddCandidateFragment.newInstance(it)
                (activity as MainActivity).replaceFragment(fragment)
            }

        }

        binding.arrowIcon.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(MainFragment())
        }
    }

    private fun updateFavStar(isFilled: Boolean) {
        if (isFilled) {
            binding.favImage.setImageResource(R.drawable.ic_star_filled)
        } else {
            binding.favImage.setImageResource(R.drawable.ic_star)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.SEND_SMS),
                REQUEST_SMS_PERMISSION
            )
        } else {
            val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phoneNumber")
                putExtra("sms_body", message)
            }
            startActivity(smsIntent)
        }
    }

    private fun sendEmail(emailAddress: String, subject: String, body: String) {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        startActivity(Intent.createChooser(emailIntent, "Send Email"))

    }

    private fun makePhoneCall(phoneNumber: String) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION
            )
        } else {
            val callIntent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(callIntent)

        }
    }

    private fun formatDate(date: String): String {
        // Transformation en objet Date
        val birthdayDate = stringToDate(date)

        // Récupération de la locale actuelle du téléphone
        val currentLocale = Locale.getDefault()

        // Formater la date en fonction de la locale
        val formattedBirthday = formatDateForLocale(birthdayDate, currentLocale)

        // Calcul de l'âge
        val age = calculateAge(birthdayDate)

        return "$formattedBirthday ($age " + getString(R.string.years_old) + ")\n" + getString(R.string.birthday)
    }

    private fun calculateAge(birthDate: Date?): Int {
        if (birthDate == null) return 0

        val birthCalendar = Calendar.getInstance().apply {
            time = birthDate
        }
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }

    private fun stringToDate(dateString: String): Date? {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE) // Format initial
        return sdf.parse(dateString)
    }

    private fun formatDateForLocale(date: Date?, locale: Locale): String {
        val dateFormat = if (locale.language == Locale.FRENCH.language) {
            SimpleDateFormat("dd/MM/yyyy", locale)
        } else {
            SimpleDateFormat("MM/dd/yyyy", locale)
        }
        return date?.let { dateFormat.format(it) } ?: ""
    }

    companion object {
        private const val ARG_CANDIDATE = "candidate"
        private const val REQUEST_CALL_PERMISSION = 1
        private const val REQUEST_SMS_PERMISSION = 2

        @JvmStatic
        fun newInstance(candidate: Candidate) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CANDIDATE, candidate)
                }
            }
    }
}