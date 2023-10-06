package com.rizalsujana.githubuser.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.rizalsujana.githubuser.R
import com.rizalsujana.githubuser.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val root = binding?.root

        binding?.apply {
            val buttonGithub: Button = root?.findViewById(R.id.btnGithub) ?: return@apply
            val buttonPorto: Button = root.findViewById(R.id.btnPorto) ?: return@apply

            buttonPorto.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://rabbitdev1.github.io/portofolio/"))
                startActivity(intent)
            }
            buttonGithub.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rabbitdev1"))
                startActivity(intent)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
