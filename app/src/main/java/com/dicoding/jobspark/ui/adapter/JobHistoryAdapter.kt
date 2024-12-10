package com.dicoding.jobspark.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.JobHistory

class JobHistoryAdapter(private val onItemClick: (JobHistory) -> Unit) :
    ListAdapter<JobHistory, JobHistoryAdapter.JobHistoryViewHolder>(JobHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobHistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_history, parent, false)
        return JobHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobHistoryViewHolder, position: Int) {
        val jobHistory = getItem(position)
        holder.bind(jobHistory)
    }

    inner class JobHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val jobTitle: TextView = itemView.findViewById(R.id.job_title)
        private val jobStatus: TextView = itemView.findViewById(R.id.job_status)
        private val companyName: TextView = itemView.findViewById(R.id.company_name)
        private val appliedAt: TextView = itemView.findViewById(R.id.applied_at)

        fun bind(jobHistory: JobHistory) {
            Log.d("JobHistoryAdapter", "Job Title: ${jobHistory.job_name}")
            jobTitle.text = jobHistory.job_name
            jobStatus.text = jobHistory.status
            companyName.text = jobHistory.company_name
            appliedAt.text = jobHistory.applied_at

            itemView.setOnClickListener {
                onItemClick(jobHistory)
            }
        }
    }

    class JobHistoryDiffCallback : DiffUtil.ItemCallback<JobHistory>() {
        override fun areItemsTheSame(oldItem: JobHistory, newItem: JobHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JobHistory, newItem: JobHistory): Boolean {
            return oldItem == newItem
        }
    }
}
