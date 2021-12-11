package com.example.sakolaapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.sakolaapp.Fragments.EntregaFragment
import com.example.sakolaapp.Fragments.RetiradaFragment
import com.example.sakolaapp.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_finalizar_pedido.*

class FinalizarPedido : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalizar_pedido)

        val adapter = AdapterTabPager(this)
        adapter.addFragment(EntregaFragment(), "Entrega")
        adapter.addFragment(RetiradaFragment(), "Retirada")

        container_finalizar_pedidos.adapter = adapter
        container_finalizar_pedidos.currentItem = 0

        TabLayoutMediator(tabLayout, container_finalizar_pedidos){ tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

    }

    class AdapterTabPager(activity: FragmentActivity?) : FragmentStateAdapter(activity!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()

        public fun getTabTitle(position : Int): String{
            return mFragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getItemCount(): Int {
            return mFragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return mFragmentList[position]
        }
    }

}