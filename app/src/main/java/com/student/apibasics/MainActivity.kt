package com.student.apibasics

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Read()
        //Post()
    }

    fun Read()
    {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute{
            try {
                val url = URL("https://opsc20240710154110.azurewebsites.net/GetAllLoans")
                val json = url.readText()
                val userList = Gson().fromJson(json, Array<Loan>::class.java).toList()

                Handler(Looper.getMainLooper()).post {
                    Log.d("AddNewUser","Plain Json Vars :" + json.toString())
                    Log.d("AddNewUser", "Converted Json :" + userList.toString())
                    var Text = findViewById<TextView>(R.id.txtOutput)
                    Text.setText(userList.toString())
                }
            }
            catch (e:Exception)
            {
                Log.d("AddNewUser","Error: "+ e.toString());
                var Text = findViewById<TextView>(R.id.txtOutput)
                Text.setText("Error: "+ e.toString())
            }
        }
    }
fun Post()
{
    val executor = Executors.newSingleThreadExecutor()
    executor.execute{
        val user =LoanPost("15","M10070895","Added by the ST10070895")

        val (_, _, result)= "https://opsc20240710154110.azurewebsites.net/AddLoan".httpPost()
            .jsonBody(Gson().toJson(user).toString())
            .responseString()
        val Json = "[" + result.component1() + "]"
        val userList = Gson().fromJson(Json, Array<Loan>::class.java).toList()

        Handler(Looper.getMainLooper()).post {
            var Text = findViewById<TextView>(R.id.txtOutput)
            Text.setText(userList.toString())
        }
    }
}
}