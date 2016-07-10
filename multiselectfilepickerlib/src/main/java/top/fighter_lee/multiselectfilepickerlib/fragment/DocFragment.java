package top.fighter_lee.multiselectfilepickerlib.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.fighter_lee.multiselectfilepickerlib.R;
import top.fighter_lee.multiselectfilepickerlib.adapter.FileListAdapter;
import top.fighter_lee.multiselectfilepickerlib.data.Document;
import top.fighter_lee.multiselectfilepickerlib.data.FileType;
import top.fighter_lee.multiselectfilepickerlib.engine.PickerManager;
import top.fighter_lee.multiselectfilepickerlib.inter.FileAdapterListener;
import top.fighter_lee.multiselectfilepickerlib.inter.FileResultCallback;
import top.fighter_lee.multiselectfilepickerlib.utils.MediaStoreHelper;
import top.fighter_lee.multiselectfilepickerlib.utils.Utils;


public class DocFragment extends Fragment implements FileAdapterListener {

    private static final String TAG = DocFragment.class.getSimpleName();
    public static final String FILE_TYPE = "FILE_TYPE";
    public static final String FILE_PATH = "FILE_PATH";
    public static final String FILE_PATH_ROOT = "FILE_PATH_ROOT";
    public static final int FILE_PATH_TYPE_SDCARD = 1;
    public static final int FILE_PATH_TYPE_USB = 2;
    RecyclerView recyclerView;

    TextView emptyView;

    private DocFragmentListener mListener;
    private FileListAdapter fileListAdapter;
    private List<FileType> fileType;
    private ProgressBar progressBar;

    public DocFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_picker, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DocFragmentListener) {
            mListener = (DocFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PhotoPickerFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static DocFragment newInstance(ArrayList<FileType> fileTypes, String path) {
        DocFragment photoPickerFragment = new DocFragment();
        Bundle bun = new Bundle();
        bun.putParcelableArrayList(FILE_TYPE, fileTypes);
        bun.putString(FILE_PATH, path);
        photoPickerFragment.setArguments(bun);
        return photoPickerFragment;
    }

    public static DocFragment newInstance(int[] path) {
        DocFragment photoPickerFragment = new DocFragment();
        Bundle bun = new Bundle();
        bun.putIntArray(FILE_PATH_ROOT, path);
        photoPickerFragment.setArguments(bun);
        return photoPickerFragment;
    }

    public List<FileType> getFileType() {
        return getArguments().getParcelableArrayList(FILE_TYPE);
    }

    public String getPath() {
        return getArguments().getString(FILE_PATH);
    }

    public int[] getRootPath() {
        return getArguments().getIntArray(FILE_PATH_ROOT);
    }

    @Override
    public void onItemSelected() {
        mListener.onItemSelected();
    }

    @Override
    public void onDirSelected(String path) {
        mListener.onDirSelected(path);
    }

    public interface DocFragmentListener {
        void onItemSelected();

        void onDirSelected(String path);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setData();
    }

    private void setData() {
        int[] rootPath = getRootPath();
        if (rootPath != null && rootPath.length > 0) {
            List<Document> files = new ArrayList<>();
            for (int i = 0; i < rootPath.length; i++) {
                Document document = null;
                if (rootPath[i] == FILE_PATH_TYPE_SDCARD) {
                    document = new Document(0, "Sdcard", Environment.getExternalStorageDirectory().getAbsolutePath());
                    document.isDir = true;
                    document.setFileType(new FileType("dir", new String[]{}, R.mipmap.ic_dir));
                }

                if (document != null) {
                    files.add(document);
                }
            }
            progressBar.setVisibility(View.GONE);
            updateList(files);
        } else {
            MediaStoreHelper.getDocs(getActivity(), getPath(), getFileType(), new FileResultCallback<Document>() {
                @Override
                public void onResultCallback(List<Document> files) {
                    if (!isAdded()) {
                        {
                            return;
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    updateList(files);
                }
            });
        }
    }


    private void initView(View view) {
        fileType = getArguments().getParcelableArrayList(FILE_TYPE);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        emptyView = (TextView) view.findViewById(R.id.empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (recyclerView.getChildCount() > 0) {
                        recyclerView.getChildAt(0).requestFocus();
                    }
                }
            }
        });
    }

    public void updateList(List<Document> dirs) {
        if (getView() == null) {
            {
                return;
            }
        }

        if (dirs.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

            FileListAdapter fileListAdapter = (FileListAdapter) recyclerView.getAdapter();
            if (fileListAdapter == null) {
                fileListAdapter = new FileListAdapter(getActivity(), dirs, PickerManager.getInstance().getSelectedFiles(), this);

                recyclerView.setAdapter(fileListAdapter);
            } else {
                fileListAdapter.setData(dirs);
                fileListAdapter.notifyDataSetChanged();
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

}
