package com.sipc.silicontech.nirman20.Evaluators;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ramotion.fluidslider.FluidSlider;
import com.sipc.silicontech.nirman20.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class EvaluatorReviewEvaluation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluator_review_evaluation);
        final TextView textView = findViewById(R.id.textView);

        final int max = 100;
        final int min = 0;
        final int total = max - min;

        final FluidSlider slider = findViewById(R.id.fluidSlider);
        slider.setBeginTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView.setVisibility(View.INVISIBLE);
                return Unit.INSTANCE;
            }
        });

        slider.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView.setVisibility(View.VISIBLE);
                return Unit.INSTANCE;
            }
        });

        // Java 8 lambda
        slider.setPositionListener(pos -> {
            final String value = String.valueOf( (int)(min + total * pos) );
            slider.setBubbleText(value);
            return Unit.INSTANCE;
        });


        slider.setPosition(0.3f);
        slider.setStartText(String.valueOf(min));
        slider.setEndText(String.valueOf(max));
    }
}