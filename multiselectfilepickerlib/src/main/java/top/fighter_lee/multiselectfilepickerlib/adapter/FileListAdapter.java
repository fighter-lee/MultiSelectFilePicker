package top.fighter_lee.multiselectfilepickerlib.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import top.fighter_lee.multiselectfilepickerlib.R;
import top.fighter_lee.multiselectfilepickerlib.data.Document;
import top.fighter_lee.multiselectfilepickerlib.data.FilePickerConst;
import top.fighter_lee.multiselectfilepickerlib.engine.PickerManager;
import top.fighter_lee.multiselectfilepickerlib.inter.FileAdapterListener;
import top.fighter_lee.multiselectfilepickerlib.view.SmoothCheckBox;


public class FileListAdapter extends SelectableAdapter<FileListAdapter.FileViewHolder, Document> {


    private final Context context;
    private final FileAdapterListener mListener;

    public FileListAdapter(Context context, List<Document> items, List<String> selectedPaths, FileAdapterListener fileAdapterListener) {
        super(items, selectedPaths);
        this.context = context;
        this.mListener = fileAdapterListener;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_doc_layout, parent, false);

        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, final int position) {
        final Document document = getItems().get(position);
        holder.imageView.setImageResource(document.getFileType().getDrawable());
        holder.fileNameTextView.setText(document.getTitle());
        if (document.isDir) {
            //文件夹
            holder.checkBox.setVisibility(View.GONE);
            holder.fileSizeTextView.setVisibility(View.GONE);
        } else {
            //文件
            holder.fileSizeTextView.setVisibility(View.VISIBLE);
            holder.fileSizeTextView.setText(Formatter.formatShortFileSize(context, Long.parseLong(document.getSize())));
        }

        holder.itemView.setFocusable(true);
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("adapter", "hasfocus:" + position + "--" + hasFocus);
                if(hasFocus){
                    holder.itemView.setBackgroundResource(R.color.bg_gray1);

                }else{
                    holder.itemView.setBackgroundResource(R.color.item_bg);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (document.isDir) {
                    onDirClicked(document.getPath());
                } else {
                    onItemClicked(document, holder);
                }
            }
        });

        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicked(document, holder);
            }
        });

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(isSelected(document));

        holder.itemView.setBackgroundResource(isSelected(document) ? R.color.bg_gray : android.R.color.white);
        holder.checkBox.setVisibility(isSelected(document) ? View.VISIBLE : View.GONE);

        holder.checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                toggleSelection(document);
                holder.itemView.setBackgroundResource(isChecked ? R.color.bg_gray : android.R.color.white);

                if (mListener != null) {
                    mListener.onItemSelected();
                }
            }
        });
    }

    private void onDirClicked(String path) {
        if (mListener != null) {
            mListener.onDirSelected(path);
        }
    }

    private void onItemClicked(Document document, FileViewHolder holder) {
        if (PickerManager.getInstance().getMaxCount() == 1) {
            PickerManager.getInstance().add(document.getPath(), FilePickerConst.FILE_TYPE_DOCUMENT);
            if (mListener != null) {
                mListener.onItemSelected();
            }
        } else {
            if (holder.checkBox.isChecked()) {
                PickerManager.getInstance().remove(document.getPath(), FilePickerConst.FILE_TYPE_DOCUMENT);
                holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
                holder.checkBox.setVisibility(View.GONE);
            } else if (PickerManager.getInstance().shouldAdd()) {
                PickerManager.getInstance().add(document.getPath(), FilePickerConst.FILE_TYPE_DOCUMENT);
                holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
                holder.checkBox.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        SmoothCheckBox checkBox;

        ImageView imageView;

        TextView fileNameTextView;

        TextView fileSizeTextView;

        public FileViewHolder(View itemView) {
            super(itemView);
            checkBox = (SmoothCheckBox) itemView.findViewById(R.id.checkbox);
            imageView = (ImageView) itemView.findViewById(R.id.file_iv);
            fileNameTextView = (TextView) itemView.findViewById(R.id.file_name_tv);
            fileSizeTextView = (TextView) itemView.findViewById(R.id.file_size_tv);
        }
    }
}
