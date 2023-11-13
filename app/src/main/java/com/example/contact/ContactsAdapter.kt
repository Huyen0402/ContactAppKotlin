import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.contact.Contact
import com.example.contact.R

class ContactsAdapter(private val contacts: List<Contact>) : BaseAdapter() {

    override fun getCount(): Int {
        return contacts.size
    }

    override fun getItem(position: Int): Any {
        return contacts[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = convertView ?: inflater.inflate(R.layout.item_contact, parent, false)

        val contact = contacts[position]

        val textViewName = contactView.findViewById<TextView>(R.id.textViewName)
        val imageViewAvatar = contactView.findViewById<ImageView>(R.id.imageViewAvatar)

        textViewName.text = contact.name

        val firstLetter = contact.name.substring(0, 1).toUpperCase()

        val avatarBitmap = textToBitmap(context, firstLetter)
        Glide.with(context)
            .load(avatarBitmap)
            .apply(RequestOptions.circleCropTransform())
            .into(imageViewAvatar)

        return contactView
    }

    private fun textToBitmap(context: Context, text: String): Bitmap {
        val width = 48
        val height = 48
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.parseColor("#FFC107") // Màu sắc của avatar
        paint.style = Paint.Style.FILL
        canvas.drawCircle(width / 2f, height / 2f, width / 2f, paint)
        paint.color = Color.WHITE // Màu sắc chữ
        paint.textSize = 24f
        paint.textAlign = Paint.Align.CENTER
        val x = width / 2f
        val y = height / 2f - (paint.descent() + paint.ascent()) / 2f
        canvas.drawText(text, x, y, paint)
        return bitmap
    }
}