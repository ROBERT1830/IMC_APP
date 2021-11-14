package com.robertconstantindinescu.myimc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.tabs.TabLayoutMediator
import com.robertconstantindinescu.myimc.adapters.ViewPagerAdapter
import com.robertconstantindinescu.myimc.databinding.ActivityMainBinding
import com.robertconstantindinescu.myimc.ui.CalculatorFragment
import com.robertconstantindinescu.myimc.ui.MainFragment

class MainActivity : AppCompatActivity() {
    //Definimos contorlador de la navegacion
    //private lateinit var navController: NavController

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //accedemos al viewPager
        val viewPager = mBinding.viewPager

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        adapter.addFragment(MainFragment(), "Historico")
        adapter.addFragment(CalculatorFragment(), "Calcular Imc")

        viewPager.adapter = adapter


        TabLayoutMediator(mBinding.tabLayout, viewPager){ tab, position ->
            tab.text = adapter.getPageTitle(position)

        }.attach()








//        //ENCONTRAMOS EL FRAGMENTO QUE HACE DE HOST Y A PARTIR DEL CUAL NAVEGAREMOS (USANDO NAVIGATION COMPONENTS).
//            //con el fragmentManager buscamos el fragment donde se hostean los demás e indicamos
//            //que será el host de la navegación
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
//                as NavHostFragment
//
//        //Indicamos al controlador de navegacion cual sera el fragmento a partir del cual se inicia
//        // la navegacion.
//        this.navController = navHostFragment.navController
//        //setup de la bara de navegacion con el navcontroller para viajar entre fragments
//        NavigationUI.setupActionBarWithNavController(this,navController)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        //retornar el controlador con el sistema de navegacion de la toolbar
//        return navController.navigateUp()
//
//
//    }
}