package com.example.tinder_test.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tinder_test.MainActivity
import com.example.tinder_test.R
import com.example.tinder_test.utils.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_join.*
import java.io.ByteArrayOutputStream

class JoinActivity : AppCompatActivity() {

    private val TAG = "JoinActivity"
    private lateinit var auth: FirebaseAuth

    private var uid = ""
    private var gender = ""
    private var city = ""
    private var age = ""
    private var nickname = ""

    lateinit var profileImage : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_join)


        profileImage = findViewById(R.id.imageArea)

        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                profileImage.setImageURI(uri)
            }
        )

        profileImage.setOnClickListener {
            getAction.launch("image/*")
        }

        btnJoin.setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pwd = findViewById<TextInputEditText>(R.id.pwdArea)

            gender = findViewById<TextInputEditText>(R.id.genderArea).text.toString()
            city = findViewById<TextInputEditText>(R.id.cityArea).text.toString()
            age = findViewById<TextInputEditText>(R.id.ageArea).text.toString()
            nickname = findViewById<TextInputEditText>(R.id.nickNameArea).text.toString()

            auth.createUserWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        uid = user?.uid.toString()

                        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                return@OnCompleteListener
                            }

                            // Get new FCM registration token
                            val token = task.result

                            val userModel = UserDataModel(
                                uid, nickname, age, gender, city, token
                            )

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)

                            FirebaseRef.userInfoRef.child(uid).setValue(userModel)
                            uploadImage(uid)
                        })

                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    }
                }
        }
    }
   private fun uploadImage(uid : String){

       val storage = Firebase.storage
       val storageRef = storage.reference.child(uid + ".png")
       // Get the data from an ImageView as bytes
       profileImage.isDrawingCacheEnabled = true
       profileImage.buildDrawingCache()
       val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
       val baos = ByteArrayOutputStream()
       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
       val data = baos.toByteArray()

       var uploadTask = storageRef.putBytes(data)
       uploadTask.addOnFailureListener {
           // Handle unsuccessful uploads
       }.addOnSuccessListener { taskSnapshot ->
           // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
           // ...
       }
   }
}
