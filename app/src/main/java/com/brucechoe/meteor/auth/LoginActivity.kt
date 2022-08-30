package com.brucechoe.meteor.auth
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.brucechoe.meteor.MainActivity
import com.brucechoe.meteor.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    private lateinit var auth: FirebaseAuth

    private var emailOk: Boolean = false
    private var pwdOk: Boolean = false

    /*비밀번호 정규식*/
    /*허용되는 문자는 숫자, 영어 대소문자, 특수문자(!, @, #, $, %, ^, +, -, =)이다.*/
    private val pwRegex = """^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^+\-=])(?=\S+$).*$"""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<TextInputEditText>(R.id.emailArea)
        val pwd = findViewById<TextInputEditText>(R.id.pwdArea)



        auth = Firebase.auth

        BtnLogin.setOnClickListener {

            auth.signInWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(input: Editable?) {
                if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    emailOk = true
                    Log.e(TAG, emailOk.toString())
                    return
                }
            }
        })

        pwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(input: Editable?) {
                if (Pattern.matches(pwRegex, input) && emailOk) {
                    Log.e(TAG, "둘다 OK")
                    BtnLogin.setBackgroundResource(R.drawable.button_active)
                    return
                } else {
                    BtnLogin.setBackgroundResource(R.drawable.buttons)
                }
            }
        })


    }

}
