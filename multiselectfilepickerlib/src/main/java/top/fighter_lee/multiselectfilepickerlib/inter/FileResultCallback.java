package top.fighter_lee.multiselectfilepickerlib.inter;

import java.util.List;

public interface FileResultCallback<T> {
    void onResultCallback(List<T> files);
  }