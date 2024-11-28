package com.dicoding.jobspark.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.Job

class JobAdapter(private val jobs: List<Job>) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.job_title)
        val company: TextView = itemView.findViewById(R.id.company_location)
        val salary: TextView = itemView.findViewById(R.id.salary)
        val jobType: Button = itemView.findViewById(R.id.job_type_button)
        val jobIcon: ImageView = itemView.findViewById(R.id.job_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job_card, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]
        holder.title.text = job.job_name
        holder.company.text = "${job.company_name} • ${job.location}"
        holder.salary.text = job.salary
        holder.jobType.text = job.job_type

        Glide.with(holder.itemView.context)
            .load(job.image)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.jobIcon)
    }

    override fun getItemCount() = jobs.size
}

