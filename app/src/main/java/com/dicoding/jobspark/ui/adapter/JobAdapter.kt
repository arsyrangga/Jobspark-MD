package com.dicoding.jobspark.ui.adapter

import android.content.Intent
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
import com.dicoding.jobspark.ui.activity.JobDescriptionActivity

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

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, JobDescriptionActivity::class.java)
            intent.putExtra("job_id", job.id)
            intent.putExtra("job_name", job.job_name)
            intent.putExtra("company_name", job.company_name)
            intent.putExtra("location", job.location)
            intent.putExtra("salary", job.salary)
            intent.putExtra("job_description", job.job_description)
            intent.putExtra("job_type", job.job_type)
            intent.putExtra("position", job.position)
            intent.putExtra("qualification", job.qualification)
            intent.putExtra("min_experience", job.min_experience)
            Glide.with(holder.itemView.context)
                .load(job.image)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.jobIcon)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = jobs.size
}
