package it.insubria.biblioteca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import it.insubria.biblioteca.databinding.ActivityMainBinding

class UserHome : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_home)
        replaceFragment(Home())

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.home -> replaceFragment(Home())
                R.id.cerca -> replaceFragment(Cerca())
                R.id.prestiti -> replaceFragment(Prestiti())
                R.id.profilo -> replaceFragment(Profilo())


                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        //supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(null).commit()
    }
}