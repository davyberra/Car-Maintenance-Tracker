package listener;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class EditTextListener<EditText> implements TextWatcher {
    private EditText target;

    public EditTextListener(EditText target) {
        this.target = target;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.onTextChanged(target, s);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public abstract void onTextChanged(EditText target, CharSequence s);
}
