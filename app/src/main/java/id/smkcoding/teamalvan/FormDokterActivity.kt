package id.smkcoding.teamalvan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_form_dokter.*

class FormDokterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_dokter)
        title = "Form Pengajuan Akun Dokter"

        ArrayAdapter.createFromResource(
            this,
            R.array.gender,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sp_form_gender_dokter.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.jenis_dokter,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sp_form_jenis_dokter.adapter = adapter
        }
    }
}
