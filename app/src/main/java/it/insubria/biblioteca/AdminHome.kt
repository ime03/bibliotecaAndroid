package it.insubria.biblioteca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class AdminHome : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val homeFragment = HomeA()
    val inserisciFragment = Inserisci()
    val gestioneFragment = Gestione()
    val profiloFragment = Profilo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        auth = FirebaseAuth.getInstance()
        val navbar = findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navbar.selectedItemId = R.id.home
        navbar.setOnNavigationItemSelectedListener {
                item->
            when(item.itemId){
                R.id.home->{
                    homeFragment()
                }
                R.id.inserisci->{
                    inserisciFragment()
                }
                R.id.gestione->{
                    gestioneFragment()
                }
                R.id.profilo->{
                    profiloFragment()
                }
            }
            true
        }

    }

    private fun homeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView,homeFragment).commit()
    }

    private fun inserisciFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView,inserisciFragment).commit()
    }

    private fun gestioneFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView,gestioneFragment).commit()
    }

    private fun profiloFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView,profiloFragment).commit()
    }


}