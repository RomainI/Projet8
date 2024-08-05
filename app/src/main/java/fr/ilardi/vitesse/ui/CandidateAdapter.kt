package fr.ilardi.vitesse.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.ilardi.vitesse.R
import fr.ilardi.vitesse.model.Candidate

class CandidateAdapter : RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder>() {

    private var candidates: List<Candidate> = emptyList()

    class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.text_name)
        val notesTextView: TextView = itemView.findViewById(R.id.text_description)
        val photoImageView: ImageView = itemView.findViewById(R.id.avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return CandidateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val currentCandidate = candidates[position]
        holder.nameTextView.text = "${currentCandidate.firstName} ${currentCandidate.lastName}"
        holder.notesTextView.text = currentCandidate.notes
        //TODO holder.photoImageView.drawable = currentCandidate.phoneNumber.toString()
    }

    override fun getItemCount() = candidates.size

    fun setCandidates(candidates: List<Candidate>) {
        this.candidates = candidates
        notifyDataSetChanged()
    }
}