package com.example.agromallapplication.screens.capture

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.FragmentCapturingBinding
import com.example.agromallapplication.models.Farmer
import com.example.agromallapplication.utils.Permissions.REQUEST_CODE
import com.example.agromallapplication.utils.Permissions.REQUEST_TAKE_PHOTO
import com.example.agromallapplication.utils.Permissions.getCameraPermission
import com.example.agromallapplication.utils.Permissions.getGalleryPermission

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CapturingFragment : Fragment() {

    lateinit var binding:FragmentCapturingBinding
    private var farmerPicture = ""
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_capturing,container,false)

        binding.photo.setOnClickListener {
            takePhoto()
        }

        binding.next.setOnClickListener {
            val name = binding.farmerName.text.toString()
            val phoneNumber = binding.phoneNumber.text.toString()
            val address = binding.address.text.toString()
            val email = binding.email.text.toString()
            val farmName = binding.farmNameID.text.toString()
            val farmLocation = binding.farmLocationID.text.toString()

            val farmerDetails = Farmer(name,address,email,phoneNumber,farmerPicture,farmName,farmLocation)

            val action =
                CapturingFragmentDirections.actionCapturingFragmentToMapLocationFragment(
                    farmerDetails
                )
            findNavController().navigate(action)
        }


        return binding.root
    }


    //select photo from gallery
    private fun uploadPicture(){
        getGalleryPermission(requireActivity())
        val image = Intent()
            image.type = "image/*"
            image.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(image,REQUEST_CODE)
        
    }



    //get file location for the picture
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    //Activate Camera to take Picture
    private fun camera() {
        getCameraPermission(requireActivity())
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this.requireContext(),
                        "com.example.agromallapplication.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }



    //choice btw camera of gallery
    private fun takePhoto(){
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        dialogBuilder.setTitle("Photo")
        dialogBuilder.setMessage("Select Photo")
        dialogBuilder.setPositiveButton("Camera"){ _, _ ->
           // camera()
            camera()
        }
        dialogBuilder.setNegativeButton("Upload"){
                _,_ ->
            uploadPicture()
        }

        //show the dialogue
        val alert = dialogBuilder.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }



    //activity Result for picture Upload & Camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                REQUEST_CODE -> {
                    val selectedImg = data!!.data
                    Log.e("ResultUpload",selectedImg.toString())
                    farmerPicture = selectedImg.toString()
                    val rest = farmerPicture.split("/")
                    val lastElement = rest.size-1
                    binding.uploadPictureName.text = rest[lastElement]

                }
                REQUEST_TAKE_PHOTO -> {
                    farmerPicture = currentPhotoPath
                    val rest = farmerPicture.split("/")
                    val lastElement = rest.size-1
                    binding.uploadPictureName.text = rest[lastElement]
                    // to pass as bitmap
        //                val file = File(currentPhotoPath);
        //                val bitmap = MediaStore.Images.Media
        //                    .getBitmap(context?.contentResolver, Uri.fromFile(file));
        //                if (bitmap != null) {
        //                    pass the bitmap to GLIDE
        //                }

                }
                else -> {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }



}
