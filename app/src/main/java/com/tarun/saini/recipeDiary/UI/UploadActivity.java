package com.tarun.saini.recipeDiary.UI;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tarun.saini.recipeDiary.Model.Ingredient;
import com.tarun.saini.recipeDiary.Model.Recipe;
import com.tarun.saini.recipeDiary.Network.RetrofitClient;
import com.tarun.saini.recipeDiary.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity implements View.OnKeyListener, TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {


    private static final String TAG = "LOG STATEMENT";
    private static final String SAVE_COLLAPSED = "save collapsed";
    private static final String SAVE_URI = "image uri";
    private Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    private ImageView recipeImage;
    private Uri selectedImageUri;
    private StorageReference mStorageRef;
    private EditText recipe_name, steps, ingredients, calories, time, serves, description, quantity;
    private String RecipeName, downLoadUrl, Rcategory, Rtype, Rcalories, Rtime, Rserves, Rdescription, Runit;
    private Spinner typeSpinner, categorySpinner, unitSpinner;
    private LinearLayout ingredientLinaerLayout, stepLinearLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private ArrayList<String> stepArrayList = new ArrayList<String>();
    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<Ingredient>();
    private RetrofitClient retrofitClient;
    private Button upload;
    private boolean collapsed;
    private AppBarLayout appBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        retrofitClient = new RetrofitClient();

        recipeImage = (ImageView) findViewById(R.id.recipe_image);
        upload = (Button) findViewById(R.id.upload_button);
        recipe_name = (EditText) findViewById(R.id.recipe_name);
        time = (EditText) findViewById(R.id.recipe_time);
        calories = (EditText) findViewById(R.id.recipe_calories);
        serves = (EditText) findViewById(R.id.recipe_serves);
        description = (EditText) findViewById(R.id.recipe_description);
        TextView addPhotoText = (TextView) findViewById(R.id.add_photo_text);
        typeSpinner = (Spinner) findViewById(R.id.spinner_type);
        categorySpinner = (Spinner) findViewById(R.id.spinner_category);
        unitSpinner = (Spinner) findViewById(R.id.spinner_unit);
        steps = (EditText) findViewById(R.id.stepEditText);
        ingredients = (EditText) findViewById(R.id.ingredient);
        quantity = (EditText) findViewById(R.id.quantity);
        stepLinearLayout = (LinearLayout) findViewById(R.id.container);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        ingredientLinaerLayout = (LinearLayout) findViewById(R.id.ingredient_container);


        recipe_name.setOnEditorActionListener(this);
        quantity.setOnEditorActionListener(this);


        RecipeActivity.setLatoRegular(this, recipe_name);
        RecipeActivity.setLatoBlack(this, upload);
        RecipeActivity.setLatoRegular(this, time);
        RecipeActivity.setLatoRegular(this, calories);
        RecipeActivity.setLatoRegular(this, description);
        RecipeActivity.setLatoRegular(this, serves);
        RecipeActivity.setLatoRegular(this, steps);
        RecipeActivity.setLatoRegular(this, ingredients);
        RecipeActivity.setLatoRegular(this, quantity);
        RecipeActivity.setLatoBlack(this, addPhotoText);


        ingredients.setOnKeyListener(this);
        steps.setOnKeyListener(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#20000000"));


        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();

                }

                if (scrollRange + verticalOffset == 0) {

                    isShow = true;
                    collapsingToolbar.setTitle("Upload Recipe");
                    collapsed = false;

                } else if (isShow) {

                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                    collapsed = true;

                }
            }

        });


        ArrayAdapter typeadapter = ArrayAdapter
                .createFromResource(this, R.array.recipe_type, android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeadapter);

        ArrayAdapter categoryadapter = ArrayAdapter
                .createFromResource(this, R.array.recipe_category, android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryadapter);

        ArrayAdapter unitadapter = ArrayAdapter
                .createFromResource(this, R.array.ingredient_unit, android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitadapter);


        typeSpinner.setOnItemSelectedListener(this);
        categorySpinner.setOnItemSelectedListener(this);
        unitSpinner.setOnItemSelectedListener(this);


        mStorageRef = FirebaseStorage.getInstance().getReference();
        int Permission_All = 1;
        String[] Permissions = new String[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL, Manifest.permission.CAMERA,};
        }
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }


        if (savedInstanceState == null) {
            appBarLayout.setExpanded(true);
        } else {
            collapsed = savedInstanceState.getBoolean(SAVE_COLLAPSED);
            selectedImageUri = savedInstanceState.getParcelable(SAVE_URI);
            appBarLayout.setExpanded(collapsed);
            recipeImage.setImageURI(selectedImageUri);

        }


    }

    public void choosePhoto(View view) {
        SelectImage();
    }

