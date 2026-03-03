package com.example.localizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private OnItemClickListener mListener;


    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        String languageCode = LocaleHelper.getLanguage(context);

        holder.recipeNameTextView.setText(recipe.getName().getByLanguage(languageCode));

        List<String> ingredients = recipe.getIngredientsByLanguage(languageCode);
        StringBuilder ingredientsText = new StringBuilder();
        for (String ingredient : ingredients) {
            ingredientsText.append(ingredient).append("\n");
        }
        holder.recipeIngredientsTextView.setText(ingredientsText.toString());

        List<String> steps = recipe.getStepsByLanguage(languageCode);
        StringBuilder stepsText = new StringBuilder();
        for (String step : steps) {
            stepsText.append(step).append("\n");
        }
        holder.recipeStepsTextView.setText(stepsText.toString());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameTextView;
        TextView recipeIngredientsTextView;
        TextView recipeStepsTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.recipeNameTextView);
            recipeIngredientsTextView = itemView.findViewById(R.id.recipeIngredientsTextView);
            recipeStepsTextView = itemView.findViewById(R.id.recipeStepsTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}