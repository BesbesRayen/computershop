package com.example.computershop.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Unified utility class for image processing operations
 * Handles Base64 encoding, compression, and decoding
 * Used by all activities that need image manipulation
 */
public class ImageUtils {
    private static final String TAG = "ImageUtils";
    private static final int IMAGE_SAMPLE_SIZE = 4;
    private static final int JPEG_QUALITY = 70;


    public static String convertImageToBase64(Uri imageUri, ContentResolver contentResolver) {
        try {
            if (imageUri == null) {
                Log.e(TAG, "Image URI is null");
                return null;
            }

            // Open input stream from the image URI
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            if (inputStream == null) {
                Log.e(TAG, "Cannot open input stream for image");
                return null;
            }

            // Decode bitmap with size reduction to save memory
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = IMAGE_SAMPLE_SIZE; // Load image at 1/4 size
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            if (bitmap == null) {
                Log.e(TAG, "Failed to decode bitmap");
                return null;
            }

            // Compress bitmap to JPEG format
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Convert to Base64 string
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            // Cleanup
            bitmap.recycle();
            byteArrayOutputStream.close();

            Log.d(TAG, "Image converted to Base64 successfully. Size: " + base64Image.length() + " chars");
            return base64Image;

        } catch (Exception e) {
            Log.e(TAG, "Error converting image to Base64: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            if (base64String == null || base64String.isEmpty()) {
                Log.e(TAG, "Base64 string is null or empty");
                return null;
            }

            byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        } catch (Exception e) {
            Log.e(TAG, "Error decoding Base64 to bitmap: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static boolean isValidImageUri(Uri imageUri, ContentResolver contentResolver) {
        if (imageUri == null) {
            return false;
        }

        try {
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            if (inputStream != null) {
                inputStream.close();
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Invalid image URI: " + e.getMessage());
        }

        return false;
    }


    public static double getBase64SizeKB(String base64String) {
        if (base64String == null) {
            return 0;
        }
        return (base64String.length() / 1024.0);
    }
}
