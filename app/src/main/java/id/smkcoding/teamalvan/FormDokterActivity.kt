package id.smkcoding.teamalvan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.smkcoding.teamalvan.model.DoctorModel
import kotlinx.android.synthetic.main.activity_form_dokter.*
import java.util.jar.Manifest

class FormDokterActivity : AppCompatActivity(), View.OnClickListener {

    private val PERMISSION_CODE: Int = 100
    private val IDENTITY_PICK_CODE: Int = 101
    private val STR_PICK_CODE: Int = 102

    lateinit var ref: DatabaseReference
    lateinit var auth: FirebaseAuth
    private lateinit var photoIdentity: Uri
    private lateinit var photoStr: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_dokter)
        title = "Form Pengajuan Akun Dokter"

        ref = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        setSpinner(R.array.gender, sp_form_gender_dokter)
        setSpinner(R.array.jenis_dokter, sp_form_jenis_dokter)

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
            R.id.btn_ajukan -> pengajuanForm()
            R.id.btn_foto_identitas -> getPhoto(IDENTITY_PICK_CODE)
            R.id.btn_foto_bukti_dokter -> getPhoto(STR_PICK_CODE)
        }
    }

    private fun pengajuanForm() {
        val getName: String = edt_form_nama_dokter?.text.toString()
        val getGender: String = sp_form_gender_dokter.selectedItem.toString()
        val getDoctorCat: String = sp_form_jenis_dokter.selectedItem.toString()
        val getDoctorID: String = edt_id_dokter?.text.toString()
        val getPhotoIdentity: Uri = photoIdentity
        val getPhotoStr: Uri = photoStr
        val getUserID: String = auth?.currentUser?.uid.toString()

        if (validateForm(getName, getDoctorID, getPhotoIdentity, getPhotoStr)) {
            val doctor = DoctorModel(getName, getGender, getDoctorCat, getDoctorID, getPhotoIdentity, getPhotoStr)
            ref.child(getUserID)
                .child("tb_calon_dokter")
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
                IDENTITY_PICK_CODE -> photoIdentity = data?.data!!
                STR_PICK_CODE -> photoStr = data?.data!!
            }
        } else {
            showToast(this, "Proses telah dibatalkan")
        }
    }

    private fun validateForm(name: String, id: String, photoIdentity: Uri, photoStr: Uri): Boolean {
        if (TextUtils.isEmpty(name)) {
            showToast(applicationContext, "Enter category!")
            return false
        }
        if (TextUtils.isEmpty(id)) {
            showToast(applicationContext, "Enter field!")
            return false
        }
        if (TextUtils.isEmpty(photoIdentity.toString())) {
            showToast(applicationContext, "Enter field!")
            return false
        }
        if (TextUtils.isEmpty(photoStr.toString())) {
            showToast(applicationContext, "Enter field!")
            return false
        }
        return true
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
