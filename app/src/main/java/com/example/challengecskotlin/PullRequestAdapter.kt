package com.example.challengecskotlin

import android.content.Context
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.pr_row.view.*


class PullRequestAdapter (var pullRequests: List<PullRequestObject>, context: Context): RecyclerView.Adapter<PullRequestAdapter.ViewHolder> () {

    private var mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pr_row, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = pullRequests.size  //quantidade de items

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pr = pullRequests[position]
        holder.name.text = pr.title
        holder.description.text = pr.body
        holder.user.text = pr.user!!.login

        Glide.with(mContext)
            .asBitmap()
            .load(pr.user.avatar_url)
            .into(holder.photo)

        holder.itemView.setOnClickListener {
            d("onClick", "clicado: $pr")
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.pr_name
        val description: TextView = itemView.pr_description
        val photo: CircleImageView = itemView.pr_photo
        val user: TextView = itemView.pr_user
    }
}
