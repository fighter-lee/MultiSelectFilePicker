package top.fighter_lee.multiselectfilepickerlib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import top.fighter_lee.multiselectfilepickerlib.R;
import top.fighter_lee.multiselectfilepickerlib.data.FilePickerConst;
import top.fighter_lee.multiselectfilepickerlib.engine.PickerManager;
import top.fighter_lee.multiselectfilepickerlib.fragment.DocFragment;
import top.fighter_lee.multiselectfilepickerlib.utils.FragmentUtil;


public class FilePickerActivity extends AppCompatActivity implements
        DocFragment.DocFragmentListener {
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(PickerManager.getInstance().getTheme());
        setContentView(R.layout.activity_file_picker);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            type = intent.getIntExtra(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.DOC_PICKER);
            ArrayList<String> selectedPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_FILES);

            if (selectedPaths != null && selectedPaths.size() > 0) {
                if (PickerManager.getInstance().getMaxCount() == 1) {
                    selectedPaths.clear();
                }
                PickerManager.getInstance().add(selectedPaths, FilePickerConst.FILE_TYPE_DOCUMENT);
            } else {
                selectedPaths = new ArrayList<>();
            }
            setToolbarTitle(PickerManager.getInstance().getCurrentCount(), PickerManager.getInstance().getTitle());
            openSpecificFragment(type, selectedPaths);

        }
    }

    private void setToolbarTitle(int count, String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (PickerManager.getInstance().getMaxCount() > 1) {
                actionBar.setTitle(String.format("附件 (%d/%d)", count, PickerManager.getInstance().getMaxCount()));
            } else {
                actionBar.setTitle(title);
            }
        }

    }

    private void openSpecificFragment(int type, @Nullable ArrayList<String> selectedPaths) {
        if (PickerManager.getInstance().isDocSupport()) {
            PickerManager.getInstance().addDocTypes();
        }

        switch (type) {
            case FilePickerConst.DOC_PICKER:
//                DocFragment docFragment = DocFragment.newInstance(PickerManager.getInstance().getFileTypes(), Environment.getExternalStorageDirectory().getAbsolutePath());
                DocFragment docFragment = DocFragment.newInstance(new int[]{DocFragment.FILE_PATH_TYPE_SDCARD,DocFragment.FILE_PATH_TYPE_USB});
                FragmentUtil.addFragment(this, R.id.container, docFragment);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picker_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_done) {
            returnData(PickerManager.getInstance().getSelectedFiles());
            return true;
        } else if (i == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (!FragmentUtil.havaFragment(this)) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void returnData(ArrayList<String> paths) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(FilePickerConst.KEY_SELECTED_FILES, paths);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemSelected() {
        setToolbarTitle(PickerManager.getInstance().getCurrentCount(), PickerManager.getInstance().getTitle());

        if (PickerManager.getInstance().getMaxCount() == 1) {
            returnData(PickerManager.getInstance().getSelectedFiles());
        }
    }

    @Override
    public void onDirSelected(String path) {
        DocFragment docFragment = DocFragment.newInstance(PickerManager.getInstance().getFileTypes(), path);
        FragmentUtil.addFragment(this, R.id.container, docFragment);
    }
}