//upload image to firebase storage
    private void uploadRecipeImage() {

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        //if there is a file to upload
        if (selectedImageUri != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            String path = "images/" + currentDateTimeString + ".jpg";
            StorageReference riversRef = mStorageRef.child(path);
            riversRef.putFile(selectedImageUri)
                    .addOnSuccessListener(UploadActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests") Uri ImageUrl = taskSnapshot.getDownloadUrl();
                            assert ImageUrl != null;
                            downLoadUrl = ImageUrl.toString().trim();
                            if (!TextUtils.isEmpty(downLoadUrl)) {
                                //Toast.makeText(UploadActivity.this, downLoadUrl+"", Toast.LENGTH_SHORT).show();
                                upload.setEnabled(false);

                            }
                            sendRecipe(downLoadUrl);
                            Intent homeIntent = new Intent(UploadActivity.this, RecipeActivity.class);
                            startActivity(homeIntent);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            progressDialog.dismiss();


                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            Toast.makeText(this, "Please Select a Recipe Image", Toast.LENGTH_SHORT).show();

        }


    }

    public void sendRecipe(String downLoadUrl) {
        retrofitClient.getRecipeService().saveRecipe(new Recipe(downLoadUrl, Rcategory, Rtype, Integer.parseInt(Rcalories), Integer.parseInt(Rserves), Rtime, Rdescription, RecipeName, stepArrayList, ingredientArrayList)).enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(@NonNull Call<Recipe> call, @NonNull Response<Recipe> response) {
                Log.d("SUCCESS", " "+ call);
            }

            @Override
            public void onFailure(@NonNull Call<Recipe> call, @NonNull Throwable t) {

                Log.d("ERROR", t+" "+ call);

            }
        });

    }

    private void SelectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    public static boolean hasPermissions(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Bitmap bmp;
            if (requestCode == REQUEST_CAMERA) {

                Bundle bundle = data.getExtras();
                bmp = (Bitmap) bundle.get("data");
                selectedImageUri = getImageUri(this, bmp);
                recipeImage.setImageBitmap(bmp);

            } else if (requestCode == SELECT_FILE) {

                selectedImageUri = data.getData();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    recipeImage.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }


//steps added to in layout inflater
    public void AddStep(View view) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.step_layout, null);
        TextView textOut = (TextView) addView.findViewById(R.id.stepTextOut);
        textOut.setText(steps.getText().toString().trim());
        steps.setText("");
        steps.setHint("Step");
        RecipeActivity.setLatoRegular(this, textOut);
        Button remove = (Button) addView.findViewById(R.id.remove_step);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) addView.getParent()).removeView(addView);
            }
        });

        stepLinearLayout.addView(addView, -1);

        uploadRecipeData();

    }

