package com.yzq.testzxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.yzq.testzxing.zxing.android.CaptureActivity;
import com.yzq.testzxing.zxing.encode.CodeCreator;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button scanBtn;
    private TextView resultTv;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private EditText mEtUrl;
    private Button mCreatBtn;
    private ImageView mIvScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        scanBtn = (Button) findViewById(R.id.scanBtn);
        resultTv = (TextView) findViewById(R.id.resultTv);
        mEtUrl = (EditText) findViewById(R.id.etUrl);
        mCreatBtn = (Button) findViewById(R.id.creatScanBtn);
        mIvScan = (ImageView) findViewById(R.id.ivScan);
        scanBtn.setOnClickListener(this);
        mCreatBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanBtn:
                Intent intent = new Intent(MainActivity.this,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.creatScanBtn:
                String url = mEtUrl.getText().toString();
                try {
                    url = new String(url.getBytes("UTF-8"), "ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    Bitmap code = CodeCreator.createQRCode(url);
                    mIvScan.setImageBitmap(code);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                resultTv.setText("解码结果： \n" + content);
            }
        }
    }
}
