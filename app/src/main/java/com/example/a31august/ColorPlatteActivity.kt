package com.example.a31august

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import com.example.a31august.databinding.ActivityColorPlatteBinding
import java.lang.reflect.Array.get

class ColorPlatteActivity : AppCompatActivity() {
    var binding:ActivityColorPlatteBinding ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_color_platte)
        setSupportActionBar(binding?.toolbar)
        binding?.apply {
            image1.setOnClickListener {
                setColorPlatte(image1.drawToBitmap())
            }

            image2.setOnClickListener {
                setColorPlatte(image2.drawToBitmap())
            }

            image3.setOnClickListener {
                setColorPlatte(image3.drawToBitmap())
            }

            image4.setOnClickListener {
                setColorPlatte(image4.drawToBitmap())
            }

            image5.setOnClickListener {
                setColorPlatte(image5.drawToBitmap())
            }

            image6.setOnClickListener {
                setColorPlatte(image6.drawToBitmap())
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setColorPlatte(bitmap:Bitmap){
        Palette.from(bitmap).generate {
            it?.let { it ->
                val swatch = it.dominantSwatch
                swatch?.let {swatch->
                    val rgb = swatch.rgb
                    val titleColor = swatch.titleTextColor
                    val bodyColor = swatch.bodyTextColor
                    binding!!.toolbar.apply {
                        setBackgroundColor(rgb)
                        setTitleTextColor(titleColor)
                    }
                }


            }

        }
    }
}