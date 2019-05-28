package com.trigger.shortify.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;

public class Util {
    public static final void ShowToast(Context ctx, String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
    }

    public static final void CopyToClipboard(Context ctx, String text) {
        final android.content.ClipboardManager clipboardManager = (ClipboardManager)ctx.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("", text);
        clipboardManager.setPrimaryClip(clipData);
    }
}
