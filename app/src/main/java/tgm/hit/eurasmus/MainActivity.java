package tgm.hit.eurasmus;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseSmartReply;
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage;
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestion;
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List conversation = new ArrayList();
    private Button[] btnlist = new Button[2];
    private EditText[] txtlist = new EditText[2];
    private final String remoteUserId = "hawara1";
    private FirebaseSmartReply smartReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtlist[0] = findViewById(R.id.editText1);
        txtlist[1] = findViewById(R.id.editText2);
    }

    public void getReplies(View v) {
        smartReply = FirebaseNaturalLanguage.getInstance().getSmartReply();
        smartReply.suggestReplies(conversation)
                .addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                    @Override
                    public void onSuccess(SmartReplySuggestionResult result) {
                        if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                            // The conversation's language isn't supported, so the
                            // the result doesn't contain any suggestions.
                        } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                            // Task completed successfully
                            // ...
                            TextView tv = (TextView) findViewById(R.id.TextView);
                            String replyText = "";
                            for (SmartReplySuggestion suggestion : result.getSuggestions()) {
                                replyText = replyText + "\n" + suggestion.getText();
                            }
                            tv.setText(replyText);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
    }

    public void addLocalText(View v) {
        conversation.add(FirebaseTextMessage.createForLocalUser(String.valueOf(txtlist[0].getText()), System.currentTimeMillis()));
        txtlist[0].setText("");
    }
    public void addRemoteText(View v) {
        conversation.add(FirebaseTextMessage.createForRemoteUser(String.valueOf(txtlist[1].getText()), System.currentTimeMillis(),remoteUserId));
        txtlist[1].setText("");
    }
}