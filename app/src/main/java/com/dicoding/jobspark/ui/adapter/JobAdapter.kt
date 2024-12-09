package com.dicoding.jobspark.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.Job
import com.dicoding.jobspark.ui.activity.UploadActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JobAdapter(
    private var jobs: MutableList<Job>,
    private val isSimplified: Boolean,
    private val isEditable: Boolean = false
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.job_title)
        val company: TextView = itemView.findViewById(R.id.company_location)
        val jobType: TextView? = itemView.findViewById(R.id.job_type)
        val jobIcon: ImageView = itemView.findViewById(R.id.job_icon)
        val applyButton: Button? = itemView.findViewById(R.id.apply_button)
        val saveIcon: ImageView? = itemView.findViewById(R.id.save_icon)
        val deleteIcon: ImageView? = itemView.findViewById(R.id.delete_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val layoutRes = when {
            isSimplified -> R.layout.item_job_card_simplified
            isEditable -> R.layout.item_saved_card
            else -> R.layout.item_job_card
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]
        holder.title.text = job.job_name
        holder.company.text = "${job.company_name} • ${job.location}"

        Glide.with(holder.itemView.context)
            .load(job.image)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.jobIcon)

        val sharedPreferences =
            holder.itemView.context.getSharedPreferences("SAVED_JOBS", Context.MODE_PRIVATE)
        val gson = Gson()
        val existingJobsJson = sharedPreferences.getString("SAVED_JOBS_LIST", "[]")
        val jobListType = object : TypeToken<MutableList<Job>>() {}.type
        val savedJobs: MutableList<Job> = gson.fromJson(existingJobsJson, jobListType)

        if (savedJobs.any { it.id == job.id }) {
            holder.saveIcon?.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            holder.saveIcon?.setImageResource(R.drawable.ic_bookmark)
        }

        holder.saveIcon?.setOnClickListener {
            val editor = sharedPreferences.edit()

            if (savedJobs.any { it.id == job.id }) {
                savedJobs.removeAll { it.id == job.id }
                holder.saveIcon?.setImageResource(R.drawable.ic_bookmark)
                Toast.makeText(
                    holder.itemView.context,
                    "Job removed from saved!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                savedJobs.add(job)
                holder.saveIcon?.setImageResource(R.drawable.ic_bookmark_filled)
                Toast.makeText(
                    holder.itemView.context,
                    "Job saved successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val updatedJobsJson = gson.toJson(savedJobs)
            editor.putString("SAVED_JOBS_LIST", updatedJobsJson)
            editor.apply()
        }

        if (isSimplified) {
        } else {
            holder.jobType?.text = job.job_type
            holder.applyButton?.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, UploadActivity::class.java).apply {
                    putExtra("job_id", job.id)
                }
                context.startActivity(intent)
            }
        }

        if (isEditable) {
            holder.deleteIcon?.visibility = View.VISIBLE
            holder.deleteIcon?.setOnClickListener {
                removeJob(holder.itemView.context, position)
            }
        } else {
            holder.deleteIcon?.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int {
        return jobs.size
    }

    private fun removeJob(context: Context, position: Int) {
        val job = jobs[position]

        jobs.removeAt(position)
        notifyItemRemoved(position)

        val sharedPreferences = context.getSharedPreferences("SAVED_JOBS", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val updatedJobsJson = gson.toJson(jobs)
        editor.putString("SAVED_JOBS_LIST", updatedJobsJson)
        editor.apply()

        Toast.makeText(context, "Job removed successfully!", Toast.LENGTH_SHORT).show()
    }

    fun updateData(newJobs: List<Job>) {
        jobs.clear()
        jobs.addAll(newJobs)
        notifyDataSetChanged()
    }
}
