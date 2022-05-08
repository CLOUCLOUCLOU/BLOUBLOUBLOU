package com.clouclouclou.bloublou.user

import android.Manifest
import android.app.Notification
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.clouclouclou.bloublou.R
import com.clouclouclou.bloublou.databinding.TasklistfragmentBinding
import com.clouclouclou.bloublou.form.FormActivity
import com.clouclouclou.bloublou.network.Api
import com.clouclouclou.bloublou.model.Task
import com.clouclouclou.bloublou.network.viewmodel.TasksListViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.permissions.RequestAccess
import com.google.modernstorage.permissions.StoragePermissions
import com.google.modernstorage.storage.AndroidFileSystem
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

class UserInfoActivity : AppCompatActivity(){

    private val getPhoto =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            var imageView = findViewById<ImageView>(R.id.avatarImageView)


            if (success) {
                imageView.load(photoUri)
                lifecycleScope.launch {
                    Api.userWebService.updateAvatar(photoUri.toRequestBody())
                }
            } else{
                showMessage("Error photo")
            }

        }

    private val requestCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { accepted ->
            getPhoto.launch(photoUri)
        }
    
    val requestWriteAccess = registerForActivityResult(RequestAccess()) { accepted ->
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            isAlreadyAccepted -> getPhoto.launch(photoUri)// lancer l'action souhaitée
            isExplanationNeeded -> showMessage("error") // afficher une explication
            else -> requestCamera.launch(camPermission)
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            .setAction("Open Settings") {
                val intent = Intent(
                    ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                startActivity(intent)
            }
            .show()
    }

    private fun launchCameraWithPermission() {
        requestWriteAccess.launch(
            RequestAccess.Args(
                action = StoragePermissions.Action.READ_AND_WRITE,
                types = listOf(StoragePermissions.FileType.Image),
                createdBy = StoragePermissions.CreatedBy.AllApps
            )
        )
    }

    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            this.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                it
            ) // this est le bitmap dans ce contexte
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = tmpFile.readBytes().toRequestBody()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)


        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()
            var imageView = findViewById<ImageView>(R.id.avatar_image_view)
            imageView.load(userInfo?.avatar) {
                error(R.drawable.ic_launcher_background)
            }
        }

        var uploadPictureView = findViewById<Button>(R.id.upload_image_button)
        uploadPictureView?.setOnClickListener {
            this.openGallery()
        }

        var takePictureView = findViewById<Button>(R.id.take_picture_button)
        takePictureView?.setOnClickListener {
            launchCameraWithPermission()
        }

    }

    private val fileSystem by lazy { AndroidFileSystem(this) }
    private val photoUri by lazy {
        fileSystem.createMediaStoreUri(
            filename = "picture-${UUID.randomUUID()}.jpg",
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            directory = "Todo",
        )!!
    }
    
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            lifecycleScope.launch {

                var imageView = findViewById<ImageView>(R.id.avatarImageView)
                imageView.load(uri) {
                    error(R.drawable.ic_launcher_background)
                }
                Api.userWebService.updateAvatar(uri!!.toRequestBody())
            }
        }
    
    fun openGallery() {
        requestReadAccess.launch(
            RequestAccess.Args(
                action = StoragePermissions.Action.READ,
                types = listOf(StoragePermissions.FileType.Image),
                createdBy = StoragePermissions.CreatedBy.AllApps
            )
        )
    }
    
    val requestReadAccess = registerForActivityResult(RequestAccess()) { hasAccess ->
        if (hasAccess) {
            galleryLauncher.launch("image/*")
        } else {
            showMessage("Access denied")
        }
    }    

    private fun Uri.toRequestBody(): MultipartBody.Part {
        val fileInputStream = contentResolver.openInputStream(this)!!
        val fileBody = fileInputStream.readBytes().toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = fileBody
        )
    }
}



