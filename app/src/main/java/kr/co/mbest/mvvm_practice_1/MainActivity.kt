package kr.co.mbest.mvvm_practice_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.mbest.mvvm_practice_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Set contactItemClick & contactItemLongClick lambda
        val adapter = MainRecyclerAdapter({ contact ->
            val intent = Intent(this, ProfileAddActivity::class.java)
            intent.putExtra(ProfileAddActivity.EXTRA_CONTACT_NAME, contact.name)
            intent.putExtra(ProfileAddActivity.EXTRA_CONTACT_NUMBER, contact.number)
            intent.putExtra(ProfileAddActivity.EXTRA_CONTACT_ID, contact.id)
            startActivity(intent)
        }, { contact ->
            deleteDialog(contact)
        })

        val lm = LinearLayoutManager(this)
        binding.recyclerMain.adapter = adapter
        binding.recyclerMain.layoutManager = lm
        binding.recyclerMain.setHasFixedSize(true)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.getAll().observe(this, Observer<List<Contact>> { contacts ->
            // Update UI
            adapter.setContacts(contacts!!)
        })


        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, ProfileAddActivity::class.java)
            startActivity(intent)
        }

    }

    private fun deleteDialog(contact: Contact) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete selected contact?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                mainViewModel.delete(contact)
            }
        builder.show()
    }

}