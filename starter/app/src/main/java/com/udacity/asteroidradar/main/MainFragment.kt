package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.ViewModelFactory
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.Asteroid

class MainFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(activity)
        ViewModelProvider(this, ViewModelFactory(activity.application))
            .get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val asteroidClickListener = AsteroidClickListener {
            viewModel.onSelected(it)
        }

        val adapter = AsteroidListAdapter(asteroidClickListener)
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.selectedAsteroid.observe(viewLifecycleOwner, Observer {
            it?.let { asteroid ->
                val isMasterDetail = resources.getBoolean(R.bool.isMasterDetail)
                if (isMasterDetail) {
                    sharedViewModel.selectedAsteroid.value = asteroid
                } else {
                    navigateToDetail(asteroid)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun navigateToDetail(asteroid: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        viewModel.onNavigationCompleted()
    }
}
