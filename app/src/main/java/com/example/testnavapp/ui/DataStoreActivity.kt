package com.example.testnavapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.testnavapp.databinding.ActivityDataStoreBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class DataStoreActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        val TAG: String = DataStoreActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityDataStoreBinding

    //Preference
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private var myText: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    //initView
    private fun initView() {
        Log.d(TAG, "initView")

        binding.btSaveData.setOnClickListener(this)
        binding.btShowData.setOnClickListener(this)

        binding.etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                myText = p0.toString()
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btSaveData.id -> {
                if (myText?.isEmpty() == true) {
                    Toast.makeText(this@DataStoreActivity, "No Text!!", Toast.LENGTH_SHORT).show()
                    return
                }

                //hide soft keyboard
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etInput.windowToken, 0)

                //write it down
                CoroutineScope(Dispatchers.Default).launch {
                    writeData(myText ?: "")
                }
            }

            binding.btShowData.id -> {
                CoroutineScope(Dispatchers.Default).launch {
                    readData()
                }
            }
        }
    }

    private suspend fun writeData(data: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("mydata")] = data
        }
    }

    private suspend fun readData() {
        val text: Flow<String> = dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("mydata")] ?: ""
        }

        Log.d(TAG, "readData/result - ${text.first()}")

        binding.tvDataResult.text = text.first()
    }
}