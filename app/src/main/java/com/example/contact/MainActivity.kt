package com.example.contact

import ContactsAdapter
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.ContextMenu
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listViewContacts: ListView
    private lateinit var contacts: List<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewContacts = findViewById(R.id.listViewContacts)

        contacts = getContacts()

        val adapter = ContactsAdapter(contacts)
        listViewContacts.adapter = adapter

        registerForContextMenu(listViewContacts)

        listViewContacts.setOnItemClickListener { _, _, position, _ ->
            val selectedContact = contacts[position]
            val dialogBuilder = AlertDialog.Builder(this)

            val contactInfoView = layoutInflater.inflate(R.layout.dialog_contact_info, null)
            val textViewId = contactInfoView.findViewById<TextView>(R.id.textViewId)
            val textViewName = contactInfoView.findViewById<TextView>(R.id.textViewName)
            val textViewPhone = contactInfoView.findViewById<TextView>(R.id.textViewPhone)
            val textViewEmail = contactInfoView.findViewById<TextView>(R.id.textViewEmail)

            val boldSpan = StyleSpan(Typeface.BOLD)

            val spannableId = SpannableString("ID: ${selectedContact.id}")
            spannableId.setSpan(boldSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            textViewId.text = spannableId

            val spannableName = SpannableString("Name: ${selectedContact.name}")
            spannableName.setSpan(boldSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            textViewName.text = spannableName

            val spannablePhone = SpannableString("Phone: ${selectedContact.phoneNumber}")
            spannablePhone.setSpan(boldSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            textViewPhone.text = spannablePhone

            val spannableEmail = SpannableString("Email: ${selectedContact.email}")
            spannableEmail.setSpan(boldSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            textViewEmail.text = spannableEmail

            val createCenteredTextView = { context: Context, text: String ->
                val textView = TextView(context)
                textView.text = text
                textView.textSize = 20f
                textView.gravity = Gravity.CENTER
                textView.setTypeface(null, Typeface.BOLD)
                textView.setPadding(0, 32, 0, 0)
                textView
            }

            dialogBuilder.setCustomTitle(createCenteredTextView(this, "Contact Information"))

            dialogBuilder.setView(contactInfoView)

            dialogBuilder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.contact_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val selectedContact = contacts[info.position]

        when (item.itemId) {
            R.id.menu_call -> {
                makePhoneCall(selectedContact.phoneNumber)
                return true
            }
            R.id.menu_send_sms -> {
                sendSMS(selectedContact.phoneNumber)
                return true
            }
            R.id.menu_send_email -> {
                sendEmail(selectedContact.email)
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    private fun getContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val contact1 = Contact(1, "John Doe", "1234567890", "john.doe@example.com")
        val contact2 = Contact(2, "Jane Smith", "9876543210", "jane.smith@example.com")
        val contact3 = Contact(3, "David Johnson", "5555555555", "david.johnson@example.com")
        val contact4 = Contact(4, "Marie Curi", "798798000", "marie@example.com")
        contactList.add(contact1)
        contactList.add(contact2)
        contactList.add(contact3)
        contactList.add(contact4)
        return contactList
    }

    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun sendSMS(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }
        startActivity(intent)
    }
}