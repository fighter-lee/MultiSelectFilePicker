package top.fighter_lee.multiselectfilepicker.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import top.fighter_lee.multiselectfilepicker.R;
import top.fighter_lee.multiselectfilepickerlib.engine.FilePickerBuilder;

/**
 * @author fighter_lee
 * @date 2018/1/9
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FilePickerBuilder.getInstance()
                .setTitle("选择文件")
                .setMaxCount(1)
                .setActivityTheme(R.style.AppTheme)
                .enableDocSupport(false)
                .setShowAllFile()
                .pickFile(this);
    }
}
