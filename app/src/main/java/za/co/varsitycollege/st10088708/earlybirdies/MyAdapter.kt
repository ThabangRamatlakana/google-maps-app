package za.co.varsitycollege.st10088708.earlybirdies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyAdapter(private val birdsList: ArrayList<BirdData>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return birdsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = birdsList[position]

        Picasso.get()
            .load(currentItem.birdImage)
            .into(holder.birdImageView)

        holder.birdName.text = currentItem.birdName
        holder.birdLocation.text = currentItem.birdLocation
        holder.birdDescription.text = currentItem.birdDescription

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val birdImageView: ImageView = itemView.findViewById(R.id.birdImage)
        val birdName: TextView = itemView.findViewById(R.id.tvBirdName)
        val birdLocation: TextView = itemView.findViewById(R.id.tvBirdLocation)
        val birdDescription: TextView = itemView.findViewById(R.id.tvBirdDescription)
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}
