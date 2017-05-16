package com.iflytek.platformadapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	private Button closeBtn = null;
	private TextView nlpText = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        closeBtn=(Button) this.findViewById(R.id.button_close);
        nlpText=(TextView) this.findViewById(R.id.textview_result);

        closeBtn.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				MainActivity.this.finish();
			}
        });
        showResult(getIntent());	
		
    }
    @Override
    protected void onNewIntent(Intent intent) {
    	// TODO Auto-generated method stub
    	super.onNewIntent(intent);
    	showResult(intent);
    }
    private void showResult(Intent intent){
		String nlpResult = intent.getStringExtra("NLPContent");
		if(nlpResult != null) {
			if(nlpResult.length() == 0) {
				nlpResult = this.getString(R.string.error_nlp_result_empty);
			}
		} else{
			nlpResult = this.getString(R.string.error_nlp_result_empty);
		}
		nlpText.setText(nlpResult);
    }

    @Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void finish() {
		super.finish();
	}
}
