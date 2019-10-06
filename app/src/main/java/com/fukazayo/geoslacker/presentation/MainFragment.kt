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

        viewModel.startedEvent.subscribe({
            setAppSettingsLayout(false)
            startButton.isEnabled = false
            stopButton.isEnabled = true
        }, {})

        viewModel.stoppedEvent.subscribe({
            setAppSettingsLayout(true)
            startButton.isEnabled = true
            stopButton.isEnabled = false
        }, {})

        getLocationButton.setOnClickListener {
            viewModel.getCurrentLocation()
        }

        startButton.setOnClickListener {
            viewModel.start()
        }

        stopButton.setOnClickListener {
            viewModel.stop()
        }

        viewModel.load()
    }

    private fun setAppSettingsLayout(isEnabled: Boolean) {
        for (cnt in 0 until appSettingsLayout.childCount) {
            val view = appSettingsLayout.getChildAt(cnt)
            view.isEnabled = isEnabled
        }
    }
}
