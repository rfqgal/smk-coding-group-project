package id.smkcoding.teamalvan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.smkcoding.teamalvan.model.DoctorModel
import kotlinx.android.synthetic.main.activity_form_dokter.*
import java.io.ByteArrayOutputStream
import java.util.*

class FormDokterActivity : AppCompatActivity(), View.OnClickListener {

    private val PERMISSION_CODE: Int = 100
    private val IDENTITY_PICK_CODE: Int = 101
    private val STR_PICK_CODE: Int = 102

    lateinit var databaseReference: DatabaseReference
    lateinit var storageReference: StorageReference
    lateinit var auth: FirebaseAuth

    private var photoIdentity: EditText = findViewById(R.id.edt_form_photo_identity)
    private var photoStr: EditText = findViewById(R.id.edt_form_photo_str)

    var uploadIdentitySuccess: Boolean = false
    var uploadStrSuccess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_dokter)
        title = "Form Pengajuan Akun Dokter"

        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()

        setSpinner(R.array.gender, sp_form_gender_dokter)
        setSpinner(R.array.jenis_dokter, sp_form_jenis_dokter)

        btn_foto_identitas.setOnClickListener(this)
        btn_foto_bukti_dokter.setOnClickListener(this)
        btn_ajukan.setOnClickListener(this)
    }

    private fun setSpinner(array: Int, spinner: Spinner) {
        ArrayAdapter.createFromResource(
            this,
            array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_foto_identitas -> getPhoto(IDENTITY_PICK_CODE)
            R.id.btn_foto_bukti_dokter -> getPhoto(STR_PICK_CODE)
            R.id.btn_ajukan -> uploadImagesStatus()
        }
    }

    private fun pengajuanForm() {
        val getName: String = edt_form_nama_dokter?.text.toString()
        val getGender: String = sp_form_gender_dokter.selectedItem.toString()
        val getDoctorCat: String = sp_form_jenis_dokter.selectedItem.toString()
        val getDoctorID: String = edt_id_dokter?.text.toString()
        val getPhotoIdentity: String = photoIdentity.text.toString()
        val getPhotoStr: String = photoStr.text.toString()
        val getUserID: String = auth.currentUser?.uid.toString()

        if (validateForm(getName, getDoctorID, getPhotoIdentity, getPhotoStr)) {
            val doctor = DoctorModel(
                getUserID,
                getName,
                getGender,
                getDoctorCat,
                getDoctorID,
                getPhotoIdentity,
                getPhotoStr
            )
            databaseReference.child("tb_calon_dokter")
                .push()
                .setValue(doctor)
                .addOnCompleteListener {
                    showToast(this, "Formulir berhasil diajukan")
                }
            finish()
        }
    }

    private fun getPhoto(requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                pickImageFromGallery(requestCode)
            }
        } else {
            pickImageFromGallery(requestCode)
        }
    }

    private fun pickImageFromGallery(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showToast(this, "Ijin telah diberikan")
                } else {
                    showToast(this, "Ijin telah ditolak")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IDENTITY_PICK_CODE -> {
                    img_identity.visibility = View.VISIBLE
                    val uri: Uri? = data!!.data
                    img_identity.setImageURI(uri)
                }
                STR_PICK_CODE -> {
                    img_str.visibility = View.VISIBLE
                    val uri: Uri? = data!!.data
                    img_str.setImageURI(uri)
                }
            }
        } else {
            showToast(this, "Proses telah dibatalkan")
        }
    }

    private fun uploadIdentity() {
        //Mendapatkan data dari ImageView sebagai Bytes
        val identity = (img_identity!!.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()

        //Mengkompress bitmap menjadi JPG dengan kualitas gambar 100%
        identity.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bytes: ByteArray = stream.toByteArray()

        //Lokasi lengkap dimana gambar akan disimpan

        val namaFile = UUID.randomUUID().toString() + ".jpg"
        val pathImage = storageReference.child("gambar/ $namaFile")
        val pathInsert = pathImage.downloadUrl.toString()
        photoIdentity.setText(pathInsert);

        val uploadTask = storageReference.child(pathImage.toString()).putBytes(bytes)
        uploadTask
            .addOnSuccessListener {
                Toast.makeText(this, "Uploading Successful", Toast.LENGTH_SHORT).show()
                uploadIdentitySuccess = true
            }
            .addOnFailureListener {
                Toast.makeText(this, "Uploading Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadStr() {
        //Mendapatkan data dari ImageView sebagai Bytes
        val str = (img_str!!.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()

        //Mengkompress bitmap menjadi JPG dengan kualitas gambar 100%
        str.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bytes: ByteArray = stream.toByteArray()

        //Lokasi lengkap dimana gambar akan disimpan

        val namaFile = UUID.randomUUID().toString() + ".jpg"
        val pathImage = storageReference.child("gambar/ $namaFile")
        val pathInsert = pathImage.downloadUrl.toString()
        photoStr.setText(pathInsert);

        val uploadTask = storageReference.child(pathImage.toString()).putBytes(bytes)
        uploadTask
            .addOnSuccessListener {
                Toast.makeText(this, "Uploading Successful", Toast.LENGTH_SHORT).show()
                uploadStrSuccess = true
            }
            .addOnFailureListener {
                Toast.makeText(this, "Uploading Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImagesStatus() {
        uploadIdentity().also { uploadStr() }
        if (uploadIdentitySuccess && uploadStrSuccess) pengajuanForm()
    }

    private fun validateForm(name: String, id: String, photoIdentity: String, photoStr: String): Boolean {
        if (TextUtils.isEmpty(name)) {
            showToast(applicationContext, "Enter category!")
            return false
        }
        if (TextUtils.isEmpty(id)) {
            showToast(applicationContext, "Enter field!")
            return false
        }
        if (TextUtils.isEmpty(photoIdentity)) {
            showToast(applicationContext, "Enter field!")
            return false
        }
        if (TextUtils.isEmpty(photoStr)) {
            showToast(applicationContext, "Enter field!")
            return false
        }
        return true
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
