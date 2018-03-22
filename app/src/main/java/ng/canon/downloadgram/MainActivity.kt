package ng.canon.downloadgram

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.github.angads25.toggle.LabeledSwitch
import com.github.angads25.toggle.interfaces.OnToggledListener
import kotlinx.android.synthetic.main.activity_main.*
import ng.canon.downloadgram.Buckets.PhotoBucktes
import ng.canon.downloadgram.Church.Bayek

class MainActivity : AppCompatActivity() {
    private var isSvcRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switches.setOnToggledListener(object: OnToggledListener {
            override fun onSwitched(labeledSwitch: LabeledSwitch?, isOn: Boolean) {

                val box = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)

                if (isOn){

                    GlideApp.with(this@MainActivity).asGif().load(R.raw.run).into(displays!!)
                    box.edit().putBoolean("locked", true).apply()
                    status!!.text = getString(R.string.switch_message_on)
                    launchers!!.visibility = View.VISIBLE
                    Genesis()

                }else{


                    GlideApp.with(this@MainActivity).load(R.drawable.a0).into(displays!!)
                    box.edit().putBoolean("locked", false).apply()
                    status!!.text = getString(R.string.switch_message)
                    launchers!!.visibility =View.GONE
                    Revelation()


                }
            }




        })

        fab.setOnClickListener {

            startActivity(Intent(this@MainActivity,PhotoBucktes::class.java))

        }

        launchers.setOnClickListener {

            callInstagram(this@MainActivity,"com.instagram.android")
        }
    }





    fun Genesis(){

        val intu = Intent(this@MainActivity, Bayek::class.java)
        ContextCompat.startForegroundService(applicationContext,intu)
    }



    fun Revelation(){

        val intu = Intent(this@MainActivity,Bayek::class.java)
       stopService(intu)
    }



    override fun onResume() {
        val manager = LocalBroadcastManager.getInstance(applicationContext)
        manager.registerReceiver(mReceiver, IntentFilter(Bayek.ACTION_PONG))
        // the service will respond to this broadcast only if it's running
        manager.sendBroadcast(Intent(Bayek.ACTION_PING))
        super.onResume()
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(mReceiver);
        super.onStop()
    }


    protected var mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // here you receive the response from the service
            if (intent.action == Bayek.ACTION_PONG) {
                isSvcRunning = true
                watchTower()
                GlideApp.with(this@MainActivity).asGif().load(R.raw.run).into(displays!!)
                status!!.text = getString(R.string.switch_message_on)
                launchers!!.visibility =View.VISIBLE

            }
        }
    }


    fun watchTower(){

        switches!!.isOn = isSvcRunning

    }




    private fun callInstagram(context: Context, packageN: String) {
        val apppackage = packageN
        try {
            val i = context.packageManager.getLaunchIntentForPackage(apppackage)
            context.startActivity(i)
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageN)))
        }

    }

}
