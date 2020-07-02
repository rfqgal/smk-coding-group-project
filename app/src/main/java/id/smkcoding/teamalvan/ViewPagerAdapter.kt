package id.smkcoding.teamalvan

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    private val JUMLAH_MENU = 2
    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> { return HomeFragment() }
            1 -> { return UserFragment() }
            else -> {
                return HomeFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return JUMLAH_MENU
    }
}