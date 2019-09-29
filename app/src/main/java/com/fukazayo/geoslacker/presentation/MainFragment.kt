package com.fukazayo.geoslacker.presentation

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fukazayo.geoslacker.R
import com.fukazayo.geoslacker.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.main_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        DataBindingUtil.bind<MainFragmentBinding>(root)?.viewModel = viewModel

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.context = context ?: return

        getLocationButton.setOnClickListener {
            viewModel.getCurrentLocation()
        }

        saveButton.setOnClickListener {
            viewModel.save()
        }

        initializeButton.setOnClickListener {
            viewModel.initialize()
        }

        startButton.setOnClickListener {
            viewModel.start()
        }

        stopButton.setOnClickListener {
            viewModel.stop()
        }

        viewModel.load()
    }
}
