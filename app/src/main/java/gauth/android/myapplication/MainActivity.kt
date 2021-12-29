package gauth.android.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import gauth.android.gauthlibrary.api.GauthApi
import gauth.android.gauthlibrary.data.SignIn
import gauth.android.gauthlibrary.data.SignUp
import gauth.android.gauthlibrary.data.Token
import gauth.android.gauthlibrary.listener.SignInListener
import gauth.android.gauthlibrary.listener.SignUpListener
import gauth.android.gauthlibrary.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val gauthApi = GauthApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val login = findViewById<Button>(R.id.login)
        val signUp = findViewById<Button>(R.id.sign_up)

        val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)

        login.setOnClickListener {
            gauthApi.signIn(this, object : SignInListener {
                override fun onFail() {
                    Log.d("fail", "sign in failed")
                }

                override fun onSuccess(token: Token) {
                    Toast.makeText(this@MainActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val editor = sharedPreferences.edit()
                    editor.putString("accessToken", token.accessToken)
                    editor.putString("refreshToken", token.refreshToken)
                    editor.apply()

                    val intent = Intent(this@MainActivity, MainPageActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            })
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://neon-dev.kro.kr:5083")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        signUp.setOnClickListener {
            gauthApi.signUp(this, object : SignUpListener {
                override fun onFail() {
                    Log.d("fail", "sign up failed")
                    Toast.makeText(this@MainActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess() {
                    Toast.makeText(this@MainActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            })
        }
    }
}
