package fr.ilardi.vitesse.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.ilardi.vitesse.R
import fr.ilardi.vitesse.model.Candidate

class CandidateAdapter(private val onItemClick: (Candidate) -> Unit) : RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder>() {

    private var candidates: List<Candidate> = emptyList()

    inner class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val nameTextView: TextView = itemView.findViewById(R.id.text_name)
        private val notesTextView: TextView = itemView.findViewById(R.id.text_description)
        private val photoImageView: ImageView = itemView.findViewById(R.id.avatar)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(candidate: Candidate) {
            nameTextView.text = "${candidate.firstName} ${candidate.lastName}"
            notesTextView.text = candidate.notes
            if (candidate.pictureURI != "") {
                photoImageView.setImageURI(Uri.parse(candidate.pictureURI))
            }
        }

        override fun onClick(v: View?) {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(candidates[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return CandidateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        holder.bind(candidates[position])
    }

    override fun getItemCount() = candidates.size

    fun setCandidates(candidates: List<Candidate>) {
        this.candidates = candidates
        notifyDataSetChanged()
    }
}