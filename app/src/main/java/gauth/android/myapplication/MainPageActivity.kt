package gauth.android.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainPageActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val button = findViewById<Button>(R.id.logout)
        val token = findViewById<TextView>(R.id.token)

        val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", "")
        val refreshToken = sharedPreferences.getString("refreshToken", "")

        if(accessToken == null || refreshToken == null || accessToken.isEmpty() || refreshToken.isEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        token.text = "accessToken : $accessToken\nrefreshToken : $refreshToken"

        button.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.remove("accessToken")
            editor.remove("refreshToken")
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}