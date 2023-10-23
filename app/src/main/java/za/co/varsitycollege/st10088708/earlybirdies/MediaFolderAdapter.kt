package za.co.varsitycollege.st10088708.earlybirdies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MediaFolderAdapter(private val mediaFolderList: ArrayList<String>) : RecyclerView.Adapter<MediaFolderAdapter.MediaFolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaFolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_media_folder, parent, false)
        return MediaFolderViewHolder(view)
    }




     override fun onBindViewHolder(holder: MediaFolderViewHolder, position: Int) {
         val imageUrl = mediaFolderList[position]
         val imageView = holder.itemView.findViewById<ImageView>(R.id.recycleView)
         // Load the image from the `imageUrl` and set it to the `imageView` using a library like Picasso or Glide
         // Example with Picasso:
         Picasso.get().load(imageUrl).into(imageView)
     }

    override fun getItemCount(): Int {
        return mediaFolderList.size
    }

    class MediaFolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare and initialize your views inside the view holder
        // e.g., val imageView: ImageView = itemView.findViewById(R.id.imageView)

    }
}
