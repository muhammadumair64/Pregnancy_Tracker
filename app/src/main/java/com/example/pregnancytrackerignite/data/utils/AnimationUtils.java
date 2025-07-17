package com.example.pregnancytrackerignite.data.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.widget.TextView;

public class AnimationUtils {

    public static void animateTextViewChange(final TextView textView, final String newText) {
        // Create a flip-out animation (rotation on X-axis from 0 to 90 degrees)
        ObjectAnimator flipOut = ObjectAnimator.ofFloat(textView, "rotationX", 0f, 90f);
        flipOut.setDuration(300);

        // Set a listener to change the text after the flip-out animation ends
        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText(newText);

                // Create a flip-in animation (rotation on X-axis from -90 to 0 degrees)
                ObjectAnimator flipIn = ObjectAnimator.ofFloat(textView, "rotationX", -90f, 0f);
                flipIn.setDuration(300);
                flipIn.start();
            }
        });

        // Start the flip-out animation
        flipOut.start();
    }

//    public static void animateTextViewChange(final TextView textView, final String newText) {
//        // Create a flip-out animation (rotation on Y-axis from 0 to 90 degrees)
//        ObjectAnimator flipOut = ObjectAnimator.ofFloat(textView, "rotationY", 0f, 90f);
//        flipOut.setDuration(300);
//
//        // Set a listener to change the text after the flip-out animation ends
//        flipOut.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                textView.setText(newText);
//
//                // Create a flip-in animation (rotation on Y-axis from -90 to 0 degrees)
//                ObjectAnimator flipIn = ObjectAnimator.ofFloat(textView, "rotationY", -90f, 0f);
//                flipIn.setDuration(300);
//                flipIn.start();
//            }
//        });
//
//        // Start the flip-out animation
//        flipOut.start();
//    }


//    public static void animateTextViewChange(final TextView textView, final String newText) {
//        // Animate the current text view to slide out upwards
//        ObjectAnimator slideOut = ObjectAnimator.ofFloat(textView, "translationY", 0, -textView.getHeight());
//        slideOut.setDuration(300);
//
//        // Set a listener to change the text after the slide-out animation ends
//        slideOut.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(android.animation.Animator animation) {
//                textView.setText(newText);
//
//                // Animate the new text view to slide in from bottom
//                ObjectAnimator slideIn = ObjectAnimator.ofFloat(textView, "translationY", textView.getHeight(), 0);
//                slideIn.setDuration(300);
//                slideIn.start();
//            }
//        });
//
//        // Start the slide-out animation
//        slideOut.start();
//    }
}
