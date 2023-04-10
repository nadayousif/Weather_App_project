package com.example.weatherappproject.ui.alert

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.example.weatherappproject.DialogeFragmentDirections
import com.example.weatherappproject.R
import com.example.weatherappproject.databinding.FragmentAlertBinding
import com.example.weatherappproject.databinding.FragmentHomeBinding
import com.example.weatherappproject.localData.AlertState
import com.example.weatherappproject.localData.LocalDataSource
import com.example.weatherappproject.remoteData.RemoteDataSource
import com.example.weatherappproject.repositary.Repositary
import com.example.weatherappproject.ui.alert.notificationAlert.AlertRecevier
import com.example.weatherappproject.ui.alert.notificationAlert.DialogeAlertFragment
import com.example.weatherappproject.util.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AlertFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var dialog: ProgressDialog
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var alertViewModel: AlertViewModel
    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPreferences = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        val repo = Repositary.getInstance(
            RemoteDataSource.getInstance(), LocalDataSource(requireActivity()),
            PreferenceManager.getDefaultSharedPreferences(context)
        )
        alertViewModel =
            ViewModelProvider(
                this.requireActivity(),
                AlertViewModelFactory(repo)
            )[AlertViewModel::class.java]
        alertViewModel.getAlertsDB()
        dialog = progressDialog(requireContext())
        checkAlertsPermission()

        binding.addAlert.setOnClickListener {
            Log.i("alertttt", "onCreateView: ")
            DialogeAlertFragment().show(requireActivity().supportFragmentManager,"AlertDialog")
            //DialogeAlertFragment().show(childFragmentManager, DialogeAlertFragment.TAG)
            lifecycleScope.launch {
                alertViewModel.currentAlert.collectLatest {
                    when (it) {
                        is AlertState.Loading -> {
                        }
                        is AlertState.Fail -> {
                            Toast.makeText(
                                requireContext(),
                                "No Connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is AlertState.Success -> {
                            binding.rvAlert.apply {
                                adapter = AlertAdapter(it.data, requireContext()) {
                                    canelAlarm(
                                        requireContext(),
                                        it.toString(),
                                        it.startDay.toInt()
                                    )
                                    alertViewModel.deleteAlertDB(it)
                                    WorkManager.getInstance(context.applicationContext)
                                        .cancelAllWorkByTag(it.startDay.toString())
                                }
                                layoutManager = LinearLayoutManager(requireContext())
                            }
                        }
                        else -> {}
                    }
                }
            }
        }

        return root
    }

    private fun progressDialog(context: Context): ProgressDialog {
        var progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false)
        return progressDialog
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun canelAlarm(context: Context, alert: String?, requestCode: Int) {
        var alarmMgr: AlarmManager? = null
        lateinit var alarmIntent: PendingIntent
        alarmMgr =
            context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context.applicationContext, AlertRecevier::class.java).putExtra(
            Constants.Alert, alert
        ).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext, requestCode, intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmMgr.cancel(alarmIntent)

    }
    private fun checkAlertsPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("permission")
                .setMessage("want_app_to_access_alerts")
                .setPositiveButton("ok") { dialog: DialogInterface, i: Int ->
                    var myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    startActivity(myIntent)
                    dialog.dismiss()
                }.setNegativeButton(
                    "cancel"
                ) { dialog: DialogInterface, i: Int ->
                    dialog.dismiss()
                    val action = AlertFragmentDirections.actionAlertFragmentToMapsFragment()
                    view?.let { Navigation.findNavController(it).navigate(action) }

                }.show()
        }
    }


}