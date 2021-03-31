package uz.revolution.pdponlinerxkotlin

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import uz.revolution.pdponlinerxkotlin.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    var doublePressed = false
    lateinit var binding: ActivityMainBinding
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_container).navigateUp()
    }

//    override fun onBackPressed() {
//        if (doublePressed) {
//            super.onBackPressed()
//            return
//        }
//        doublePressed = true
//        handler = Handler(Looper.getMainLooper())
//        Toast.makeText(this, "Dasturdan chiqish uchun yana bir marta bosing", Toast.LENGTH_SHORT)
//            .show()
//        handler.postDelayed({
//            doublePressed = false
//
//        }, 2000)
//    }

}