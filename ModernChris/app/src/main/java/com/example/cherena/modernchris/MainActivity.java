package com.example.cherena.modernchris;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends AppCompatActivity {

    public SeekBar colourSlide = null;
    public int slideProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout topLeft = (LinearLayout) findViewById(R.id.topLeft);
        final LinearLayout topRight = (LinearLayout) findViewById(R.id.topRight);
        final LinearLayout upperMidLeft = (LinearLayout) findViewById(R.id.upperMidLeft);
        final LinearLayout upperMidCentre = (LinearLayout) findViewById(R.id.upperMidCentre);
        final LinearLayout upperMidRight = (LinearLayout) findViewById(R.id.upperMidRight);
        final LinearLayout lowerMidLeft = (LinearLayout) findViewById(R.id.lowerMidLeft);
        final LinearLayout lowerMidRight = (LinearLayout) findViewById(R.id.lowerMidRight);
        final LinearLayout bottomRow= (LinearLayout) findViewById(R.id.bottomRow);

        colourSlide = (SeekBar) findViewById(R.id.colourSlide);
        colourSlide.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                slideProgress = i;
                changeColour(topLeft, ((ColorDrawable)topLeft.getBackground()).getColor());
                changeColour(topRight, ((ColorDrawable)topRight.getBackground()).getColor());
                changeColour(upperMidLeft, ((ColorDrawable)upperMidLeft.getBackground()).getColor());
                changeColour(upperMidRight, ((ColorDrawable)upperMidRight.getBackground()).getColor());
                changeColour(lowerMidLeft, ((ColorDrawable)lowerMidLeft.getBackground()).getColor());
                changeColour(lowerMidRight, ((ColorDrawable)upperMidRight.getBackground()).getColor());
                changeColour(bottomRow, ((ColorDrawable)bottomRow.getBackground()).getColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
            private void changeColour(LinearLayout l, int OriginalColour){
                float[] hsvColour = new float[3];
                Color.colorToHSV(OriginalColour, hsvColour);
                hsvColour[0] = hsvColour[0] + slideProgress;
                hsvColour[0] = hsvColour[0] % 360;
                l.setBackgroundColor(Color.HSVToColor(Color.alpha(OriginalColour), hsvColour));

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mItem){
        int id = mItem.getItemId();
        if (id == R.id.action_more_info){

            DialogFragment moreInfo = new MoreInfoFragment();
            moreInfo.show(getFragmentManager(), "moreInfo");
            return true;
        }
        return super.onOptionsItemSelected(mItem);

    }

}
