package com.example.upath;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

    public static File getFile(Context context, Uri uri) {
        String fileName = getFileName(context, uri);
        File tempFile = new File(context.getCacheDir(), fileName);

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempFile;
    }

    private static String getFileName(Context context, Uri uri) {
        String name = "temp_file";

        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(
                    cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
            );
            cursor.close();
        }
        return name;
    }
}
