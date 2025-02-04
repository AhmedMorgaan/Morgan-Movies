package com.morgan.movies.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.morgan.movies.R
import com.morgan.movies.databinding.FragmentHomeBinding
import com.morgan.movies.ui.base.BaseFragment
import com.morgan.movies.ui.home.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.drawer_header.view.image_profile


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by viewModels()
    private lateinit var  actionBarDrawerToggle : ActionBarDrawerToggle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayoutView()
        initDrawerButton()
        handelSearchClick()
        handelDrawerMenuClick()
        handelProfileImageClick()
    }

    private fun handelSearchClick(){
        binding.appbar.setOnMenuItemClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            true
        }
    }
    private fun initDrawerButton() {
        binding.appbar.setNavigationOnClickListener {
            binding.mainDrawer.openDrawer(binding.navigationView)
        }
       actionBarDrawerToggle = ActionBarDrawerToggle(activity,binding.mainDrawer,R.string.app_name,R.string.app_name)
       actionBarDrawerToggle.syncState()
    }
    private fun handelProfileImageClick(){
        val header = binding.navigationView.getHeaderView(0)
        val image = header.image_profile
        image.setOnClickListener {
            val unicode = 0x1F612
            val emoji = getEmoji(unicode)
            Toast.makeText(context," عيب عليك لما تدوس على راجل قد ابوك $emoji",Toast.LENGTH_LONG).show()
        }
    }
    private fun handelDrawerMenuClick(){
        binding.navigationView.setNavigationItemSelectedListener {
            binding.mainDrawer.closeDrawer(binding.navigationView)
            when(it.itemId){
                R.id.favorite ->{
                    findNavController().navigate(R.id.action_homeFragment_to_favoriteMoviesFragment)
                }
                R.id.my_profile ->{
                    val unicode = 0x1F601
                    val emoji = getEmoji(unicode)
                    Toast.makeText(context,"ايه مش عاجبك صورة اشرف عبدالباقى لو مش عاجبك طلقني (بصوت احمد حلمى $emoji ) ",Toast.LENGTH_LONG).show()
                }
                R.id.rate_us ->{
                    val unicode = 0x1F602
                    val emoji = getEmoji(unicode)
                    Toast.makeText(context," تقيم ايه يا عم صلي على النبي ده كويس ان الأبلكيشن شغال اصلا $emoji  ",Toast.LENGTH_LONG).show()
                }
                R.id.follow_us ->{
                    val unicode = 0x1F60A
                    val emoji = getEmoji(unicode)
                    Toast.makeText(context," يا عم مش عايز فولو ولا حاجة أذكر الله بس و انا هبقى مبسوط $emoji ",Toast.LENGTH_LONG).show()
                }
                R.id.popular_actors ->{
                    findNavController().navigate(R.id.action_homeFragment_to_popularActorsFragment)
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }
    fun getEmoji(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    private fun initTabLayoutView(){
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.isUserInputEnabled = false
        binding.typeTablayout.addTab(binding.typeTablayout.newTab().setText("Movies"),true)
        binding.typeTablayout.addTab(binding.typeTablayout.newTab().setText("Tv Shows"))
        binding.typeTablayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun getViewBinding(v: View): FragmentHomeBinding {
        return FragmentHomeBinding.bind(v)
    }
}