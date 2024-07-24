package com.student.apibasics

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtMemberID)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun ShowAll(view: View?) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val url = URL("https://opsc20240710154110.azurewebsites.net/GetAllLoans")
            val json = url.readText()
            val userList = Gson().fromJson(json, Array<Loan>::class.java).toList()

            Handler(Looper.getMainLooper()).post {
                val text = findViewById<TextView>(R.id.txtOutput)
                text.text = userList.toString()
            }
        }
    }

    fun Search(view: View?) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val inputMemberID = findViewById<EditText>(R.id.txtSearch)
            val memberID = inputMemberID.text.toString()
            val searchUrl = "https://opsc20240710154110.azurewebsites.net/GetLoan/$memberID"
            val (_, _, result) = searchUrl.httpGet().responseString()

            val loanJson = result.get()
            val loan = Gson().fromJson(loanJson, Loan::class.java)
            Handler(Looper.getMainLooper()).post {
                val textView = findViewById<TextView>(R.id.txtOutput)
                textView.text = loan.toString()
            }
        }
    }

    fun Add(view: View?) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val inputMemberID = findViewById<EditText>(R.id.txtMemberID)
            val inputAmount = findViewById<EditText>(R.id.txtAmount)
            val inputMessage = findViewById<EditText>(R.id.txtMessage)
            val details = LoanPost(
                inputAmount.text.toString(),
                inputMemberID.text.toString(),
                inputMessage.text.toString()
            )

            val (_, _, result) = "https://opsc20240710154110.azurewebsites.net/AddLoan".httpPost()
                .jsonBody(Gson().toJson(details).toString())
                .responseString()
            val json = "[" + result.component1() + "]"
            val userList = Gson().fromJson(json, Array<Loan>::class.java).toList()

            Handler(Looper.getMainLooper()).post {
                val text = findViewById<TextView>(R.id.txtOutput)
                text.text = userList.toString()
            }
        }
    }

    fun Delete(view: View?) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val inputMemberID = findViewById<EditText>(R.id.txtMemberID)
            val memberID = inputMemberID.text.toString()
            val deleteUrl =
                "https://opsc20240710154110.azurewebsites.net/DeleteLoan/$memberID"
            val (_, _, result) = deleteUrl.httpDelete().responseString()
            val successResponse = result.get()
            val jsonResponse = Gson().fromJson(successResponse, DeleteResponse::class.java)

            Handler(Looper.getMainLooper()).post {
                val textView = findViewById<TextView>(R.id.txtOutput)
                textView.text =
                    "Successfully Deleted Member $memberID. Rows affected: ${jsonResponse.rowsAffected}"
            }
        }
    }
}
