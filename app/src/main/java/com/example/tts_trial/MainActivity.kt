package com.example.tts_trial

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Log.VERBOSE
import android.util.TypedValue
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import org.w3c.dom.Text
import java.util.*
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {

    lateinit var tts: TextToSpeech
    //lateinit var utteranceProgressListener: UtteranceProgressListener
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var sentence = "Inside the plant cell are small organelles called chloroplasts, which store the energy of sunlight. Within the thylakoid membranes of the chloroplast is a light-absorbing pigment called chlorophyll, which is responsible for giving the plant its green color. During photosynthesis, chlorophyll absorbs energy from blue- and red-light waves, and reflects green-light waves, making the plant appear green."
        var e1 = findViewById<TextView>(R.id.edText)
        var b1 = findViewById<Button>(R.id.btnSpeak)
        var b2 = findViewById<Button>(R.id.btnSlow)
        var b3 = findViewById<Button>(R.id.btnIncrease)
        var b4 = findViewById<Button>(R.id.btnDecrease)
        var b5 = findViewById<Button>(R.id.btnStop)
        var b6 = findViewById<Button>(R.id.btnSpeed)



        if(!this::tts.isInitialized)
            b5.setBackgroundColor(Color.LTGRAY)

        var speechRate = 0.6f
        var txtSize = 17f
        e1.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize)
        e1.setText(sentence)


        class speechListener : UtteranceProgressListener(){
            override fun onStart(p0: String?) {

                Toast.makeText(this@MainActivity,"Started",Toast.LENGTH_LONG).show()

            }

            override fun onDone(p0: String?){
                b1.setBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.button_color))
                b5.setBackgroundColor(Color.LTGRAY)
                b1.isEnabled = true
                b5.isEnabled = false
            }

            override fun onError(p0: String?) {
                Toast.makeText(this@MainActivity,"Yes",Toast.LENGTH_LONG).show()
            }

            override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
                super.onRangeStart(utteranceId, start, end, frame)
                runOnUiThread {
                    val spannable = SpannableString(sentence)
                    spannable.setSpan(
                        BackgroundColorSpan(Color.LTGRAY),
                        start
                        ,end
                        ,Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                    e1.setText(spannable)

                }
            }

        }
        fun initTTS(){
            tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                if(it==TextToSpeech.SUCCESS){
                    b1.isEnabled = false
                    b5.isEnabled = true
                    b1.setBackgroundColor(Color.LTGRAY)
                    b5.setBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.button_color))
                    tts.language = Locale.forLanguageTag("hin")
                    tts.setSpeechRate(speechRate)
                    tts.setOnUtteranceProgressListener(speechListener())
                    tts.speak(sentence,TextToSpeech.QUEUE_ADD,null,"UNOQUE_UTTERANCE_ID")

                }
            })
        }

        b1.setOnClickListener {
           initTTS()

        }

        b2.setOnClickListener {
            if(this::tts.isInitialized && speechRate>0.2f){
                speechRate-=0.1f
                tts.stop()
                initTTS()

            }else{
                if(!this::tts.isInitialized){
                    speechRate-=0.1f
                    initTTS()
                }else if(speechRate<=0.2f){
                    Toast.makeText(this@MainActivity, "Cannot slow beyond this",Toast.LENGTH_LONG).show()

                }
            }
        }

        b3.setOnClickListener {
            txtSize++
            e1.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize)
        }

        b4.setOnClickListener {
            if(txtSize>17f) {
                txtSize--
                e1.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize)
            }else{
                Toast.makeText(this@MainActivity, "Cannot decrease below this",Toast.LENGTH_LONG).show()
            }
        }

        b5.setOnClickListener {
            if(this::tts.isInitialized){
                b1.isEnabled = true
                b5.isEnabled = false
                b1.setBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.button_color))
                b5.setBackgroundColor(Color.LTGRAY)
                tts.stop()

            }
        }

        b6.setOnClickListener {
            if(this::tts.isInitialized && speechRate<=1.0f){
                speechRate+=0.1f
                tts.stop()
                initTTS()

            }else{
                if(!this::tts.isInitialized){
                    speechRate+=0.1f
                    initTTS()
                }else if(speechRate>=1.0f){
                    Toast.makeText(this@MainActivity, "Cannot speed up beyond this",Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}



