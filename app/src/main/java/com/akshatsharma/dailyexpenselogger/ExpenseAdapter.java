package com.akshatsharma.dailyexpenselogger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akshatsharma.dailyexpenselogger.database.Expense;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    // Member variable to handle item clicks
    final private ItemClickListener itemClickListener;

    // Class variable for the List that holds the expense data
    private List<Expense> expenses;
    private Context context;

    public ExpenseAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Inflate the expense_layout to a view
        View view = LayoutInflater.from(context)
                .inflate(R.layout.expense_layout, viewGroup, false);

        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder expenseViewHolder, int position) {
        // Determine the values of the wanted data
        Expense expense = expenses.get(position);
        String description = expense.getDescription();
        int amount = expense.getAmount();
        // TODO: Check if an expense is recurring and add a TextView for the same

        // Set values
        expenseViewHolder.expenseDescriptionView.setText(description);
        String amountString = "₹" + String.valueOf(amount);
        expenseViewHolder.expenseAmountView.setText(String.valueOf(amountString));
//        expenseViewHolder.expenseTypeView.setText();
    }

    @Override
    public int getItemCount() {
        if(expenses == null) {
            return 0;
        }
        return expenses.size();
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class ExpenseViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView expenseDescriptionView;
        TextView expenseAmountView;
        TextView expenseTypeView;

        public ExpenseViewHolder(View itemView) {
            super(itemView);

            expenseDescriptionView = itemView.findViewById(R.id.expense_description);
            expenseAmountView = itemView.findViewById(R.id.amount);
//            expenseTypeView = itemView.findViewById(R.id.expense_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = expenses.get(getAdapterPosition()).getExpenseId();
            itemClickListener.onItemClickListener(elementId);
        }
    }
}
