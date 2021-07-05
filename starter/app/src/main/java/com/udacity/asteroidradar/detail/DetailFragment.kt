package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.main.SharedViewModel
import com.udacity.asteroidradar.model.Asteroid

class DetailFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this


        val isMasterDetail = resources.getBoolean(R.bool.isMasterDetail)
        if (isMasterDetail) {
            viewModel.selectedAsteroid.observe(viewLifecycleOwner, Observer { asteroid ->
                binding.asteroid = asteroid
            })
        } else {
            val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid
            binding.asteroid = asteroid
        }

        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }

        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
