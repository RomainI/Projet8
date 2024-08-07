package fr.ilardi.vitesse.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.ilardi.vitesse.databinding.DetailsFragmentBinding
import fr.ilardi.vitesse.model.Candidate

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
            binding.birthdayTextView.text = candidate.dateOfBirth
            binding.salaryTextView.text = candidate.salary
            binding.notesTextView.text = candidate.notes

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

        // Vous pouvez également ajouter des click listeners ici si nécessaire
        binding.favImage.setOnClickListener {
            // Action on favorite icon click
        }

        binding.deleteImage.setOnClickListener {
            // Action on delete icon click
        }

        binding.editImage.setOnClickListener {
            // Action on edit icon click
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