//Ingredients added to inflated layouts
    public void AddIngredient(View view) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.add_ingredient_layout, null);
        TextView quantityOut = (TextView) addView.findViewById(R.id.quantityTextOut);
        String rquantity = quantity.getText().toString();
        if (!TextUtils.isEmpty(rquantity)) {
            quantityOut.setText(rquantity);
        } else {
            Toast.makeText(this, "Please Add Quantity", Toast.LENGTH_SHORT).show();
        }

        TextView ingredientOut = (TextView) addView.findViewById(R.id.ingredientTextOut);
        String ringredient = ingredients.getText().toString().trim();
        ingredientOut.setText(ringredient);
        TextView unitOut = (TextView) addView.findViewById(R.id.unitTextOut);
        unitOut.setText(Runit);
        RecipeActivity.setLatoRegular(this, quantityOut);
        RecipeActivity.setLatoRegular(this, ingredientOut);
        RecipeActivity.setLatoRegular(this, unitOut);
        ingredients.setText("");
        quantity.setText("");
        ingredients.setHint("Ingredient");
        quantity.setHint("Quantity");

        Button remove = (Button) addView.findViewById(R.id.remove_ingredient);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) addView.getParent()).removeView(addView);


            }
        });

        ingredientLinaerLayout.addView(addView, -1);
        uploadRecipeData();
    }


    public void uploadRecipeData() {

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeName = recipe_name.getText().toString();
                Rcalories = calories.getText().toString();
                Rtime = time.getText().toString();
                Rdescription = description.getText().toString();
                Rserves = serves.getText().toString();
                int countSteps = stepLinearLayout.getChildCount();
                int countIngredients = ingredientLinaerLayout.getChildCount();
                String childQuantity, childIngredient, childUnit;
                String childStep;
                //Get all steps and put in a String ArrayList
                for (int c = 0; c < countSteps; c++) {
                    View childView = stepLinearLayout.getChildAt(c);
                    TextView childTextView = (TextView) childView.findViewById(R.id.stepTextOut);
                    childStep = childTextView.getText().toString();
                    stepArrayList.add(childStep);
                }

                //Get all ingredients from inflated layouts
                for (int d = 0; d < countIngredients; d++) {
                    View childViewI = ingredientLinaerLayout.getChildAt(d);
                    TextView childTextViewQ = (TextView) childViewI.findViewById(R.id.quantityTextOut);
                    TextView childTextViewI = (TextView) childViewI.findViewById(R.id.ingredientTextOut);
                    TextView childTextViewU = (TextView) childViewI.findViewById(R.id.unitTextOut);
                    childQuantity = childTextViewQ.getText().toString();
                    childIngredient = childTextViewI.getText().toString();
                    childUnit = childTextViewU.getText().toString();
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredient(childIngredient);
                    ingredient.setQuantity(Double.parseDouble(childQuantity));
                    ingredient.setMeasure(childUnit);
                    ingredientArrayList.add(ingredient);

                }
                if (TextUtils.isEmpty(Rcalories)) {
                    Toast.makeText(UploadActivity.this, "Please Fill Calories Column", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Rserves)) {
                    Toast.makeText(UploadActivity.this, "Please Fill ServesColumn", Toast.LENGTH_SHORT).show();
                } else if (ingredientArrayList.size() == 0) {
                    Toast.makeText(UploadActivity.this, "Please add Ingredients", Toast.LENGTH_SHORT).show();

                } else if (stepArrayList.size() == 0) {
                    Toast.makeText(UploadActivity.this, "Please add Steps", Toast.LENGTH_SHORT).show();

                } else {
                    //recipe image is uploaded along with other information
                    uploadRecipeImage();

                }

            }
        });

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (v == steps) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                AddStep(v);
            }
        } else if (v == ingredients) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                AddIngredient(v);
                v.clearFocus();
                quantity.requestFocus();
                quantity.performClick();
            }
        }
        return false;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_COLLAPSED, collapsed);
        outState.putParcelable(SAVE_URI, selectedImageUri);


    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        switch (v.getId()) {
            case R.id.recipe_name:
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    v.clearFocus();
                    typeSpinner.requestFocus();
                    typeSpinner.performClick();
                }
                break;
            case R.id.quantity:
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    v.clearFocus();
                    unitSpinner.requestFocus();
                    unitSpinner.performClick();
                }
                break;


        }


        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.spinner_type:
                Rtype = typeSpinner.getSelectedItem().toString();
                break;
            case R.id.spinner_category:
                Rcategory = categorySpinner.getSelectedItem().toString();
                break;
            case R.id.spinner_unit:
                Runit = unitSpinner.getSelectedItem().toString();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}





