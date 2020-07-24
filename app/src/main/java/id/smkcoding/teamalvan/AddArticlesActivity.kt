package id.smkcoding.teamalvan

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.smkcoding.teamalvan.model.ArticlesModel
import kotlinx.android.synthetic.main.activity_add_articles.*
import retrofit2.http.Url
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.Arrays.toString


class AddArticlesActivity : AppCompatActivity() {
    private var Title: EditText? = null
    private var Caption: EditText? = null
    private var Category: Spinner? = null
    private var Cover: EditText? = null
    lateinit var ref: DatabaseReference
    private var auth: FirebaseAuth? = null
    private lateinit var button: ImageButton

    //Post Image
    private var reference: StorageReference? = null
    private var progressBar: ProgressBar? = null
    private var imgPath: Uri? = null
    private var PICK_IMAGE_REQUEST = 71


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_articles)
        title = "Write Article"

        Title = findViewById<EditText>(R.id.txt_title)
        Caption = findViewById<EditText>(R.id.txt_caption)
        Category = findViewById<Spinner>(R.id.category)
//        Cover = findViewById<EditText>(R.id.cover)
        reference = FirebaseStorage.getInstance().getReference("Cover");
        ref = FirebaseDatabase.getInstance().getReference()
        auth = FirebaseAuth.getInstance()

        progressBar = findViewById(R.id.progressBar);

        select_image.setOnClickListener {
            getImage()
        }

        simpan.setOnClickListener {
            prosesSave()
        }


    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imgPath = data?.data
            imageContainer.setImageURI(imgPath)
        }
    }

    private fun prosesSave() {
        val getTitle: String = Title?.getText().toString()
        val getCaption: String = Caption?.getText().toString()
        val getCategory: String = Category?.getSelectedItem().toString()
//        val getCover: String = Cover?.getText().toString()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val current = SimpleDateFormat("yyyy-MM-dd")
        val getTime = current.format(Date())
        val getPrimaryKey = ref.push().key.toString()
        val getNameDokter: String = auth?.getCurrentUser()?.displayName.toString()
        val getPhotoDokter: String = auth?.getCurrentUser()?.photoUrl.toString()

//        if (getTitle.isEmpty() && getCaption.isEmpty() && getCategory.isEmpty() && getCover.isEmpty()) {
//            Toast.makeText(this@AddArticlesActivity,"Data Can't Be Empty", Toast.LENGTH_SHORT).show()
//        } else {
//            val articles = ArticlesModel(getUserID, getNameDokter, getPhotoDokter, getTime, getTitle, getCaption, getCategory, getCover, getPrimaryKey)
//            ref.child(getUserID).child("tb_articles").push().setValue(articles).addOnCompleteListener {
//                Toast.makeText(this, "Posting Successful", Toast.LENGTH_SHORT).show()
//            }
//            val intent = Intent (this, MyArticlesActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        when {
            getTitle.isEmpty() -> txt_title.error = "Can't be empty"
            getCaption.isEmpty() -> txt_caption.error = "Can't be empty"
            else -> {
                if (imgPath == null) {
                    Toast.makeText(this@AddArticlesActivity, "Can't be empty", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    finish()
                    Toast.makeText(this@AddArticlesActivity, "Uploading...", Toast.LENGTH_LONG)
                        .show()
                    reference?.child(getUserID)?.putFile(imgPath!!)?.addOnSuccessListener {
                        reference?.child(getUserID)?.downloadUrl?.addOnSuccessListener {
                            val articles = ArticlesModel(
                                getUserID,
                                getNameDokter,
                                getPhotoDokter,
                                getTime,
                                getTitle,
                                getCaption,
                                getCategory,
                                it.toString(),
                                getPrimaryKey
                            )
                            ref.child(getUserID).child("tb_articles").push().setValue(articles)
                                .addOnCompleteListener {}
                        }
                    }
                        ?.addOnFailureListener {
                            Toast.makeText(this@AddArticlesActivity, it.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                }
            }
        }


//    private fun uploadImage() {
//        //Mendapatkan data dari ImageView sebagai Bytes
//        ImageContainer?.isDrawingCacheEnabled = true
//        ImageContainer?.buildDrawingCache()
//        var bitmap = (ImageContainer!!.drawable as BitmapDrawable).bitmap
//        var stream = ByteArrayOutputStream()
//
//        //Mengkompress bitmap menjadi JPG dengan kualitas gambar 100%
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        var bytes: ByteArray = stream.toByteArray()
//
//        //Lokasi lengkap dimana gambar akan disimpan
//
//        var namaFile = UUID.randomUUID().toString() + ".jpg"
//        var pathImage = reference?.child("gambar/ $namaFile")
//        var pathInsert = pathImage?.downloadUrl.toString()
//        Cover?.setText(pathInsert);
//
////        pathImage?.downloadUrl?.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
////            val downloadUrl = uri.toString()
////            Cover?.setText(downloadUrl);
////
////        })
//
//        var uploadTask = reference!!.child(pathImage.toString()).putBytes(bytes)
//        uploadTask.addOnSuccessListener {
//            progressBar?.setVisibility(View.GONE)
//            Toast.makeText(this@AddArticlesActivity, "Uploading Successful", Toast.LENGTH_SHORT).show()
//            prosesSave()
//        }
//            .addOnFailureListener {
//                progressBar?.setVisibility(View.GONE)
//                Toast.makeText(this@AddArticlesActivity, "Uploading Failed", Toast.LENGTH_SHORT).show()
//            }
//            .addOnProgressListener { taskSnapshot ->
//                progressBar?.setVisibility(View.VISIBLE)
//                var progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
//                progressBar?.setProgress(progress.toInt())
//            }
//    }
    }
}