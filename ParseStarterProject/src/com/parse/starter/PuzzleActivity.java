package com.parse.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.Puzzle;


/**
 * PuzzleActivity displays the puzzle view:
 * 		currently, the puzzle view is defined by puzzle.xml
 * 		at all times, at most 1 CheckBox in the view can be checked
 * 			if the CheckBox checked is the wrong answer, the rightorwrong TextView displays a wrong message
 * 			if the CheckBox checked is the right answer, the view changes to the GPS view
 * 		if the main_menu_button Button is pressed, the view changes to the Main Menu view
 */
// I WOULD LIKE TO ADD 3 WRONG ANSWERS IN THE PUZZLE CLASS
public class PuzzleActivity extends Activity {
	
	private final int totalNumMultChoice = 4;
	
	private CheckBox chk1, chk2, chk3, chk4;
	private Button mainMenu;
	private String correctAnswer;
	
	// message displayed to user when they click the CheckBox with text that doesn't match correctAnswer
	private final String wrongMessage = "This is wrong.";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.puzzle);
		setTitle(R.string.puzzle_view_name);
		
		String ingredient = ((User) User.getCurrentUser()).getIngredient();
		
		ParseQuery<Puzzle> query = Puzzle.getQuery();

		// NOTE: Have to match value type EXACTLY
		query.whereEqualTo("ingredient", ingredient);
		
		// choosing a random puzzle from the query
		query.findInBackground(new FindCallback<Puzzle>(){
			@Override
			public void done(List<Puzzle> potentialPuzzles, ParseException e) {
				if (e == null && potentialPuzzles.size() > 0) {
					Random randomizer = new Random();
					Puzzle puzzle = potentialPuzzles.get(randomizer.nextInt(potentialPuzzles.size()));

					// setting the question text
					TextView question = (TextView) findViewById(R.id.question);
					question.setText(puzzle.getString("riddle"));
					
					correctAnswer = puzzle.getString("answer");
					
					chk1 = (CheckBox) findViewById(R.id.chkanswer_1);
					chk2 = (CheckBox) findViewById(R.id.chkanswer_2);
					chk3 = (CheckBox) findViewById(R.id.chkanswer_3);
					chk4 = (CheckBox) findViewById(R.id.chkanswer_4);

					ArrayList<String> options = puzzle.getOptions();
					options.add(correctAnswer);

					// randomly assigning CheckBoxes different answer options
					List<CheckBox> checkBoxes = new ArrayList<CheckBox>(Arrays.asList(chk1, chk2, chk3, chk4));
					List<Integer> ordering = generateRandomOrder();
					for (int i = 0; i < totalNumMultChoice; i++){
						checkBoxes.get(i).setText(options.get(ordering.get(i)));
					}
					
					addListenerOnMainMenuButton();
					addListenerOnChkAnswer_1();
					addListenerOnChkAnswer_2();
					addListenerOnChkAnswer_3();
					addListenerOnChkAnswer_4();
					
				} else {
					// NOT SURE WHAT TO DO HERE!!!
				}
			}
		});
		
	}
		
	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_puzzle);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				startActivity(i);
				
			}
		});
	}
	
// THE SAME ACTIVITY OCCURS FOR ALL addListenerOnChkAnswer_[1-4]()
// SEE checkBoxChecked FOR SPECIFIC ACTIVITY
	private void addListenerOnChkAnswer_1() {
		chk1 = (CheckBox) findViewById(R.id.chkanswer_1);
		chk1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkBoxChecked(chk1, v);
			}
		});
	}

	private void addListenerOnChkAnswer_2() {
		chk2 = (CheckBox) findViewById(R.id.chkanswer_2);
		chk2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkBoxChecked(chk2, v);
			}
		});
	}
	
	private void addListenerOnChkAnswer_3() {
		chk3 = (CheckBox) findViewById(R.id.chkanswer_3);
		chk3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkBoxChecked(chk3,v);
			}
		});
	}
	
	public void addListenerOnChkAnswer_4(){
		chk4 = (CheckBox) findViewById(R.id.chkanswer_4);
		chk4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkBoxChecked(chk4, v);
			}
		});
	}
	
	
	/**
	 * 
	 * @return List that randomly orders 1:totalNumMultChoice
	 */
	private List<Integer> generateRandomOrder() {
		ArrayList<Integer> ordering = new ArrayList<Integer>();
		for (int i = 0; i < totalNumMultChoice; i++) {
			ordering.add(i);
		}
		Collections.shuffle(ordering);
		return ordering;
	}
	
	/**
	 * Called when a CheckBox is clicked on.
	 * 
	 * If CheckBox CB is checked, removes checks from all other CheckBoxes in View that are not CB
	 * 		If the text of CB is not the correctAnswer, displays the wrongMessage
	 * 		If the text of CB is the correctAnswer, changes view to GPS view
	 * No message is displayed if all CheckBoxes in the view are unchecked
	 * 
	 * @param CB CheckBox that the user clicks on
	 * @param v the View that CB belongs in, which is altered by this method
	 */
	private void checkBoxChecked(CheckBox CB, View v) {
		TextView txt = (TextView) findViewById (R.id.rightorwrong);
		if(((CheckBox) v).isChecked()) {
			List<CheckBox> checkBoxes = new ArrayList<CheckBox>(Arrays.asList(chk1, chk2, chk3, chk4));
			
			// unchecking CheckBoxes that are not CB
			for (CheckBox chk: checkBoxes) {
				if (!CB.equals(chk)){
					chk.setChecked(false);
				}
			}
			
			if (CB.getText().equals(correctAnswer)) {
				txt.setText("");
				Intent i = new Intent(v.getContext(), GPSActivity.class);
				startActivity(i);
			}
			else{
				txt.setText(wrongMessage);
			}			
		}
		else{
			txt.setText("");
		}
	}
}