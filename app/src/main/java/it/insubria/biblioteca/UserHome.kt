package it.insubria.biblioteca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserHome : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_home)
        replaceFragment(HomeU())

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.home -> replaceFragment(HomeU())
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