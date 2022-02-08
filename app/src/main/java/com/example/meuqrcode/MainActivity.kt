package com.example.meuqrcode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.zxing.client.android.BeepManager
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.database.DataSetObserver
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meuqrcode.adapter.QRItemAdapter
import com.example.meuqrcode.databinding.ActivityMainBinding
import com.example.meuqrcode.domain.QRLido
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var captureManager: CaptureManager
    private var scanContinuousState: Boolean = false
    lateinit var beepManager: BeepManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var qrItemAdapter: QRItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupQRList()
        setupBtnScan(savedInstanceState)
    }

    private fun setupBtnScan(savedInstanceState: Bundle?) {
        val btnScanContinuous = binding.btnScanContinuous

        captureManager = CaptureManager(this, binding.barcodeView)
        captureManager.initializeFromIntent(intent, savedInstanceState)
        beepManager = BeepManager(this)
        beepManager.isVibrateEnabled = true

        val callback = object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    addToList(it.text)
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
            }
        }

        btnScanContinuous.setOnClickListener {
            if (!scanContinuousState) {
                scanContinuousState = true
                btnScanContinuous.background.setTint(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.secondaryColor,
                        null
                    )
                )
                binding.txtResultContinuous.text = ""
                btnScanContinuous.text = getString(R.string.scanning)
                binding.barcodeView.decodeContinuous(callback)
            } else {
                scanContinuousState = false
                btnScanContinuous.text = getString(R.string.scan)
                btnScanContinuous.background.setTint(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.primaryDarkColor,
                        null
                    )
                )
                binding.barcodeView.barcodeView.stopDecoding()
            }
        }
    }

    private fun setupQRList() {
        binding.rvQRList.layoutManager = LinearLayoutManager(this)
        qrItemAdapter = QRItemAdapter()
        binding.rvQRList.adapter = qrItemAdapter

    }

    private fun addToList(text: String) {
        qrItemAdapter.qrLidoList?.let {
            val findEl = qrItemAdapter.qrLidoList.find { it.code == text }
            if (findEl == null) {
                val qr = QRLido(code = text, date = Date())
                qrItemAdapter.add_qr(qr)
                binding.rvQRList.scrollToPosition(qrItemAdapter.itemCount - 1 )
                binding.txtResultContinuous.text = text
                beepManager.playBeepSound()
                animateBackground()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        captureManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        captureManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager.onDestroy()
    }

    private fun animateBackground() {
        val colorFrom = R.color.primaryColor
        val colorTo = R.color.secondaryColor
        val colorAnimation =
            ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 2000 // milliseconds

        colorAnimation.addUpdateListener { animator ->
            binding.txtResultContinuous.setBackgroundColor(
                animator.animatedValue as Int
            )
        }
        colorAnimation.start()
    }
}