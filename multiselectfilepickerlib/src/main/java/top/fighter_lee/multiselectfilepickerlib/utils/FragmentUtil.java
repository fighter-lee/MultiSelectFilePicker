package top.fighter_lee.multiselectfilepickerlib.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import top.fighter_lee.multiselectfilepickerlib.R;


public class FragmentUtil {

    public static boolean hadFragment(AppCompatActivity activity) {
        return activity.getSupportFragmentManager().getBackStackEntryCount() != 0;
    }

    public static void replaceFragment(AppCompatActivity activity, int contentId, Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);

        transaction.replace(contentId, fragment, fragment.getClass().getSimpleName());

        transaction.addToBackStack(null);
        transaction.commit();
    }


    public static void addFragment(AppCompatActivity activity, int contentId, Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out,R.anim.slide_right_in, R.anim.slide_right_out);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.add(contentId, fragment, fragment.getClass().getSimpleName());
        transaction.addToBackStack(fragment.getTag());
        transaction.commit();
    }

    public static void addFragmentWithoutAnim(AppCompatActivity activity, int contentId, Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(contentId, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public static void removeHead(AppCompatActivity activity) {
        if (havaFragment(activity)) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.popBackStack();
            havaFragment(activity);
        }
    }

    public static boolean havaFragment(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count > 1) {
            return true;
        }
        return false;
    }

    public static void removeFragment(AppCompatActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }


    public static void showFragment(AppCompatActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commit();
    }

    public static void hideFragment(AppCompatActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commit();
    }

    public static void attachFragment(AppCompatActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .attach(fragment)
                .commit();
    }

    public static void detachFragment(AppCompatActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .detach(fragment)
                .commit();
    }

    public static Fragment getFragmentByTag(AppCompatActivity appCompatActivity, String tag) {
        return appCompatActivity.getSupportFragmentManager().findFragmentByTag(tag);
    }

}