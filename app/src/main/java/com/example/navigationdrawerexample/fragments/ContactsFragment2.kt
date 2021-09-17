package com.example.navigationdrawerexample.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navigationdrawerexample.Contacts
import com.example.navigationdrawerexample.R
import com.example.navigationdrawerexample.RecyclerViewAdapter
import java.util.*
import kotlin.collections.ArrayList


class ContactsFragment2 : Fragment() {



    private lateinit var v: View
    private var mRecyclerView: RecyclerView? = null
    private val mDataArray: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_contacts2, container, false)

        mRecyclerView = view.findViewById(R.id.recyclerView)
        getContactList(view)

        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_CONTACTS
                )
            } !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_CONTACTS,
                )
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                    1
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_CONTACTS), 1
                )
            }
        } else {
           // Call method which fetches contact from Adapter and send to model class
            getContactList(view)
        }

        return view
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((
                                activity?.let {
                                    ContextCompat.checkSelfPermission(
                                        it,
                                        Manifest.permission.WRITE_CONTACTS
                                    )
                                }
                                        ===
                                        PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()
                        // Call method which fetches contact from Adapter and send to model class
                         getContactList(v)
                    }
                } else {
                    Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


    private fun getContactList(vie: View) {

        val contact = activity?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC "
        )


        val recyclerView = vie.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val contactList: MutableList<Contacts> = ArrayList()
        if (contact != null) {

            while (contact.moveToNext()) {
                val contactId =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))

                val name =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                val obj = Contacts(name, phone, contactId)
                contactList.add(obj)

            }
            contact.close()
        }

        val contactListFiltered: MutableList<Contacts> = ArrayList()

        for ((a, item) in contactList.withIndex()) {

            val b = a + 1
            if (contactList.size != b) {
                val ab = item.number.replace("[()\\s-]".toRegex(), "")
                val bc = contactList[b].number.replace("[\\s\\-]".toRegex(), "")
                if (ab != bc) {
                    mDataArray.add(item.name)
                    contactListFiltered.add(item)

                }
            } else {
                mDataArray.add(item.name)
                contactListFiltered.add(item)

            }
        }

        Log.d("Check ContactList",contactListFiltered.size.toString())

        var recentCallLogsListStickeyHeaderAdapter =
            context?.let { RecyclerViewAdapter(contactListFiltered, it) }

        recyclerView.setAdapter(recentCallLogsListStickeyHeaderAdapter)
//        recyclerView.setItemViewCacheSize(20)
//        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_LOW

//        val mDecor = StickyRecyclerHeadersDecoration(recentCallLogsListStickeyHeaderAdapter)
//        if (0 == vie.recyclerView.getItemDecorationCount()) {
//            vie.recyclerView.addItemDecoration(mDecor)
//        }

//        Objects.requireNonNull(recyclerView.layoutManager)?.scrollToPosition(0)
//    }
    }
}

