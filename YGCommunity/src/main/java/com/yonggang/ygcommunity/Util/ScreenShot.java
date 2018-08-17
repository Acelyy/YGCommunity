package com.yonggang.ygcommunity.Util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShot {

	public static void shoot(Activity a, File filePath) {
		if (filePath == null) {
			return;
		}
		if (!filePath.getParentFile().exists()) {
			filePath.getParentFile().mkdirs();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			if (null != fos) {
				takeScreenShot(a).compress(Bitmap.CompressFormat.PNG, 100, fos);
				Toast.makeText(a, "截图已保存至SD卡下/DCIM", Toast.LENGTH_SHORT).show();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static Bitmap takeScreenShot(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		Bitmap b = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width,
				height - statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}

}
