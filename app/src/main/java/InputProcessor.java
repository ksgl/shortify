import android.util.Patterns;

public class InputProcessor {

    boolean is_valid(CharSequence url) {
        return Patterns.WEB_URL.matcher(url).matches();
        // Check if this detects scheme
    }
}
