package com.example.computershop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;


 // Utility class for displaying images in full-screen dialogs


public class ImageViewerUtils {
    private static final String TAG = "ImageViewerUtils";


    public static void showImageDialog(Context context, String base64Image, String title) {
        if (context == null || base64Image == null || base64Image.isEmpty()) {
            return;
        }

        try {
            // Decode Base64 to Bitmap
            Bitmap bitmap = ImageUtils.decodeBase64ToBitmap(base64Image);
            if (bitmap == null) {
                return;
            }

            // Create dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title != null ? title : "Image View");

            // Create ImageView
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(16, 16, 16, 16);

            // Create layout with proper sizing
            LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            layout.addView(imageView);

            builder.setView(layout);
            builder.setPositiveButton("Close", null);

            AlertDialog dialog = builder.create();
            dialog.show();

            // Set dialog width to match screen
            if (dialog.getWindow() != null) {
                int width = (int)(context.getResources().getDisplayMetrics().widthPixels * 0.95);
                dialog.getWindow().setLayout(width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showImageDialog(Context context, String base64Image, String title, 
                                      String buttonText, Runnable onButtonClick) {
        if (context == null || base64Image == null || base64Image.isEmpty()) {
            return;
        }

        try {
            // Decode Base64 to Bitmap
            Bitmap bitmap = ImageUtils.decodeBase64ToBitmap(base64Image);
            if (bitmap == null) {
                return;
            }

            // Create dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title != null ? title : "Image View");

            // Create ImageView
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(16, 16, 16, 16);

            // Create layout with proper sizing
            LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            layout.addView(imageView);

            builder.setView(layout);
            
            if (buttonText != null && !buttonText.isEmpty() && onButtonClick != null) {
                builder.setNeutralButton(buttonText, (dialog, which) -> onButtonClick.run());
            }
            
            builder.setPositiveButton("Close", null);

            AlertDialog dialog = builder.create();
            dialog.show();

            // Set dialog width to match screen
            if (dialog.getWindow() != null) {
                int width = (int)(context.getResources().getDisplayMetrics().widthPixels * 0.95);
                dialog.getWindow().setLayout(width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
