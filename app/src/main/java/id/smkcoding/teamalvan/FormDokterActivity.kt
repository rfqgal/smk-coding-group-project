package id.smkcoding.teamalvan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_form_dokter.*

class FormDokterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_dokter)
        title = "Form Pengajuan Akun Dokter"

        setSpinner(R.array.gender, sp_form_gender_dokter)
        setSpinner(R.array.jenis_dokter, sp_form_jenis_dokter)

        
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
}
